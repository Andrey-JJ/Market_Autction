package ru.project.market_auction.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.project.market_auction.models.books.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
}