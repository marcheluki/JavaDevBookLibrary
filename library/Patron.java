import java.util.Random;
import java.util.List;

public class Patron implements Runnable {
    private String name;
    private int id;
    private String contact;
    private LibraryManagementSystem library;
    private Random random;
    private boolean running;
    private int completedTurns;
    private int maxTurns;
    private boolean hasBorrowed;

    public Patron(String name, int id, String contact, LibraryManagementSystem library, int maxTurns) {
        this.name = name;
        this.id = id;
        this.contact = contact;
        this.library = library;
        this.random = new Random();
        this.running = true;
        this.completedTurns = 0;
        this.maxTurns = maxTurns;
        this.hasBorrowed = false;
    }

    public Patron(String name, int id, String contact) {
        this.name = name;
        this.id = id;
        this.contact = contact;
        this.random = new Random();
        this.running = false;
        this.completedTurns = 0;
        this.maxTurns = 0;
        this.hasBorrowed = false;
        this.library = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public int getId() {
        return id;
    }

    public void setId(int newId) {
        this.id = newId;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String newContact) {
        this.contact = newContact;
    }

    public void display() {
        System.out.println("ID: " + id 
            + ", Nombre: " + name 
            + ", Contacto: " + contact);
    }

    public void stop() {
        this.running = false;
    }

    public int getCompletedTurns() {
        return completedTurns;
    }

    public boolean isFinished() {
        return completedTurns >= maxTurns;
    }

    @Override
    public void run() {
        System.out.println("[Simulación] Usuario " + id + " (" + name + ") ha comenzado a usar la biblioteca");
        
        while (running && completedTurns < maxTurns) {
            try {
                // Simulate patron behavior
                int action;
                if (!hasBorrowed) {
                    action = 0;
                } else {
                    action = 1;
                }

                switch (action) {
                    case 0:
                        if (!library.getBookInventory().isEmpty()) {
                            int bookIndex = random.nextInt(library.getBookInventory().size());
                            Book book = library.getBookInventory().get(bookIndex);
                            System.out.println("[Simulación] Usuario " + id + " está intentando prestar el libro '" + book.getTitle() + "'");
                            if (library.borrowBook(book.getTitle(), this.id)) {
                                hasBorrowed = true;
                            }
                        } else {
                            System.out.println("[Simulación] Usuario " + id + " intentó prestar un libro pero no hay libros disponibles");
                        }
                        break;
                    case 1:
                        if (library.getBorrowedBooks().containsKey(this.id) && 
                            !library.getBorrowedBooks().get(this.id).isEmpty()) {
                            List<Book> borrowed = library.getBorrowedBooks().get(this.id);
                            int bookIndex = random.nextInt(borrowed.size());
                            Book book = borrowed.get(bookIndex);
                            System.out.println("[Simulación] Usuario " + id + " está intentando devolver el libro '" + book.getTitle() + "'");
                            if (library.returnBook(book.getTitle(), this.id)) {
                                hasBorrowed = false;
                                completedTurns++;
                                System.out.println("[Simulación] Usuario " + id + " ha completado " + completedTurns + " de " + maxTurns + " vueltas");
                            }
                        } else {
                            System.out.println("[Simulación] Usuario " + id + " intentó devolver un libro pero no tiene libros prestados");
                        }
                        break;
                }
                
                Thread.sleep(random.nextInt(4000) + 1000);
            } catch (InterruptedException e) {
                System.out.println("[Simulación] Usuario " + id + " fue interrumpido");
                break;
            }
        }
        
        System.out.println("[Simulación] Usuario " + id + " ha completado sus " + maxTurns + " vueltas y ha dejado la biblioteca");
    }
}
