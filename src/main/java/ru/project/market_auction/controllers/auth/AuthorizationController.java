package ru.project.market_auction.controllers.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.project.market_auction.models.Role;
import ru.project.market_auction.models.User;
import ru.project.market_auction.repositories.RoleRepository;
import ru.project.market_auction.repositories.UserRepository;

import java.util.Optional;

@Controller
public class AuthorizationController {
    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @GetMapping("/registration")
    public String showRegistrationForm(Model model){
        model.addAttribute("user", new User());
        return "auth/registr";
    }

    @PostMapping("/registration")
    public String processRegistrationForm(Model model, @ModelAttribute User user){
        if(userRepository.findByLogin(user.getLogin()) != null) {
            return "redirect:/registration";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Optional<Role> role = roleRepository.findByName("USER");
        if(!role.isEmpty()) {
            user.setRole(role.get());
            userRepository.save(user);
        }
        return "redirect:/market/main";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model){
        return "auth/login";
    }

    @PostMapping("/login")
    public String showLoginForm(Model model, @RequestParam String loginOrEmail, @RequestParam String password){
        User user = userRepository.findByLogin(loginOrEmail);
        if(user == null){
            return "redirect:/login";
        }
        return "redirect:/";
    }
}
