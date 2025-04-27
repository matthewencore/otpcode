package otp.task.models.role;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import otp.task.models.user.User;


import java.util.List;

@Entity
@Getter
@Setter
public class RoleUser {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String nameRole;

    @ManyToMany(mappedBy = "roleUsers")
    List<User> userList;

    public RoleUser(String nameRole) {
        this.nameRole = nameRole;
    }

    public RoleUser() {

    }
}
