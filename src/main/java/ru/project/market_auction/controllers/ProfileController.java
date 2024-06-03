package ru.project.market_auction.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.project.market_auction.models.sales.BookSale;
import ru.project.market_auction.models.sales.UserCart;
import ru.project.market_auction.models.users.User;
import ru.project.market_auction.repositories.UserCartRepository;
import ru.project.market_auction.repositories.UserRepository;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    @Autowired private UserRepository userRepository;
    @Autowired private UserCartRepository userCartRepository;

    @GetMapping("/{username}")
    public String getUserProfile(Model model, @PathVariable("username") String usernameOrEmail) {
        Optional<User> user = userRepository.findByLoginOrEmail(usernameOrEmail);
        if (user.isEmpty()) {
            return "redirect:/";
        }
        model.addAttribute("user", user.get());
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

    @DeleteMapping("/cart/remove/{bookSaleId}")
    public ResponseEntity<?> removeItemFromCart(@PathVariable("bookSaleId") Long bookSaleId, Principal principal) {
        User user = userRepository.findByLogin(principal.getName());
        try {
            userCartRepository.deleteAllByUserAndBookSale(user.getId(), bookSaleId);

            // Возврат успешного статуса
            return ResponseEntity.ok().build();
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("Ошибка при удалении товара из корзины", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
