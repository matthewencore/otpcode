package otp.task.models.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@Getter
@Setter
public class CodeOtp {
    @NotNull(message = "Код не может быть null")
    @NotBlank(message = "Код не может быть пустым")
    private String code;
}
