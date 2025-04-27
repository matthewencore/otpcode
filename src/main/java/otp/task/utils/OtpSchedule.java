package otp.task.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import otp.task.models.otp.StatusOtp;
import otp.task.repository.OTPCodeRepository;
import otp.task.service.OTPCodeService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
@RequiredArgsConstructor
public class OtpSchedule {
    private final OTPCodeService otpCode;
    private final OTPCodeRepository repository;

    @Scheduled(fixedRate = 60_000)
    public void checkStatus(){
        log.info("Проверка статусов OTP кодов была вызвана.");
        otpCode.getAllCodeWithCurrentStatus(StatusOtp.ACTIVE_CODE)
                .stream()
                .filter(item -> item.getDateExpired().isBefore(LocalDateTime.now()))
                .forEach(item -> {
                    log.info("✅ Истёк код: ID={} | CODE={} | Истёк в {}",
                            item.getId(), item.getCode(), item.getDateExpired().format(DateTimeFormatter.ISO_DATE));


                    item.setStatusOtp( StatusOtp.EXPIRED_CODE);
                    repository.save(item);
                    }
                );
    }

}
