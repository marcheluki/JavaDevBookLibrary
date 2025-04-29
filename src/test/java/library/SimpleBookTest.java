package library;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SimpleBookTest {
    
    @Test
    public void testBookCreation() {
        Book book = new Book("Test Title", "Test Author", "12345", 5, 2023);
        assertEquals("Test Title", book.getTitle());
        assertEquals("Test Author", book.getAuthor());
        assertEquals("12345", book.getIsbn());
        assertEquals(5, book.getCopies());
        assertEquals(2023, book.getYear());
    }
} 