package cz.krapmatt.minesweeper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cz.krapmatt.minesweeper.entity.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {
    
}
