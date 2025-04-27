package otp.task.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import otp.task.models.role.DefaultRoleNotFound;
import otp.task.models.role.RoleUser;
import otp.task.repository.RoleRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleUser findRole(String nameRole){
        return roleRepository
                .findByNameRole(nameRole)
                .orElseThrow(() -> new DefaultRoleNotFound(""));
    }
    public void checkRoleOrCreate(String arg){
        try {
            // вызываем
            RoleUser role = findRole(arg);
            log.info(String.format("Предустановленная роль: %s не удалена",role.getNameRole()));

        } catch (DefaultRoleNotFound ex){
            log.warn("Отсутствует предустановленная роль, создаём...");

            roleRepository.save(new RoleUser(arg));
        }
    }
}
