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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import library.Patron;
import library.Book;
import library.EBook;
import library.BookManager;

public class LibraryManagementSystem implements BookManager {

    private final List<Book> bookInventory = new ArrayList<>();
    private final List<Patron> patrons = new ArrayList<>();
    private final Map<Integer, List<Book>> borrowedBooks = new HashMap<>();

    private final Map<String, List<Book>> bookTitleIndex = new HashMap<>();
    private final Map<String, List<Book>> bookAuthorIndex = new HashMap<>();
    private final Map<Integer, Patron> patronIndex = new HashMap<>();

    private final Scanner scanner = new Scanner(System.in);

    private final String BOOKS_FILE = "books.txt";
    private final String PATRONS_FILE = "patrons.txt";
    private final String LIBRARIANS_FILE = "librarians.txt";

    public LibraryManagementSystem() {
        System.out.println("Library Management System initialized.");
        loadBooksFromFile();
        loadPatronsFromFile();
    }

    public static void main(String[] args) {
        LibraryManagementSystem manager = new LibraryManagementSystem();
        manager.authenticateLibrarian();

        System.out.println("\n-------------------------------------");
        System.out.println("Bienvenido al Sistema de Biblioteca");
        System.out.println("Esta versión incluye características de Java 8");
        System.out.println("como expresiones lambda, referencias a métodos");
        System.out.println("y métodos por defecto en interfaces");
        System.out.println("-------------------------------------\n");

        manager.runMenu();

        System.out.println("Gracias por utilizar el Sistema de Biblioteca.");
    }

    // ------------------------------
    // Menu
    // ------------------------------
    private void runMenu() {
        while (true) {
            System.out.println("\n--- Menú Principal ---");
            System.out.println("1) Administración de Libros");
            System.out.println("2) Mostrar Biblioteca");
            System.out.println("3) Búsquedas");
            System.out.println("4) Sistema de Usuarios");
            System.out.println("5) Sistema de Préstamos");
            System.out.println("6) Ordenar Libros");
            System.out.println("0) Salir");
            System.out.print("Seleccione una opción: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    bookAdministrationMenu();
                    break;
                case "2":
                    displayLibraryMenu();
                    break;
                case "3":
                    searchMenu();
                    break;
                case "4":
                    userSystemMenu();
                    break;
                case "5":
                    bookLendingMenu();
                    break;
                case "6":
                    sortBooksMenu();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Opción inválida, intente de nuevo.");
            }
        }
    }

    // Book Administration Submenu
    private void bookAdministrationMenu() {
        while (true) {
            System.out.println("\n--- Administración de Libros ---");
            System.out.println("1) Agregar libro");
            System.out.println("2) Eliminar libro");
            System.out.println("3) Editar libro");
            System.out.println("0) Volver al menú principal");
            System.out.print("Seleccione una opción: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    addBook();
                    break;
                case "2":
                    removeBook();
                    break;
                case "3":
                    editBook();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Opción inválida, intente de nuevo.");
            }
        }
    }

