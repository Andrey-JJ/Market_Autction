package ru.project.market_auction.models.sales;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ru.project.market_auction.models.users.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_cart")
@AllArgsConstructor
@NoArgsConstructor
public class UserCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "booksale_id")
    private BookSale bookSale;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BookSale getBookSale() {
        return bookSale;
    }

    public void setBookSale(BookSale bookSale) {
        this.bookSale = bookSale;
    }

    public UserCart(User user, BookSale bookSale){
        this.user = user;
        this.bookSale = bookSale;
    }
}
