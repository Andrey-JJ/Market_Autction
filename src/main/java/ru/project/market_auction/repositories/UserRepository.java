package ru.project.market_auction.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.project.market_auction.models.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByLogin(String login);
    User findByEmail(String email);
    @Query(value = "SELECT * FROM users u WHERE u.login = :login OR u.email = :email LIMIT 1", nativeQuery = true)
    User findByLoginOrEmail(@Param("login") String login, @Param("email") String email);
}
