package ru.project.market_auction.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.project.market_auction.models.books.Book;
import ru.project.market_auction.models.books.Publisher;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT b FROM Book b WHERE b.title = :title AND b.publisher.name = :publisher AND b.publicationYear = :publicationYear")
    Book findByTitleAndPublisherAndPublicationYear(@Param("title") String title, @Param("publisher") String publisher, @Param("publicationYear") Integer publicationYear);

    Book findByTitle(String title);
}

