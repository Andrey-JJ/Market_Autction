package ru.project.market_auction.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.project.market_auction.models.Publisher;

public interface PublisherRepository extends CrudRepository<Publisher, Long> {
}
