package com.library.api.service;

import com.library.api.model.Book;
import com.library.api.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book saveBook(Book book) {
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Book title cannot be empty");
        }
        if (book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
            throw new IllegalArgumentException("Book author cannot be empty");
        }
        if (book.getIsbn() == null || book.getIsbn().trim().isEmpty()) {
            throw new IllegalArgumentException("Book ISBN cannot be empty");
        }
        if (book.getCopies() < 0) {
            throw new IllegalArgumentException("Number of copies cannot be negative");
        }

        // Check if book with same ISBN already exists
        if (bookRepository.findByIsbn(book.getIsbn()).isPresent()) {
            throw new IllegalStateException("Book with ISBN " + book.getIsbn() + " already exists");
        }

        return bookRepository.save(book);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Book> getBookById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Book ID cannot be null");
        }
        return bookRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Book> getBookByIsbn(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be empty");
        }
        return bookRepository.findByIsbn(isbn);
    }

    @Override
    public Book updateBook(Long id, Book book) {
        if (id == null) {
            throw new IllegalArgumentException("Book ID cannot be null");
        }
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }

        return bookRepository.findById(id)
                .map(existingBook -> {
                    existingBook.setTitle(book.getTitle());
                    existingBook.setAuthor(book.getAuthor());
                    existingBook.setIsbn(book.getIsbn());
                    existingBook.setYear(book.getYear());
                    existingBook.setCopies(book.getCopies());
                    return bookRepository.save(existingBook);
                })
                .orElseThrow(() -> new IllegalStateException("Book with ID " + id + " not found"));
    }

    @Override
    public void deleteBook(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Book ID cannot be null");
        }
        if (!bookRepository.existsById(id)) {
            throw new IllegalStateException("Book with ID " + id + " not found");
        }
        bookRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> findBooksByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> findBooksByAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Author cannot be empty");
        }
        return bookRepository.findByAuthorContainingIgnoreCase(author);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> findBooksByYear(int year) {
        if (year < 0) {
            throw new IllegalArgumentException("Year cannot be negative");
        }
        return bookRepository.findByYear(year);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> findAvailableBooks() {
        return bookRepository.findByCopiesGreaterThan(0);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isBookAvailable(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be empty");
        }
        return bookRepository.findByIsbn(isbn)
                .map(book -> book.getCopies() > 0)
                .orElse(false);
    }

    @Override
    public void updateBookCopies(String isbn, int copies) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be empty");
        }
        if (copies < 0) {
            throw new IllegalArgumentException("Number of copies cannot be negative");
        }

        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new IllegalStateException("Book with ISBN " + isbn + " not found"));

        book.setCopies(copies);
        bookRepository.save(book);
    }
}