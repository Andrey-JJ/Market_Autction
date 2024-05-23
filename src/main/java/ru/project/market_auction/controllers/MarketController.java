package ru.project.market_auction.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.project.market_auction.models.books.Book;
import ru.project.market_auction.models.sales.BookSale;
import ru.project.market_auction.models.users.User;
import ru.project.market_auction.repositories.BookRepository;
import ru.project.market_auction.repositories.BookSaleRepository;
import ru.project.market_auction.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@RequestMapping("/market")
@Controller
public class MarketController {
    @Autowired private BookSaleRepository marketRepository;
    @Autowired private BookRepository bookRepository;
    @Autowired private UserRepository userRepository;

    @GetMapping("/main")
    public String getAll(Model model){
        List<BookSale> all = (List<BookSale>) marketRepository.findAll();
        model.addAttribute("bookSales", all);
        return "market/main";
    }

    @GetMapping("/new")
    public String addNewSale(Model model){
        List<Book> books = (List<Book>) bookRepository.findAll();

        model.addAttribute("bookSale", new BookSale());
        model.addAttribute("books", books);
        return "market/add";
    }

    @PostMapping("/new")
    public String addNewSale(Model model, @RequestParam String selectedBook, @ModelAttribute BookSale bookSale){
        String[] bookData = selectedBook.split("/");
        Book book = bookRepository.findByTitleAndPublisherAndPublicationYear(bookData[0], bookData[1], Integer.parseInt(bookData[2]));
        bookSale.setBook(book);

        User user = userRepository.findByLogin("admin");
        bookSale.setUser(user);

        marketRepository.save(bookSale);

        user.getBookSales().add(bookSale);
        userRepository.save(user);

        book.getSales().add(bookSale);
        bookRepository.save(book);

        return "redirect:/market/" + bookSale.getId();
    }

    @GetMapping("/delete/{id}")
    public String delBookSale(Model model, @PathVariable("id") Long id){
        Optional<BookSale> bookSale = marketRepository.findById(id);

        if(bookSale.isEmpty()){
            return "redirect:/market/main";
        }

        model.addAttribute("bookSale", bookSale.get());
        return "market/del";
    }

    @PostMapping("/delete/{id}")
    public String delBookSale(@PathVariable("id") Long id){
        Optional<BookSale> bookSale = marketRepository.findById(id);

        if(bookSale.isPresent()){
            marketRepository.deleteById(id);
        }
        return "redirect:/market/main";
    }

    @PostMapping("/add-to-cart/{id}")
    public String addToCart(@PathVariable("id") Long id) {
        return "redirect:/";
    }

}
