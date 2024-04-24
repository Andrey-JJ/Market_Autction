package ru.projects.market_auction.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.projects.market_auction.models.Auction;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
}
