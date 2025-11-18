package edu.guvi.kaddysShop.Controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import edu.guvi.kaddysShop.Model.User;
import edu.guvi.kaddysShop.Service.UserService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    // Registration Page
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register"; // templates/register.html
    }

    // Handle Register
    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {

        try {
            userService.register(user);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("user", user);
            return "register";
        }

        return "redirect:/auth/login";
    }

    // Login Page
    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // templates/login.html
    }

    // Profile Page
    @GetMapping("/profile")
    public String userProfile(Authentication auth, Model model) {

        if (auth == null) {
            return "redirect:/auth/login";
        }

        User user = userService.findByEmail(auth.getName());
        model.addAttribute("user", user);

        return "profile"; // templates/profile.html
    }

    // Update Profile
    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute User updated, Authentication auth) {

        User user = userService.findByEmail(auth.getName());
        userService.updateProfile(user.getId(), updated);

        return "redirect:/auth/profile?success";
    }
}
