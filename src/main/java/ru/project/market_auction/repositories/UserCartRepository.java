package ru.project.market_auction.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.project.market_auction.models.sales.UserCart;

@Repository
public interface UserCartRepository extends JpaRepository<UserCart, Long> {
    @Query("DELETE FROM UserCart uc WHERE uc.user.id = :userId AND uc.bookSale.id = :bookSaleId")
    void deleteAllByUserAndBookSale(@Param("userId") Long userId, @Param("bookSaleId") Long bookSaleId);
}
