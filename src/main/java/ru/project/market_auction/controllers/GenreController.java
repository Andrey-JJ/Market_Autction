package ru.project.market_auction.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.project.market_auction.models.books.Genre;
import ru.project.market_auction.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;

@RequestMapping("/genres")
@Controller
public class GenreController {
    @Autowired private GenreRepository genreRepository;

    @GetMapping("/main")
    public String getAllGenres(Model model){
        List<Genre> genres = (List<Genre>) genreRepository.findAll();
        model.addAttribute("genres", genres);
        return "genre/main";
    }

    @GetMapping("/{id}")
    public String getGenre(Model model, @PathVariable("id") Long id){
        Optional<Genre> genre = genreRepository.findById(id);
        if(genre.isEmpty()){
            return "redirect:/genres/main";
        }
        model.addAttribute("genre", genre.get());
        return "genre/detail";
    }

    @GetMapping("/new")
    public String addGenre(Model model){
        model.addAttribute("genre", new Genre());
        return "genre/add";
    }

    @PostMapping("/new")
    public String addGenre(@ModelAttribute Genre genre, Model model){
        genreRepository.save(genre);
        return "redirect:/genres/" + genre.getId();
    }

    @GetMapping("/update/{id}")
    public String editGenre(Model model, @PathVariable("id") Long id){
        Optional<Genre> genre = genreRepository.findById(id);
        if(genre.isEmpty()){
            return "redirect:/genres/main";
        }
        model.addAttribute("genre", genre.get());
        return "genre/edit";
    }

    @PostMapping("/update")
    public String editGenre(Model model, @ModelAttribute Genre genre){
        genreRepository.save(genre);
        return "redirect:/genres/" + genre.getId();
    }

    @GetMapping("/delete/{id}")
    public String delGenre(Model model, @PathVariable("id") Long id){
        Optional<Genre> genre = genreRepository.findById(id);
        if(genre.isEmpty()){
            return "redirect:/genres/main";
        }
        model.addAttribute("genre", genre.get());
        return "genre/del";
    }

    @PostMapping("/delete")
    public String delGenre(@PathVariable("id") Long id){
        Optional<Genre> genre = genreRepository.findById(id);
        if(genre.isPresent()){
            genreRepository.delete(genre.get());
        }
        return "redirect:/genres/main";
    }
}
