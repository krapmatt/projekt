package cz.krapmatt.minesweeper.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cz.krapmatt.minesweeper.entity.Board;
import cz.krapmatt.minesweeper.entity.Game;
import cz.krapmatt.minesweeper.entity.GameState;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
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
            return entityManager.merge(game);
        }
        return game;    
    }

    @Transactional
    public void saveBoard(Board board) {
        if (board.getId() == null) {
            entityManager.persist(board);
        } else {
            entityManager.merge(board);
        }
    }

    @Transactional
    public Game findGameById(int id) {
        Game game = entityManager.find(Game.class, id);
        return game;
    }
    
    public Board findLatestBoardByGameId(int gameId) {
        TypedQuery<Board> query = entityManager.createQuery("SELECT b FROM Board b WHERE b.game.id = :gameId ORDER BY b.id DESC", Board.class);
        query.setParameter("gameId", gameId);
        query.setMaxResults(1);
        Board board = query.getSingleResult();
        
        return board;
    }

    public List<Game> findAllPlayableGames() {
        TypedQuery<Game> query = entityManager.createQuery("SELECT b.game FROM Board b WHERE b.gameState = :state " + "AND b.id IN (SELECT MAX(b2.id) FROM Board b2 GROUP BY b2.game)", Game.class);
        query.setParameter("state", GameState.ONGOING);
        return query.getResultList();
    }
}
