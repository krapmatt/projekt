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

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<Board> boards;


    @Column(name="rows")
    private int rows;

    @Column(name="columns")
    private int columns;

    @Column(name="numOfMines")
    private int numOfMines;


    // Getters and setters
    
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
     * @return int return the numOfMines
     */
    public int getNumOfMines() {
        return numOfMines;
    }

    /**
     * @param numOfMines the numOfMines to set
     */
    public void setNumOfMines(int numOfMines) {
        this.numOfMines = numOfMines;
    }


    /**
     * @return List<Board> return the boards
     */
    public List<Board> getBoards() {
        return boards;
    }

    /**
     * @param boards the boards to set
     */
    public void setBoards(List<Board> boards) {
        this.boards = boards;
    }

    @Override
    public String toString() {
        return "Game [id=" + id + ", boards=" + boards + ", rows=" + rows + ", columns=" + columns + ", numOfMines="
                + numOfMines + "]";
    }



}
