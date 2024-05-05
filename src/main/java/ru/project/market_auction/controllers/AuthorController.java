package ru.project.market_auction.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.project.market_auction.models.Author;
import ru.project.market_auction.repositories.AuthorRepository;

import java.util.List;
import java.util.Optional;

@RequestMapping("/authors")
@Controller
public class AuthorController {
    @Autowired private AuthorRepository authorRepository;

    @GetMapping("/main")
    public String getAllAuthors(Model model){
        List<Author> authors = (List<Author>) authorRepository.findAll();
        model.addAttribute("authors", authors);
        return "author/main";
    }

    @GetMapping("/{id}")
    public String getAuthor(Model model, @PathVariable("id") Long id){
        Optional<Author> author = authorRepository.findById(id);
        if(author.isEmpty())
            return "redirect:/authors/main";
        model.addAttribute("author", author.get());
        return "author/detail";
    }

    @GetMapping("/new")
    public String addAuthor(Model model){
        model.addAttribute("author", new Author());
        return "author/add";
    }

    @PostMapping("/new")
    public String addAuthor(@ModelAttribute Author author, Model model){
        authorRepository.save(author);
        return "redirect:/authors/main";
    }

    @GetMapping("/update/{id}")
    public String editAuthor(Model model, @PathVariable("id") Long id){
        Optional<Author> author = authorRepository.findById(id);
        if(author.isEmpty()){
            return "redirect:/authors/main";
        }
        model.addAttribute("author", author.get());
        return "author/edit";
    }

    @PostMapping("/update")
    public String editAuthor(@ModelAttribute Author author, Model model){
        authorRepository.save(author);
        return "redirect:/authors/main";
    }

    @GetMapping("/delete/{id}")
    public String delAuthor(Model model, @PathVariable("id") Long id){
        Optional<Author> author = authorRepository.findById(id);
        if(author.isEmpty()){
            return "redirect:/authors/main";
        }
        model.addAttribute("author", author.get());
        return "author/del";
    }

    @PostMapping("/delete/{id}")
    public String delAuthor(@PathVariable("id") Long id, Model model){
        Optional<Author> author = authorRepository.findById(id);
        if(author.isPresent()){
            authorRepository.deleteById(id);
        }
        return "redirect:/authors/main";
    }
}
