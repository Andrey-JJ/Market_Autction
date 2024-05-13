package ru.project.market_auction.models.books;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "authorsbooks")
@IdClass(AuthorBookId.class)
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

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
}
