package ru.projects.market_auction.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.projects.market_auction.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
