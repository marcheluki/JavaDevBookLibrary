package library;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class PatronTest {
    
    private Patron patron;
    private LibraryManagementSystem library;
    
    @BeforeEach
    public void setUp() {
        library = new LibraryManagementSystem();
        patron = new Patron("Test Patron", 100, "test@example.com");
    }
    
    @Test
    @DisplayName("Test Patron constructor with only name, ID, and contact")
    public void testPatronConstructor() {
        assertEquals("Test Patron", patron.getName());
        assertEquals(100, patron.getId());
        assertEquals("test@example.com", patron.getContact());
        assertFalse(patron.isFinished());
    }
    
    @Test
    @DisplayName("Test Patron constructor with library and max turns")
    public void testPatronWithLibraryConstructor() {
        Patron patronWithLibrary = new Patron("Library Patron", 101, "library@example.com", library, 3);
        assertEquals("Library Patron", patronWithLibrary.getName());
        assertEquals(101, patronWithLibrary.getId());
        assertEquals("library@example.com", patronWithLibrary.getContact());
        assertFalse(patronWithLibrary.isFinished());
    }
    
    @Test
    @DisplayName("Test setting and getting patron name")
    public void testSetAndGetName() {
        patron.setName("New Name");
        assertEquals("New Name", patron.getName());
    }
    
    @Test
    @DisplayName("Test setting and getting patron contact")
    public void testSetAndGetContact() {
        patron.setContact("new@example.com");
        assertEquals("new@example.com", patron.getContact());
    }
    
    @Test
    @DisplayName("Test stopping a patron")
    public void testStopPatron() {
        patron.stop();
        assertTrue(patron.isFinished());
    }
    
    @Test
    @DisplayName("Test patron run method with basic functionality")
    public void testRunBasic() throws InterruptedException {
        // Create a library with books
        LibraryManagementSystem testLibrary = new LibraryManagementSystem();
        Book testBook = new Book("Test Run Book", "Test Author", "TEST123", 5, 2023);
        testLibrary.addBook(testBook);
        
        // Create a patron with 1 max turn
        Patron testPatron = new Patron("Run Test", 999, "run@test.com", testLibrary, 1);
        
        // Create a latch to wait for the thread to complete
        CountDownLatch latch = new CountDownLatch(1);
        
        // Create and start a thread with the patron
        Thread thread = new Thread(() -> {
            testPatron.run();
            latch.countDown();
        });
        thread.start();
        
        // Wait for the thread to complete (with timeout)
        boolean completed = latch.await(5, TimeUnit.SECONDS);
        
        // Stop the patron
        testPatron.stop();
        
        // Check that the thread completed
        assertTrue(completed, "Patron run method should complete");
        assertTrue(testPatron.isFinished(), "Patron should be finished after running");
    }
    
    @Test
    @DisplayName("Test patron display method")
    public void testDisplay() {
        // This test just makes sure the display method doesn't throw an exception
        assertDoesNotThrow(() -> patron.display());
    }
} 