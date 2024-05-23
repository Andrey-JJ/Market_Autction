package ru.project.market_auction.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.project.market_auction.models.users.Role;
import ru.project.market_auction.models.users.User;
import ru.project.market_auction.repositories.RoleRepository;
import ru.project.market_auction.repositories.UserRepository;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("")
public class AuthorizationController {
    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private AuthenticationManager authenticationManager;
    //@Autowired private PasswordEncoder passwordEncoder;

    @GetMapping("/registration")
    public String showRegistrationForm(HttpServletRequest request, HttpServletResponse response, Model model){
        model.addAttribute("user", new User());
        return "auth/registr";
    }

    @PostMapping("/registration")
    public String processRegistrationForm(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("user")User user, Model model){
        try{
            Role role = roleRepository.findByName("USER");
            user.setRole(role);

            userRepository.save(user);

            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword()));
            SecurityContext securityContext = SecurityContextHolder.getContext();
            HttpSession session = request.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,securityContext);

            return "redirect:/";
        }
        catch (Exception e){
            return "redirect:/registration?error";
        }
    }

    @GetMapping("/login")
    public String showLoginForm(){
        return "auth/login";
    }

    @GetMapping("/change-password")
    public String changePassword(Model model) {
        return "auth/change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(Model model, @RequestParam String newPassword, @RequestParam String username){
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.invalidate();
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(null);
        return "redirect:/";
    }
}
