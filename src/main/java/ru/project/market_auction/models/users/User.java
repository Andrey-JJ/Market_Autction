package ru.project.market_auction.models.users;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import ru.project.market_auction.models.sales.BookSale;
import ru.project.market_auction.models.sales.UserCart;
import ru.project.market_auction.models.auctions.Auction;
import ru.project.market_auction.models.auctions.AuctionBid;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@AllArgsConstructor
public class User {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login", nullable = false)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "email", nullable = false)
    private String email;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<BookSale> bookSales;

    @OneToMany(mappedBy = "user")
    private List<Auction> auctions;

    @OneToMany(mappedBy = "user")
    private List<AuctionBid> auctionBids;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserCart> userCarts = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<BookSale> getBookSales() {
        return bookSales;
    }

    public void setBookSales(List<BookSale> bookSales) {
        this.bookSales = bookSales;
    }

    public List<Auction> getAuctions() {
        return auctions;
    }

    public void setAuctions(List<Auction> auctions) {
        this.auctions = auctions;
    }

    public List<AuctionBid> getAuctionBids() {
        return auctionBids;
    }

    public void setAuctionBids(List<AuctionBid> auctionBids) {
        this.auctionBids = auctionBids;
    }

    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<UserCart> getUserCarts() {
        return userCarts;
    }

    public void setUserCarts(List<UserCart> userCarts) {
        this.userCarts = userCarts;
    }

    public User(){
        this.bookSales = new ArrayList<>();
        this.auctions = new ArrayList<>();
        this.auctionBids = new ArrayList<>();
        this.userCarts = new ArrayList<>();
    }

    @Override
    public String toString() {
        return lastName + " " + firstName + " " + middleName;
    }
}