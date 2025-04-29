package library;

import java.util.Random;
import java.util.List;

public class Patron implements Runnable {
    private String name;
    private int id;
    private String contact;
    private LibraryManagementSystem library;

    private boolean running = true;
    private boolean finished = false;
    private int completedTurns = 0;
    private int maxTurns;

    // Books currently borrowed by this patron
    private Book borrowedBook;

    // Constructor for simulation
    public Patron(String name, int id, String contact, LibraryManagementSystem library,
            int maxTurns) {
        this.name = name;
        this.id = id;
        this.contact = contact;
        this.library = library;
        this.maxTurns = maxTurns;
    }

    // Constructor for general use
    public Patron(String name, int id, String contact) {
        this(name, id, contact, null, 0);
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void display() {
        System.out.println("ID: " + id + ", Nombre: " + name + ", Contacto: " + contact);
    }

    // Stop the patron's activity
    public void stop() {
        this.running = false;
        this.finished = true;
    }

    // Check if the patron has completed their turns
    public boolean isFinished() {
        return finished;
    }

    @Override
    public void run() {
        Random random = new Random();
        try {
            while (running && !finished) {
                // Simulate each turn (borrow and return a book)
                if (borrowedBook == null) {
                    // Try to borrow a book
                    Thread.sleep(random.nextInt(1000) + 500); // Random delay

                    List<Book> availableBooks = library.getBooks().stream()
                            .filter(book -> book.getCopies() > 0)
                            .collect(java.util.stream.Collectors.toList());

                    if (!availableBooks.isEmpty()) {
                        int bookIndex = random.nextInt(availableBooks.size());
                        Book selectedBook = availableBooks.get(bookIndex);

                        System.out.println("\n[" + name + "] intentando prestar el libro: " + selectedBook.getTitle());
                        boolean success = library.borrowBook(selectedBook.getTitle(), id);

                        if (success) {
                            borrowedBook = selectedBook;
                            System.out.println("[" + name + "] préstamo exitoso: " + selectedBook.getTitle());
                        } else {
                            System.out.println("[" + name + "] no pudo prestar ningún libro. Reintentando...");
                            Thread.sleep(random.nextInt(1000) + 1000);
                        }
                    } else {
                        System.out.println("[" + name + "] no hay libros disponibles. Esperando...");
                        Thread.sleep(random.nextInt(2000) + 1000);
                    }
                } else {
                    // Return the borrowed book
                    Thread.sleep(random.nextInt(2000) + 1000); // Random reading time

                    System.out.println("\n[" + name + "] intentando devolver el libro: " + borrowedBook.getTitle());
                    boolean success = library.returnBook(borrowedBook.getTitle(), id);

                    if (success) {
                        System.out.println("[" + name + "] devolvió exitosamente: " + borrowedBook.getTitle());
                        borrowedBook = null;
                        completedTurns++;

                        if (completedTurns >= maxTurns) {
                            System.out.println("[" + name + "] ha completado sus " + maxTurns
                                    + " vueltas. Saliendo de la biblioteca.");
                            finished = true;
                        }
                    } else {
                        System.out.println("[" + name + "] no pudo devolver el libro. Reintentando...");
                        Thread.sleep(random.nextInt(1000) + 500);
                    }
                }

                // Small pause between operations
                Thread.sleep(random.nextInt(500) + 200);
            }
        } catch (InterruptedException e) {
            System.out.println("[" + name + "] fue interrumpido.");
        }

        System.out.println("[" + name + "] ha terminado su actividad en la biblioteca.");
    }
}
