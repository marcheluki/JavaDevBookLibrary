package com.library.api.repository;

import com.library.api.model.Patron;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatronRepository extends JpaRepository<Patron, Long> {
    Patron findByPatronId(String patronId);
}
