package ru.project.market_auction.controllers.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.project.market_auction.repositories.RoleRepository;
import ru.project.market_auction.repositories.UserRepository;

@Controller
public class LoginController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
}
