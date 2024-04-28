package ru.project.market_auction.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.project.market_auction.models.AuthorBook;

public interface AuthorBookRepository extends JpaRepository<AuthorBook, Long> {
}
