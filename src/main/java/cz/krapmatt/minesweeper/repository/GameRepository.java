package cz.krapmatt.minesweeper.repository;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import cz.krapmatt.minesweeper.entity.Game;

@Repository
public interface GameRepository extends JpaRepository <Game, Integer> {
    
}