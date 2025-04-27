package otp.task.controller.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import otp.task.models.dto.ResponseDTO;
import otp.task.models.exception.DeleteException;
import otp.task.models.exception.DoNotFoundOTPCode;
import otp.task.models.exception.ErrorGenerateOTPCode;
import otp.task.models.exception.UserNotFound;
import otp.task.service.telegram.BadChatIDTelegram;


@RestControllerAdvice
@Slf4j
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(BadChatIDTelegram.class)
    public ResponseEntity<ResponseDTO> handlerTelegramException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(
                ResponseDTO.builder().statusCode(HttpStatus.BAD_REQUEST.toString()).message("Не верный чат id").build());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(DoNotFoundOTPCode.class)
    public ResponseEntity<ResponseDTO> handlerValidateOTPCode() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(
                ResponseDTO.builder().statusCode(HttpStatus.BAD_REQUEST.toString()).message("Введенный вами ОТП код не валиден").build());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ErrorGenerateOTPCode.class)
    public ResponseEntity<ResponseDTO> handlerValidateOTPCode2() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(
                ResponseDTO.builder().statusCode(HttpStatus.BAD_REQUEST.toString()).
                        message("Возникла ошибка в процессе генерации ОТП кодов, пожалуйста убедитесь что конфигурация не нарушает политику генерации ОТП кода").build());
    }
    @org.springframework.web.bind.annotation.ExceptionHandler(DeleteException.class)
    public ResponseEntity<ResponseDTO> deleteHandler() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(
                ResponseDTO.builder().statusCode(HttpStatus.BAD_REQUEST.toString()).
                        message("Возникла ошибка в процессе удаления пользователя, " +
                                "вы пытаетесь удалить администратора или зарезервованого пользователя, " +
                                "либо себя. ").build());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UserNotFound.class)
    public ResponseEntity<ResponseDTO> userNotFoundHandler() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(
                ResponseDTO.builder().statusCode(HttpStatus.NOT_FOUND.toString()).
                        message("Выполнить операцию с данным пользователем не удалось, по причине того что его не существует в информационной базе. ").build());
    }



}
