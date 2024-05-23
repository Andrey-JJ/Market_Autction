package ru.project.market_auction.models.books;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import ru.project.market_auction.models.auctions.Auction;
import ru.project.market_auction.models.auctions.AuctionDetail;
import ru.project.market_auction.models.sales.BookSale;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "books")
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;

    @ManyToOne
    @JoinColumn(name = "publisher_id", nullable = false)
    private Publisher publisher;

    @Column(name = "publication_year")
    private Integer publicationYear;

    @Column(name = "page_count")
    private Integer pageCount;

    @Column(name = "image")
    private String image;

    @OneToMany(mappedBy = "book")
    private List<AuthorBook> authors;

    @OneToMany(mappedBy = "book")
    private List<BookSale> sales;

    @OneToMany(mappedBy = "book")
    private List<AuctionDetail> auctions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<AuthorBook> getAuthors() {
        return authors;
    }

    public void setAuthors(List<AuthorBook> authors) {
        this.authors = authors;
    }

    public List<BookSale> getSales() {
        return sales;
    }

    public void setSales(List<BookSale> sales) {
        this.sales = sales;
    }

    public List<AuctionDetail> getAuctions() {
        return auctions;
    }

    public void setAuctions(List<AuctionDetail> auctions) {
        this.auctions = auctions;
    }

    public Book(){
        this.authors = new ArrayList<>();
        this.sales = new ArrayList<>();
        this.auctions = new ArrayList<>();
    }
}
