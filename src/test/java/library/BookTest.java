package library;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class BookTest {
    
    private Book book;
    
    @BeforeEach
    public void setUp() {
        // Create a new Book instance before each test
        book = new Book("Test Title", "Test Author", "12345", 5, 2023);
    }
    
    @Test
    public void testBookConstructor() {
        assertEquals("Test Title", book.getTitle());
        assertEquals("Test Author", book.getAuthor());
        assertEquals("12345", book.getIsbn());
        assertEquals(5, book.getCopies());
        assertEquals(2023, book.getYear());
    }
    
    @Test
    public void testSecondConstructor() {
        Book bookWithoutYear = new Book("Title2", "Author2", "54321", 3);
        assertEquals("Title2", bookWithoutYear.getTitle());
        assertEquals("Author2", bookWithoutYear.getAuthor());
        assertEquals("54321", bookWithoutYear.getIsbn());
        assertEquals(3, bookWithoutYear.getCopies());
        assertEquals(0, bookWithoutYear.getYear()); // Default year should be 0
    }
    
    @Test
    public void testSetTitle() {
        book.setTitle("New Title");
        assertEquals("New Title", book.getTitle());
    }
    
    @Test
    public void testSetAuthor() {
        book.setAuthor("New Author");
        assertEquals("New Author", book.getAuthor());
    }
    
    @Test
    public void testSetIsbn() {
        book.setIsbn("987654");
        assertEquals("987654", book.getIsbn());
    }
    
    @Test
    public void testSetCopies() {
        book.setCopies(10);
        assertEquals(10, book.getCopies());
    }
    
    @Test
    public void testSetYear() {
        book.setYear(2025);
        assertEquals(2025, book.getYear());
    }
    
    @Test
    public void testToString() {
        String expected = "Title: Test Title, Author: Test Author, ISBN: 12345, Year: 2023, Available copies: 5";
        assertEquals(expected, book.toString());
    }
} 