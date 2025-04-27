package otp.task.controller.otp;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import otp.task.models.dto.CodeOtp;
import otp.task.models.dto.ResponseDTO;
import otp.task.service.OTPCodeService;
import otp.task.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/api/otp")
@RequiredArgsConstructor
public class OTPCodeController {

    private final OTPCodeService otpCodeService;
    private final UserService userService;

    @PostMapping("/validate")
    public ResponseEntity<ResponseDTO> validateOTP(@Valid @RequestBody CodeOtp otp, Principal principal) {
        if (principal == null) {
            ResponseDTO response = ResponseDTO.builder()
                    .statusCode(HttpStatus.UNAUTHORIZED.toString())
                    .message("Пользователь не авторизирован / Не существует.")
                    .build();
            return ResponseEntity.ok(response);
        }

        otpCodeService.validateOTPCode(otp.getCode(),principal.getName());

        ResponseDTO response = ResponseDTO.builder()
                .statusCode(HttpStatus.OK.toString())
                .message("Валидация прошла успешно.")
                .build();
        return ResponseEntity.ok(response);
    }
}
