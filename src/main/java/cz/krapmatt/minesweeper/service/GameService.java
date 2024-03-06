package cz.krapmatt.minesweeper.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cz.krapmatt.minesweeper.entity.Square;
import cz.krapmatt.minesweeper.entity.Board;
import cz.krapmatt.minesweeper.entity.Game;
import cz.krapmatt.minesweeper.repository.GameRepository;
import jakarta.transaction.Transactional;

//pracování s daty
//Vytvoření entit, uložení, jak probíhají tahy - v MineSweeper starání se o zobrazení v konzoli a uživateli
@Service
public class GameService {
    @Autowired
    private GameRepository gameRepository;
    

    
    @Transactional
    public Game createGame(int rows, int columns, int numOfMines) {
        Game game = new Game();
        game.setRows(rows);
        game.setColumns(columns);
        game.setNumOfMines(numOfMines);
        List<Square> squares = createSquares(rows, columns, game);
        game.setSquares(squares);
        gameRepository.saveGame(game);
        return game;      
    }
    
    
    private int markedCount;
   
    
    private List<Square> createSquares(int rows, int columns, Game game) {
        List<Square> squares = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Square square = new Square();
                square.setGame(game);
                // Set other properties of the square as needed
                squares.add(square);
            }
        }
        return squares;
    }

    @Transactional
    public void createBoard(Game game) {
        List<Square> squares = game.getSquares();
        squares = placeMines(game);
        squares = placeNumbers(game);
        game.setSquares(squares);
    }
    
    


    private List<Square> placeMines(Game game) {
        int minesPlaced = 0;
        int randRange = game.getRows() * game.getColumns();
        Random rand = new Random();
        List<Square> squares = game.getSquares();
        while (minesPlaced < game.getNumOfMines()) {
            int randomNumber = rand.nextInt(randRange);
            if (!squares.get(randomNumber).checkHasMine()) {
                squares.get(randomNumber).setHasMine();
                minesPlaced++;
            }
        }
        return squares;
    }

    private List<Square> placeNumbers(Game game) {
        int neighbourCount = Square.getNeighbourCount();
        List<Square> squares = game.getSquares();
        int columns = game.getColumns();

        for (int i = 0; i < game.getRows(); i++) {
            for (int j = 0; j < columns; j++) {
                int index = i * columns + j;
                Square currentSquare = squares.get(index);
                if (!currentSquare.checkHasMine()) {
                    int neighbouringMines = 0;
                    for (int k = 0; k < neighbourCount; k++) {
                        int newRow = i + di[k];
                        int newCol = j + dj[k];
                        if (isValidSquare(game, newRow, newCol) && squares.get(newRow * columns + newCol).checkHasMine()) {
                            neighbouringMines++;
                        }
                    }
                    currentSquare.setMineCount(neighbouringMines);
                }
            }
        }
        return squares;
    }

    private boolean isValidSquare(Game game, int currentRow, int currentColumn) {
        return currentRow >= 0 && currentRow < game.getRows() && currentColumn >= 0 && currentColumn < game.getColumns();
    }
    
    private boolean shouldOpen(Game game, int currentRow, int currentColumn) {
        List<Square> squares = game.getSquares();
        int columns = game.getColumns();
        return currentRow >= 0 &&
               currentRow < game.getRows() &&
               currentColumn >= 0 &&
               currentColumn < columns &&
               !squares.get(currentRow * columns + currentColumn).checkIsMarked() &&
               !squares.get(currentRow * columns + currentColumn).checkIsOpened();
    }
    
    private void openNeighboursRecursively(Game game, int currentRow, int currentColumn) {
        List<Square> squares = game.getSquares();
        int columns = game.getColumns();
        squares.get(currentRow * columns + currentColumn).setIsOpened();
        int neighbourCount = Square.getNeighbourCount();
        for(int k = 0; k < neighbourCount; k++) {
            int newRow = currentRow + di[k];
            int newCol = currentColumn + dj[k];
    
            if (shouldOpen(game, newRow, newCol)) {
                if (squares.get(newRow * columns + newCol).getMineCount() > 0) {
                    squares.get(newRow * columns + newCol).setIsOpened();
                }
                else {
                    openNeighboursRecursively(game, newRow, newCol);
                }
            }
        }
    }
    
    public boolean openSquare(Game game, int selectedRow, int selectedColumn) {
        List<Square> squares = game.getSquares();
        int columns = game.getColumns();
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
        openNeighboursRecursively(game, selectedRow, selectedColumn);
        return true;
    }
    
    public void toggleMarkedSquare(Game game, int selectedRow, int selectedColumn) {
        List<Square> squares = game.getSquares();
        Square curSquare = squares.get(selectedRow * game.getColumns() + selectedColumn);
        if (!curSquare.checkIsOpened()) {
            if (curSquare.checkIsMarked()) {
                markedCount -= 1;
                curSquare.toggleMarked();
            } else if (markedCount < game.getNumOfMines()) {
                markedCount += 1;
                curSquare.toggleMarked();
            }
        }
    }
    
    public boolean isAllOpenedOrMarked(Game game) {
        List<Square> squares = game.getSquares();
        int columns = game.getColumns();
        for(int i = 0; i < game.getRows(); i++) {
            for(int j = 0; j < columns; j++) {
                if (!squares.get(i * columns + j).checkIsOpened() || !squares.get(i * columns + j).checkIsMarked()) {
                    return false;
                }
            }
        }
        return true;
    }
    
        

    private static final int[] di = { -1, -1, -1, 0, 0, 1, 1, 1 };
    private static final int[] dj = { -1, 0, 1, -1, 1, -1, 0, 1 };

    // Add other methods for CRUD operations
}

