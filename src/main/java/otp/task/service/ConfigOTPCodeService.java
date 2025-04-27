package otp.task.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import otp.task.models.otp.OtpConfig;
import otp.task.models.dto.ConfigOTPCode;
import otp.task.models.exception.DoesNotExistOTPConfig;
import otp.task.repository.ConfigOTPRepository;


@Transactional
@Slf4j
@Service
@RequiredArgsConstructor
public class ConfigOTPCodeService {
    private final ConfigOTPRepository configOTPRepository;

    public boolean isExistOTP(){
        return configOTPRepository.existsById(1L);
    }


    public OtpConfig getOTPConfig(){
        return configOTPRepository.findById(1L)
                .orElseThrow(() -> new DoesNotExistOTPConfig("OTP конфига нет"));
    }

    public void getOrCreateOTP(){
        try {
            getOTPConfig();
            log.info("Объект конфигурации OTP ключа имеется в базе данных.");
        } catch (DoesNotExistOTPConfig ex) {
            log.warn(ex.getMessage());

            configOTPRepository.save(OtpConfig.builder()
                    .id(1L)
                    .countCharInCode(6)
                    .lifeSecondCode(300)
                    .build());

            log.info("Объект конфигурации был создан по умолчанию, ему был присвоен id: 1 / dead-time 300 sec / countChar 6");
        }

    }

    public void changeOTPConfig(ConfigOTPCode configOTPCode){
        OtpConfig otpConfig = getOTPConfig();
        log.info("Изменение конфигурации ОТП кода, было: lifeCycle: {} | countChar {}"
                ,otpConfig.getLifeSecondCode(), otpConfig.getCountCharInCode());

        otpConfig.setLifeSecondCode(configOTPCode.getLifeSecondCode());
        otpConfig.setCountCharInCode(configOTPCode.getCountCharInCode());

        log.info("Изменение конфигурации ОТП кода, стало: lifeCycle: {} | countChar {}"
                ,configOTPCode.getLifeSecondCode(), configOTPCode.getCountCharInCode());

    }

}
