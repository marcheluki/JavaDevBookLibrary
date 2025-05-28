package com.library.api.service;

import com.library.api.model.Book;
import java.util.List;
import java.util.Optional;

public interface BookService {
    // CRUD Operations
    Book saveBook(Book book);

    List<Book> getAllBooks();

    Optional<Book> getBookById(Long id);

    Optional<Book> getBookByIsbn(String isbn);

    Book updateBook(Long id, Book book);

    void deleteBook(Long id);

    // Business Logic Operations
    List<Book> findBooksByTitle(String title);

    List<Book> findBooksByAuthor(String author);

    List<Book> findBooksByYear(int year);

    List<Book> findAvailableBooks();

    boolean isBookAvailable(String isbn);

    void updateBookCopies(String isbn, int copies);

    // Search Operations
    List<Book> searchBooks(String query);
}