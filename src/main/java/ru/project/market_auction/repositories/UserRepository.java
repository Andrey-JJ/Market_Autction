package ru.project.market_auction.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.project.market_auction.models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);
    Optional<User> findByEmail(String email);
}
