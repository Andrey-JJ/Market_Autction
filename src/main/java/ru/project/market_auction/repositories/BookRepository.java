package ru.project.market_auction.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.project.market_auction.models.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}

