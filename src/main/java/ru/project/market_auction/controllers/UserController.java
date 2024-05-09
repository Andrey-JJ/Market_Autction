package ru.project.market_auction.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.project.market_auction.models.Role;
import ru.project.market_auction.models.User;
import ru.project.market_auction.repositories.RoleRepository;
import ru.project.market_auction.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@RequestMapping("/users")
@Controller
public class UserController {
    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;

    @GetMapping("/main")
    public String getAllUsers(Model model){
        List<User> users = (List<User>) userRepository.findAll();
        model.addAttribute("users", users);
        return "user/main";
    }

    @GetMapping("/{id}")
    public String getUser(Model model, @PathVariable("id") Long id){
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()){
            return "redirect:/users/main";
        }
        model.addAttribute("user", user.get());
        return "user/detail";
    }

    @GetMapping("/new")
    public String addUser(Model model){
        List<Role> roles = (List<Role>) roleRepository.findAll();
        model.addAttribute("user", new User());
        model.addAttribute("roles", roles);
        return "user/add";
    }

    @PostMapping("/new")
    public String addUser(Model model, @ModelAttribute User user){
        userRepository.save(user);
        return "redirect:/users/" + user.getId();
    }

    @GetMapping("/update/{id}")
    public String editUser(Model model, @PathVariable("id") Long id){
        Optional<User> user = userRepository.findById(id);

        if(user.isEmpty()){
            return "redirect:/users/main";
        }

        List<Role> roles = (List<Role>) roleRepository.findAll();

        model.addAttribute("user", user.get());
        model.addAttribute("roles", roles);

        return "user/edit";
    }

    @PostMapping("/update")
    public String editUser(Model model, @ModelAttribute User user){
        userRepository.save(user);
        return "redirect:/users/" + user.getId();
    }

    @GetMapping("/delete/{id}")
    public String delUser(Model model, @PathVariable("id") Long id){
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()){
            return "redirect:/users/main";
        }
        model.addAttribute("user", user.get());
        return "user/del";
    }

    @PostMapping("/delete")
    public String delUser(@PathVariable("id") Long id){
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            return "redirect:/users/main";
        }
        userRepository.delete(user.get());
        return "user/del";
    }
}
