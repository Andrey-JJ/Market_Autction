package ru.project.market_auction.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.project.market_auction.models.auctions.Auction;
import ru.project.market_auction.models.users.User;

import java.util.List;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {
    List<Auction> findByAuctionTypeAndStatus(String auctionType, Boolean status);
    List<Auction> findByUserAndStatus(User user, Boolean status);
}
