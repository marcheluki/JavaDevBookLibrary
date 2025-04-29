package library;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class EBookTest {
    
    private EBook ebook;
    
    @BeforeEach
    public void setUp() {
        // Create a new EBook instance before each test
        ebook = new EBook("Digital Book", "E-Author", "E12345", 10, 2023, 5.5, "PDF");
    }
    
    @Test
    public void testEBookConstructor() {
        assertEquals("Digital Book", ebook.getTitle());
        assertEquals("E-Author", ebook.getAuthor());
        assertEquals("E12345", ebook.getIsbn());
        assertEquals(10, ebook.getCopies());
        assertEquals(2023, ebook.getYear());
        assertEquals(5.5, ebook.getFileSize());
        assertEquals("PDF", ebook.getFormat());
    }
    
    @Test
    public void testSetFileSize() {
        ebook.setFileSize(10.5);
        assertEquals(10.5, ebook.getFileSize());
    }
    
    @Test
    public void testSetFormat() {
        ebook.setFormat("EPUB");
        assertEquals("EPUB", ebook.getFormat());
    }
    
    @Test
    public void testInheritance() {
        // Test that EBook is a Book
        assertTrue(ebook instanceof Book);
    }
    
    @Test
    public void testToString() {
        String expected = "EBook - Title: Digital Book, Author: E-Author, ISBN: E12345, Year: 2023, Available copies: 10, File Size: 5.5 MB, Format: PDF";
        assertEquals(expected, ebook.toString());
    }
} 