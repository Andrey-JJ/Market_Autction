package ru.project.market_auction.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.project.market_auction.models.Auction;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
}
