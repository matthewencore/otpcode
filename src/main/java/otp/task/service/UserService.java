package otp.task.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import otp.task.models.exception.DeleteException;
import otp.task.models.exception.UserNotFound;
import otp.task.models.telegram.TelegramChats;
import otp.task.models.user.User;
import otp.task.models.role.RoleUser;
import otp.task.repository.TelegramChatIDRepository;
import otp.task.repository.UserRepository;

import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final TelegramChatIDRepository telegramChatIDRepository;

    public boolean isExistUser(User user){
       return userRepository.existsById(user.getId());
    }
    public boolean isExist(String username){
        return userRepository.existsByUsername(username);
    }

    public boolean existChatId(User user) {
        if (!isExistUser(user)) return false;
        log.info("Пользователь имеется: {}",user.getUsername());

        if (user.getTelegramChats() == null) {
            log.warn("У пользователя нет активного чата, необходимо привязать");
            return false;
        }
        log.info("У пользователя есть синхронизация с телеграм-id");
        return true;
    }

    public User findUser(String username){
        return userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("Не корректный логин"));
    }

    public void createDefaultAdmin(){
        if (userRepository.existsByUsername("admin")) {
            log.info("Уже имеется пользователь с username admin, " +
                    "удалите текущий иначе пользователь с правами администратора не будет создан.");
            return;
        }

        RoleUser userRole = roleService.findRole("ADMIN");
        String password = String.valueOf(UUID.randomUUID());

        userRepository.save(User.builder()
                .username("admin")
                .password(passwordEncoder.encode(password))
                .roleUsers(Set.of(userRole))
                .enabled(true)
                .build());

        log.info("Был создан дефолтный администратор, пароль от него [{}]",password);
    }

    public void addToUserChatId(User user, String chatId){
        if (user.getTelegramChats() != null) {
            user.setTelegramChats(null);
            userRepository.save(user);
            log.info("Обнуляем текущий сhatId, присваиваем null");
        }

        log.info("Заполнение telegramID для пользователя: {}",user.getUsername());
        TelegramChats tgChat = TelegramChats.builder()
                .chat_id(chatId)
                .build();

        // Сохраняем телеграмЧат
        telegramChatIDRepository.save(tgChat);

        user.setTelegramChats(tgChat);
        userRepository.save(user);
    }

    public List<User> getAllUser(){
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getRoleUsers()
                        .stream()
                        .map(roleUser -> roleUser.getNameRole())
                        .anyMatch(string -> string.equalsIgnoreCase("USER"))

                ).collect(Collectors.toList());
    }

    public void deleteById(Long id, Principal principal){

        User user = userRepository.
                findById(id)
                .orElseThrow(() -> new UserNotFound("Пользователь не найден"));

        if (user.getUsername().equalsIgnoreCase("admin")) {
            log.error("Вы не можете удалить предустановленного администратора. ");
            throw new DeleteException("Вы не можете удалить предустановленного администратора.");
        }

        if (user.getUsername().equalsIgnoreCase(principal.getName())) {
            log.error("Вы не можете удалить себя же. ");
            throw new DeleteException("Вы не можете удалить себя же.");
        }

        if (user.getRoleUsers().stream().anyMatch(roleUser -> roleUser.getNameRole().equalsIgnoreCase("ADMIN"))) {
            log.error("Вы не можете удалить администратора. ");

            throw new DeleteException("Вы не можете удалить администратора.");
        }

        userRepository.deleteById(id);
        log.info("Пользователь {} был успешно удалён с информационной базы. ",user.getUsername());
    }

    @Transactional
    public void otpAccessGrant(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Не корректный аргумент для пользователя, пользователь не найден"));

        log.info("Выдан доступ к остальным роутам, OTP подтвержден.");

        user.setOtpVerified(true);
        userRepository.save(user);
    }
}
