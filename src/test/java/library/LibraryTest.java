package library;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class LibraryTest {
    
    private Library library;
    private Book book1;
    private Book book2;
    private Book book3;
    
    @BeforeEach
    public void setUp() {
        library = new Library();
        book1 = new Book("Java Programming", "John Doe", "123456", 5, 2020);
        book2 = new Book("Advanced Java", "Jane Smith", "234567", 3, 2022);
        book3 = new Book("Design Patterns", "John Doe", "345678", 2, 2018);
        
        library.addBook(book1);
        library.addBook(book2);
        library.addBook(book3);
    }
    
    @Test
    public void testAddBook() {
        // Check if books were added correctly
        List<Book> books = library.getBooks();
        assertEquals(3, books.size());
        assertTrue(books.contains(book1));
        assertTrue(books.contains(book2));
        assertTrue(books.contains(book3));
    }
    
    @Test
    public void testGetBooks() {
        List<Book> books = library.getBooks();
        assertEquals(3, books.size());
        
        // Verify that the list is a copy
        books.clear(); // This should not affect the library's internal list
        assertEquals(3, library.getBooks().size());
    }
    
    @Test
    public void testFindBooksByAuthor() {
        List<Book> johnDoeBooks = library.findBooksByAuthor("John Doe");
        assertEquals(2, johnDoeBooks.size());
        assertTrue(johnDoeBooks.contains(book1));
        assertTrue(johnDoeBooks.contains(book3));
        
        List<Book> janeSmithBooks = library.findBooksByAuthor("Jane Smith");
        assertEquals(1, janeSmithBooks.size());
        assertTrue(janeSmithBooks.contains(book2));
        
        List<Book> unknownAuthorBooks = library.findBooksByAuthor("Unknown Author");
        assertEquals(0, unknownAuthorBooks.size());
    }
    
    @Test
    public void testSortBooksByTitle() {
        List<Book> sortedBooks = library.sortBooksByTitle();
        assertEquals(3, sortedBooks.size());
        assertEquals(book2, sortedBooks.get(0)); // "Advanced Java"
        assertEquals(book3, sortedBooks.get(1)); // "Design Patterns"
        assertEquals(book1, sortedBooks.get(2)); // "Java Programming"
    }
    
    @Test
    public void testFindBooksPublishedBefore() {
        List<Book> booksBefore2021 = library.findBooksPublishedBefore(2021);
        assertEquals(2, booksBefore2021.size());
        assertTrue(booksBefore2021.contains(book1));
        assertTrue(booksBefore2021.contains(book3));
        
        List<Book> booksBefore2018 = library.findBooksPublishedBefore(2018);
        assertEquals(0, booksBefore2018.size());
    }
    
    @Test
    public void testSortBooksByYearAscending() {
        List<Book> sortedBooks = library.sortBooksByYearAscending();
        assertEquals(3, sortedBooks.size());
        assertEquals(book3, sortedBooks.get(0)); // 2018
        assertEquals(book1, sortedBooks.get(1)); // 2020
        assertEquals(book2, sortedBooks.get(2)); // 2022
    }
    
    @Test
    public void testSortBooksByYearDescending() {
        List<Book> sortedBooks = library.sortBooksByYearDescending();
        assertEquals(3, sortedBooks.size());
        assertEquals(book2, sortedBooks.get(0)); // 2022
        assertEquals(book1, sortedBooks.get(1)); // 2020
        assertEquals(book3, sortedBooks.get(2)); // 2018
    }
    
    @Test
    public void testFindBooksByTitleContaining() {
        List<Book> javaBooks = library.findBooksByTitleContaining("Java");
        assertEquals(2, javaBooks.size());
        assertTrue(javaBooks.contains(book1));
        assertTrue(javaBooks.contains(book2));
        
        List<Book> designBooks = library.findBooksByTitleContaining("Design");
        assertEquals(1, designBooks.size());
        assertTrue(designBooks.contains(book3));
        
        List<Book> nonexistentBooks = library.findBooksByTitleContaining("Python");
        assertEquals(0, nonexistentBooks.size());
    }
} 