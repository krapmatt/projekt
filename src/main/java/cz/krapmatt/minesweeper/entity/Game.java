package cz.krapmatt.minesweeper.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "game")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="game_id")
    private Integer id;

    //Jen seznam provedených tahů - neukládat všechny board/čtverce
    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<Moves> moves;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<Mine> mines;

    @Enumerated(EnumType.STRING)
    @Column(name = "game_state")
    private GameState gameState;

    @Column(name="rows")
    private int rows;

    @Column(name="columns")
    private int columns;

    
    public Game(int rows, int columns) {
        this.gameState = GameState.ONGOING;
        this.rows = rows;
        this.columns = columns;
    }

    
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
     * @return List<Moves> return the moves
     */
    public List<Moves> getMoves() {
        return moves;
    }

    /**
     * @param moves the moves to set
     */
    public void setMoves(List<Moves> moves) {
        this.moves = moves;
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

    /**
     * @return int return the rows
     */
    public int getRows() {
        return rows;
    }

    /**
     * @param rows the rows to set
     */
    public void setRows(int rows) {
        this.rows = rows;
    }

    /**
     * @return int return the columns
     */
    public int getColumns() {
        return columns;
    }

    /**
     * @param columns the columns to set
     */
    public void setColumns(int columns) {
        this.columns = columns;
    }


    /**
     * @return List<Mine> return the mines
     */
    public List<Mine> getMines() {
        return mines;
    }

    /**
     * @param mines the mines to set
     */
    public void setMines(List<Mine> mines) {
        this.mines = mines;
    }

}
