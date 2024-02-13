package task312.demo.RestControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import task312.demo.models.User;
import task312.demo.services.RegistrationService;

@RestController
@RequestMapping("/api/auth")
public class RestAuthController {

    private final RegistrationService registrationService;

    @Autowired
    public RestAuthController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/registration")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        try {
            registrationService.register(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
