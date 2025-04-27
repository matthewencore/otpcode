package otp.task.models.otp;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import otp.task.models.user.User;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "otp_codes")
public class OTPCode {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String code;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @Enumerated(EnumType.STRING)
    private StatusOtp statusOtp;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    private LocalDateTime dateExpired;


    @PrePersist
    protected void setCreatedAtDateTime(){
        createdAt = LocalDateTime.now();
    }
}

