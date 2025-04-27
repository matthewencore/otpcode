package otp.task.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

//                 message.setTo(to);
//                message.setSubject(subject);
//                message.setText(body);
@Data
@Getter
@Setter
@Builder
public class EmailDTO {
    @NotNull(message = "Адрес получателя не может быть null")
    @NotBlank(message = "Адрес получателя не может быть пуст")
    private String to;

    @NotNull(message = "Заголовок не должен быть null")
    @NotBlank(message = "Заголовок не может быть пустой")
    private String subject;

    private String message;
}
