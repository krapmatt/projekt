package cz.krapmatt.minesweeper.repository;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import cz.krapmatt.minesweeper.entity.Square;
import cz.krapmatt.minesweeper.entity.Board;
import cz.krapmatt.minesweeper.entity.Game;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;


//Ručně SQL, přepsat to, jedna repository - ukládání všeho roundy, hry
@Repository
public class GameRepository {
    
    private EntityManager entityManager;

    @Autowired
    public GameRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Game saveGame(Game game) {
        if (game.getId() == null) {
            entityManager.persist(game);
        } else {
            game = entityManager.merge(game);
        }
        return game;    
    }

    public void saveBoard(Board board) {
        if (board.getId() == null) {
            entityManager.persist(board);
        } else {
            entityManager.merge(board);
        }
    }
    
    public Game findGameById(int id) {
        return entityManager.getReference(Game.class, id);        
    }
    
    
}