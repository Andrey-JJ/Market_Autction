package ru.project.market_auction.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.project.market_auction.models.User;
import ru.project.market_auction.repositories.UserRepository;

import java.security.Principal;
import java.util.Optional;

@Controller
public class MainController {
    @Autowired private UserRepository userRepository;
    @GetMapping("/")
    public String home(Model model, Principal principal) {
        /*if (principal != null) {
            String username = principal.getName();
            Optional<User> user = userRepository.findByLogin(username);
            model.addAttribute("user", user.get());
        }*/
        return "main/index";
    }
}
