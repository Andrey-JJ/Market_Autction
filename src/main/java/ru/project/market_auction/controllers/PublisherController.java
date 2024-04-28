package ru.project.market_auction.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.project.market_auction.models.Publisher;
import ru.project.market_auction.repositories.PublisherRepository;

import java.util.List;
import java.util.Optional;

@RequestMapping("/publishers")
@Controller
public class PublisherController {
    @Autowired private PublisherRepository publisherRepository;

    @GetMapping("/")
    public String getPublishers(Model model){
        List<Publisher> publishers = (List<Publisher>) publisherRepository.findAll();
        model.addAttribute("publishers", publishers);
        return "publisher/main";
    }

    @GetMapping("/{id}")
    public String getPublisherDetails(Model model, @PathVariable("id") Long id){
        Optional<Publisher> publisher = publisherRepository.findById(id);
        if(publisher.isEmpty()){
            return "redirect:/publishers/";
        }
        model.addAttribute("publisher", publisher.get());
        return "publisher/detail";
    }

    @GetMapping("/new")
    public String addPublisher(Model model){
        model.addAttribute("publisher", new Publisher());
        return "publisher/add";
    }

    @PostMapping("/new")
    public String addPublisher(@ModelAttribute Publisher publisher, Model model){
        publisherRepository.save(publisher);
        return "redirect:/publishers/";
    }

    @GetMapping("/update/{id}")
    public String editPublisher(Model model, @PathVariable("id") Long id){
        Optional<Publisher> publisher = publisherRepository.findById(id);
        if (publisher.isEmpty())
            return "redirect:/publishers/main";
        model.addAttribute("publisher", publisher);
        return "publisher/edit";
    }

    @PostMapping("/update")
    public String editPublisher(@ModelAttribute Publisher publisher, Model model){
        publisherRepository.save(publisher);
        return "redirect:/publishers/";
    }

    @GetMapping("/delete/{id}")
    public String delPublisher(Model model, @PathVariable("id") Long id){
        Optional<Publisher> publisher = publisherRepository.findById(id);
        if (publisher.isEmpty())
            return "redirect:/publishers/";
        model.addAttribute("publisher", publisher.get());
        return "publisher/del";
    }

    @PostMapping("/delete/{id}")
    public String delPublisher(@PathVariable("id") Long id){
        Optional<Publisher> publisher = publisherRepository.findById(id);
        if (publisher.isPresent())
            publisherRepository.deleteById(id);
        return "redirect:/publishers/";
    }
}
