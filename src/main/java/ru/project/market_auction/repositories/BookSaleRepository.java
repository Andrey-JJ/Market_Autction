package ru.project.market_auction.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.project.market_auction.models.sales.BookSale;

@Repository
public interface BookSaleRepository extends JpaRepository<BookSale, Long> {
}

