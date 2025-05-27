# 📚 Final evaluation project: Book Management API and React Application

### Project Description
The objective of this project is to create a book management system consisting of a microservice API developed using Java Spring Boot and a React application that consumes the API. The API implements CRUD (Create, Read, Update, Delete) operations for managing book data and is containerized using Docker.

### Project Steps and Our Solution

We followed the outlined steps to build the full-stack application:

**Part 1: Create the API Microservice**

1. **Set Up Project Structure:** A new directory was created, and a Spring Boot project was bootstrapped with Spring Web, Spring Data JPA, and PostgreSQL database dependencies.
2. **Define the Book Entity:** A `Book` class was created with necessary fields (id, title, author, isbn, year, copies) and JPA annotations.
3. **Create Repository Interface:** A `BookRepository` interface extending `JpaRepository` was implemented with custom query methods.
4. **Implement the Service Layer:** A `BookService` class was created to handle the CRUD logic and business rules.
5. **Create REST Controller:** A `BookController` class was implemented with the following endpoints:
   - `GET /api/books`: Retrieve all books
   - `GET /api/books/{id}`: Retrieve a book by ID
   - `POST /api/books`: Insert a new book
   - `PUT /api/books/{id}`: Update an existing book
   - `DELETE /api/books/{id}`: Delete a book
   - Additional endpoints for searching by title, author, and year
6. **Dockerize the API:** A `Dockerfile` was created for the Spring Boot application.
7. **Create a docker-compose.yml file:** A `docker-compose.yml` file was created to orchestrate the Spring Boot API service and PostgreSQL database service.
8. **Build and Run Docker Containers:** We configured a root `docker-compose.yml` to build and run both the API and Frontend containers together.

**Part 2: Create the React Application**

1. **Set Up Project Structure:** A new directory for the React application was created using Create React App with TypeScript.
2. **Install Necessary Dependencies:** 
   - Axios for HTTP requests
   - Material-UI for UI components
   - React Router for navigation
   - TypeScript for type safety
3. **Create Components:**
   - `BookList`: Main component with a bookshelf-style interface and modal forms
   - `AddBookForm`: Modal form for adding new books with validation
   - `EditBookForm`: Modal form for editing books (ISBN field disabled)
   - `BookDetails`: Detailed view of a single book
   - `Header`: Navigation component
   - `ErrorAlert`: Reusable error display component
4. **Connect to the API:** Axios was used to interact with the API service, with proper error handling and loading states.
5. **Run the React Application:** The application is configured to run via Docker Compose, served by Nginx, which proxies API requests to the backend service.

### Key Features Implemented

1. **API Features:**
   - Full CRUD operations for books
   - Search functionality by title, author, and year
   - Data validation and error handling
   - PostgreSQL database integration
   - Docker containerization

2. **Frontend Features:**
   - Modern, responsive UI with Material-UI
   - Bookshelf-style book list
   - Modal forms for add/edit operations
   - Form validation
   - Real-time updates without page refresh
   - Error handling and user feedback
   - TypeScript for type safety
   - Nginx for serving static files and API proxying

3. **User Experience Improvements:**
   - Modal-based forms for better workflow
   - Snackbar notifications for operation feedback
   - Disabled ISBN field in edit mode to prevent data inconsistency
   - Visual separation between books in the list
   - Immediate feedback on operations
   - Error alerts for failed operations

4. **Development Features:**
   - Docker Compose for easy deployment
   - Development and production configurations
   - API request logging in development
   - TypeScript for better code quality
   - Component-based architecture

### How to Run

1. **Clone the repository:**
   ```bash
   git clone <repository_url>
   ```

2. **Navigate to the project root:**
   ```bash
   cd JavaDevBookLibrary
   ```

3. **Build and run with Docker Compose:**
   ```bash
   docker-compose up --build
   ```

4. **Access the application:**
   - Frontend: http://localhost
   - API: http://localhost/api

