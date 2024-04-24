package ru.project.market_auction.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.project.market_auction.models.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}