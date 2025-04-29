package library;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@ExtendWith(BookCleanupExtension.class)
public class LibraryManagementSystemTest {

    private LibraryManagementSystem library;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    @BeforeEach
    public void setUp() throws Exception {
        // Create test files
        try (PrintWriter pwBooks = new PrintWriter(new FileWriter("books_test.txt"));
                PrintWriter pwPatrons = new PrintWriter(new FileWriter("patrons_test.txt"));
                PrintWriter pwLibrarians = new PrintWriter(new FileWriter("librarians_test.txt"))) {

            // Add test book data
            pwBooks.println("Test Book|Test Author|TB12345|5|2022");

            // Add test patron data
            pwPatrons.println("101|Test Patron|test@email.com");

            // Add test librarian data (with a simple hash)
            pwLibrarians
                    .println("test@librarian.com|00112233445566778899aabbccddeeff|0123456789abcdef0123456789abcdef");
        }

        // Redirect System.out to capture output
        System.setOut(new PrintStream(outContent));

        // Redirect System.in to simulate user input
        String simulatedUserInput = "2\ntest@librarian.com\npassword123\n";
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

        // Create library system with test paths
        // Note: This test requires modification of the LibraryManagementSystem class
        // to accept file paths as constructor parameters
        library = new LibraryManagementSystem();

        // For testing purposes, we'll need to set the file paths through reflection
        // or modify the constructor to accept custom file paths
    }

    @AfterEach
    public void tearDown() throws Exception {
        // Restore original System.in and System.out
        System.setIn(originalIn);
        System.setOut(originalOut);

        // Delete test files
        Files.deleteIfExists(Paths.get("books_test.txt"));
        Files.deleteIfExists(Paths.get("patrons_test.txt"));
        Files.deleteIfExists(Paths.get("librarians_test.txt"));
    }

    @Test
    public void testGetBooks() {
        // This test depends on the books.txt file content
        List<Book> books = library.getBooks();
        assertNotNull(books);
        // We can't assert the exact size without knowing the content of books.txt
    }

    @Test
    public void testAddBook() {
        // Add a new book
        Book newBook = new Book("New Test Book", "New Author", "NTB12345", 3, 2023);
        library.addBook(newBook);

        // Verify the book was added
        List<Book> books = library.getBooks();
        assertTrue(books.stream().anyMatch(b -> b.getTitle().equals("New Test Book")));
    }

    @Test
    public void testFindBooksByAuthor() {
        // Add a book by a specific author
        Book testBook = new Book("Test Author Book", "Unique Author", "UAB12345", 2, 2023);
        library.addBook(testBook);

        // Find books by that author
        List<Book> foundBooks = library.findBooksByAuthor("Unique Author");
        assertTrue(foundBooks.stream().anyMatch(b -> b.getTitle().equals("Test Author Book")));
    }

    // Additional tests can be added based on the actual implementation
    // of LibraryManagementSystem class
}