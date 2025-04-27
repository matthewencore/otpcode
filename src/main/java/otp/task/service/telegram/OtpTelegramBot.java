package otp.task.service.telegram;


import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import otp.task.utils.OtpGenerator;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtpTelegramBot extends TelegramLongPollingBot {


    private final OtpGenerator otpGenerator;

    @Value("${telegrambots.bots[0].token}")
    private String token;

    @Value("${telegrambots.bots[0].username}")
    private String username;

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            String messageText = update.getMessage().getText();

            switch (messageText.toLowerCase()) {
                case "/start" -> sendMessage(chatId, "👋 Привет! Напиши /getid, чтобы узнать свой Chat ID.");
                case "/getid" -> sendMessage(chatId, "🔐 Ваш код для привязки аккаунта к телеграм: `" + chatId + "`");
                default -> sendMessage(chatId, "⚠️ Неизвестная команда.");
            }

        }

    }

    public void sendTextMessage(String chatId, String text, String username) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.enableMarkdown(true);

        message.setChatId(chatId);

        StringBuilder sb = new StringBuilder();
        sb.append("🔐 *Ваш код для авторизации в веб-клиенте:*\n")
                .append("`\n").append(text).append("\n`\n\n")
                .append("⚠️ _Ключ действует 5 минут._ После этого он *просрочится*, и вам нужно будет запросить новый.\n\n")
                .append("👤 *Запрос был от пользователя:*\n")
                .append("`").append(username).append("`\n\n")
                .append("Если это были не вы — отправьте команду /dellink");

        message.setText(sb.toString());

        try {
            execute(message); // Метод из TelegramLongPollingBot\
        } catch (TelegramApiException ex) {
            if (ex.getMessage().contains("chat not found")) {
                // Доп. логика: например, предложить пользователю снова ввести chatId

                log.warn("🔍 Чат не найден. chatId : [{}]", chatId);

                // можно кинуть custom exception в контроллер
                throw new BadChatIDTelegram(String.format("Чат не найден. chatId = %s", chatId));
            }
        }
    }


    // Инициализация меню команд бота
    @PostConstruct
    public void initCommands() {
        try {
            List<BotCommand> commandList = new ArrayList<>();
            commandList.add(new BotCommand("/start", "Запустить бота"));
            commandList.add(new BotCommand("/getid", "Получить id чата"));
            commandList.add(new BotCommand("/delid", "Получить id чата"));

            execute(new SetMyCommands(commandList, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Ошибка при инициализации команд бота", e);
        }
    }



    public void sendMessage(Long chatId, String text) {
        SendMessage msg = new SendMessage(String.valueOf(chatId), text);
        msg.enableMarkdown(true);
        try {
            execute(msg);
        } catch (Exception e) {
            log.error("❌ Ошибка отправки сообщения: {}", e.getMessage());
        }
    }
}
