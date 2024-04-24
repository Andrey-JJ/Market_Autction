package ru.project.market_auction.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.project.market_auction.models.Genre;

public interface GenreRepository extends CrudRepository<Genre, Long> {
}
