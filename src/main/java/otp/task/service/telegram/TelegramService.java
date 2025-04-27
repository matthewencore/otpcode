package otp.task.service.telegram;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@RequiredArgsConstructor
public class TelegramService {
    private final OtpTelegramBot telegramBot;

    public boolean sendMessage(String chatId, String message, String username) throws BadChatIDTelegram, TelegramApiException {
            telegramBot.sendTextMessage(chatId, message, username);
            return true;
    }


}
