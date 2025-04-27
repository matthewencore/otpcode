package otp.task.controller.auth;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import otp.task.models.dto.ResponseDTO;
import otp.task.models.user.User;
import otp.task.models.dto.LoginRequest;
import otp.task.models.dto.LoginResponse;
import otp.task.models.role.RoleUser;
import otp.task.repository.UserRepository;
import otp.task.service.AuthUserService;
import otp.task.service.RoleService;
import otp.task.service.jwt.JwtService;

import java.util.Set;


@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthApiController {

    private final AuthenticationManager authenticationManager;
    private final AuthUserService userService;
    private final JwtService jwtService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        log.info("Авторизация {}", request.username());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        UserDetails user = userService.loadUserByUsername(request.username());
        String token = jwtService.generateToken(user);
        log.info(token);
        return ResponseEntity.ok(new LoginResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> register(@RequestBody LoginRequest request, User user) {
        RoleUser userRole = roleService.findRole("USER");

        User newUser = User.builder()
                .password(passwordEncoder.encode(request.password()))
                .username(request.username())
                .roleUsers(Set.of(userRole))
                .enabled(true)
                .build();

        userRepository.save(newUser);

        return ResponseEntity.ok(ResponseDTO.builder()
                .statusCode("200")
                .message("Успешная регистрация")
                .build());
    }

}