### Project Structure

```
JavaDevBookLibrary/
├── api/                    # Spring Boot API
│   ├── src/
│   │   ├── main/java/com/library/api/
│   │   │   ├── controller/  # REST controllers
│   │   │   ├── service/     # Business logic
│   │   │   ├── repository/  # Data access
│   │   │   └── model/       # Entity classes
│   │   └── resources/       # Configuration
│   ├── Dockerfile
│   └── pom.xml
├── frontend/              # React application
│   ├── src/
│   │   ├── components/    # React components
│   │   ├── services/      # API services
│   │   └── config/        # Configuration
│   ├── Dockerfile
│   └── nginx.conf
└── docker-compose.yml     # Root Docker Compose
```

---

# Book Library on Console: 📚 Library Management System

## 🔍 Overview
The Library Management System is a Java-based console application designed to help librarians and library staff manage the library's collection of books. This project implements Java 8 features such as Lambda expressions, Method References, and Default Methods in interfaces to create a functional and efficient library management solution.

The system provides a simple text-based menu for performing essential operations such as adding, editing, removing, and displaying books; registering, editing, removing, and searching for patrons; and managing book borrowing and returns. Additionally, the system includes a librarian authentication module where the user must sign up or log in with an email ending in @librarian.com. Passwords are securely hashed (using PBKDF2 with HMAC-SHA256) and stored in a file.

## 💾 Data Persistence (Console App)

Data persistence is implemented via text files:

books.txt – stores book records in the format: title|author|isbn|copies|year|type
patrons.txt – stores patron records in the format: id|name|contact
librarians.txt – stores librarian authentication records (email, salt, hashed password)

## ✨ Key Features (Console App)

🔖 Book Management:

Add Books: Enter book details (title, author, ISBN, number of copies) to add to the inventory.
Remove Books: Remove books from the inventory (if no longer available or needed).
Display Books: View a list of all available books.
Edit Books: Search for a book by its ISBN, display current values, and update the book's information.
Search Books: Optimized search by title or author using hash indices.
👥 Patron Management:

Register Patrons: Register new patrons by entering their name, ID, and contact details.
View Patrons: Display all registered patrons.
Edit Patrons: Update a patron's contact information by searching with their ID.
Remove Patrons: Delete a patron from the system.
Search Patrons: Search for a patron by ID and display their details along with borrowed books.
🔄 Borrowing and Returning Books:

Borrow Books: Allows patrons to borrow books; the system updates available copies and tracks which books are on loan.
Return Books: Patrons can return borrowed books, which updates the inventory accordingly.
Books per Patron: View the number of books a patron currently has borrowed and their details.
🔒 Librarian Authentication:

Sign Up/Log In: Before accessing the system, the user must authenticate as a librarian using an email that ends with @librarian.com.
Secure Password Storage: Passwords are hashed (using PBKDF2 with HMAC-SHA256) along with a salt, and the credentials are stored in librarians.txt.
👨‍💻 User-Friendly Interface:

The application features a simple text-based menu and provides clear instructions at every step.
Proper exception handling ensures that the application handles errors gracefully.

## 📋 Java 8 Features Implementation (Console App)

This project meets the requirements for "Exploring Lambdas, Method References, and Default Methods in Java" by implementing:

1.  🔹 Lambda Expressions:
    Used for filtering books in findBooksByAuthor, findBooksPublishedBefore, and findBooksByTitleContaining methods:
    ```java
    // Filter books by author
    return bookInventory.stream()
            .filter(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()))
            .collect(Collectors.toList());
    ```
    Used for sorting books in sortBooksByTitle, sortBooksByYearAscending, and sortBooksByYearDescending methods:
    ```java
    // Sort books by title
    return bookInventory.stream()
            .sorted((b1, b2) -> b1.getTitle().compareTo(b2.getTitle()))
            .collect(Collectors.toList());
    ```
