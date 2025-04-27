package otp.task.models.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseDTO{
    private String statusCode;
    private String message;
}