package cz.krapmatt.minesweeper.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

//držet u každého čtverce jedno kolo
@Table(name = "squares")
@Entity
public class Square {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "square_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name="board_id")
    private Board board;


    private boolean hasMine;
    private boolean isOpened;
    private boolean isMarked;
    private int mineCount;
    
    public Square() {
        hasMine = false;
        isOpened = false;
        isMarked = false;
        mineCount = 0;
    }
    
    private static final int neighbourCount = 8;
    /**
     * @return int return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return boolean return the hasMine
     */
    public boolean isHasMine() {
        return hasMine;
    }

    /**
     * @param hasMine the hasMine to set
     */
    public void setHasMine(boolean hasMine) {
        this.hasMine = hasMine;
    }

    /**
     * @return boolean return the isOpened
     */
    public boolean isIsOpened() {
        return isOpened;
    }

    /**
     * @param isOpened the isOpened to set
     */
    public void setIsOpened(boolean isOpened) {
        this.isOpened = isOpened;
    }

    /**
     * @return boolean return the isMarked
     */
    public boolean isIsMarked() {
        return isMarked;
    }

    /**
     * @param isMarked the isMarked to set
     */
    public void setIsMarked(boolean isMarked) {
        this.isMarked = isMarked;
    }

    /**
     * @return Board return the board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * @param board the board to set
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    public boolean checkIsOpened() {
        return isOpened;
    }

    public void setIsOpened() {
        isOpened = true;
        isMarked = false;
    }

    public void toggleMarked() {
        isMarked = !isMarked;
    }

    public boolean checkIsMarked() {
        return isMarked;
    }

    public void setHasMine() {
        hasMine = true;
    }

    public boolean checkHasMine() {
        return hasMine;
    }

    public void setMineCount(int mineCount) {
        this.mineCount = mineCount;
    }

    public int getMineCount() {
        return mineCount;
    }

    public static int getNeighbourCount() {
        return neighbourCount;
    }

    @Override
    public Square clone() {
        Square square = new Square();
        square.hasMine = this.hasMine;
        square.isMarked = this.isMarked;
        square.isOpened = this.isOpened;
        square.mineCount = this.mineCount;

        return square;
    }

    

}
