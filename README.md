# 📚 Final evaluation project: Book Management API and React Application

### Project Description
The objective of this project is to create a book management system consisting of a microservice API developed using Java Spring Boot and a React application that consumes the API. The API will implement CRUD (Create, Read, Update, Delete) operations for managing book data and will be containerized using Docker.

### Project Steps and Our Solution

We followed the outlined steps to build the full-stack application:

**Part 1: Create the API Microservice**

1.  **Set Up Project Structure:** A new directory was created, and a Spring Boot project was bootstrapped with Spring Web, Spring Data JPA, and H2 database dependencies.
2.  **Define the Book Entity:** A `Book` class was created with necessary fields and JPA annotations.
3.  **Create Repository Interface:** A `BookRepository` interface extending `JpaRepository` was implemented.
4.  **Implement the Service Layer:** A `BookService` class was created to handle the CRUD logic.
5.  **Create REST Controller:** A `BookController` class was implemented with the specified endpoints (`GET /books`, `GET /books/{id}`, `POST /books`, `PUT /books/{id}`, `DELETE /books/{id}`). The API endpoints are accessible under the `/api` path.
6.  **Dockerize the API:** A `Dockerfile` was created for the Spring Boot application.
7.  **Create a docker-compose.yml file:** A `docker-compose.yml` file was created to orchestrate the Spring Boot API service and a PostgreSQL database service (initially considered H2, but switched to PostgreSQL for persistence).
8.  **Build and Run Docker Containers:** We configured a root `docker-compose.yml` to build and run both the API and Frontend containers together.

**Part 2: Create the React Application**

1.  **Set Up Project Structure:** A new directory for the React application was created using Create React App.
2.  **Install Necessary Dependencies:** Axios and Material-UI were installed for HTTP requests and UI components. `react-router-dom` was also installed for navigation.
3.  **Create Components:** Components for `Header`, `BookList`, `BookDetails`, and `BookForm` were developed.
4.  **Connect to the API:** Axios was used to interact with the API service, making sure to use the correct `/api` prefix for requests.
5.  **Run the React Application:** The application is configured to run via Docker Compose, served by Nginx, which proxies API requests to the backend service.

During the development and integration process, we addressed several issues, including Docker build errors related to Node.js versions and npm dependencies, frontend-backend communication issues due to incorrect API base URL and Nginx configuration, and frontend UI/UX improvements like adding an Add Book modal, changing the Edit Book flow to a modal, implementing Snackbar notifications for success messages, and ensuring the book list updates automatically after add/delete operations without requiring a page refresh.

## 🔍 Full-Stack Project Overview

The Library Management System is a full-stack application consisting of a Java Spring Boot API backend and a React frontend, both containerized with Docker and orchestrated using Docker Compose. This system allows users to manage a collection of books through a web interface.

## 💾 Full-Stack Project Data Persistence

Data persistence for the book information is handled by a PostgreSQL database, managed as a service within the Docker Compose setup. The API interacts with this database using Spring Data JPA.

## ✨ Full-Stack Project Key Features

-   **Full-Stack Architecture:** A decoupled backend API and frontend React application.
-   **Containerization:** Both API and Frontend are containerized using Docker.
-   **Orchestration:** Docker Compose is used to build, run, and manage the multi-container application (API, Database, Frontend/Nginx).
-   **📖 Book Management (via API and Frontend):**
    -   **Retrieve Books:** View a list of all books.
    -   **View Book Details:** See details for a specific book.
    -   **Add Books:** Add new books using a form in a modal on the book list page.
    -   **Update Books:** Edit existing book details using a form in a modal on the book list page.
    -   **Delete Books:** Remove books from the collection.
-   **Responsive UI:** React frontend built with Material-UI components.
-   **Improved User Experience:**
    -   Dedicated Home page.
    -   Integrated Add and Edit book forms as modals on the Books list page.
    -   Snackbar notifications for successful operations.
    -   Automatic list updates after adding or deleting books.

## 🎯 Full-Stack Project Objectives

-   **Object-Oriented Programming (OOP):** Demonstrates OOP principles in the Java API design.
-   **RESTful API Development:** Implementation of CRUD endpoints using Spring Boot.
-   **Frontend Development with React:** Building a single-page application to consume the API.
-   **Containerization with Docker:** Packaging the API and Frontend into Docker images.
-   **Orchestration with Docker Compose:** Defining and running the multi-container application.
-   **Database Integration:** Connecting the API to a PostgreSQL database.
-   **Improved User Interface:** Enhancing the frontend with better navigation and workflows.

## 🚀 How to Run the Full-Stack Application

The application can be built and run using Docker Compose from the root directory of the project.

1.  **Clone the repository:**

    ```bash
    git clone <repository_url>
    ```

2.  **Navigate to the project root directory:**

    ```bash
    cd JavaDevBookLibrary
    ```

3.  **Build and run the Docker containers:**

    ```bash
    docker-compose up --build
    ```

    This command will build the Docker images for the API and Frontend (if not already built or if changes are detected) and start the API, Database, and Frontend services.

4.  **Access the application:**

    -   Frontend: Open your web browser and go to `http://localhost`.
    -   API: The API is available internally within the Docker network and is exposed via the frontend's Nginx proxy at `http://localhost/api`. Direct access to the API service is at `http://localhost:8080/api`.

5.  **Stop the application:**

    To stop the running containers, press `Ctrl+C` in the terminal where `docker-compose up` is running. To stop and remove the containers, networks, and volumes, run:

    ```bash
    docker-compose down -v
    ```

## 📂 Project Structure (Full-Stack and Console App)

```
JavaDevBookLibrary/
├── src/ ... (Original console app source)
├── api/ # Spring Boot API microservice
│   ├── src/ ...
│   ├── Dockerfile
│   └── pom.xml
├── frontend/ # React application
│   ├── public/ ...
│   ├── src/ ...
│   ├── Dockerfile
│   ├── nginx.conf
│   ├── package.json
│   └── ...
├── docker-compose.yml # Root Docker Compose file
├── README.md          # Main project README
├── .git/ ...
├── .gradle/ ...
├── target/ ...
├── lib/ ...
├── books.txt          # Original console app data
├── patrons.txt        # Original console app data
├── librarians.txt     # Original console app data
├── settings.xml
└── build.gradle
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
