package otp.task.models.telegram;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TelegramChats {
    @Id
    @GeneratedValue
    private Long id;

    private String chat_id;

}
