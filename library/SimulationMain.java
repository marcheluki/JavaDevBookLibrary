import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Separate Main class focused on running the library simulation
 * with multi-threaded patrons
 */
public class SimulationMain {
    
    public static void main(String[] args) {
        LibraryManagementSystem library = new LibraryManagementSystem();
        Scanner scanner = new Scanner(System.in);
        
        // Authenticate the librarian
        library.authenticateLibrarian();
        
        System.out.println("\n=== Iniciando Simulación ===");
        System.out.println("Ingrese el número de usuarios para la simulación:");
        int numberOfPatrons = Integer.parseInt(scanner.nextLine());
        System.out.println("Número de usuarios: " + numberOfPatrons);
        System.out.println("Libros disponibles: " + library.getBooks().size());
        
        System.out.println("\nIngrese el número de vueltas por usuario (una vuelta = prestar y devolver un libro):");
        int maxTurns = Integer.parseInt(scanner.nextLine());
        
        System.out.println("\nLa simulación comenzará en 3 segundos...");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<Thread> patronThreads = new ArrayList<>();
        List<Patron> patrons = new ArrayList<>();

        // Create and start patron threads
        for (int i = 0; i < numberOfPatrons; i++) {
            Patron patron = new Patron("Patron " + (i + 1), i + 1, "contact" + (i + 1) + "@email.com", library, maxTurns);
            patrons.add(patron);
            Thread thread = new Thread(patron);
            patronThreads.add(thread);
            thread.start();
            System.out.println("Usuario " + (i + 1) + " ha entrado a la biblioteca");
        }

        System.out.println("\n=== Simulación en curso ===");
        System.out.println("La simulación terminará cuando todos los usuarios completen " + maxTurns + " vueltas");
        System.out.println("===========================\n");

        Thread monitorThread = new Thread(() -> {
            boolean allFinished = false;
            while (!allFinished) {
                allFinished = true;
                for (Patron patron : patrons) {
                    if (!patron.isFinished()) {
                        allFinished = false;
                        break;
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
            
            System.out.println("\n=== Simulación Completada ===");
            for (Patron patron : patrons) {
                patron.stop();
            }
            for (Thread thread : patronThreads) {
                try {
                    thread.join(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            
            System.out.println("\nResumen de la simulación:");
            System.out.println("Usuarios participantes: " + patrons.size());
            System.out.println("Vueltas completadas por usuario: " + maxTurns);
            System.out.println("Libros disponibles al final: " + library.getBooks().size());
            System.out.println("===========================");
            
            scanner.close();
            // Exit the program
            System.exit(0);
        });
        
        monitorThread.start();
        
        // Keep the main thread alive
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            System.out.println("Simulation interrupted");
        }
    }
} 