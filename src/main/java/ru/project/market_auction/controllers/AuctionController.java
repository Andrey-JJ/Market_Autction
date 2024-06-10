package ru.project.market_auction.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.project.market_auction.models.auctions.Auction;
import ru.project.market_auction.models.auctions.AuctionBid;
import ru.project.market_auction.models.auctions.AuctionDTO;
import ru.project.market_auction.models.auctions.AuctionDetail;
import ru.project.market_auction.models.books.Book;
import ru.project.market_auction.models.sales.BookSale;
import ru.project.market_auction.models.users.User;
import ru.project.market_auction.repositories.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
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
        List<Auction> auctions = (List<Auction>) auctionRepository.findAll();
        model.addAttribute("auctions", auctions);
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

    @GetMapping("/new")
    public String createAuction(Model model){
        List<String> auctionTypes = new ArrayList<>();
        auctionTypes.add("Завершить по последней ставке");
        auctionTypes.add("Завершить по времени");

        List<Book> books = (List<Book>) bookRepository.findAll();

        model.addAttribute("newAuction", new AuctionDTO());
        model.addAttribute("auctionTypes", auctionTypes);
        model.addAttribute("books", books);
        return "auction/add";
    }

    @PostMapping("/new")
    public String createAuction(Model model, @ModelAttribute AuctionDTO auctionDTO, Principal principal){
        Auction auction = new Auction();

        //Устанавливаем пользователя создавшего аукцион
        User user = userRepository.findByLogin(principal.getName());
        if (user == null)
            return "redirect:/auctions/main";
        auction.setUser(user);

        // Устанавливаем тип аукциона в зависимости от выбранного значения в форме
        if (auctionDTO.getType().equals("Завершить по последней ставке")) {
            auction.setAuctionType("lb"); //lb - limit by bid
        } else if(auctionDTO.getType().equals("Завершить по времени")) {
            auction.setAuctionType("lt"); //lt - limit by time
        }

        auction.setAuctionDuration(auctionDTO.getDuration());
        auction.setMinimumPrice(auctionDTO.getStartPrice());
        auction.setCurrentPrice(auctionDTO.getStartPrice());

        auctionRepository.save(auction);

        if(auctionDTO.getBooks() != null){
            List<Book> books = (List<Book>) bookRepository.findAllById(auctionDTO.getBooks());

            Integer position = 1;
            for(var book: books){
                AuctionDetail auctionDetail = new AuctionDetail(auction, position, book);
                auctionDetailRepository.save(auctionDetail);
                position++;
            }
        }

        return "redirect:/auctions/" + auction.getId();
    }

    @GetMapping("/update/{id}")
    public String editAuction(Model model, @PathVariable("id") Long id){
        Optional<Auction> auction = auctionRepository.findById(id);

        if(auction.isEmpty()){
            return "redirect:/auctions/main";
        }

        //List<Book> books = (List<Book>) bookRepository.findAll();

        model.addAttribute("auction", auction.get());
        //model.addAttribute("books", books);
        return "auction/edit";
    }

    @PostMapping("/update")
    public String editAuction(@ModelAttribute Auction auction, Principal principal){
        if(auction.getAuctionBids().isEmpty()){
            auction.setCurrentPrice(auction.getMinimumPrice());
        }
        else{
            Optional<Auction> oldAuction = auctionRepository.findById(auction.getId());
            auction.setMinimumPrice(oldAuction.get().getMinimumPrice());
        }
        auctionRepository.save(auction);
        return "redirect:/auctions/" + auction.getId();
    }

    @GetMapping("/delete/{id}")
    public String delAuction(Model model, @PathVariable("id") Long id){
        Optional<Auction> auction = auctionRepository.findById(id);

        if(auction.isEmpty()){
            return "redirect:/auctions/main";
        }

        model.addAttribute("auction", auction.get());
        return "auction/del";
    }

    @PostMapping("/delete/{id}")
    public String delAuction(@PathVariable("id") Long id){
        Optional<Auction> auction = auctionRepository.findById(id);

        if(auction.isPresent()){
            auctionDetailRepository.deleteAll(auction.get().getAuctionDetails());
            auctionBidRepository.deleteAll(auction.get().getAuctionBids());
            auctionRepository.delete(auction.get());
        }
        return "redirect:/auctions/main";
    }

    @PostMapping("/close/{id}")
    public String closeAuction(@PathVariable("id") Long id, RedirectAttributes redirectAttributes){
        Optional<Auction> auction = auctionRepository.findById(id);

        if(auction.isPresent()){
            auction.get().setStatus(true);
            auctionRepository.save(auction.get());
        }
        else {
            redirectAttributes.addFlashAttribute("error", "При завершении аукциона произошла ошибка");
        }

        return "redirect:/auctions/" + auction.get().getId();
    }

    @PostMapping("/add_bid")
    public String addBidToAuction(@RequestParam("auctionId") Long auctionId, @RequestParam("bidAmount") BigDecimal bidAmount, Principal principal,
                                  RedirectAttributes redirectAttributes){
        User user = userRepository.findByLogin(principal.getName());

        Optional<Auction> auction = auctionRepository.findById(auctionId);

        if(auction.isEmpty())
            return "redirect:/auctions/main";

        if (bidAmount.compareTo(auction.get().getCurrentPrice()) <= 0) {
            redirectAttributes.addFlashAttribute("error", "Ставка должна быть больше текущей цены.");
            return "redirect:/auctions/" + auctionId;
        }

        if (user == auction.get().getUser()) {
            redirectAttributes.addFlashAttribute("error", "Владелец не может делать ставки на свой аукцион");
            return "redirect:/auctions/" + auctionId;
        }

        auction.get().setCurrentPrice(bidAmount);
        auctionRepository.save(auction.get());

        AuctionBid bid = new AuctionBid();
        bid.setAuction(auction.get());
        bid.setUser(user);
        bid.setBidAmount(bidAmount);
        bid.setBidTime(LocalDateTime.now());

        auctionBidRepository.save(bid);
        return "redirect:/auctions/" + auction.get().getId();
    }
}
