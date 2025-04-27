package otp.task.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import otp.task.models.otp.OTPCode;
import otp.task.models.otp.OtpConfig;
import otp.task.models.otp.StatusOtp;
import otp.task.models.user.User;
import otp.task.models.exception.DoNotFoundOTPCode;
import otp.task.models.exception.ErrorGenerateOTPCode;
import otp.task.repository.OTPCodeRepository;
import otp.task.repository.UserRepository;
import otp.task.utils.OtpGenerator;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OTPCodeService {

    private final UserService userService;
    private final ConfigOTPCodeService configOTPCodeService;
    private final OtpGenerator otpGenerator;

    private final OTPCodeRepository otpCodeRepository;
    private final UserRepository userRepository;


    public void validateOTPCode(String code, String username){
            User user = userService.findUser(username);

            OTPCode otpCode = otpCodeRepository.findByCodeAndAndUser_UsernameAndStatusOtp(code, username, StatusOtp.ACTIVE_CODE)
                    .orElseThrow(() -> new DoNotFoundOTPCode("По такому имени и коду ничего не найдено"));

            if (otpCode.getDateExpired().isBefore(LocalDateTime.now())) {
                log.error("ОТП ключ больше не актуален, попробуйте еще раз.");
                return;
            }

            // ставим флаг что использован
            otpCode.setStatusOtp(StatusOtp.USED);
            otpCodeRepository.save(otpCode);

            // флаг доступ разрешен
            user.setOtpVerified(true);
            userRepository.save(user);
    }

    public String createOTPForUser(User user){
        if (!userService.isExistUser(user)) {
            log.error("Такого пользователя не существует, пожалуйста" +
                    " убедитесь что пользователь действительно имеется в базе данных.");

            throw new IllegalArgumentException("Возникла ошибка");
        }

        if (!configOTPCodeService.isExistOTP()) {
            log.error("Случилась ошибка, конфигурация была утеряна, создаем новую по-умолчанию.");
            configOTPCodeService.getOrCreateOTP();
        }

        OtpConfig otpConfig = configOTPCodeService.getOTPConfig();
        String code = otpGenerator.generateOTP(otpConfig.getCountCharInCode());
        if (code.equals("000000")) {
            log.error("В процессе генерации кода возникла ошибка.");
            throw new ErrorGenerateOTPCode("Возникла ошибка при выполнении кода, пожалуйста посмотрите логи..");
        }

        otpCodeRepository.save(OTPCode.builder()
                .code(code)
                .statusOtp(StatusOtp.ACTIVE_CODE)
                .dateExpired(LocalDateTime.now().plusSeconds(otpConfig.getLifeSecondCode()))
                .user(user)
                .build());


        log.info("OTP код был успешно записан в базу данных.");

        return code;
    }

    public List<OTPCode> getAllCode(String username){
        return otpCodeRepository.findAllByUserUsernameAndStatusOtp(username,StatusOtp.ACTIVE_CODE)
                .orElseThrow(() -> new DoNotFoundOTPCode("Не были найдены ОТП коды"));
    }

    public List<OTPCode> getAllCodeWithCurrentStatus(StatusOtp statusOtp){
        return otpCodeRepository.findAllByStatusOtp(statusOtp);
    }
}
