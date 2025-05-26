package com.library.api.repository;

import com.library.api.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    // Find books by title containing the given string (case-insensitive)
    List<Book> findByTitleContainingIgnoreCase(String title);

    // Find books by author containing the given string (case-insensitive)
    List<Book> findByAuthorContainingIgnoreCase(String author);

    // Find a book by its ISBN
    Optional<Book> findByIsbn(String isbn);

    // Find books published in a specific year
    List<Book> findByYear(int year);

    // Find books with available copies (copies > 0)
    List<Book> findByCopiesGreaterThan(int copies);
}