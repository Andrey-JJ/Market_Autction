package ru.project.market_auction.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.project.market_auction.models.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
}
