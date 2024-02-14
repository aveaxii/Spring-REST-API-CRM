package task312.demo.RestControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import task312.demo.models.User;
import task312.demo.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class RestAdminController {

    private final UserService userService;
    @Autowired
    public RestAdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.findAll();
    }

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable("id") Long id) {
        return userService.findById(id);
    }

    @PostMapping("/add-user")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createRestfulUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PatchMapping("/user/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") Long id, @RequestBody User user) {
        User existingUser = userService.updateRestfulUser(id, user);
        return ResponseEntity.ok(existingUser);
    }

    @DeleteMapping("/user/{id}")
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
