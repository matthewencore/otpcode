package otp.task.component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import otp.task.service.ConfigOTPCodeService;
import otp.task.service.RoleService;
import otp.task.service.UserService;
import otp.task.utils.OtpSchedule;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    private final RoleService roleService;
    private final ConfigOTPCodeService configOTPCodeService;
    private final OtpSchedule checkStatus;
    private final UserService userService;

    @Override
    public void run(String... args) throws Exception {
        log.info("Проверяем целостность таблицы с ролями");

        roleService.checkRoleOrCreate("ADMIN");
        roleService.checkRoleOrCreate("USER");

        log.info("Проверка таблицы с OTP кодами");

        // Конфигурация ОТП
        configOTPCodeService.getOrCreateOTP();

        // Проверка статусов кодов
        checkStatus.checkStatus();

        // Создание дефолтного админа
        userService.createDefaultAdmin();
    }
}
