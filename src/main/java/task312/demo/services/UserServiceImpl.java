package task312.demo.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import task312.demo.models.Role;
import task312.demo.models.User;
import task312.demo.repositories.RoleRepository;
import task312.demo.repositories.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        Optional<User> foundUser = userRepository.findById(id);

        return foundUser.orElseThrow(EntityNotFoundException::new);
    }

    @Override
    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void update(Long id, User updatedUser) {
        updatedUser.setId(id);
        userRepository.save(updatedUser);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Optional<User> userToDelete = userRepository.findById(id);

        if (userToDelete.isPresent()) {
            userRepository.delete(userToDelete.get());
        } else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    @Transactional
    public void deleteAll() {
        userRepository.deleteAll();
    }

    @Override
    public User findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UsernameNotFoundException(email);
        }
    }

    @Transactional
    public User createRestfulUser(User user) {
        Collection<Role> roles = new ArrayList<>();
        checkAndSetRolesCollectionForUser(roles, user);
        user.setRoles(roles); // If the selected role is ADMIN, add ROLE_ADMIN to the user

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        save(user);

        return user;
    }

    @Transactional
    public User updateRestfulUser(Long id, User user) {
        Optional<User> uncheckedUser = userRepository.findById(id);

        User existingUser;
        if (uncheckedUser.isPresent()) {
            existingUser = uncheckedUser.get();
        } else {
            throw new EntityNotFoundException();
        }



        Collection<Role> roles = new ArrayList<>();
        checkAndSetRolesCollectionForUser(roles, user);
        existingUser.setRoles(roles); // If the selected role is ADMIN, add ROLE_ADMIN to the user

        String currentPassword = existingUser.getPassword();

        existingUser.setName(user.getName());
        existingUser.setSurname(user.getSurname());
        existingUser.setBirthYear(user.getBirthYear());
        existingUser.setEmail(user.getEmail());

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            existingUser.setPassword(currentPassword);
        } else {
            String newEncryptedPassword = passwordEncoder.encode(user.getPassword());
            existingUser.setPassword(newEncryptedPassword);
        }

        update(id, existingUser);

        return existingUser;
    }

    public User getUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Optional<User> uncheckedUser = userRepository.findByEmail(userDetails.getUsername());

        User user;
        if (uncheckedUser.isPresent()) {
            user = uncheckedUser.get();
        } else {
            throw new EntityNotFoundException();
        }

        return user;
    }

    private void checkAndSetRolesCollectionForUser(Collection<Role> roles, User user) {
        checkIfDefaultRolesExistsOrCreateIfAbsent();

        Role userRole = roleRepository.findByName("ROLE_USER").get();
        roles.add(userRole);

        if (!(user.getRoles() == null)) {
            if (user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"))) {
                Role adminRole = roleRepository.findByName("ROLE_ADMIN").get();
                roles.add(adminRole);
            }
        }
    }

    private void checkIfDefaultRolesExistsOrCreateIfAbsent() {
        roleRepository.findByName("ROLE_ADMIN").orElseGet(() -> roleRepository.save(new Role(1, "ROLE_ADMIN")));
        roleRepository.findByName("ROLE_USER").orElseGet(() -> roleRepository.save(new Role(2, "ROLE_USER")));
    }
}
