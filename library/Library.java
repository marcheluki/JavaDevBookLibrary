import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the BookManager interface
 * Demonstrates the use of Lambda expressions and Method References
 */
public class Library implements BookManager {
    
    private final List<Book> books;
    
    public Library() {
        this.books = new ArrayList<>();
    }
    
    @Override
    public void addBook(Book book) {
        books.add(book);
    }
    
    @Override
    public List<Book> getBooks() {
        return new ArrayList<>(books);
    }
    
    @Override
    public List<Book> findBooksByAuthor(String author) {
        // Using lambda expression to filter books by author
        return books.stream()
                .filter(book -> book.getAuthor().equalsIgnoreCase(author))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Book> sortBooksByTitle() {
        // Using lambda expression to sort books by title
        return books.stream()
                .sorted((b1, b2) -> b1.getTitle().compareTo(b2.getTitle()))
                .collect(Collectors.toList());
    }
    
    /**
     * Finds books published before a given year
     * @param year The year to compare against
     * @return List of books published before the specified year
     */
    public List<Book> findBooksPublishedBefore(int year) {
        return books.stream()
                .filter(book -> book.getYear() < year)
                .collect(Collectors.toList());
    }
    
    /**
     * Sorts books by publication year in ascending order
     * @return List of books sorted by year
     */
    public List<Book> sortBooksByYearAscending() {
        return books.stream()
                .sorted((b1, b2) -> Integer.compare(b1.getYear(), b2.getYear()))
                .collect(Collectors.toList());
    }
    
    /**
     * Sorts books by publication year in descending order
     * @return List of books sorted by year in descending order
     */
    public List<Book> sortBooksByYearDescending() {
        return books.stream()
                .sorted((b1, b2) -> Integer.compare(b2.getYear(), b1.getYear()))
                .collect(Collectors.toList());
    }
    
    /**
     * Finds books with titles containing the specified substring
     * @param substring The substring to search for in book titles
     * @return List of books with matching titles
     */
    public List<Book> findBooksByTitleContaining(String substring) {
        return books.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(substring.toLowerCase()))
                .collect(Collectors.toList());
    }
} 