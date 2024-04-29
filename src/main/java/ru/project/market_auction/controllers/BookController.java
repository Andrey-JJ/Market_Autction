package ru.project.market_auction.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.project.market_auction.models.*;
import ru.project.market_auction.repositories.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@RequestMapping("/books")
@Controller
public class BookController {
    @Autowired BookRepository bookRepository;
    @Autowired AuthorRepository authorRepository;
    @Autowired PublisherRepository publisherRepository;
    @Autowired GenreRepository genreRepository;
    @Autowired AuthorBookRepository authorBookRepository;

    @GetMapping("/main")
    public String getAllBooks(Model model){
        List<Book> books = (List<Book>) bookRepository.findAll();
        model.addAttribute("books", books);
        return "book/main";
    }

    @GetMapping("/{id}")
    public String getBook(Model model, @PathVariable("id") Long id){
        Optional<Book> book = bookRepository.findById(id);
        if(book.isEmpty()){
            return "redirect:/books/main";
        }
        model.addAttribute("book", book.get());
        return "book/detail";
    }

    @GetMapping("/new")
    public String addBook(Model model){
        List<Author> authors = (List<Author>) authorRepository.findAll();
        List<Publisher> publishers = (List<Publisher>) publisherRepository.findAll();
        List<Genre> genres = (List<Genre>) genreRepository.findAll();
        model.addAttribute("book", new Book());
        model.addAttribute("authors", authors);
        model.addAttribute("publishers", publishers);
        model.addAttribute("genres", genres);
        return "book/add";
    }

    @PostMapping("/new")
    public String addBook(@ModelAttribute Book book,
                          @RequestParam("imageFile") MultipartFile imageFile,
                          @RequestParam("selectedAuthors") List<Long> selectedAuthors,
                          Model model){

        // Обработка файла изображения
        if (!imageFile.isEmpty()) {
            try {
                // Сохранение файла на сервере, например, в папку resources\static\images
                String imagePath = imageFile.getOriginalFilename();
                Path uploadPath = Paths.get("src/main/resources/static/images/books");
                Files.copy(imageFile.getInputStream(), uploadPath.resolve(imageFile.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
                // Установка пути изображения в модель книги
                book.setImage(imagePath);
            } catch (IOException e) {
                e.printStackTrace(); // Обработка ошибок ввода-вывода
            }
        }
        //Обработка авторов книги
        if(selectedAuthors != null){
            //Поиск авторов
            List<Author> authors = (List<Author>) authorRepository.findAllById(selectedAuthors);
            //Внесение авторов в запись книги
            for(var author : authors){
                book.getAuthors().add(new AuthorBook(book, author));
            }
            //Сохранение записей
            bookRepository.save(book);
            authorBookRepository.saveAll(book.getAuthors());
        }

        return "redirect:/books/main";
    }
}
