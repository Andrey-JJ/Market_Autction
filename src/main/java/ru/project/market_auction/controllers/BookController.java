package ru.project.market_auction.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.project.market_auction.models.books.*;
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
    @Autowired private BookRepository bookRepository;
    @Autowired private AuthorRepository authorRepository;
    @Autowired private PublisherRepository publisherRepository;
    @Autowired private GenreRepository genreRepository;
    @Autowired private AuthorBookRepository authorBookRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/main")
    public String getAllBooks(Model model,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "8") int size){

        Pageable pageable = PageRequest.of(page, size);
        Page<Book> booksPage = bookRepository.findAll(pageable);
        //List<Book> books = (List<Book>) bookRepository.findAll();
        model.addAttribute("booksPage", booksPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        return "book/main";
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/{id}")
    public String getBook(Model model, @PathVariable("id") Long id, RedirectAttributes redirectAttributes){
        Optional<Book> book = bookRepository.findById(id);
        if(book.isEmpty()){
            redirectAttributes.addFlashAttribute("error", "Запись не была найдена");
            return "redirect:/books/main";
        }
        model.addAttribute("book", book.get());
        return "book/detail";
    }

    @PreAuthorize("hasRole('ADMIN')")
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

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/new")
    public String addBook(@ModelAttribute Book book,
                          @RequestParam("selectedPublisher") String publisher,
                          @RequestParam("selectedGenre") String genre,
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

        book.setPublisher(publisherRepository.findByName(publisher));
        book.setGenre(genreRepository.findByName(genre));

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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/update/{id}")
    public String updateBook(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes){
        Optional<Book> book = bookRepository.findById(id);

        if (book.isEmpty()) {
            return "redirect:/books/main";
        }

        // Получение необходимых данных для выпадающих списков
        List<Author> authors = (List<Author>) authorRepository.findAll();
        List<Genre> genres = (List<Genre>) genreRepository.findAll();
        List<Publisher> publishers = (List<Publisher>) publisherRepository.findAll();

        model.addAttribute("book", book.get());
        model.addAttribute("genres", genres);
        model.addAttribute("allAuthors", authors);
        model.addAttribute("publishers", publishers);

        return "book/edit";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/update")
    public String editBook(@ModelAttribute Book book,
                           @RequestParam("selectedPublisher") String publisher,
                           @RequestParam("selectedGenre") String genre,
                           @RequestParam("imageFile") MultipartFile imageFile,
                           @RequestParam(name = "selectedAuthors") List<Long> authorIds,
                           Model model) {
        // Обработка файла изображения
        if (!imageFile.isEmpty()) {
            try {
                // Сохранение файла на сервере, например, в папку resources\static\images
                String imagePath = imageFile.getOriginalFilename();
                Path uploadPath = Paths.get("src/main/resources/static/images/");
                Files.copy(imageFile.getInputStream(), uploadPath.resolve(imageFile.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);

                // Установка пути изображения в модель книги
                book.setImage(imagePath);
            } catch (IOException e) {
                e.printStackTrace(); // Обработка ошибок ввода-вывода
            }
        }

        if (authorIds != null) {
            List<Author> authors = (List<Author>) authorRepository.findAllById(authorIds);
            // Удаление существующих связей с авторами

            // Добавление новых связей в дополнительную таблицу bookauthor
            for (var author : authors) {
                book.getAuthors().add(new AuthorBook(book, author));
            }
        }

        book.setPublisher(publisherRepository.findByName(publisher));
        book.setGenre(genreRepository.findByName(genre));

        // Обновление книги
        bookRepository.save(book);
        authorBookRepository.saveAll(book.getAuthors());

        return "redirect:/books/details/" + book.getId(); // Перенаправление на страницу деталей книги
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/delete/{id}")
    public String delBook(Model model, @PathVariable("id") Long id, RedirectAttributes redirectAttributes){
        Optional<Book> book = bookRepository.findById(id);
        if(book.isEmpty()){
            redirectAttributes.addFlashAttribute("error", "Не найдена запись для удаления");
            return "redirect:/books/main";
        }
        model.addAttribute("book", book.get());
        return "book/del";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete/{id}")
    public String delBook(@PathVariable("id") Long id, RedirectAttributes redirectAttributes){
        Optional<Book> book = bookRepository.findById(id);
        if(book.isPresent()){
            bookRepository.deleteById(id);
        }
        return "redirect:/books/main";
    }
}
