# Library Management System

## Overview

The Library Management System is a Java-based console application designed to help librarians and library staff manage the library's collection of books. The system provides a simple text-based menu for performing essential operations such as adding, editing, removing, and displaying books; registering, editing, removing, and searching for patrons; and managing book borrowing and returns. Additionally, the system includes a librarian authentication module where the user must sign up or log in with an email ending in **@librarian.com**. Passwords are securely hashed (using PBKDF2 with HMAC-SHA256) and stored in a file.

Data persistence is implemented via text files:

- **books.txt** – stores book records in the format: `title|author|isbn|copies`
- **patrons.txt** – stores patron records in the format: `id|name|contact`
- **librarians.txt** – stores librarian authentication records (email, salt, hashed password)

## Key features

- **Book management:**
  - *Add Books:* Enter book details (title, author, ISBN, number of copies) to add to the inventory.
  - *Remove Books:* Remove books from the inventory (if no longer available or needed).
  - *Display Books:* View a list of all available books.
  - *Edit Books:* Search for a book by its ISBN, display current values, and update the book’s information.
  - *Search Books:* Optimized search by title or author using hash indices.

- **Patron Management:**
  - *Register Patrons:* Register new patrons by entering their name, ID, and contact details.
  - *View Patrons:* Display all registered patrons.
  - *Edit Patrons:* Update a patron's contact information by searching with their ID.
  - *Remove Patrons:* Delete a patron from the system.
  - *Search Patrons:* Search for a patron by ID and display their details along with borrowed books.

- **Borrowing and returning books:**
  - *Borrow Books:* Allows patrons to borrow books; the system updates available copies and tracks which books are on loan.
  - *Return Books:* Patrons can return borrowed books, which updates the inventory accordingly.
  - *Books per Patron:* View the number of books a patron currently has borrowed and their details.

- **Librarian Authentication:**
  - *Sign Up/Log In:* Before accessing the system, the user must authenticate as a librarian using an email that ends with **@librarian.com**.
  - *Secure Password Storage:* Passwords are hashed (using PBKDF2 with HMAC-SHA256) along with a salt, and the credentials are stored in **librarians.txt**.

- **User-Friendly Interface:**
  - The application features a simple text-based menu and provides clear instructions at every step.
  - Proper exception handling ensures that the application handles errors gracefully.

## Objectives

- **Object-Oriented programming (OOP):**  
  Demonstrates the creation and use of classes and objects, encapsulation of data, and modeling of real-world entities (books and patrons).

- **Data structures:**  
  Uses collections such as ArrayList and HashMap to manage lists of books, patrons, and loan records efficiently.

- **Input/Output handling:**  
  Manages user input and displays output in a console application.  
  Incorporates file I/O to persist data.

- **Exception handling:**  
  Correct use of try-catch blocks to handle potential runtime errors and input mismatches.

- **Basic algorithm design:**  
  Implements algorithms for searching, adding, editing, and removing items from collections.

## How to run

1. **Clone the repository:**

   ```bash
   git clone https://github.com/marcheluki/JavaDevBookLibrary.git
   ```

2. **Navigate to the project directory:**

   ```bash
   cd JavaDevBookLibrary
   ```

3. **Compile the project:**

   Ensure that your Java source files are correctly compiled. For example, if your source files are in a folder named `library`, you can compile with:

   ```bash
   javac -d bin library/*.java
   ```

4. **Run the application:**

   ```bash
   java -cp bin library.LibraryManagementSystem
   ```

   The application will first prompt for librarian authentication (sign up or log in). Once authenticated, the main menu will appear.

## Repository and submission

- The project is maintained in a GIT repository with proper commit history.
- All team members have contributed to the development of the project.
- A README file (this file) is included with a detailed project description, instructions for running the application, and team information.

## Team members

- Marcela Beatriz De La Rosa Barrios A01637239  
- Sebastián Denhi Vega Saint Martín A01637397  
- Ángela Estefanía Aguilar Medina A01637703  
- Axel Daniel Padilla Reyes A01642700  
- Diana Nicole Arana Sánchez A01642924
