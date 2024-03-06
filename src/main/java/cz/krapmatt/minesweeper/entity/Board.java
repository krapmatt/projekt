package cz.krapmatt.minesweeper.entity;
import java.util.*;

import cz.krapmatt.minesweeper.entity.Square;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "board")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    //nějaký způsob uložení board
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Square> squares;

    @Enumerated(EnumType.STRING)
    @Column(name = "game_state")
    private GameState gameState;


    /**
     * @return Integer return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return List<Squares> return the squares
     */
    public List<Square> getSquares() {
        return squares;
    }

    /**
     * @param squares the squares to set
     */
    public void setSquares(List<Square> squares) {
        this.squares = squares;
    }

    /**
     * @return GameState return the gameState
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * @param gameState the gameState to set
     */
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

}
