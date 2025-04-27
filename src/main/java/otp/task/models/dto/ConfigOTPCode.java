package otp.task.models.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder

public class ConfigOTPCode {
    @Builder.Default
    private long lifeSecondCode = 300;

    @Builder.Default
    private int countCharInCode = 6;
}
