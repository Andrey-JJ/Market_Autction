package ru.project.market_auction.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "authorsbooks")
@IdClass(AuthorBookId.class)
@Data
@NoArgsConstructor
public class AuthorBook {
    @Id
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Id
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    public AuthorBook(Book book, Author author){
        this.book = book;
        this.author = author;
    }
}
