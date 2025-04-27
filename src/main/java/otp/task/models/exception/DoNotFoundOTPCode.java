package otp.task.models.exception;

public class DoNotFoundOTPCode extends RuntimeException {
    public DoNotFoundOTPCode(String message) {
        super(message);
    }
}
