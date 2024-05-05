package ru.project.market_auction.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "auctions")
@Data
@AllArgsConstructor
public class Auction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "auction_type")
    private String auctionType;

    @Column(name = "auction_duration")
    private LocalTime auctionDuration;

    @Column(name = "minimum_price")
    private BigDecimal minimumPrice;

    @Column(name = "current_price")
    private BigDecimal currentPrice;

    @OneToMany(mappedBy = "auction")
    private List<AuctionDetail> auctionDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Auction(){
        this.auctionDetails = new ArrayList<>();
    }
}