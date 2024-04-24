package ru.project.market_auction.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.project.market_auction.models.BookSale;

public interface BookSaleRepository extends JpaRepository<BookSale, Long> {
}

