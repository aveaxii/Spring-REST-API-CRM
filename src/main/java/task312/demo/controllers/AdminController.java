package task312.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("")
    public String getAdminPage() {
        return "adminPages/users";
    }

    // Content pages

    @GetMapping("/user-content-sidebar")
    public String getUserSidebarContentPage() {
        return "contentPages/user-content-sidebar";
    }

    @GetMapping("/admin-content-card-sidebar")
    public String getAdminSidebarContentPage() {
        return "contentPages/admin-content-card-sidebar";
    }

    @GetMapping("/add-content-card-tab")
    public String getAddTabContentPage() {
        return "contentPages/add-content-card-tab";
    }

    @GetMapping("/admin-content-card-tab")
    public String getAdminContentCardPage() {
        return "contentPages/admin-content-card-tab";
    }
}
