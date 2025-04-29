# 📚 Library Management System

This project demonstrates the use of Java 8 features such as Lambda expressions, Method References, Default Methods in interfaces, and implements a comprehensive book library management system.

## 🌟 Project Overview

The Library Management System is a robust application that allows users to:

- 📖 Add and manage books and e-books
- 🔍 Search for books by author, title, or other criteria
- 📋 Sort books by title and publication year
- 👤 Register and manage patrons
- 🔄 Handle book lending and returns
- ⚡ Use advanced Java 8 features for data manipulation

## ☕ Java 8 Features Demonstrated

### 1. Lambda Expressions

Lambda expressions are used in various places:

- Filtering books by author: 
  ```java
  books.stream().filter(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()))
  ```

- Sorting books by title: 
  ```java
  books.stream().sorted((b1, b2) -> b1.getTitle().compareTo(b2.getTitle()))
  ```

- Sorting books by publication year

### 2. Method References

Method references are used to make the code more concise:

- Printing books: 
  ```java
  getBooks().forEach(System.out::println);
  ```

### 3. Default Methods

Default methods are used in interfaces to provide a default implementation:

- The `printAllBooks()` method in the `BookManager` interface is a default method:
  ```java
  default void printAllBooks() {
      getBooks().forEach(System.out::println);
  }
  ```

## 📁 File Structure and Purpose

1. **Book.java** - 📕 Base class representing a book with attributes like title, author, ISBN, copies, and year.
2. **EBook.java** - 💻 Subclass of Book with additional attributes for file size and format.
3. **BookManager.java** - 🧩 Interface defining operations that can be performed on a collection of books.
4. **Library.java** - 🏛️ Implementation of the BookManager interface.
5. **LibraryManagementSystem.java** - 🎮 Main system implementation with authentication, book and patron management.
6. **Patron.java** - 👥 Represents a library patron with lending capabilities.
7. **SimulationMain.java** - 🔄 Provides a multi-threaded simulation of library usage.
8. **books.txt** - 📝 Text file containing book data for persistence.
9. **patrons.txt** - 📝 Text file containing patron data for persistence.
10. **librarians.txt** - 🔐 Text file containing librarian authentication information.

## 🚀 How to Compile and Run

### Compilation
```bash
javac *.java
```

### Running Options

#### 1. Using the LibraryManagementSystem (Full System)
```bash
java LibraryManagementSystem
```
This launches the complete library system with all features including authentication, book management, and patron operations.

#### 2. Using the Simulation
```bash
java SimulationMain
```
The simulation creates multiple patron threads that concurrently borrow and return books, demonstrating thread-safe operations in a multi-user environment.

## 🔧 Features and Functionality

### Book Management
- Add, update, and remove books
- Search books by various criteria
- Sort and filter book collections

### Patron Management
- Register new patrons
- Track borrowed books
- Manage book returns and lending history

### Librarian Authentication
- Secure login system
- Administrative capabilities
- System configuration

### Data Persistence
- Book data stored in books.txt
- Patron data stored in patrons.txt
- Librarian credentials in librarians.txt

## 🧪 Running the Multi-Thread Simulation

The SimulationMain provides a demonstration of the system under concurrent usage:

1. It creates multiple patron threads
2. Each thread attempts to borrow and return books
3. Thread synchronization ensures data integrity
4. The simulation demonstrates the system's capability to handle concurrent operations

## 📌 Additional Features

- 🔒 User authentication system for librarians
- 💾 File persistence for books and patrons
- 📱 Support for e-books with additional attributes
- 🔄 Advanced sorting and filtering using Java 8 streams
- 📊 Book borrowing and return tracking
- 🧵 Multi-threaded library simulation for real-world usage scenarios 