package com.example.demo;

import javax.persistence.*;
import java.util.List;

@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<Round> rounds;

    // Add other fields as needed

    // Getters and setters
}
