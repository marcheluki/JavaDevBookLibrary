package library;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

public class BookManagerTest {

    private BookManager bookManager;
    private Book book1;
    private Book book2;
    private Book book3;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUp() {
        // Using Library as an implementation of BookManager
        bookManager = new Library();
        book1 = new Book("Java Programming", "John Doe", "123456", 5, 2020);
        book2 = new Book("C# Basics", "Jane Smith", "234567", 3, 2022);
        book3 = new Book("Python for Beginners", "John Doe", "345678", 2, 2018);

        bookManager.addBook(book1);
        bookManager.addBook(book2);
        bookManager.addBook(book3);

        // Redirect System.out to capture output
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void testAddBook() {
        Book newBook = new Book("New Book", "New Author", "999999", 1, 2023);
        bookManager.addBook(newBook);

        List<Book> books = bookManager.getBooks();
        assertEquals(4, books.size());
        assertTrue(books.contains(newBook));
    }

    @Test
    public void testGetBooks() {
        List<Book> books = bookManager.getBooks();
        assertEquals(3, books.size());
        assertTrue(books.contains(book1));
        assertTrue(books.contains(book2));
        assertTrue(books.contains(book3));
    }

    @Test
    public void testFindBooksByAuthor() {
        List<Book> johnDoeBooks = bookManager.findBooksByAuthor("John Doe");
        assertEquals(2, johnDoeBooks.size());
        assertTrue(johnDoeBooks.contains(book1));
        assertTrue(johnDoeBooks.contains(book3));

        List<Book> janeSmithBooks = bookManager.findBooksByAuthor("Jane Smith");
        assertEquals(1, janeSmithBooks.size());
        assertTrue(janeSmithBooks.contains(book2));
    }

    @Test
    public void testSortBooksByTitle() {
        List<Book> sortedBooks = bookManager.sortBooksByTitle();
        assertEquals(3, sortedBooks.size());
        assertEquals("C# Basics", sortedBooks.get(0).getTitle());
        assertEquals("Java Programming", sortedBooks.get(1).getTitle());
        assertEquals("Python for Beginners", sortedBooks.get(2).getTitle());
    }

    @Test
    public void testPrintAllBooks() {
        // Reset the output stream
        outContent.reset();

        // Call the default method
        bookManager.printAllBooks();

        // Check that output contains all book titles
        String output = outContent.toString();
        assertTrue(output.contains("Java Programming"));
        assertTrue(output.contains("C# Basics"));
        assertTrue(output.contains("Python for Beginners"));

        // Restore System.out
        System.setOut(originalOut);
    }
}