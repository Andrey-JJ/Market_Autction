package ru.project.market_auction.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.project.market_auction.models.auctions.Auction;
import ru.project.market_auction.models.sales.BookSale;
import ru.project.market_auction.repositories.AuctionRepository;
import ru.project.market_auction.repositories.BookSaleRepository;
import ru.project.market_auction.repositories.UserRepository;

import java.util.List;

@Controller
public class MainController {
    @Autowired private UserRepository userRepository;
    @Autowired private AuctionRepository auctionRepository;
    @Autowired private BookSaleRepository bookSaleRepository;
    @GetMapping("/")
    public String home(Model model) {
        /*if (principal != null) {
            String username = principal.getName();
            Optional<User> user = userRepository.findByLogin(username);
            model.addAttribute("user", user.get());
        }*/

        List<Auction> auctions = (List<Auction>) auctionRepository.findAll();
        List<BookSale> bookSales = (List<BookSale>) bookSaleRepository.findAll();

        model.addAttribute("auctions", auctions);
        model.addAttribute("bookSales", bookSales);

        return "main/index";
    }
}
