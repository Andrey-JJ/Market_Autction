package ru.projects.market_auction.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.projects.market_auction.models.AuctionDetail;

public interface AuctionDetailRepository extends JpaRepository<AuctionDetail, Long> {
}
