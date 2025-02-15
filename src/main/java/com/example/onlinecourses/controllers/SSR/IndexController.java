package com.example.onlinecourses.controllers.SSR;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @GetMapping("/")
    public String index() {
        return "index"; // Refers to src/main/resources/templates/index.html
    }
    @GetMapping("/home")
    public String home(@AuthenticationPrincipal OAuth2User user, Model model) {
        String name = user.getAttribute("name");
        String email = user.getAttribute("email");
        model.addAttribute("userName", name);
        model.addAttribute("userEmail", email);
        return "home"; // Refers to src/main/resources/templates/home.html
    }
}
