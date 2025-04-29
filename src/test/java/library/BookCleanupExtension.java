package library;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * JUnit extension to save the state of the books.txt file before tests
 * and restore it after tests, ensuring test books don't persist.
 */
public class BookCleanupExtension implements BeforeAllCallback, ExtensionContext.Store.CloseableResource {

    private static final Path BOOKS_FILE = Paths.get("books.txt");
    private static final Path STANDARD_BACKUP_FILE = Paths.get("books.txt.standard");
    private static boolean initialized = false;

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        if (!initialized) {
            initialized = true;

            // Register a callback hook to restore the books file when the JVM exits
            context.getRoot().getStore(ExtensionContext.Namespace.GLOBAL)
                    .put(BookCleanupExtension.class.getName(), this);

            // Create a standard backup of the books file if it doesn't exist yet
            createStandardBackupIfNeeded();
        }
    }

    @Override
    public void close() {
        try {
            // Always restore from the standard backup after tests
            restoreFromStandardBackup();
        } catch (IOException e) {
            System.err.println("Failed to restore books file: " + e.getMessage());
        }
    }

    private void createStandardBackupIfNeeded() throws IOException {
        // Only create the standard backup if it doesn't exist yet
        if (Files.exists(BOOKS_FILE) && !Files.exists(STANDARD_BACKUP_FILE)) {
            Files.copy(BOOKS_FILE, STANDARD_BACKUP_FILE);
            System.out.println("Created standard backup of books file for test restoration");
        }
    }

    private void restoreFromStandardBackup() throws IOException {
        if (Files.exists(STANDARD_BACKUP_FILE)) {
            Files.copy(STANDARD_BACKUP_FILE, BOOKS_FILE, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Restored books file from standard backup after tests");
        }
    }
}