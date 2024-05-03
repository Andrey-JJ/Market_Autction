package ru.project.market_auction.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.project.market_auction.models.Role;
import ru.project.market_auction.repositories.RoleRepository;
import ru.project.market_auction.repositories.RoleRepository;

import java.util.List;
import java.util.Optional;

@RequestMapping("/roles")
@Controller
public class RoleController {
    @Autowired RoleRepository roleRepository;

    @GetMapping("/main")
    public String getAllRoles(Model model){
        List<Role> roles = (List<Role>) roleRepository.findAll();
        model.addAttribute("roles", roles);
        return "role/main";
    }

    @GetMapping("/{id}")
    public String getRole(Model model, @PathVariable("id") Long id){
        Optional<Role> role = roleRepository.findById(id);
        if(role.isEmpty()){
            return "redirect:/roles/main";
        }
        model.addAttribute("role", role.get());
        return "role/detail";
    }

    @GetMapping("/new")
    public String addRole(Model model){
        model.addAttribute("role", new Role());
        return "role/add";
    }

    @PostMapping("/new")
    public String addRole(@ModelAttribute Role role, Model model){
        roleRepository.save(role);
        return "redirect:/roles/main";
    }

    @GetMapping("/update/{id}")
    public String editRole(Model model, @PathVariable("id") Long id){
        Optional<Role> role = roleRepository.findById(id);
        if(role.isEmpty()){
            return "redirect:/roles/main";
        }
        model.addAttribute("role", role.get());
        return "role/edit";
    }

    @PostMapping("/update")
    public String editRole(Model model, @ModelAttribute Role role){
        roleRepository.save(role);
        return "redirect:/roles/main";
    }

    @GetMapping("/delete/{id}")
    public String delRole(Model model, @PathVariable("id") Long id){
        Optional<Role> role = roleRepository.findById(id);
        if(role.isEmpty()){
            return "redirect:/roles/main";
        }
        model.addAttribute("role", role.get());
        return "role/del";
    }

    @PostMapping("/delete")
    public String delRole(@PathVariable("id") Long id){
        Optional<Role> role = roleRepository.findById(id);
        if(role.isPresent()){
            roleRepository.delete(role.get());
        }
        return "redirect:/roles/main";
    }
}
