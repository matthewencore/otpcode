package otp.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import otp.task.models.telegram.TelegramChats;

@Repository
public interface TelegramChatIDRepository extends JpaRepository<TelegramChats, Long> {
}
