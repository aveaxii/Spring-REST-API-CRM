package task312.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import task312.demo.exceptions.RoleNotFoundException;
import task312.demo.models.Role;
import task312.demo.models.User;
import task312.demo.repositories.RoleRepository;
import task312.demo.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RegistrationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Transactional
    public void register(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        Optional<Role> userRole = roleRepository.findByName("ROLE_USER");

        if (userRole.isPresent()) {
            user.setRoles(List.of(userRole.get()));
        } else {
            throw new RoleNotFoundException("ROLE_USER");
        }

        userRepository.save(user);
    }
}
