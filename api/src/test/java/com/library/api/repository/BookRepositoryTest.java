package com.library.api.repository;

import com.library.api.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookRepository bookRepository;

    private Book testBook;

    @BeforeEach
    void setUp() {
        // Create a test book
        testBook = new Book();
        testBook.setTitle("Test Book");
        testBook.setAuthor("Test Author");
        testBook.setIsbn("1234567890");
        testBook.setYear(2024);
        testBook.setCopies(5);

        // Save the test book
        entityManager.persist(testBook);
        entityManager.flush();
    }

    @Test
    void whenFindByTitle_thenReturnBook() {
        // when
        List<Book> found = bookRepository.findByTitleContainingIgnoreCase("test");

        // then
        assertThat(found).isNotEmpty();
        assertThat(found.get(0).getTitle()).isEqualTo(testBook.getTitle());
    }

    @Test
    void whenFindByAuthor_thenReturnBook() {
        // when
        List<Book> found = bookRepository.findByAuthorContainingIgnoreCase("test");

        // then
        assertThat(found).isNotEmpty();
        assertThat(found.get(0).getAuthor()).isEqualTo(testBook.getAuthor());
    }

    @Test
    void whenFindByIsbn_thenReturnBook() {
        // when
        Optional<Book> found = bookRepository.findByIsbn(testBook.getIsbn());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getIsbn()).isEqualTo(testBook.getIsbn());
    }

    @Test
    void whenFindByYear_thenReturnBook() {
        // when
        List<Book> found = bookRepository.findByYear(2024);

        // then
        assertThat(found).isNotEmpty();
        assertThat(found.get(0).getYear()).isEqualTo(testBook.getYear());
    }

    @Test
    void whenFindByCopiesGreaterThan_thenReturnBook() {
        // when
        List<Book> found = bookRepository.findByCopiesGreaterThan(0);

        // then
        assertThat(found).isNotEmpty();
        assertThat(found.get(0).getCopies()).isGreaterThan(0);
    }

    @Test
    void whenSaveBook_thenReturnSavedBook() {
        // given
        Book newBook = new Book();
        newBook.setTitle("New Book");
        newBook.setAuthor("New Author");
        newBook.setIsbn("0987654321");
        newBook.setYear(2024);
        newBook.setCopies(3);

        // when
        Book saved = bookRepository.save(newBook);

        // then
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTitle()).isEqualTo(newBook.getTitle());
    }

    @Test
    void whenDeleteBook_thenBookShouldNotExist() {
        // when
        bookRepository.delete(testBook);
        Optional<Book> deleted = bookRepository.findById(testBook.getId());

        // then
        assertThat(deleted).isEmpty();
    }
}