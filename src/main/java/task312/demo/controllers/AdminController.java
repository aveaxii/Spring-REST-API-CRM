package task312.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import task312.demo.exceptions.RoleNotFoundException;
import task312.demo.models.Role;
import task312.demo.models.User;
import task312.demo.services.RoleService;
import task312.demo.services.UserService;

import java.util.List;
import java.util.Optional;

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
        model.addAttribute("allUsers", userService.findAll());
        return "admin/users";
    }

    @GetMapping("/{id}")
    public String user(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.findById(id));
        return "admin/user";
    }

    @GetMapping("/add")
    public String addUser(@ModelAttribute("user") User user) {
        return "admin/add";
    }

    @PostMapping("")
    public String create(@ModelAttribute("user") User user) {
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

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") Long id) {
        model.addAttribute("user", userService.findById(id));
        return "admin/edit";
    }

    @PostMapping("/{id}")
    public String update(@ModelAttribute User user, @PathVariable("id") Long id) {
        User existingUser = userService.findById(id);

        String currentPassword = existingUser.getPassword();

        existingUser.setName(user.getName());
        existingUser.setSurname(user.getSurname());
        existingUser.setBirthYear(user.getBirthYear());

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            existingUser.setPassword(currentPassword);
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
