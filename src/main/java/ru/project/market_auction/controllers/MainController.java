package ru.project.market_auction.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.project.market_auction.models.auctions.Auction;
import ru.project.market_auction.models.sales.BookSale;
import ru.project.market_auction.models.sales.UserCart;
import ru.project.market_auction.models.users.User;
import ru.project.market_auction.repositories.AuctionRepository;
import ru.project.market_auction.repositories.BookSaleRepository;
import ru.project.market_auction.repositories.UserRepository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class MainController {
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


}
