package otp.task.service.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import otp.task.models.exception.EmailSenderException;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendSimpleEmail(String to, String subject, String body, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom("matthewencore@yandex.ru");
                message.setTo(to);
                message.setSubject(subject);
                message.setText(body + " " + otp);

                mailSender.send(message);

            log.info("✅ Письмо успешно отправлено на {}", to);

        } catch (Exception e) {
            log.error("Возникла ошибка в процессе отправки письма, пожалуйста по смотрите логи...");
            throw new EmailSenderException("При отправке письма возникла неизвестная ошибка, " +
                    "пожалуйста посмотрите в лог. " + e.getMessage());
        }
    }
}
