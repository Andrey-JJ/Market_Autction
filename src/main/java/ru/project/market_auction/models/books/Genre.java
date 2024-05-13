package ru.project.market_auction.models.books;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import ru.project.market_auction.models.books.Book;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "genres")
@AllArgsConstructor
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "genre")
    private List<Book> books;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public Genre(){
        this.books = new ArrayList<>();
    }
}