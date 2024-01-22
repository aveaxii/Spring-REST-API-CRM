package task312.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import task312.demo.models.User;
import task312.demo.services.RegistrationService;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final RegistrationService registrationService;

    @Autowired
    public AuthController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "auth/login";
    }

    @GetMapping("/registration")
    public String getRegistrationPage(@ModelAttribute("user") User user) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String doRegistration(@ModelAttribute("user") User user) {
        try {
            registrationService.register(user);
        } catch (Exception e) {
            e.printStackTrace();
            return "auth/registration";
        }

        return "redirect:/auth/login";
    }
}
