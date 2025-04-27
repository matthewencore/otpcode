package otp.task.models.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import otp.task.models.role.RoleUser;

import java.util.List;
import java.util.Set;

@Builder
@Getter
@Setter
public class UserDTO {

    private Long id;
    private String username;
    private boolean otpVerified;
    private Set<String> roles;

    private boolean enabled;
    private boolean accountNonExpired;
    private boolean credentialsNonExpired;
    private boolean accountNonLocked;

}
