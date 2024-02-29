package cz.krapmatt.minesweeper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import cz.krapmatt.minesweeper.entity.Round;

@Repository
public interface RoundRepository extends JpaRepository <Round, Integer>  {
    
}
