package com.library.api.config;

import com.library.api.model.Patron;
import com.library.api.repository.PatronRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.*;

@Component
public class PatronDataLoader implements CommandLineRunner {

    private final PatronRepository patronRepository;

    @Value("${library.patrons.filepath:../patrons.txt}")
    private String patronsFilePath;

    public PatronDataLoader(PatronRepository patronRepository) {
        this.patronRepository = patronRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (patronRepository.count() == 0) {
            Path path = Paths.get(patronsFilePath);
            if (Files.exists(path)) {
                try (BufferedReader reader = Files.newBufferedReader(path)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split("\\|");
                        if (parts.length >= 3) {
                            Patron patron = new Patron();
                            patron.setPatronId(parts[0].trim());
                            patron.setName(parts[1].trim());
                            patron.setContact(parts[2].trim());
                            patronRepository.save(patron);
                        }
                    }
                }
                System.out.println("Patrons imported from " + patronsFilePath);
            } else {
                System.out.println("patrons.txt not found at: " + path.toAbsolutePath());
            }
        }
    }
}