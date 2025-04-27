package otp.task.service.telegram;

public class BadChatIDTelegram extends RuntimeException {
    public BadChatIDTelegram(String message) {
        super(message);
    }
}
