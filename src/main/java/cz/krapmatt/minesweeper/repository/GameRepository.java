package cz.krapmatt.minesweeper.repository;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cz.krapmatt.minesweeper.entity.Game;


//Ručně SQL, přepsat to, jedna repository - ukládání všeho roundy, hry
@Repository
public class GameRepository {
    
    private final SessionFactory sessionFactory;

    @Autowired
    public GameRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /*public Game saveGame(Game game) {
        Session session = sessionFactory.getCurrentSession();
        if (game.getId() == null) {
            session.persist(game);
        } else {
            game = session.merge(game);
        }
        return game;    
    }*/

    public void saveGame(Game game) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.saveOrUpdate(game);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }
    }

}