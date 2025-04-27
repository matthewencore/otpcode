package otp.task.models.user;

import jakarta.persistence.*;
import lombok.*;
import otp.task.models.telegram.TelegramChats;
import otp.task.models.otp.OTPCode;
import otp.task.models.role.RoleUser;


import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Getter
@Setter
@Builder
@Table(name = "user_table")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String username;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "m2m_role_table",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "roles_id")
    )
    private Set<RoleUser> roleUsers;

    // коды человека
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    Set<OTPCode> otpCodes = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "telegram_chats_id")
    private TelegramChats telegramChats;

    // флаги

    @Builder.Default
    private boolean isOtpVerified = false;

    @Builder.Default
    private boolean enabled = true;
    @Builder.Default
    private boolean accountNonExpired = true;
    @Builder.Default
    private boolean credentialsNonExpired = true;
    @Builder.Default
    private boolean accountNonLocked = true;

}
