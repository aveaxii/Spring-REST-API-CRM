package task312.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/login")
    public String getLoginPage() {
        return "authPages/login";
    }

    @GetMapping("/registration")
    public String getRegistrationPage() {
        return "authPages/registration";
    }
}

