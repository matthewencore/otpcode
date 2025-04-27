package otp.task.controller.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import otp.task.models.dto.ResponseDTO;
import otp.task.models.otp.OTPCode;
import otp.task.service.OTPCodeService;
import otp.task.service.file.FileService;

import java.io.ByteArrayInputStream;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;
    private final OTPCodeService otpCodeService;

    @GetMapping("/create")
    public ResponseEntity<?> createFile(Principal principal){


        if (principal == null) {
            ResponseDTO response = ResponseDTO.builder()
                    .statusCode(HttpStatus.UNAUTHORIZED.toString())
                    .message("Пользователь не существует либо не авторизован.")
                    .build();

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        List<OTPCode> listOTP = otpCodeService.getAllCode(principal.getName());

        ByteArrayInputStream listOTPCode = fileService.createListOTPCode(listOTP);

        String currentDate = LocalDate.now().toString();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=codes_" + currentDate + ".xlsx");

        log.info("Файл сгенерирован успешно. ");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(listOTPCode));
    }
}
