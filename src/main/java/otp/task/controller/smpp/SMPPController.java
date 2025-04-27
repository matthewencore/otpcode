package otp.task.controller.smpp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import otp.task.models.dto.ResponseDTO;
import otp.task.models.dto.SMPP_DTO;
import otp.task.models.user.User;
import otp.task.service.OTPCodeService;
import otp.task.service.UserService;
import otp.task.service.smpp.SMPPService;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/api/smpp")
@RequiredArgsConstructor
public class SMPPController {
    private final SMPPService smppService;
    private final OTPCodeService otpCodeService;
    private final UserService userService;

    @PostMapping("/send-otp")
    public ResponseEntity<ResponseDTO> sendOTP(@RequestBody SMPP_DTO smppDto, Principal principal){
        User user = userService.findUser(principal.getName());
        String otpCode = otpCodeService.createOTPForUser(user);

        smppService.sendCode("+79900006294",otpCode);

        log.info("Сообщение было отправлен на номер " + smppDto.getNumber());

        ResponseDTO response = ResponseDTO.builder()
                .statusCode(HttpStatus.OK.toString())
                .message("Сообщение успешно отправлено.")
                .build();

        return ResponseEntity.ok(response);
    }
}
