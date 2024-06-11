package ru.project.market_auction.controllers;

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
import java.math.BigDecimal;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    /*@PostMapping("/cart/checkout")
    public ResponseEntity<byte[]> checkoutCart(@RequestParam("selectedItems") List<Long> selectedItems, Principal principal) throws IOException {
        User user = userRepository.findByLogin(principal.getName());
        List<UserCart> userCarts = user.getUserCarts();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                InputStream fontStream = getClass().getResourceAsStream("/static/fonts/arial.ttf");
                PDType0Font font = PDType0Font.load(document, fontStream);

                contentStream.setFont(font, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 750);
                contentStream.showText("Корзина пользователя");
                contentStream.endText();

                contentStream.setFont(font, 10);
                int yPosition = 730;
                int index = 1;
                BigDecimal totalSum = BigDecimal.ZERO;

                for (Long bookSaleId : selectedItems) {
                    Optional<BookSale> bookSaleOpt = marketRepository.findById(bookSaleId);
                    if (bookSaleOpt.isPresent()) {
                        BookSale bookSale = bookSaleOpt.get();
                        String title = bookSale.getBook().getTitle();
                        String seller = bookSale.getUser().getLogin();
                        int quantity = (int) userCarts.stream().filter(cart -> cart.getBookSale().getId().equals(bookSaleId)).count();
                        BigDecimal price = bookSale.getPrice();
                        BigDecimal total = price.multiply(BigDecimal.valueOf(quantity));
                        totalSum = totalSum.add(total);

                        contentStream.beginText();
                        contentStream.newLineAtOffset(50, yPosition);
                        contentStream.showText(String.format("%d. %s, Продавец: %s, Кол-во: %d, Цена: %s, Всего: %s",
                                index, title, seller, quantity, price.toString(), total.toString()));
                        contentStream.endText();
                        yPosition -= 20;
                        index++;
                    }
                }

                contentStream.beginText();
                contentStream.newLineAtOffset(50, yPosition - 20);
                contentStream.showText("Итоговая сумма: " + totalSum.toString() + " руб.");
                contentStream.endText();
            }
            document.save(byteArrayOutputStream);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=checkout.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(byteArrayOutputStream.toByteArray());
    }*/


}
