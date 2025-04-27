package otp.task.models.exception;

public class DoesNotExistOTPConfig extends RuntimeException {
    public DoesNotExistOTPConfig(String message) {
        super(message);
    }
}
