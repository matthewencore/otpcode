package otp.task.controller.email;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import otp.task.models.dto.EmailDTO;
import otp.task.models.dto.ResponseDTO;
import otp.task.models.user.User;
import otp.task.service.OTPCodeService;
import otp.task.service.UserService;
import otp.task.service.email.EmailService;

import java.security.Principal;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/email")
public class EmailController {
    private final EmailService emailController;
    private final OTPCodeService otpCodeService;
    private final UserService userService;

    @PostMapping("/send-otp")
    public ResponseEntity<ResponseDTO> sendEmail(
            @Valid @RequestBody EmailDTO emailDTO,
            Principal principal){
        User user = userService.findUser(principal.getName());

        String otpForUser = otpCodeService.createOTPForUser(user);
        log.info("ОТП ключ успешно создан, отправляем по почте. ");

        emailController.sendSimpleEmail(
                emailDTO.getTo(),
                emailDTO.getSubject(),
                emailDTO.getMessage(),
                otpForUser
        );

        ResponseDTO response = ResponseDTO.builder()
                .statusCode(HttpStatus.OK.toString())
                .message("Письмо успешно отправлено.")
                .build();

        return ResponseEntity.ok(response);
    }
}
