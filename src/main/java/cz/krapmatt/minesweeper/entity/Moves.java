package cz.krapmatt.minesweeper.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "moves")
public class Moves {
    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Integer id;

    private Integer action;
    private Integer selectedRow;
    private Integer selectedColumn;
    public Moves(int selectedRow, int selectedColumn, int action, Game game) {
        this.selectedRow = selectedRow;
        this.selectedColumn = selectedColumn;
        this.action = action;
        this.game = game;
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
     * @return Integer return the selectedRow
     */
    public Integer getSelectedRow() {
        return selectedRow;
    }

    /**
     * @param selectedRow the selectedRow to set
     */
    public void setSelectedRow(Integer selectedRow) {
        this.selectedRow = selectedRow;
    }

    /**
     * @return Integer return the selectedColumn
     */
    public Integer getSelectedColumn() {
        return selectedColumn;
    }

    /**
     * @param selectedColumn the selectedColumn to set
     */
    public void setSelectedColumn(Integer selectedColumn) {
        this.selectedColumn = selectedColumn;
    }


    /**
     * @return Integer return the action
     */
    public Integer getAction() {
        return action;
    }

    /**
     * @param action the action to set
     */
    public void setAction(Integer action) {
        this.action = action;
    }

}
