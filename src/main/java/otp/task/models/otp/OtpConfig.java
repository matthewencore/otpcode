package otp.task.models.otp;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OtpConfig {
    @Id
    private Long id;

    private long lifeSecondCode;
    private int countCharInCode;

}
