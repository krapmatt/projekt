package cz.krapmatt.minesweeper.entity;
import java.util.*;

import cz.krapmatt.minesweeper.Square;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;


@Entity
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "round_id")
    private Round round;


    private final int rows;
    private final int columns;
    private final int numOfMines;
    private int markedCount;
    private static final char filledSquareCharacter = '\u25A0';
    private static final char markedFlagCharacter = '\u2691';

    private ArrayList<Square> squares;

    public Board(int rows, int columns, int numOfMines) {
        this.rows = rows;
        this.columns = columns;
        this.numOfMines = numOfMines;
        this.markedCount = 0;

        squares = new ArrayList<>();
        for (int i = 0; i < rows * columns; i++) {
            Square newSquare = new Square();
            squares.add(newSquare);
        }
    }

    public void initBoard() {
        placeMines();
        placeNumbers();
    }

    private void placeMines() {
        int minesPlaced = 0;
        int randRange = rows * columns;
        Random rand = new Random();
        while (minesPlaced < numOfMines) {
            int randomNumber = rand.nextInt(randRange);
            if (!squares.get(randomNumber).checkHasMine()) {
                squares.get(randomNumber).setHasMine();
                minesPlaced++;
            }
        }
    }

    private void placeNumbers() {
        int neighbourCount = Square.getNeighbourCount();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                int index = i * columns + j;
                Square currentSquare = squares.get(index);
                if (!currentSquare.checkHasMine()) {
                    int neighbouringMines = 0;
                    for (int k = 0; k < neighbourCount; k++) {
                        int newRow = i + di[k];
                        int newCol = j + dj[k];
                        if (isValidSquare(newRow, newCol) && squares.get(newRow * columns + newCol).checkHasMine()) {
                            neighbouringMines++;
                        }
                    }
                    currentSquare.setMineCount(neighbouringMines);
                }
            }
        }
    }

    private boolean isValidSquare(int currentRow, int currentColumn) {
        return currentRow >= 0 && currentRow < rows && currentColumn >= 0 && currentColumn < columns;
    }
    
    private boolean shouldOpen(int currentRow, int currentColumn) {
        return currentRow >= 0 &&
               currentRow < rows &&
               currentColumn >= 0 &&
               currentColumn < columns &&
               !squares.get(currentRow * columns + currentColumn).checkIsMarked() &&
               !squares.get(currentRow * columns + currentColumn).checkIsOpened();
    }
    
    private void openNeighboursRecursively(int currentRow, int currentColumn) {
        squares.get(currentRow * columns + currentColumn).setIsOpened();
        int neighbourCount = Square.getNeighbourCount();
        for(int k = 0; k < neighbourCount; k++) {
            int newRow = currentRow + di[k];
            int newCol = currentColumn + dj[k];
    
            if (shouldOpen(newRow, newCol)) {
                if (squares.get(newRow * columns + newCol).getMineCount() > 0) {
                    squares.get(newRow * columns + newCol).setIsOpened();
                }
                else {
                    openNeighboursRecursively(newRow, newCol);
                }
            }
        }
    }
    
    public boolean openSquare(int selectedRow, int selectedColumn) {
        if (squares.get(selectedRow * columns + selectedColumn).checkIsMarked()) {
            return true;
        }
        if (squares.get(selectedRow * columns + selectedColumn).checkHasMine()) {
            return false;
        }
        if (squares.get(selectedRow * columns + selectedColumn).getMineCount() > 0) {
            squares.get(selectedRow * columns + selectedColumn).setIsOpened();
            return true;
        }
        openNeighboursRecursively(selectedRow, selectedColumn);
        return true;
    }
    
    public void toggleMarkedSquare(int selectedRow, int selectedColumn) {
        Square curSquare = squares.get(selectedRow * columns + selectedColumn);
        if (curSquare.checkIsOpened() == false) {
            if (curSquare.checkIsMarked()) {
                markedCount -= 1;
                curSquare.toggleMarked();
            }
            else if (markedCount < numOfMines) {
                markedCount += 1;
                curSquare.toggleMarked();
            }
        }
    }
    
    public boolean isAllOpenedOrMarked() {
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                if (!squares.get(i * columns + j).checkIsOpened() || !squares.get(i * columns + j).checkIsMarked()) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public void display() {
        System.out.print("   ");
        for(int j = 0; j < columns; j++) {
            System.out.printf("%3d", j + 1);
        }
        System.out.println();
    
        for(int i = 0; i < rows; i++) {
            System.out.printf("%3d", i + 1);
            for(int j = 0; j < columns; j++) {
                if (squares.get(i * columns + j).checkIsOpened()) {
                    System.out.printf("%3d", squares.get(i * columns + j).getMineCount());
                }
                else if (squares.get(i * columns + j).checkIsMarked()) {
                    System.out.printf("%3c", markedFlagCharacter);
                }
                else {
                    System.out.printf("%3c", filledSquareCharacter);
                }
            }
            System.out.println();
        }
    }    

    private static final int[] di = { -1, -1, -1, 0, 0, 1, 1, 1 };
    private static final int[] dj = { -1, 0, 1, -1, 1, -1, 0, 1 };

    

    /**
     * @return Long return the id
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
     * @return Round return the round
     */
    public Round getRound() {
        return round;
    }

    /**
     * @param round the round to set
     */
    public void setRound(Round round) {
        this.round = round;
    }

    /**
     * @return int return the markedCount
     */
    public int getMarkedCount() {
        return markedCount;
    }

    /**
     * @param markedCount the markedCount to set
     */
    public void setMarkedCount(int markedCount) {
        this.markedCount = markedCount;
    }

    /**
     * @return ArrayList<Square> return the squares
     */
    public ArrayList<Square> getSquares() {
        return squares;
    }

    /**
     * @param squares the squares to set
     */
    public void setSquares(ArrayList<Square> squares) {
        this.squares = squares;
    }

}