2.  🔹 Method References:
    Implemented in the printAllBooks default method:
    ```java
    // Default method to print all books
    default void printAllBooks() {
        getBooks().forEach(System.out::println);
    }
    ```
    Used throughout the application for displaying books:
    ```java
    books.forEach(book -> book.display());
    ```
3.  🔹 Default Methods:
    Implemented in the BookManager interface:
    ```java
    default void printAllBooks() {
        getBooks().forEach(System.class::println);
    }
    ```
    This allows all implementing classes (Library, LibraryManagementSystem) to inherit the method implementation.
4.  🔹 Additional Features:
    Advanced filtering capabilities
    Multiple sorting methods
    Support for ebooks and physical books through inheritance
    Multi-threaded patron simulation

## 🎯 Objectives (Console App)

Object-Oriented Programming (OOP):
Demonstrates the creation and use of classes and objects, encapsulation of data, and modeling of real-world entities (books and patrons).

Java 8 Functional Programming:
Effectively uses Lambdas, Method References, Default Methods, and Streams to handle collections and operations.

Data Structures:
Uses collections such as ArrayList, HashMap, and streams to manage and process data efficiently.

Input/Output Handling:
Manages user input and displays output in a console application.
Incorporates file I/O to persist data.

Exception Handling:
Correct use of try-catch blocks to handle potential runtime errors and input mismatches.

Multi-threading:
Implements thread-safe operations and a multi-threaded simulation.

## 🚀 How to Run the Console Application

Clone the repository:

```bash
git clone https://github.com/marcheluki/JavaDevBookLibrary.git
```

Navigate to the project directory:

```bash
cd JavaDevBookLibrary
```

Compile the project:

Using Maven:

```bash
mvn compile
```

Or manually:

```bash
javac -d target/classes src/main/java/library/*.java
```

Run the application:

Using Maven:

```bash
mvn exec:java -Dexec.mainClass="library.LibraryManagementSystem"
```

Or manually:

```bash
java -cp target/classes library.LibraryManagementSystem
```

The application will first prompt for librarian authentication (sign up or log in). Once authenticated, the main menu will appear.

## 🧪 Unit Testing (Console App)

The project includes a comprehensive suite of unit tests to verify the functionality of all major components. These tests are designed using JUnit 5 and test the following classes:

-   **Book**: Tests for constructors, getters, setters, and display methods
-   **EBook**: Tests for EBook-specific functionality and inheritance from Book
-   **Patron**: Tests for patron attributes, borrowing/returning books, and multi-threading
-   **Library**: Tests for the BookManager implementation and book collection management
-   **LibraryManagementSystem**: Tests for the main system functionality
-   **BookManager**: Tests for the interface implementation
-   **SimulationMain**: Tests for the multi-threaded simulation

### Prerequisites for Testing

To run the tests, you need:

1.  JUnit 5 (JUnit Jupiter)
2.  Maven (recommended)

### Running Tests with Maven

```bash
# Navigate to the project directory
cd JavaDevBookLibrary

# Run all tests
mvn test
```

### Running Tests Manually

You can also run the TestRunner class to execute all tests:

```bash
java -cp target/test-classes:target/classes:lib/* TestRunner
```

The test results will show the number of tests run, any failures, and overall test success.

### Book File Cleanup Feature

We've implemented a custom JUnit extension called `BookCleanupExtension` that ensures your test books don't persist in your books.txt file. This extension:

1.  Creates a standard backup of your books.txt file
2.  Automatically restores this backup after tests run
3.  Keeps your production data clean and test-free

---

## 🤝 Team Members

- Marcela Beatriz De La Rosa Barrios A01637239
- Sebastián Denhi Vega Saint Martín A01637397
- Ángela Estefanía Aguilar Medina A01637703
- Axel Daniel Padilla Reyes A01642700
- Diana Nicole Arana Sánchez A01642924