package otp.task.models.otp;

public enum StatusOtp {
    ACTIVE_CODE("код активен"),
    EXPIRED_CODE("код просрочен"),
    USED("код прошел валидацию и был использован");

    public String ch;

    StatusOtp(String ch) {
        this.ch = ch;
    }
}
