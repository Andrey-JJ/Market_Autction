package ru.project.market_auction.controllers;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.project.market_auction.models.auctions.Auction;
import ru.project.market_auction.models.sales.BookSale;
import ru.project.market_auction.models.sales.UserCart;
import ru.project.market_auction.models.users.User;
import ru.project.market_auction.repositories.AuctionRepository;
import ru.project.market_auction.repositories.BookSaleRepository;
import ru.project.market_auction.repositories.UserCartRepository;
import ru.project.market_auction.repositories.UserRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import ru.project.market_auction.services.FileService;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    @Autowired private UserRepository userRepository;
    @Autowired private UserCartRepository userCartRepository;
    @Autowired private BookSaleRepository marketRepository;
    @Autowired private AuctionRepository auctionRepository;

    @GetMapping("/{username}")
    public String getUserProfile(Model model, @PathVariable("username") String usernameOrEmail) {
        Optional<User> user = userRepository.findByLoginOrEmail(usernameOrEmail);
        if (user.isEmpty()) {
            return "redirect:/";
        }

        List<Auction> currentAuctions = auctionRepository.findByUserAndStatus(user.get(), false);
        List<Auction> closeAuctions = auctionRepository.findByUserAndStatus(user.get(), true);

        model.addAttribute("user", user.get());
        model.addAttribute("currentAuctions", currentAuctions);
        model.addAttribute("closeAuctions", closeAuctions);
        return "profile/detail";
    }

    @GetMapping("/cart/{username}")
    public String getUserCart(Model model, @PathVariable("username") String usernameOrEmail) {
        Optional<User> user = userRepository.findByLoginOrEmail(usernameOrEmail);
        if (user.isEmpty()) {
            return "redirect:/";
        }

        // Создаем карту для хранения количества каждой уникальной записи BookSale
        Map<BookSale, Integer> bookSaleCountMap = new HashMap<>();
        BigDecimal totalSum = BigDecimal.ZERO;

        for (UserCart userCart : user.get().getUserCarts()) {
            BookSale bookSale = userCart.getBookSale();
            bookSaleCountMap.put(bookSale, bookSaleCountMap.getOrDefault(bookSale, 0) + 1);
            totalSum = totalSum.add(bookSale.getPrice());
        }

        model.addAttribute("bookSaleCountMap", bookSaleCountMap);
        model.addAttribute("totalSum", totalSum);
        return "profile/cart";
    }

    @PostMapping("/cart/delete")
    public String delFromUserCart(@RequestParam("bookSaleId") Long bookSaleId, Principal principal) {
        User user = userRepository.findByLogin(principal.getName());
        userCartRepository.deleteAllByUserAndBookSale(user.getId(), bookSaleId);
        return "redirect:/profile/cart/" + principal.getName();
    }

    @PostMapping("/cart/add_one")
    public String addOneUserCart(@RequestParam("bookSaleId") Long bookSaleId, Principal principal) {
        Optional<BookSale> bookSale = marketRepository.findById(bookSaleId);
        User user = userRepository.findByLogin(principal.getName());

        UserCart userCart = new UserCart();
        userCart.setUser(user);
        userCart.setBookSale(bookSale.get());
        userCartRepository.save(userCart);

        return "redirect:/profile/cart/" + principal.getName();
    }

    @PostMapping("/cart/delete_one")
    public String delOneUserCart(@RequestParam("bookSaleId") Long bookSaleId, Principal principal) {
        Optional<BookSale> bookSale = marketRepository.findById(bookSaleId);
        User user = userRepository.findByLogin(principal.getName());

        if (bookSale.isPresent()){
            List<UserCart> userCarts = userCartRepository.findByUserAndBookSale(user, bookSale.get());

            if(!userCarts.isEmpty()){
                UserCart userCart = userCarts.get(0);
                userCartRepository.delete(userCart);
            }
        }

        return "redirect:/profile/cart/" + principal.getName();
    }

    @PostMapping("/cart/checkout")
    public void checkout(HttpServletResponse response, @RequestParam("selectedItems") List<Long> selectedItemIds,
                         Principal principal) throws Exception {
        Optional<User> user = userRepository.findByLoginOrEmail(principal.getName());
        if (user.isEmpty()) {
            response.sendRedirect("/");
            return;
        }

        // Получаем карту из записей
        Map<BookSale, Integer> bookSaleCountMap = new HashMap<>();
        List<UserCart> findedItems = userCartRepository.findAllByIdsAndUser(selectedItemIds, user.get());
        for (UserCart userCart : findedItems) {
            BookSale bookSale = userCart.getBookSale();
            bookSaleCountMap.put(bookSale, bookSaleCountMap.getOrDefault(bookSale, 0) + 1);
        }

        FileService.getCartCheckout(response, bookSaleCountMap, user.get());
    }
}