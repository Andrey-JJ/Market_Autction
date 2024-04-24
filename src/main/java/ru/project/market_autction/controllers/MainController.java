package ru.projects.market_auction.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Optional;

@Controller
public class MainController {
    @GetMapping("/")
    public String showMain(Model model){
        return "main/index";
    }
}
