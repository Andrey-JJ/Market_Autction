package ru.project.market_auction.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.project.market_auction.models.sales.BookSale;
import ru.project.market_auction.models.sales.UserCart;
import ru.project.market_auction.models.users.User;
import ru.project.market_auction.repositories.*;

import java.security.Principal;
import java.time.LocalTime;
import java.util.Map;
import java.util.Optional;

@RestController
public class MarketAuctionApiController {
    @Autowired private BookSaleRepository marketRepository;
    @Autowired private BookRepository bookRepository;
    @Autowired private GenreRepository genreRepository;
    @Autowired private PublisherRepository publisherRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private UserCartRepository userCartRepository;

    @RequestMapping(value = "/cart/remove", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<?> removeItemFromCart(@RequestParam("bookSaleId") Long bookSaleId, Principal principal) {
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

    @RequestMapping(value = "/add-to-cart/{id}", method = RequestMethod.POST)
    public String addToCart(@PathVariable("id") Long id, Principal principal) {
        User user = userRepository.findByLogin(principal.getName());
        Optional<BookSale> bookSale = marketRepository.findById(id);
        if(bookSale.isPresent()){

            user.getUserCarts().add(new UserCart(user, bookSale.get()));
            userCartRepository.saveAll(user.getUserCarts());
        }

        return "redirect:/market/main";
    }

    @PostMapping("/updateAuctionTime")
    public ResponseEntity<?> updateAuctionTime() {

        return ResponseEntity.ok().build();
    }
}
