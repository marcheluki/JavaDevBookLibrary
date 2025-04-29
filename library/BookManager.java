import java.util.List;

/**
 * Interface for managing books in a library system using Java 8 features
 */
public interface BookManager {
    
    /**
     * Adds a book to the collection
     * @param book The book to be added
     */
    void addBook(Book book);
    
    /**
     * Gets all books in the collection
     * @return List of all books
     */
    List<Book> getBooks();
    
    /**
     * Finds books by a specific author
     * @param author The author to search for
     * @return List of books by the specified author
     */
    List<Book> findBooksByAuthor(String author);
    
    /**
     * Sorts books by title
     * @return A list of books sorted by title
     */
    List<Book> sortBooksByTitle();
    
    /**
     * Default method to print all books in the collection
     * Uses method reference to print each book
     */
    default void printAllBooks() {
        getBooks().forEach(System.out::println);
    }
} 