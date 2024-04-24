package ru.projects.market_auction.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.projects.market_auction.models.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}