    // Display Library Submenu
    private void displayLibraryMenu() {
        while (true) {
            System.out.println("\n--- Mostrar Biblioteca ---");
            System.out.println("1) Mostrar todos los libros");
            System.out.println("0) Volver al menú principal");
            System.out.print("Seleccione una opción: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    displayBooks();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Opción inválida, intente de nuevo.");
            }
        }
    }

    // Search Submenu
    private void searchMenu() {
        while (true) {
            System.out.println("\n--- Búsquedas ---");
            System.out.println("1) Buscar libros por autor");
            System.out.println("2) Buscar libros por título");
            System.out.println("3) Buscar libros publicados antes de un año");
            System.out.println("4) Buscar libros por usuario");
            System.out.println("0) Volver al menú principal");
            System.out.print("Seleccione una opción: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    findBooksByAuthorMenu();
                    break;
                case "2":
                    findBooksByTitleContainingMenu();
                    break;
                case "3":
                    findBooksPublishedBeforeMenu();
                    break;
                case "4":
                    booksPerPatron();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Opción inválida, intente de nuevo.");
            }
        }
    }

    // User System Submenu
    private void userSystemMenu() {
        while (true) {
            System.out.println("\n--- Sistema de Usuarios ---");
            System.out.println("1) Registrar usuario");
            System.out.println("2) Mostrar usuarios");
            System.out.println("3) Editar usuario");
            System.out.println("4) Eliminar usuario");
            System.out.println("5) Buscar usuario");
            System.out.println("0) Volver al menú principal");
            System.out.print("Seleccione una opción: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    registerPatron();
                    break;
                case "2":
                    displayPatrons();
                    break;
                case "3":
                    editPatron();
                    break;
                case "4":
                    removePatron();
                    break;
                case "5":
                    searchPatron();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Opción inválida, intente de nuevo.");
            }
        }
    }

    // Sort Books Submenu
    private void sortBooksMenu() {
        while (true) {
            System.out.println("\n--- Ordenar Libros ---");
            System.out.println("1) Ordenar por título");
            System.out.println("2) Ordenar por año (ascendente)");
            System.out.println("3) Ordenar por año (descendente)");
            System.out.println("0) Volver al menú principal");
            System.out.print("Seleccione una opción: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    sortBooksByTitleMenu();
                    break;
                case "2":
                    sortBooksByYearAscendingMenu();
                    break;
                case "3":
                    sortBooksByYearDescendingMenu();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Opción inválida, intente de nuevo.");
            }
        }
    }

    // Book Lending System Submenu
    private void bookLendingMenu() {
        while (true) {
            System.out.println("\n--- Sistema de Préstamos ---");
            System.out.println("1) Prestar libro");
            System.out.println("2) Devolver libro");
            System.out.println("3) Ver libros prestados por usuario");
            System.out.println("0) Volver al menú principal");
            System.out.print("Seleccione una opción: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    borrowBook();
                    break;
                case "2":
                    returnBook();
                    break;
                case "3":
                    booksPerPatron();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Opción inválida, intente de nuevo.");
            }
        }
    }

    public void authenticateLibrarian() {
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

    private static String hashPassword(final char[] password, final byte[] salt, int iterations, int keyLength)
            throws Exception {
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
                if (parts.length >= 4) {
                    String title = parts[0];
                    String author = parts[1];
                    String isbn = parts[2];
                    int copies = Integer.parseInt(parts[3]);

                    // Handle year field if present, otherwise default to 0
                    int year = 0;
                    if (parts.length >= 5) {
                        try {
                            year = Integer.parseInt(parts[4]);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid year format for book: " + title);
                        }
                    }

                    Book book;

                    // Check for book type if present (format: type|specific_attributes...)
                    if (parts.length > 5) {
                        String bookType = parts[5];

                        if ("E".equals(bookType) && parts.length >= 8) {
                            // EBook: E|fileSize|format
                            double fileSize = Double.parseDouble(parts[6]);
                            String format = parts[7];
                            book = new EBook(title, author, isbn, copies, year, fileSize, format);
                        } else {
                            // Regular book
                            book = new Book(title, author, isbn, copies, year);
                        }
                    } else {
                        // Older format without book type
                        book = new Book(title, author, isbn, copies, year);
                    }
                    bookInventory.add(book);
                    addBookToIndices(book);
                }
            }

        } catch (IOException e) {
            System.out.println("No se pudo cargar books.txt. Se creará uno nuevo al guardar.");
        } catch (NumberFormatException e) {
            System.out.println("Error al parsear valores numéricos en books.txt.");
        }
    }

    private void saveBooksToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(BOOKS_FILE))) {
            for (Book b : bookInventory) {
                StringBuilder line = new StringBuilder();
                line.append(b.getTitle()).append("|")
                        .append(b.getAuthor()).append("|")
                        .append(b.getIsbn()).append("|")
                        .append(b.getCopies()).append("|")
                        .append(b.getYear());

                // Add book type and specific attributes
                if (b instanceof EBook) {
                    EBook ebook = (EBook) b;
                    line.append("|E|") // E for EBook
                            .append(ebook.getFileSize()).append("|")
                            .append(ebook.getFormat());
                } else {
                    line.append("|B"); // B for regular Book
                }

                pw.println(line.toString());
            }
        } catch (IOException e) {
            System.out.println("Error al guardar books.txt.");
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
                    Patron patron = new Patron(name, id, contact);
                    patrons.add(patron);
                    patronIndex.put(id, patron);
                }
            }
        } catch (IOException e) {
            System.out.println("No se pudo cargar patrons.txt. Se creará uno nuevo al guardar.");
        }
    }

    private void savePatronsToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(PATRONS_FILE))) {
            for (Patron p : patrons) {
                pw.println(p.getId() + "|" + p.getName() + "|" + p.getContact());
            }
        } catch (IOException e) {
            System.out.println("Error al guardar patrons.txt.");
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

            System.out.println("Ingrese el año de publicación:");
            int year = Integer.parseInt(scanner.nextLine());

            // Ask for book type
            System.out.println("Seleccione el tipo de libro:");
            System.out.println("1) Libro normal");
            System.out.println("2) Libro electrónico (E-Book)");
            String bookTypeChoice = scanner.nextLine();

            Book newBook;
            if (bookTypeChoice.equals("2")) {
                System.out.println("Ingrese el tamaño del archivo (MB):");
                double fileSize = Double.parseDouble(scanner.nextLine());
                System.out.println("Ingrese el formato (PDF, EPUB, etc.):");
                String format = scanner.nextLine();
                newBook = new EBook(title, author, isbn, copies, year, fileSize, format);
            } else {
                newBook = new Book(title, author, isbn, copies, year);
            }

            bookInventory.add(newBook);
            addBookToIndices(newBook);
            saveBooksToFile();
            System.out.println("¡Libro agregado exitosamente!");
        } catch (NumberFormatException e) {
            System.out.println("Error al parsear valores numéricos.");
            System.out.println("Valor numérico inválido. Operación cancelada.");
        }
    }

    private void addBookToIndices(Book book) {
        String titleKey = book.getTitle().toLowerCase();
        bookTitleIndex.computeIfAbsent(titleKey, k -> new ArrayList<>()).add(book);
        String authorKey = book.getAuthor().toLowerCase();
        bookAuthorIndex.computeIfAbsent(authorKey, k -> new ArrayList<>()).add(book);
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
            System.out.println("Valor inválido para copias; se mantiene el valor anterior.");
        }

        System.out.println("Ingrese el nuevo año de publicación (ingrese -1 para mantener el valor actual):");
        try {
            int newYear = Integer.parseInt(scanner.nextLine());
            if (newYear != -1) {
                target.setYear(newYear);
            }
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido para año; se mantiene el valor anterior.");
        }

        // Handle EBook specific fields if applicable
        if (target instanceof EBook) {
            EBook ebook = (EBook) target;

            System.out.println("Ingrese el nuevo tamaño de archivo en MB (ingrese -1 para mantener el valor actual):");
            try {
                double newFileSize = Double.parseDouble(scanner.nextLine());
                if (newFileSize != -1) {
                    ebook.setFileSize(newFileSize);
                }
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido para tamaño de archivo; se mantiene el valor anterior.");
            }

            System.out.println("Ingrese el nuevo formato (o presione ENTER para mantener):");
            String newFormat = scanner.nextLine();
            if (!newFormat.trim().isEmpty()) {
                ebook.setFormat(newFormat);
            }
        }

        System.out.println("Libro actualizado exitosamente!");
        rebuildBookIndices();
        saveBooksToFile();
    }

    public void searchBook() {
        System.out.println("Ingrese el título o autor del libro a buscar:");
        String query = scanner.nextLine().toLowerCase();

        // Use stream to search for books by title or author
        List<Book> results = bookInventory.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(query) ||
                        book.getAuthor().toLowerCase().contains(query))
                .collect(Collectors.toList());

        if (results.isEmpty()) {
            System.out.println("No se encontraron libros que coincidan con la búsqueda: " + query);
        } else {
            System.out.println("\nLibros encontrados con la búsqueda '" + query + "':");
            results.forEach(book -> book.display());
        }
    }

    public void registerPatron() {
        try {
            System.out.println("Ingrese el ID numérico para el nuevo usuario:");
            int id = Integer.parseInt(scanner.nextLine());
            if (patronIndex.containsKey(id)) {
                System.out.println("Ya existe un usuario con ese ID. Operación cancelada.");
                return;
            }
            System.out.println("Ingrese el nombre del usuario:");
            String name = scanner.nextLine();
            System.out.println("Ingrese la información de contacto del usuario:");
            String contact = scanner.nextLine();
            Patron newPatron = new Patron(name, id, contact);
            patrons.add(newPatron);
            patronIndex.put(id, newPatron);
            savePatronsToFile();
            System.out.println("¡Usuario registrado exitosamente!");
        } catch (NumberFormatException e) {
            System.out.println("Error al parsear el ID del usuario.");
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
            System.out.println("ID inválido.");
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
            System.out.println("ID inválido.");
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
            System.out.println("ID inválido.");
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
            System.out.println("ID inválido.");
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
            System.out.println("Error al parsear el ID del usuario.");
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
            System.out.println("Error al parsear el ID del usuario.");
            System.out.println("ID inválido. Operación cancelada.");
        }
    }

    public void startSimulation(int numberOfPatrons) {
        System.out.println("La función de simulación está ahora disponible a través de la clase SimulationMain.");
        System.out.println("Por favor ejecute: java SimulationMain");
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
            System.out.println("\n[Simulación] Usuario " + patronId + " intentó prestar el libro '" + bookTitle
                    + "' pero no está disponible.");
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
            System.out.println("\n[Simulación] Usuario " + patronId + " intentó devolver el libro '" + bookTitle
                    + "' pero no tiene libros prestados.");
            return false;
        }

        List<Book> list = borrowedBooks.get(patronId);
        Iterator<Book> it = list.iterator();
        while (it.hasNext()) {
            Book b = it.next();
            if (b.getTitle().equalsIgnoreCase(bookTitle)) {
                b.setCopies(b.getCopies() + 1);
                it.remove();
                System.out.println(
                        "\n[Simulación] Usuario " + patronId + " devolvió exitosamente el libro '" + bookTitle + "'");
                System.out.println("  - Copias disponibles: " + b.getCopies());
                System.out.println("  - Libros prestados por el usuario: " + borrowedBooks.get(patronId).size());
                saveBooksToFile();
                return true;
            }
        }
        System.out.println("\n[Simulación] Usuario " + patronId + " intentó devolver el libro '" + bookTitle
                + "' pero no lo tiene prestado.");
        return false;
    }

    // --------------------------------
    // BookManager Interface Methods
    // --------------------------------
    @Override
    public synchronized void addBook(Book book) {
        bookInventory.add(book);
        addBookToIndices(book);
        saveBooksToFile();
    }

    @Override
    public List<Book> getBooks() {
        return new ArrayList<>(bookInventory);
    }

    @Override
    public List<Book> findBooksByAuthor(String author) {
        // Using lambda expression to filter books by author (partial match)
        return bookInventory.stream()
                .filter(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> sortBooksByTitle() {
        // Using lambda expression to sort books by title
        return bookInventory.stream()
                .sorted((b1, b2) -> b1.getTitle().compareTo(b2.getTitle()))
                .collect(Collectors.toList());
    }

    // Additional sorting methods using Java 8 features

    public List<Book> sortBooksByYearAscending() {
        return bookInventory.stream()
                .sorted((b1, b2) -> Integer.compare(b1.getYear(), b2.getYear()))
                .collect(Collectors.toList());
    }

    public List<Book> sortBooksByYearDescending() {
        return bookInventory.stream()
                .sorted((b1, b2) -> Integer.compare(b2.getYear(), b1.getYear()))
                .collect(Collectors.toList());
    }

    public List<Book> findBooksPublishedBefore(int year) {
        return bookInventory.stream()
                .filter(book -> book.getYear() < year)
                .collect(Collectors.toList());
    }

    public List<Book> findBooksByTitleContaining(String substring) {
        return bookInventory.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(substring.toLowerCase()))
                .collect(Collectors.toList());
    }

    // New menu methods for Java 8 features

    private void findBooksByAuthorMenu() {
        System.out.println("\n--- Buscar Libros por Autor ---");
        System.out.println("Ingrese el nombre del autor:");
        String author = scanner.nextLine();

        List<Book> books = bookInventory.stream()
                .filter(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .collect(Collectors.toList());

        if (books.isEmpty()) {
            System.out.println("No se encontraron libros del autor: " + author);
        } else {
            System.out.println("Libros del autor " + author + ":");
            books.forEach(book -> book.display());
        }
    }

    private void sortBooksByTitleMenu() {
        System.out.println("\n--- Libros Ordenados por Título ---");
        List<Book> sortedBooks = sortBooksByTitle();

        if (sortedBooks.isEmpty()) {
            System.out.println("No hay libros en la biblioteca.");
        } else {
            sortedBooks.forEach(book -> book.display());
        }
    }

    private void sortBooksByYearAscendingMenu() {
        System.out.println("\n--- Libros Ordenados por Año (Ascendente) ---");
        List<Book> sortedBooks = sortBooksByYearAscending();

        if (sortedBooks.isEmpty()) {
            System.out.println("No hay libros en la biblioteca.");
        } else {
            sortedBooks.forEach(book -> book.display());
        }
    }

    private void sortBooksByYearDescendingMenu() {
        System.out.println("\n--- Libros Ordenados por Año (Descendente) ---");
        List<Book> sortedBooks = sortBooksByYearDescending();

        if (sortedBooks.isEmpty()) {
            System.out.println("No hay libros en la biblioteca.");
        } else {
            sortedBooks.forEach(book -> book.display());
        }
    }

    private void findBooksPublishedBeforeMenu() {
        System.out.println("\n--- Buscar Libros Publicados Antes de un Año ---");
        System.out.println("Ingrese el año:");
        try {
            int year = Integer.parseInt(scanner.nextLine());
            List<Book> books = findBooksPublishedBefore(year);

            if (books.isEmpty()) {
                System.out.println("No se encontraron libros publicados antes del año " + year);
            } else {
                System.out.println("Libros publicados antes del año " + year + ":");
                books.forEach(book -> book.display());
            }
        } catch (NumberFormatException e) {
            System.out.println("Año inválido. Operación cancelada.");
        }
    }

    private void findBooksByTitleContainingMenu() {
        System.out.println("\n--- Buscar Libros por Título ---");
        System.out.println("Ingrese una palabra o frase para buscar en los títulos:");
        String substring = scanner.nextLine();

        List<Book> books = findBooksByTitleContaining(substring);

        if (books.isEmpty()) {
            System.out.println("No se encontraron libros con '" + substring + "' en el título");
        } else {
            System.out.println("Libros con '" + substring + "' en el título:");
            books.forEach(book -> book.display());
        }
    }
}
