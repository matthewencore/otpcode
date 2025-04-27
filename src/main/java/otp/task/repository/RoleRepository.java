package otp.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import otp.task.models.role.RoleUser;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleUser, Long> {
    public Optional<RoleUser> findByNameRole(String nameRole);
}
