package ru.project.market_auction.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.project.market_auction.models.Role;
import ru.project.market_auction.models.User;
import ru.project.market_auction.models.UserRole;
import ru.project.market_auction.repositories.RoleRepository;
import ru.project.market_auction.repositories.UserRepository;
import ru.project.market_auction.repositories.UserRoleRepository;

import java.util.Optional;

@Controller
public class RegistrationController {
    @Autowired UserRepository userRepository;
    @Autowired RoleRepository roleRepository;
    @Autowired UserRoleRepository userRoleRepository;

    @GetMapping("/registration")
    public String showRegistrationForm(Model model){
        model.addAttribute("user", new User());
        return "auth/registr";
    }

    @PostMapping("/registration")
    public String showRegistrationForm(Model model, @ModelAttribute User user){
        Optional<Role> role = roleRepository.findByName("USER");
        UserRole userRole = new UserRole(user, role.get());
        user.getRoles().add(userRole);
        userRepository.save(user);
        userRoleRepository.save(userRole);
        return "redirect:/market/main";
    }
}
