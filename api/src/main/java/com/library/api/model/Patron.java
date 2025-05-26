package com.library.api.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Patron {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String patronId; // from file, e.g. "1"
    private String name;
    private String contact;
}
