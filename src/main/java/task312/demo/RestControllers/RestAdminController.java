package task312.demo.RestControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import task312.demo.models.Role;
import task312.demo.models.User;
import task312.demo.services.RoleService;
import task312.demo.services.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class RestAdminController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Autowired
    public RestAdminController(UserService userService, PasswordEncoder passwordEncoder, RoleService roleService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") Long id) {
        return userService.findById(id);
    }

    @PostMapping("/add-user")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        Collection<Role> roles = new ArrayList<>();
        if (user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"))) {
            Role adminRole = roleService.findByName("ROLE_ADMIN");
            roles.add(adminRole);
        }
        Role userRole = roleService.findByName("ROLE_USER");
        roles.add(userRole);

        user.setRoles(roles);

        userService.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") Long id, @RequestBody User user) {
        User existingUser = userService.findById(id);

        String currentPassword = existingUser.getPassword();

        existingUser.setName(user.getName());
        existingUser.setSurname(user.getSurname());
        existingUser.setBirthYear(user.getBirthYear());
        existingUser.setEmail(user.getEmail());

        Collection<Role> roles = new ArrayList<>();
        if (user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"))) {
            Role adminRole = roleService.findByName("ROLE_ADMIN");
            roles.add(adminRole);
        }
        Role userRole = roleService.findByName("ROLE_USER");
        roles.add(userRole);

        // If the selected role is ADMIN, add ROLE_ADMIN to the user


        existingUser.setRoles(roles);

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            existingUser.setPassword(currentPassword);
        } else {
            String newEncryptedPassword = passwordEncoder.encode(user.getPassword());
            existingUser.setPassword(newEncryptedPassword);
        }

        userService.update(id, existingUser);

        return ResponseEntity.ok(existingUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        userService.delete(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete-all-users")
    public ResponseEntity<Void> deleteAllUsers() {
        userService.deleteAll();
        return ResponseEntity.ok().build();
    }
}
