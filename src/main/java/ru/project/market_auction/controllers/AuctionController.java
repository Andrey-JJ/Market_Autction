package ru.project.market_auction.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.project.market_auction.models.auctions.Auction;
import ru.project.market_auction.models.users.User;
import ru.project.market_auction.repositories.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequestMapping("/auctions")
@Controller
public class AuctionController {
    @Autowired private AuctionRepository auctionRepository;
    @Autowired private AuctionDetailRepository auctionDetailRepository;
    @Autowired private AuctionBidRepository auctionBidRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private BookRepository bookRepository;

    @GetMapping("/main")
    public String getAllAuctions(Model model){
        List<String> auctionTypes = new ArrayList<>();
        auctionTypes.add("Ограниченный");
        auctionTypes.add("Неограниченный");

        List<Auction> auctions = (List<Auction>) auctionRepository.findAll();
        model.addAttribute("auctions", auctions);
        model.addAttribute("auctionTypes", auctionTypes);
        return "auction/main";
    }

    @GetMapping("/{id}")
    public String getAuction(Model model, @PathVariable("id") Long id){
        Optional<Auction> auction = auctionRepository.findById(id);
        if (auction.isEmpty()){
            return "redirect:/auctions/main";
        }
        model.addAttribute("auction", auction.get());
        return "auction/detail";
    }

    @PostMapping("/createAuction")
    public String createAuction(Model model,
                                @RequestParam("auctionType") String auctionType){
        Auction auction = new Auction();

        Optional<User> user = userRepository.findById(1L);
        if (user.isEmpty())
            return "redirect:/auctions/main";

        auction.setUser(user.get());

        // Устанавливаем тип аукциона в зависимости от выбранного значения в форме
        if ("Ограниченный".equals(auctionType)) {
            auction.setAuctionType("lt");
        } else if("Неограниченный".equals(auctionType)) {
            auction.setAuctionType("ult");
        }

        auctionRepository.save(auction);

        return "redirect:/auctions/" + auction.getId();
    }
}
