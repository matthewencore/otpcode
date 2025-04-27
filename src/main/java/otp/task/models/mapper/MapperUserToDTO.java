package otp.task.models.mapper;

import org.springframework.stereotype.Component;
import otp.task.models.dto.UserDTO;
import otp.task.models.role.RoleUser;
import otp.task.models.user.User;

import java.util.stream.Collectors;

@Component
public class MapperUserToDTO {
    public UserDTO createDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .roles(user.getRoleUsers()
                        .stream()
                        .map(RoleUser::getNameRole).
                        collect(Collectors.toSet()))
                .accountNonExpired(user.isAccountNonExpired())
                .credentialsNonExpired(user.isCredentialsNonExpired())
                .accountNonLocked(user.isAccountNonLocked())
                .enabled(user.isEnabled())
                .build();
    }
}
