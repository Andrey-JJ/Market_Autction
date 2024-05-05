package ru.project.market_auction.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.project.market_auction.models.Genre;

import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    Genre findByName(String name);
}
