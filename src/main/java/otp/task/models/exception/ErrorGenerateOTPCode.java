package otp.task.models.exception;

public class ErrorGenerateOTPCode extends RuntimeException {
    public ErrorGenerateOTPCode(String message) {
        super(message);
    }
}
