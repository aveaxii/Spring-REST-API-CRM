package task312.demo.RestControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import task312.demo.models.User;
import task312.demo.services.UserService;

@RestController
@RequestMapping("/api/auth")
public class RestAuthController {

    private final UserService userService;

    @Autowired
    public RestAuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/registration")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        try {
            userService.createRestfulUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
