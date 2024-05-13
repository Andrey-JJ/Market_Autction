package ru.project.market_auction.models.sales;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ru.project.market_auction.models.users.User;

@Entity
@Table(name = "user_cart_detail")
@AllArgsConstructor
@NoArgsConstructor
public class UserCartDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_cart_id")
    private UserCart userCart;

    @ManyToOne
    @JoinColumn(name = "book_sale_id")
    private BookSale bookSale;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserCart getUserCart() {
        return userCart;
    }

    public void setUserCart(UserCart userCart) {
        this.userCart = userCart;
    }

    public BookSale getBookSale() {
        return bookSale;
    }

    public void setBookSale(BookSale bookSale) {
        this.bookSale = bookSale;
    }
}
