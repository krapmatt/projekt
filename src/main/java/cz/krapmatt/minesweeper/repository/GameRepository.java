package cz.krapmatt.minesweeper.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cz.krapmatt.minesweeper.entity.Moves;
import cz.krapmatt.minesweeper.entity.Game;
import cz.krapmatt.minesweeper.entity.GameState;
import cz.krapmatt.minesweeper.entity.Mine;
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
    @Transactional
    public Game saveGame(Game game) {
        if (game.getId() == null) {
            entityManager.persist(game);
        } else {
            return entityManager.merge(game);
        }
        return game;    
    }

    @Transactional
    public void saveMine(Mine mine) {
        if (!existsMine(mine.getX(), mine.getY(), mine.getGame().getId())) {
            entityManager.persist(mine);
        } else {
            entityManager.merge(mine);
        }
    }

    public boolean existsMine(int x, int y, int gameId) {
        Long mineCount = entityManager.createQuery(
                "SELECT COUNT(m) FROM Mine m WHERE m.x = :x AND m.y = :y AND m.game.id = :gameId", Long.class)
                .setParameter("x", x)
                .setParameter("y", y)
                .setParameter("gameId", gameId)
                .getSingleResult();
        
        return mineCount > 0;
    }

    @Transactional
    public Game findGameById(int id) {
        Game game = entityManager.find(Game.class, id);
        return game;
    }
    
    public List<Moves> findAllMovesByGameId(int gameId) {
        TypedQuery<Moves> query = entityManager.createQuery("SELECT m FROM Moves m WHERE m.game.id", Moves.class);
        query.setParameter("gameId", gameId);
        List<Moves> moves = query.getResultList();
        
        return moves;
    }

    public List<Game> findAllPlayableGames() {
        TypedQuery<Game> query = entityManager.createQuery("SELECT b.game FROM Board b WHERE b.gameState = :state " + "AND b.id IN (SELECT MAX(b2.id) FROM Board b2 GROUP BY b2.game)", Game.class);
        query.setParameter("state", GameState.ONGOING);
        return query.getResultList();
    }
    
}
