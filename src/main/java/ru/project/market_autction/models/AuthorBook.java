package ru.projects.market_auction.models;

import jakarta.persistence.*;
import lombok.Data;
import ru.projects.market_auction.models.utils.AuthorsBooksId;

@Entity
@Table(name = "authorsbooks")
@IdClass(AuthorsBooksId.class)
@Data
public class AuthorBook {
    @Id
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Id
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;
}
