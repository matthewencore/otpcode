package otp.task.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import otp.task.models.dto.ConfigOTPCode;
import otp.task.models.dto.ResponseDTO;
import otp.task.models.mapper.MapperUserToDTO;
import otp.task.service.ConfigOTPCodeService;
import otp.task.service.UserService;

import java.security.Principal;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final ConfigOTPCodeService otpCodeService;
    private final UserService userService;
    private final MapperUserToDTO mapperUserToDTO;

    @PostMapping("/otp-config")
    public ResponseEntity<ResponseDTO> changeConfigurationOTPCode(@RequestBody ConfigOTPCode configOTPCode){

        otpCodeService.changeOTPConfig(configOTPCode);

        return ResponseEntity.ok(ResponseDTO.builder()
                .statusCode("200")
                .message("Изменения были успешны применены.")
                .build());
    }

    @PostMapping("/get-all-users")
    public ResponseEntity<?> getAllUsers(){

        return ResponseEntity.status(HttpStatus.OK.value())
                .body(userService.getAllUser()
                        .stream()
                        .map(mapperUserToDTO::createDTO)
                        .collect(Collectors.toSet())
                );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseDTO> deleteById(@PathVariable Long id, Principal principal){
        userService.deleteById(id,principal);

        return ResponseEntity.ok(ResponseDTO.builder()
                .statusCode("200")
                .message(String.format("Пользователь под id: %s был удален...",id))
                .build());
    }




}
