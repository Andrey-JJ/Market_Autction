package ru.project.market_auction.controllers.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.project.market_auction.models.users.Role;
import ru.project.market_auction.models.users.User;
import ru.project.market_auction.repositories.RoleRepository;
import ru.project.market_auction.repositories.UserRepository;

import java.security.Principal;
import java.util.Optional;

@Controller
public class AuthorizationController {
    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private AuthenticationManager authenticationManager;
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
    public String showLoginForm(Model model, @RequestParam String loginOrEmail, @RequestParam String password, HttpServletResponse response, HttpServletRequest request){
        try {
            // Аутентификация пользователя
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginOrEmail, password));

            // Устанавливаем аутентифицированного пользователя в контекст безопасности
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Редирект на главную страницу после успешной аутентификации
            return "redirect:/";
        } catch (Exception e) {
            return "redirect:/login";
        }
    }

    @GetMapping("/change-password")
    public String changePassword(Model model) {
        return "auth/change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(Model model, @RequestParam String newPassword, @RequestParam String username){
        // Находим пользователя в репозитории по его логину
        //User user = userRepository.findByLogin(principal.getName());
        User user = userRepository.findByLogin(username);
        if(user == null){
            // Пользователь не найден
            return "redirect:/login";
        }

        // Обновляем пароль пользователя
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Перенаправляем пользователя на главную страницу
        return "redirect:/";
    }

}
