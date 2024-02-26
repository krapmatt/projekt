package com.example.demo;
import org.springframework.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    // Add custom query methods if needed
}