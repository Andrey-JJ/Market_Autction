package ru.project.market_auction.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.project.market_auction.models.auctions.Auction;
import ru.project.market_auction.models.sales.BookSale;
import ru.project.market_auction.models.users.User;
import ru.project.market_auction.repositories.AuctionRepository;
import ru.project.market_auction.repositories.BookSaleRepository;
import ru.project.market_auction.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Controller
public class MainController {
    @Autowired private UserRepository userRepository;
    @Autowired private AuctionRepository auctionRepository;
    @Autowired private BookSaleRepository bookSaleRepository;
    @GetMapping("/")
    public String home(Model model) {

        List<Auction> auctions = (List<Auction>) auctionRepository.findAll();
        List<BookSale> bookSales = (List<BookSale>) bookSaleRepository.findAll();

        model.addAttribute("auctions", auctions);
        model.addAttribute("bookSales", bookSales);

        return "main/index";
    }

    @GetMapping("/profile/{username}")
    public String getUserProfile(Model model, @PathVariable("username") String usernameOrEmail) {
        Optional<User> user = userRepository.findByLoginOrEmail(usernameOrEmail);
        if (user.isEmpty()) {
            return "redirect:/";
        }
        model.addAttribute("user", user.get());
        return "profile/detail";
    }

    @GetMapping("/profile/cart/{username}")
    public String getUserCart(Model model, @PathVariable("username") String usernameOrEmail) {
        Optional<User> user = userRepository.findByLoginOrEmail(usernameOrEmail);
        if (user.isEmpty()) {
            return "redirect:/";
        }
        model.addAttribute("cartDetails", user.get().getUserCarts());
        return "profile/cart";
    }
}
