package otp.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import otp.task.models.user.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername (String login);
    boolean existsByUsername (String login);
    boolean existsByTelegramChats(User user);

}
