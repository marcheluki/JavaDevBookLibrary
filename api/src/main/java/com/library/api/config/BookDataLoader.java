package com.library.api.config;

import com.library.api.model.Book;
import com.library.api.repository.BookRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.*;
import java.util.*;

@Component
public class BookDataLoader implements CommandLineRunner {

    private final BookRepository bookRepository;

    @Value("${library.books.filepath:../books.txt}")
    private String booksFilePath;

    public BookDataLoader(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Only import if DB is empty
        if (bookRepository.count() == 0) {
            Path path = Paths.get(booksFilePath);
            if (Files.exists(path)) {
                try (BufferedReader reader = Files.newBufferedReader(path)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split("\\|");
                        if (parts.length >= 5) {
                            Book book = new Book();
                            book.setTitle(parts[0].trim());
                            book.setAuthor(parts[1].trim());
                            book.setIsbn(parts[2].trim());
                            book.setCopies(Integer.parseInt(parts[3].trim()));
                            book.setYear(Integer.parseInt(parts[4].trim()));
                            bookRepository.save(book);
                        }
                    }
                }
                System.out.println("Books imported from " + booksFilePath);
            } else {
                System.out.println("books.txt not found at: " + path.toAbsolutePath());
            }
        }
    }
}
