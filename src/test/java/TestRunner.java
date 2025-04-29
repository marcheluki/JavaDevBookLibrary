import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.engine.discovery.PackageSelector;
import library.BookCleanupExtension;

import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * A test runner for the Java Dev Book Library project
 * This class runs all unit tests and provides a summary of results
 */
public class TestRunner {
    public static void main(String[] args) {
        Path booksFile = Paths.get("books.txt");
        Path standardBackupFile = Paths.get("books.txt.standard");

        // Create a standard backup of the books file if it doesn't exist yet
        try {
            if (Files.exists(booksFile) && !Files.exists(standardBackupFile)) {
                Files.copy(booksFile, standardBackupFile);
                System.out.println("Created standard backup of books file for test restoration");
            }
        } catch (Exception e) {
            System.err.println("Failed to create standard backup: " + e.getMessage());
        }

        // Create a test summary listener
        SummaryGeneratingListener listener = new SummaryGeneratingListener();

        // Create a discovery request for the 'library' package
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(DiscoverySelectors.selectPackage("library"))
                .build();

        // Create a launcher
        Launcher launcher = LauncherFactory.create();

        // Register the listener
        launcher.registerTestExecutionListeners(listener);

        // Execute tests
        launcher.execute(request);

        // Print test results
        TestExecutionSummary summary = listener.getSummary();
        summary.printTo(new PrintWriter(System.out));

        // Restore the books file from standard backup after running tests
        try {
            if (Files.exists(standardBackupFile)) {
                Files.copy(standardBackupFile, booksFile, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Restored books file from standard backup after tests");
            }
        } catch (Exception e) {
            System.err.println("Failed to restore from standard backup: " + e.getMessage());
        }
    }
}