package ru.project.market_auction.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.project.market_auction.models.books.Book;
import ru.project.market_auction.models.books.Genre;
import ru.project.market_auction.models.books.Publisher;
import ru.project.market_auction.models.sales.BookSale;
import ru.project.market_auction.models.sales.UserCart;
import ru.project.market_auction.models.users.User;
import ru.project.market_auction.repositories.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RequestMapping("/market")
@Controller
public class MarketController {
    @Autowired private BookSaleRepository marketRepository;
    @Autowired private BookRepository bookRepository;
    @Autowired private GenreRepository genreRepository;
    @Autowired private PublisherRepository publisherRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private UserCartRepository userCartRepository;

    //@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/main")
    public String getAll(Model model){
        List<BookSale> all = (List<BookSale>) marketRepository.findAll();
        model.addAttribute("bookSales", all);
        return "market/main";
    }

    //@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/new")
    public String addNewSale(Model model){
        List<Book> books = (List<Book>) bookRepository.findAll();
        List<Genre> genres = (List<Genre>) genreRepository.findAll();
        List<Publisher> publishers = (List<Publisher>) publisherRepository.findAll();
        model.addAttribute("books", books);
        model.addAttribute("genres", genres);
        model.addAttribute("publishers", publishers);
        return "market/add";
    }

    //@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PostMapping("/new")
    public String addNewSale(Model model, Principal principal, @RequestParam("bookTitle") String bookTitle, @RequestParam("price") BigDecimal price){
        Book book = bookRepository.findByTitle(bookTitle);
        User user = userRepository.findByLogin(principal.getName());

        BookSale bookSale = new BookSale();
        bookSale.setUser(user);
        bookSale.setBook(book);
        bookSale.setPrice(price);

        marketRepository.save(bookSale);

        return "redirect:/books/" + bookSale.getBook().getId();
    }

    //@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/delete/{id}")
    public String delBookSale(Model model, @PathVariable("id") String id){
        Long bookid = Long.parseLong(id);

        Optional<BookSale> bookSale = marketRepository.findById(bookid);

        if(bookSale.isEmpty()){
            return "redirect:/market/main";
        }

        model.addAttribute("bookSale", bookSale.get());
        return "market/del";
    }

    //@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PostMapping("/delete/{id}")
    public String delBookSale(@PathVariable("id") Long id){
        Optional<BookSale> bookSale = marketRepository.findById(id);

        if(bookSale.isPresent()){
            marketRepository.delete(bookSale.get());
        }
        return "redirect:/market/main";
    }

    //@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PostMapping("/add_to_cart")
    public String addToCart(@RequestParam("bookSaleId") Long bookSaleId, @RequestParam("page") String page, Principal principal, RedirectAttributes redirectAttributes){
        String returnPage = "";

        User user = userRepository.findByLogin(principal.getName());

        Optional<BookSale> bookSale = marketRepository.findById(bookSaleId);
        if(bookSale.isEmpty()){
            redirectAttributes.addFlashAttribute("error", "Объявление не было добавлено из-за ошибки");
            returnPage = getReturnPage(page, bookSale.get(), principal.getName());
        }

        returnPage = getReturnPage(page, bookSale.get(), principal.getName());

        UserCart userCart = new UserCart();
        userCart.setUser(user);
        userCart.setBookSale(bookSale.get());
        userCartRepository.save(userCart);

        return returnPage;
    }

    private String getReturnPage(String page, BookSale bookSale, String user){
        String text = "";
        switch (page){
            case "mainPage":
                text = "redirect:/";
                break;
            case "marketPage":
                text = "redirect:/market/main";
                break;
            case "bookPage":
                text = "redirect:/books/" + bookSale.getBook().getId();
                break;
            case "cartPage":
                text = "redirect:/profile/cart/" + bookSale.getBook().getId();
                break;
        }
        return text;
    }
}
