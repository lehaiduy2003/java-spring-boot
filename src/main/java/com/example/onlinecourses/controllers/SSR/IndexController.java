package com.example.onlinecourses.controllers.SSR;

import com.example.onlinecourses.configs.impls.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @GetMapping("/index")
    public String index() {
        return "index"; // Refers to src/main/resources/templates/index.html
    }
    @GetMapping("/home")
    public String home(@AuthenticationPrincipal Object principal, Model model) {
        if(principal instanceof OAuth2User oAuth2User) {
            String name = oAuth2User.getAttribute("name");
            String email = oAuth2User.getAttribute("email");
            model.addAttribute("userName", name);
            model.addAttribute("userEmail", email);
        } else if(principal instanceof UserDetailsImpl userDetails) {
            model.addAttribute("userName", userDetails.getFullname());
            model.addAttribute("userEmail", userDetails.getEmail());
        }
        return "home"; // Refers to src/main/resources/templates/home.html
    }
}
