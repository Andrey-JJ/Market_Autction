package ru.projects.market_auction.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.projects.market_auction.models.Genre;

public interface GenreRepository extends CrudRepository<Genre, Long> {
}
