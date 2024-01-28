package task312.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import task312.demo.exceptions.RoleNotFoundException;
import task312.demo.models.Role;
import task312.demo.models.User;
import task312.demo.services.RoleService;
import task312.demo.services.UserService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, PasswordEncoder passwordEncoder, RoleService roleService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    @GetMapping("")
    public String allUsers(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        User user = userService.findByEmail(userDetails.getUsername());

        Long currentUserId = user.getId();

        model.addAttribute("currentUserId", currentUserId);
        model.addAttribute("user", user);
        model.addAttribute("allUsers", userService.findAll());
        return "admin/users";
    }

    @GetMapping("/user")
    public String getUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        User user = userService.findByEmail(userDetails.getUsername());

        Long currentUserId = user.getId();

        model.addAttribute("currentUserId", currentUserId);
        model.addAttribute("user", user);
        return "admin/user";
    }


    @GetMapping("/add")
    public String getAddUser(@ModelAttribute("user") User userToCreate, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        User user = userService.findByEmail(userDetails.getUsername());

        model.addAttribute("adminUser", user);

        return "admin/add";
    }

    @PostMapping("/add")
    public String postCreateUser(@ModelAttribute("user") User user) {
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);

        Optional<Role> userRole = roleService.getAllRoles().stream()
                .filter(role -> "ROLE_USER".equals(role.getName()))
                .findFirst();

        if (userRole.isPresent()) {
            user.setRoles(List.of(userRole.get()));
        } else {
            throw new RoleNotFoundException("ROLE_USER");
        }

        userService.save(user);

        return "redirect:/admin";
    }

    @PostMapping("/{id}")
    public String postUpdate(@ModelAttribute User user, @PathVariable("id") Long id) {
        User existingUser = userService.findById(id);

        String currentPassword = existingUser.getPassword();

        existingUser.setName(user.getName());
        existingUser.setSurname(user.getSurname());
        existingUser.setBirthYear(user.getBirthYear());
        existingUser.setEmail(user.getEmail());

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            existingUser.setPassword(currentPassword);
        } else {
            String encryptedPassword = passwordEncoder.encode(user.getPassword());
            existingUser.setPassword(encryptedPassword);
        }

        userService.update(id, existingUser);
        return "redirect:/admin";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }

    @PostMapping("/deleteAll")
    public String deleteAll() {
        userService.deleteAll();
        return "redirect:/admin";
    }
}
