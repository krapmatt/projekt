package cz.krapmatt.minesweeper.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


@Entity
public class Mine {
    //Udělat id dohromady z pozice a hry
    @Id
    private Integer x;
    
    @Id
    private Integer y;
    
    @Id
    @ManyToOne
    @JoinColumn(name="game_id")
    private Game game;


    public Mine(int x, int y, int gameId) {
        this.x = x;
        this.y = y;
        this.game.setId(gameId);
    }

    /**
     * @return Integer return the x
     */
    public Integer getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(Integer x) {
        this.x = x;
    }

    /**
     * @return Integer return the y
     */
    public Integer getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(Integer y) {
        this.y = y;
    }

    /**
     * @return Game return the game
     */
    public Game getGame() {
        return game;
    }

    /**
     * @param game the game to set
     */
    public void setGame(Game game) {
        this.game = game;
    }

}
