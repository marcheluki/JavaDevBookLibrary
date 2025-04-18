package library;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class LibraryManagementSystem {


    private static final Logger log = Logger.getLogger(LibraryManagementSystem.class.getName());


    private final List<Book> bookInventory = new ArrayList<>();
    private final List<Patron> patrons = new ArrayList<>();
    private final Map<Integer, List<Book>> borrowedBooks = new HashMap<>();


    private final Map<String, List<Book>> bookTitleIndex = new HashMap<>();
    private final Map<String, List<Book>> bookAuthorIndex = new HashMap<>();
    private final Map<Integer, Patron> patronIndex = new HashMap<>();


    private final Scanner scanner = new Scanner(System.in);


    private final String BOOKS_FILE = "library/books.txt";
    private final String PATRONS_FILE = "library/patrons.txt";
    private final String LIBRARIANS_FILE = "library/librarians.txt";


    public LibraryManagementSystem() {
        log.info("Library Management System initialized.");
        loadBooksFromFile();
        loadPatronsFromFile();

    }


    public static void main(String[] args) {
        LibraryManagementSystem manager = new LibraryManagementSystem();
        manager.authenticateLibrarian();
        
        System.out.println("\n¿Desea ejecutar en modo simulación? (s/n)");
        String choice = manager.scanner.nextLine().toLowerCase();
        
        if (choice.equals("s")) {
            System.out.println("Ingrese el número de usuarios para la simulación:");
            int numberOfPatrons = Integer.parseInt(manager.scanner.nextLine());
            manager.startSimulation(numberOfPatrons);
            
            // Keep the main thread alive
            try {
                Thread.sleep(Long.MAX_VALUE);
            } catch (InterruptedException e) {
                System.out.println("Simulation interrupted");
            }
        } else {
            manager.runMenu();
        }
        
        System.out.println("Gracias por utilizar el Sistema de Biblioteca.");
    }

    // ------------------------------
    // Menu 
    // ------------------------------
    private void runMenu() {
        while (true) {
            System.out.println("\n--- Menú Principal ---");
            System.out.println("1) Agregar libro");
            System.out.println("2) Mostrar libros");
            System.out.println("3) Eliminar libro");
            System.out.println("4) Editar libro");
            System.out.println("5) Buscar libro");
            System.out.println("6) Registrar usuario");
            System.out.println("7) Mostrar usuarios");
            System.out.println("8) Editar usuario");
            System.out.println("9) Eliminar usuario");
            System.out.println("10) Buscar usuario");
            System.out.println("11) Prestar libro");
            System.out.println("12) Devolver libro");
            System.out.println("13) Libros por usuario");
            System.out.println("0) Salir");
            System.out.print("Seleccione una opción: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    addBook();
                    break;
                case "2":
                    displayBooks();
                    break;
                case "3":
                    removeBook();
                    break;
                case "4":
                    editBook();
                    break;
                case "5":
                    searchBook();
                    break;
                case "6":
                    registerPatron();
                    break;
                case "7":
                    displayPatrons();
                    break;
                case "8":
                    editPatron();
                    break;
                case "9":
                    removePatron();
                    break;
                case "10":
                    searchPatron();
                    break;
                case "11":
                    borrowBook();
                    break;
                case "12":
                    returnBook();
                    break;
                case "13":
                    booksPerPatron();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Opción inválida, intente de nuevo.");
            }
        }
    }


    private void authenticateLibrarian() {
        while (true) {
            System.out.println("\n--- Librarian Authentication ---");
            System.out.println("1) Sign Up");
            System.out.println("2) Log In");
            System.out.print("Seleccione una opción: ");
            String choice = scanner.nextLine();
            if (choice.equals("1")) {
                signupLibrarian();
            } else if (choice.equals("2")) {
                if (loginLibrarian()) {
                    break;
                } else {
                    System.out.println("Login failed. Please try again.");
                }
            } else {
                System.out.println("Opción inválida, intente de nuevo.");
            }
        }
    }

    private void signupLibrarian() {
        try {
            System.out.println("Ingrese su email (debe terminar con @librarian.com):");
            String email = scanner.nextLine().trim();
            if (!email.toLowerCase().endsWith("@librarian.com")) {
                System.out.println("El email debe terminar con @librarian.com. Operación cancelada.");
                return;
            }
            if (librarianExists(email)) {
                System.out.println("Ya existe un usuario con ese email. Por favor, inicie sesión.");
                return;
            }
            System.out.println("Ingrese su contraseña:");
            String password = scanner.nextLine();
            System.out.println("Confirme su contraseña:");
            String confirm = scanner.nextLine();
            if (!password.equals(confirm)) {
                System.out.println("Las contraseñas no coinciden. Operación cancelada.");
                return;
            }
            byte[] salt = getSalt();
            int iterations = 10000;
            int keyLength = 256;
            String hashed = hashPassword(password.toCharArray(), salt, iterations, keyLength);

            try (PrintWriter pw = new PrintWriter(new FileWriter(LIBRARIANS_FILE, true))) {
                pw.println(email + "|" + toHex(salt) + "|" + hashed);
            }
            System.out.println("Registro exitoso. Ahora inicie sesión.");
        } catch (Exception ex) {
            System.out.println("Error durante el registro: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private boolean loginLibrarian() {
        try {
            System.out.println("Ingrese su email:");
            String email = scanner.nextLine().trim();
            System.out.println("Ingrese su contraseña:");
            String password = scanner.nextLine();
            try (BufferedReader br = new BufferedReader(new FileReader(LIBRARIANS_FILE))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split("\\|");
                    if (parts.length != 3)
                        continue;
                    String storedEmail = parts[0];
                    String saltHex = parts[1];
                    String storedHash = parts[2];
                    if (storedEmail.equalsIgnoreCase(email)) {
                        byte[] salt = fromHex(saltHex);
                        int iterations = 10000;
                        int keyLength = 256;
                        String computedHash = hashPassword(password.toCharArray(), salt, iterations, keyLength);
                        if (computedHash.equals(storedHash)) {
                            System.out.println("Login exitoso. Bienvenido, " + email);
                            return true;
                        } else {
                            System.out.println("Contraseña incorrecta.");
                            return false;
                        }
                    }
                }
            }
            System.out.println("No se encontró un registro para ese email. Por favor, regístrese.");
            return false;
        } catch (Exception ex) {
            System.out.println("Error durante el login: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }

    private boolean librarianExists(String email) {
        try (BufferedReader br = new BufferedReader(new FileReader(LIBRARIANS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 1 && parts[0].equalsIgnoreCase(email)) {
                    return true;
                }
            }
        } catch (IOException e) {

            return false;
        }
        return false;
    }


    // Password Hashing Helpers (PBKDF2WithHmacSHA256)

    private static byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstanceStrong();
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    private static String hashPassword(final char[] password, final byte[] salt, int iterations, int keyLength) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLength);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return toHex(hash);
    }

    private static String toHex(byte[] array) {
        StringBuilder sb = new StringBuilder(array.length * 2);
        for (byte b : array) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private static byte[] fromHex(String hex) {
        int len = hex.length();
        byte[] bytes = new byte[len / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }




    private void loadBooksFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(BOOKS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {

                String[] parts = line.split("\\|");
                if (parts.length == 4) {
                    String title = parts[0];
                    String author = parts[1];
                    String isbn = parts[2];
                    int copies = Integer.parseInt(parts[3]);
                    Book book = new Book(title, author, isbn, copies);
                    bookInventory.add(book);
                    addBookToIndices(book);
                }
            }
        } catch (IOException e) {
            log.log(Level.WARNING, "No se pudo cargar books.txt. Se creará uno nuevo al guardar.", e);
        }
    }

    private void saveBooksToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(BOOKS_FILE))) {
            for (Book b : bookInventory) {

                pw.println(b.getTitle() + "|" + b.getAuthor() + "|" + b.getIsbn() + "|" + b.getCopies());
            }
        } catch (IOException e) {
            log.log(Level.SEVERE, "Error al guardar books.txt.", e);
        }
    }


    private void loadPatronsFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(PATRONS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 3) {
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    String contact = parts[2];
                    Patron patron = new Patron(name, id, contact, this, 0);
                    patrons.add(patron);
                    patronIndex.put(id, patron);
                }
            }
        } catch (IOException e) {
            log.log(Level.WARNING, "No se pudo cargar patrons.txt. Se creará uno nuevo al guardar.", e);
        }
    }

    private void savePatronsToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(PATRONS_FILE))) {
            for (Patron p : patrons) {


                pw.println(p.getId() + "|" + p.getName() + "|" + p.getContact());
            }
        } catch (IOException e) {
            log.log(Level.SEVERE, "Error al guardar patrons.txt.", e);
        }
    }






    public void addBook() {
        try {
            System.out.println("Ingrese el título del libro:");
            String title = scanner.nextLine();
            System.out.println("Ingrese el autor del libro:");
            String author = scanner.nextLine();
            System.out.println("Ingrese el ISBN del libro:");
            String isbn = scanner.nextLine();

            for (Book bk : bookInventory) {
                if (bk.getIsbn().equalsIgnoreCase(isbn)) {
                    System.out.println("Ya existe un libro con el ISBN " + isbn + ". Operación cancelada.");
                    return;
                }
            }
            System.out.println("Ingrese la cantidad de copias disponibles:");
            int copies = Integer.parseInt(scanner.nextLine());
            Book newBook = new Book(title, author, isbn, copies);
            bookInventory.add(newBook);
            addBookToIndices(newBook);
            saveBooksToFile();
            System.out.println("¡Libro agregado exitosamente!");
        } catch (NumberFormatException e) {
            log.log(Level.SEVERE, "Error al parsear el número de copias.", e);
            System.out.println("Cantidad de copias inválida. Operación cancelada.");
        }
    }

    private void addBookToIndices(Book book) {
        String titleKey = book.getTitle().toLowerCase();
        bookTitleIndex.computeIfAbsent(titleKey, _ -> new ArrayList<>()).add(book);
        String authorKey = book.getAuthor().toLowerCase();
        bookAuthorIndex.computeIfAbsent(authorKey, _ -> new ArrayList<>()).add(book);
    }

    private void rebuildBookIndices() {
        bookTitleIndex.clear();
        bookAuthorIndex.clear();
        for (Book book : bookInventory) {
            addBookToIndices(book);
        }
    }

    public void displayBooks() {
        if (bookInventory.isEmpty()) {
            System.out.println("No hay libros disponibles en la biblioteca.");
            return;
        }
        for (Book b : bookInventory) {
            b.display();
        }
    }

    public void removeBook() {
        System.out.println("Ingrese el título del libro que desea eliminar:");
        String removeTitle = scanner.nextLine();
        String titleKey = removeTitle.toLowerCase();
        if (!bookTitleIndex.containsKey(titleKey)) {
            System.out.println("No se encontró un libro con ese título.");
            return;
        }
        List<Book> booksToRemove = bookTitleIndex.get(titleKey);
        Book bookToRemove = null;
        if (booksToRemove.size() == 1) {
            bookToRemove = booksToRemove.get(0);
        } else {
            System.out.println("Se encontraron varios libros con ese título. Ingrese el ISBN del libro a eliminar:");
            String isbnInput = scanner.nextLine();
            for (Book b : booksToRemove) {
                if (b.getIsbn().equalsIgnoreCase(isbnInput)) {
                    bookToRemove = b;
                    break;
                }
            }
            if (bookToRemove == null) {
                System.out.println("No se encontró un libro con ese ISBN.");
                return;
            }
        }
        final Book finalBookToRemove = bookToRemove;
        bookInventory.remove(bookToRemove);
        for (List<Book> loans : borrowedBooks.values()) {
            loans.removeIf(b -> b.getIsbn().equalsIgnoreCase(finalBookToRemove.getIsbn()));
        }
        System.out.println("Libro eliminado de la biblioteca.");
        log.info("Libro eliminado: " + bookToRemove.getTitle());
        rebuildBookIndices();
        saveBooksToFile();
    }

    public void editBook() {
        System.out.println("Ingrese el ISBN del libro a editar:");
        String targetIsbn = scanner.nextLine();
        Book target = null;
        for (Book b : bookInventory) {
            if (b.getIsbn().equalsIgnoreCase(targetIsbn)) {
                target = b;
                break;
            }
        }
        if (target == null) {
            System.out.println("Libro con ISBN " + targetIsbn + " no encontrado.");
            return;
        }
        System.out.println("Valores actuales:");
        target.display();
        System.out.println("Ingrese el nuevo título (o presione ENTER para mantener):");
        String newTitle = scanner.nextLine();
        if (!newTitle.trim().isEmpty()) {
            target.setTitle(newTitle);
        }
        System.out.println("Ingrese el nuevo autor (o presione ENTER para mantener):");
        String newAuthor = scanner.nextLine();
        if (!newAuthor.trim().isEmpty()) {
            target.setAuthor(newAuthor);
        }
        System.out.println("Ingrese el nuevo ISBN (o presione ENTER para mantener):");
        String newIsbn = scanner.nextLine();
        if (!newIsbn.trim().isEmpty()) {
            target.setIsbn(newIsbn);
        }
        System.out.println("Ingrese la nueva cantidad de copias (ingrese -1 para mantener el valor actual):");
        try {
            int newCopies = Integer.parseInt(scanner.nextLine());
            if (newCopies != -1) {
                target.setCopies(newCopies);
            }
        } catch (NumberFormatException e) {
            log.log(Level.WARNING, "Valor inválido para copias; se mantiene el valor anterior.", e);
        }
        System.out.println("Libro actualizado exitosamente!");
        rebuildBookIndices();
        saveBooksToFile();
    }


    public void searchBook() {
        System.out.println("Ingrese el título o autor del libro a buscar:");
        String query = scanner.nextLine().toLowerCase();
        Set<Book> results = new HashSet<>();
        if (bookTitleIndex.containsKey(query)) {
            results.addAll(bookTitleIndex.get(query));
        }
        if (bookAuthorIndex.containsKey(query)) {
            results.addAll(bookAuthorIndex.get(query));
        }
        if (results.isEmpty()) {
            System.out.println("No se encontraron libros que coincidan con ese criterio.");
        } else {
            for (Book b : results) {
                b.display();
            }
        }
    }


    public void registerPatron() {
        try {
            System.out.println("Ingrese el nombre del usuario:");
            String name = scanner.nextLine();
            System.out.println("Ingrese el ID del usuario:");
            int id = Integer.parseInt(scanner.nextLine());
            if (patronIndex.containsKey(id)) {
                System.out.println("Ya existe un usuario con el ID " + id + ". Operación cancelada.");
                return;
            }
            System.out.println("Ingrese el contacto del usuario:");
            String contact = scanner.nextLine();
            Patron newPatron = new Patron(name, id, contact, this, 0);
            patrons.add(newPatron);
            patronIndex.put(id, newPatron);
            System.out.println("Usuario registrado exitosamente!");
            savePatronsToFile();
        } catch (NumberFormatException e) {
            log.log(Level.SEVERE, "Error al parsear el ID del usuario.", e);
            System.out.println("ID inválido. Operación cancelada.");
        }
    }

    public void displayPatrons() {
        if (patrons.isEmpty()) {
            System.out.println("No hay usuarios registrados.");
            return;
        }
        for (Patron p : patrons) {
            p.display();
        }
    }

    public void editPatron() {
        System.out.println("Ingrese el ID del usuario a editar:");
        int targetId;
        try {
            targetId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            log.log(Level.WARNING, "ID inválido.", e);
            System.out.println("Operación cancelada.");
            return;
        }
        Patron target = null;
        for (Patron p : patrons) {
            if (p.getId() == targetId) {
                target = p;
                break;
            }
        }
        if (target == null) {
            System.out.println("Usuario con ID " + targetId + " no encontrado.");
            return;
        }
        System.out.println("Valores actuales:");
        target.display();
        System.out.println("Ingrese el nuevo contacto (o presione ENTER para mantener el actual):");
        String newContact = scanner.nextLine();
        if (!newContact.trim().isEmpty()) {
            target.setContact(newContact);
        }
        System.out.println("Usuario actualizado exitosamente!");
        patronIndex.put(targetId, target);
        savePatronsToFile();
    }

    public void removePatron() {
        System.out.println("Ingrese el ID del usuario a eliminar:");
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            log.log(Level.WARNING, "ID inválido.", e);
            System.out.println("Operación cancelada.");
            return;
        }
        Iterator<Patron> it = patrons.iterator();
        boolean found = false;
        while (it.hasNext()) {
            Patron p = it.next();
            if (p.getId() == id) {
                it.remove();
                patronIndex.remove(id);
                found = true;
                System.out.println("Usuario eliminado exitosamente.");
                break;
            }
        }
        if (!found) {
            System.out.println("No se encontró un usuario con ID " + id + ".");
        }
        savePatronsToFile();
    }

    public void searchPatron() {
        System.out.println("Ingrese el ID del usuario a buscar:");
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            log.log(Level.WARNING, "ID inválido.", e);
            System.out.println("Operación cancelada.");
            return;
        }
        if (patronIndex.containsKey(id)) {
            Patron p = patronIndex.get(id);
            p.display();
            if (borrowedBooks.containsKey(id) && !borrowedBooks.get(id).isEmpty()) {
                System.out.println("Libros prestados por el usuario:");
                for (Book b : borrowedBooks.get(id)) {
                    b.display();
                }
            } else {
                System.out.println("Este usuario no tiene libros prestados.");
            }
        } else {
            System.out.println("No existe un usuario con ID " + id + ".");
        }
    }

    public void booksPerPatron() {
        System.out.println("Ingrese el ID del usuario para ver los libros prestados:");
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            log.log(Level.SEVERE, "ID inválido.", e);
            System.out.println("Operación cancelada.");
            return;
        }
        if (borrowedBooks.containsKey(id)) {
            List<Book> list = borrowedBooks.get(id);
            System.out.println("El usuario con ID " + id + " ha prestado " + list.size() + " libro(s):");
            for (Book b : list) {
                b.display();
            }
        } else {
            System.out.println("El usuario con ID " + id + " no tiene libros prestados.");
        }
    }

    public void borrowBook() {
        try {
            System.out.println("Ingrese el ID del usuario:");
            int id = Integer.parseInt(scanner.nextLine());
            System.out.println("Ingrese el título del libro a prestar:");
            String title = scanner.nextLine();
            borrowBook(title, id);
        } catch (NumberFormatException e) {
            log.log(Level.SEVERE, "Error al parsear el ID del usuario.", e);
            System.out.println("ID inválido. Operación cancelada.");
        }
    }

    public void returnBook() {
        try {
            System.out.println("Ingrese el ID del usuario:");
            int id = Integer.parseInt(scanner.nextLine());
            System.out.println("Ingrese el título del libro a devolver:");
            String title = scanner.nextLine();
            returnBook(title, id);
        } catch (NumberFormatException e) {
            log.log(Level.SEVERE, "Error al parsear el ID del usuario.", e);
            System.out.println("ID inválido. Operación cancelada.");
        }
    }

    public void startSimulation(int numberOfPatrons) {
        System.out.println("\n=== Iniciando Simulación ===");
        System.out.println("Número de usuarios: " + numberOfPatrons);
        System.out.println("Libros disponibles: " + bookInventory.size());
        
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

        // Create and start patron threads       for (int i = 0; i < numberOfPatrons; i++) {
            Patron patron = new Patron("Patron " + (i + 1), i + 1, "contact" + (i + 1) + "@email.com", this, maxTurns);
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
            System.out.println("Libros disponibles al final: " + bookInventory.size());
            System.out.println("===========================");
            
            // Exit the program
            System.exit(0);
        });
        
        monitorThread.start();
    }

    // Add these getter methods for thread-safe access
    public synchronized List<Book> getBookInventory() {
        return new ArrayList<>(bookInventory);
    }

    public synchronized Map<Integer, List<Book>> getBorrowedBooks() {
        return new HashMap<>(borrowedBooks);
    }

    public synchronized boolean borrowBook(String bookTitle, int patronId) {
        Book target = null;
        for (Book b : bookInventory) {
            if (b.getTitle().equalsIgnoreCase(bookTitle) && b.getCopies() > 0) {
                target = b;
                break;
            }
        }
        if (target == null) {
            System.out.println("\n[Simulación] Usuario " + patronId + " intentó prestar el libro '" + bookTitle + "' pero no está disponible.");
            return false;
        }
        borrowedBooks.putIfAbsent(patronId, new ArrayList<>());
        borrowedBooks.get(patronId).add(target);
        target.setCopies(target.getCopies() - 1);
        System.out.println("\n[Simulación] Usuario " + patronId + " prestó exitosamente el libro '" + bookTitle + "'");
        System.out.println("  - Copias restantes: " + target.getCopies());
        System.out.println("  - Libros prestados por el usuario: " + borrowedBooks.get(patronId).size());
        saveBooksToFile();
        return true;
    }

    public synchronized boolean returnBook(String bookTitle, int patronId) {
        if (!borrowedBooks.containsKey(patronId) || borrowedBooks.get(patronId).isEmpty()) {
            System.out.println("\n[Simulación] Usuario " + patronId + " intentó devolver el libro '" + bookTitle + "' pero no tiene libros prestados.");
            return false;
        }
        
        List<Book> list = borrowedBooks.get(patronId);
        Iterator<Book> it = list.iterator();
        while (it.hasNext()) {
            Book b = it.next();
            if (b.getTitle().equalsIgnoreCase(bookTitle)) {
                b.setCopies(b.getCopies() + 1);
                it.remove();
                System.out.println("\n[Simulación] Usuario " + patronId + " devolvió exitosamente el libro '" + bookTitle + "'");
                System.out.println("  - Copias disponibles: " + b.getCopies());
                System.out.println("  - Libros prestados por el usuario: " + borrowedBooks.get(patronId).size());
                saveBooksToFile();
                return true;
            }
        }
        System.out.println("\n[Simulación] Usuario " + patronId + " intentó devolver el libro '" + bookTitle + "' pero no lo tiene prestado.");
        return false;
    }
}
