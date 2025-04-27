package otp.task.utils;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.stream.IntStream;

@Component
@Slf4j
public class OtpGenerator {
    private final SecureRandom secureRandom = new SecureRandom();

    public String generateOTP(int length){
        log.info("Генерируем ключ OTP, для авторизации");
        if (length <= 0) {
            log.warn("Предупреждение, длина вашего ключа не может быть отрицательной или равна нулю, пересмотрите конфигурацию.");
            return "000000";
        }

        if (length > 8) {
            log.warn("Предупреждение, длина вашего ключа не может быть больше заявленной в генераторе.");
            return "000000";
        }

        String code = IntStream.range(0, length)
                .mapToObj(i -> String.valueOf(secureRandom.nextInt(10)))
                .reduce("", String::concat);

        log.info("Успешно сгенерирован ключ для авторизации {}", code);

        return code;
    }

}
