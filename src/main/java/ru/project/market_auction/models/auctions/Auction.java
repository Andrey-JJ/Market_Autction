package ru.project.market_auction.models.auctions;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import ru.project.market_auction.models.users.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "auctions")
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

    @Column(name = "auction_cduration")
    private LocalTime auctionCDuration;

    @Column(name = "minimum_price")
    private BigDecimal minimumPrice;

    @Column(name = "current_price")
    private BigDecimal currentPrice;

    @Column(name = "is_ended")
    private Boolean status;

    @Column(name = "beg_time")
    private String begTime;

    @Column(name = "end_time")
    private String endTime;

    @OneToMany(mappedBy = "auction")
    private List<AuctionDetail> auctionDetails;

    @OneToMany(mappedBy = "auction")
    private List<AuctionBid> auctionBids;

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

    public String getAuctionType() {
        return auctionType;
    }

    public void setAuctionType(String auctionType) {
        this.auctionType = auctionType;
    }

    public LocalTime getAuctionDuration() {
        return auctionDuration;
    }

    public void setAuctionDuration(LocalTime auctionDuration) {
        this.auctionDuration = auctionDuration;
    }

    public LocalTime getAuctionCDuration() {
        return auctionCDuration;
    }

    public void setAuctionCDuration(LocalTime auctionCDuration) {
        this.auctionCDuration = auctionCDuration;
    }

    public BigDecimal getMinimumPrice() {
        return minimumPrice;
    }

    public void setMinimumPrice(BigDecimal minimumPrice) {
        this.minimumPrice = minimumPrice;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<AuctionDetail> getAuctionDetails() {
        return auctionDetails;
    }

    public void setAuctionDetails(List<AuctionDetail> auctionDetails) {
        this.auctionDetails = auctionDetails;
    }

    public List<AuctionBid> getAuctionBids() {
        return auctionBids;
    }

    public void setAuctionBids(List<AuctionBid> auctionBids) {
        this.auctionBids = auctionBids;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getBegTime() {
        return begTime;
    }

    public void setBegTime(String begTime) {
        this.begTime = begTime;
    }

    public Auction(){
        this.auctionDetails = new ArrayList<>();
        this.auctionBids = new ArrayList<>();
    }
}