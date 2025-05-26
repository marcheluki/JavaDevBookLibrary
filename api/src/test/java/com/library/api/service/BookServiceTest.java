package com.library.api.service;

import com.library.api.model.Book;
import com.library.api.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book testBook;

    @BeforeEach
    void setUp() {
        testBook = new Book();
        testBook.setId(1L);
        testBook.setTitle("Test Book");
        testBook.setAuthor("Test Author");
        testBook.setIsbn("1234567890");
        testBook.setYear(2024);
        testBook.setCopies(5);
    }

    @Test
    void whenSaveBook_thenReturnSavedBook() {
        when(bookRepository.findByIsbn(anyString())).thenReturn(Optional.empty());
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        Book savedBook = bookService.saveBook(testBook);

        assertNotNull(savedBook);
        assertEquals(testBook.getTitle(), savedBook.getTitle());
        assertEquals(testBook.getAuthor(), savedBook.getAuthor());
        assertEquals(testBook.getIsbn(), savedBook.getIsbn());
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void whenSaveBookWithExistingIsbn_thenThrowException() {
        when(bookRepository.findByIsbn(anyString())).thenReturn(Optional.of(testBook));

        assertThrows(IllegalStateException.class, () -> bookService.saveBook(testBook));
    }

    @Test
    void whenGetAllBooks_thenReturnListOfBooks() {
        List<Book> books = Arrays.asList(testBook);
        when(bookRepository.findAll()).thenReturn(books);

        List<Book> foundBooks = bookService.getAllBooks();

        assertNotNull(foundBooks);
        assertEquals(1, foundBooks.size());
        assertEquals(testBook.getTitle(), foundBooks.get(0).getTitle());
    }

    @Test
    void whenGetBookById_thenReturnBook() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(testBook));

        Optional<Book> foundBook = bookService.getBookById(1L);

        assertTrue(foundBook.isPresent());
        assertEquals(testBook.getTitle(), foundBook.get().getTitle());
    }

    @Test
    void whenUpdateBook_thenReturnUpdatedBook() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(testBook));
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        Book updatedBook = bookService.updateBook(1L, testBook);

        assertNotNull(updatedBook);
        assertEquals(testBook.getTitle(), updatedBook.getTitle());
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void whenDeleteBook_thenBookShouldBeDeleted() {
        when(bookRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(bookRepository).deleteById(anyLong());

        assertDoesNotThrow(() -> bookService.deleteBook(1L));
        verify(bookRepository).deleteById(anyLong());
    }

    @Test
    void whenFindBooksByTitle_thenReturnMatchingBooks() {
        List<Book> books = Arrays.asList(testBook);
        when(bookRepository.findByTitleContainingIgnoreCase(anyString())).thenReturn(books);

        List<Book> foundBooks = bookService.findBooksByTitle("Test");

        assertNotNull(foundBooks);
        assertEquals(1, foundBooks.size());
        assertEquals(testBook.getTitle(), foundBooks.get(0).getTitle());
    }

    @Test
    void whenFindBooksByAuthor_thenReturnMatchingBooks() {
        List<Book> books = Arrays.asList(testBook);
        when(bookRepository.findByAuthorContainingIgnoreCase(anyString())).thenReturn(books);

        List<Book> foundBooks = bookService.findBooksByAuthor("Test");

        assertNotNull(foundBooks);
        assertEquals(1, foundBooks.size());
        assertEquals(testBook.getAuthor(), foundBooks.get(0).getAuthor());
    }

    @Test
    void whenFindAvailableBooks_thenReturnBooksWithCopies() {
        List<Book> books = Arrays.asList(testBook);
        when(bookRepository.findByCopiesGreaterThan(anyInt())).thenReturn(books);

        List<Book> foundBooks = bookService.findAvailableBooks();

        assertNotNull(foundBooks);
        assertEquals(1, foundBooks.size());
        assertTrue(foundBooks.get(0).getCopies() > 0);
    }

    @Test
    void whenIsBookAvailable_thenReturnAvailability() {
        when(bookRepository.findByIsbn(anyString())).thenReturn(Optional.of(testBook));

        boolean isAvailable = bookService.isBookAvailable("1234567890");

        assertTrue(isAvailable);
    }

    @Test
    void whenUpdateBookCopies_thenCopiesShouldBeUpdated() {
        when(bookRepository.findByIsbn(anyString())).thenReturn(Optional.of(testBook));
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        assertDoesNotThrow(() -> bookService.updateBookCopies("1234567890", 10));
        verify(bookRepository).save(any(Book.class));
    }
}