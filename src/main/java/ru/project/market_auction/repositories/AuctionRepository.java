package ru.project.market_auction.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.project.market_auction.models.auctions.Auction;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {
}
