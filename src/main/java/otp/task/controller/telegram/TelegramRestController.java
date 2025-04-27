package otp.task.controller.telegram;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import otp.task.models.dto.ResponseDTO;
import otp.task.models.user.User;
import otp.task.models.dto.TelegramChatId;
import otp.task.service.OTPCodeService;
import otp.task.service.UserService;
import otp.task.service.telegram.OtpTelegramBot;

import java.security.Principal;

@Slf4j
@RequiredArgsConstructor
@RestController()
@RequestMapping("/api/telegram")
public class TelegramRestController {

    private final UserService userService;
    private final OtpTelegramBot telegramService;
    private final OTPCodeService otpCodeService;

    @GetMapping("/get/status-link")
    public ResponseEntity<ResponseDTO> checkChatId(Principal principal){
        log.info("Пользователь {} вызвал проверку chatID", principal.getName());
        boolean b = userService.existChatId(userService.findUser(principal.getName()));

        ResponseDTO response = ResponseDTO.builder()
                .statusCode(HttpStatus.OK.toString())
                .message(String.valueOf(b))
                .build();


        return ResponseEntity.ok(response);
    }

    @PostMapping("/send-telegram")
    public ResponseEntity<ResponseDTO> sendCodeTG(Principal principal) throws TelegramApiException {
        User user = userService.findUser(principal.getName());

        if (!userService.existChatId(user)) {
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDTO.builder()
                 .statusCode(HttpStatus.BAD_REQUEST.toString())
                 .message("noChatId")
                 .build());
        }

        String otpCode = otpCodeService.createOTPForUser(user);
        String chatId = user.getTelegramChats().getChat_id();

        telegramService.sendTextMessage(chatId,otpCode, user.getUsername());


        ResponseDTO response = ResponseDTO.builder()
                .statusCode(HttpStatus.OK.toString())
                .message("Отправлено")
                .build();


        return ResponseEntity.ok(response);
    }

    @PostMapping("/fill-chat-id")
    public ResponseEntity<ResponseDTO> createChatId(
        @Valid
        @RequestBody TelegramChatId telegramChats,
        Principal principal) {

        User user = userService.findUser(principal.getName());

        userService.addToUserChatId(user,telegramChats.getChatID());

        ResponseDTO response = ResponseDTO.builder()
                .statusCode(HttpStatus.OK.toString())
                .message("Изменено на " + telegramChats.getChatID())
                .build();

        return ResponseEntity.ok(response);
    }

}
