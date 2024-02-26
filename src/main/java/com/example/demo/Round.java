package com.example.demo;

import javax.persistence.*;

@Entity
public class Round {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    // Add other fields as needed

    // Getters and setters
}