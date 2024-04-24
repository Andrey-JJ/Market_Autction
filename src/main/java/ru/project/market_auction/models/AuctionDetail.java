package ru.project.market_auction.models;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "auctions_detail")
@Data
public class AuctionDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "auction_id")
    private Auction auction;

    @Column(name = "position_number")
    private Integer positionNumber;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Column(name = "minimum_price")
    private BigDecimal minimumPrice;

    @Column(name = "current_price")
    private BigDecimal currentPrice;
}