package task312.demo;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import task312.demo.models.Role;
import task312.demo.models.User;
import task312.demo.repositories.RoleRepository;
import task312.demo.services.RegistrationService;

import java.util.Optional;

@Component
public class PresetLoader {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RegistrationService registrationService;

    @PostConstruct
    public void init() {
        createRoleIfNotFound("ROLE_ADMIN");
        createRoleIfNotFound("ROLE_USER");
        User admin = new User();
        admin.setName("admin");
        admin.setPassword("1");
        admin.setEmail("1@1");
        admin.setSurname("admin");
        admin.setBirthYear(1);
        registrationService.register(admin);
    }

    private void createRoleIfNotFound(String name) {
        Optional<Role> role = roleRepository.findByName(name);
        if (role.isEmpty()) {
            roleRepository.save(new Role(name));
        }
    }
}
