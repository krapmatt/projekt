package cz.krapmatt.minesweeper.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cz.krapmatt.minesweeper.entity.Game;
import cz.krapmatt.minesweeper.entity.GameState;
import cz.krapmatt.minesweeper.entity.Mine;
import cz.krapmatt.minesweeper.entity.Moves;
import cz.krapmatt.minesweeper.repository.GameRepository;
import cz.krapmatt.minesweeper.simulation.Board;
import cz.krapmatt.minesweeper.simulation.Square;
import jakarta.transaction.Transactional;
//vytvoření factory, která vytvoří hru pomocí id
//Simulaci hry, strčím entitu hry z databáze -> funkce na hraní

@Service
public class GameService {
    @Autowired
    private GameRepository gameRepository;
    
    @Transactional
    public void saveGame(Game game) {
        gameRepository.saveGame(game);
    }

    @Transactional
    public Game getGame(int id) {
        return gameRepository.findGameById(id);
    }

    
    public Game createGame(int rows, int columns, int numOfMines)  {
        Game game = new Game(rows, columns);
        createMines(game, numOfMines);
        gameRepository.saveGame(game);
        return game;      
    }

    private void createMines(Game game, int numOfMines) {
        int minesPlaced = 0;
        Random rand = new Random();
        
        while (minesPlaced < numOfMines) {
            int x = rand.nextInt(game.getRows());
            int y = rand.nextInt(game.getColumns());

            if (!gameRepository.existsMine(x, y, game.getId())) {
                Mine mine = new Mine(x, y, game.getId());
                gameRepository.saveMine(mine);
                minesPlaced++;
            }
        }
    }

//RESTAPI
    public void ClickOnSquare(int id, int selectedRow, int selectedColumn, int action) {
        Game game = getGame(id);
        List<Moves> moves = game.getMoves();
        Moves move = new Moves(selectedRow, selectedColumn, action, game);
        moves.add(move);
        
        gameRepository.saveGame(game);
    }

    public void FlagBomb(int id, int selectedRow, int selectedColumn, int action) {
        Game game = getGame(id);
        List<Moves> moves = game.getMoves();
        Moves move = new Moves(selectedRow, selectedColumn, action, game);
        moves.add(move);
        if(getGamestate(id) == GameState.ONGOING) {
            
        }
    }

    public GameState getGamestate(int id) {
        return GameState.ONGOING;    
    }

    public List<Integer> getPlayebleGames() {
        List<Game> games = gameRepository.findAllPlayableGames();
        List<Integer> ids = new ArrayList<>();
        for(Game game : games) {
            ids.add(game.getId());
        }
        return ids;
    }

    /*public String generateGameString(int id) {
        Game game = getGame(id);
        Moves board = findNewestBoard(id);
        int columns = game.getColumns();
        int rows = game.getRows();
        GameState gameState = getGamestate(id);

        StringBuilder gameStringBuilder = new StringBuilder();
        gameStringBuilder.append("<div style=\"font-family: monospace;\">");

        gameStringBuilder.append("Game ID: ").append(game.getId()).append("<br>").append("Rows: ").append(rows).append("<br>")
        .append("Columns: ").append(columns).append("<br>").append("Number of mines: ").append(game.getNumOfMines()).append("<br>").append(gameState).append("<br>");

        
        List<Square> squares = board.getSquares();
        gameStringBuilder.append("Board: ");
        
        gameStringBuilder.append("<br>\tX");

       
        for(int j = 0; j < columns; j++) {
            gameStringBuilder.append(String.format("%3d", j+1));
        }
        gameStringBuilder.append("<br>");
    
        for(int i = 0; i < rows; i++) {
            gameStringBuilder.append(String.format("%3d", i+1));
            for(int j = 0; j < columns; j++) {
                if (squares.get(i * columns + j).checkIsOpened()) {
                    gameStringBuilder.append(String.format("%3d", squares.get(i * columns + j).getMineCount()));
                }
                else if (squares.get(i * columns + j).checkIsMarked()) {
                    gameStringBuilder.append(String.format("%3s" , "\u2690"));
                }
                else {
                    gameStringBuilder.append(String.format("%3s", '\u25A0'));
                }
            }
            gameStringBuilder.append("<br>");
        }
        gameStringBuilder.append("</div>");

        return gameStringBuilder.toString();
    }*/
    //Logic
    public boolean openSquare(Board board, Game game, int selectedRow, int selectedColumn) {
        List<Square> squares = board.getSquares();
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
        openNeighboursRecursively(squares, game, selectedRow, selectedColumn);
        return true;
    }
    
    private int markedCount;
    public void toggleMarkedSquare(Board board, Game game, int selectedRow, int selectedColumn) {
        List<Square> squares = board.getSquares();
        Square curSquare = squares.get(selectedRow * game.getColumns() + selectedColumn);
        if (!curSquare.checkIsOpened()) {
            if (curSquare.checkIsMarked()) {
                markedCount -= 1;
                curSquare.toggleMarked();
            } else if (markedCount < board.getNumOfMines()) {
                markedCount += 1;
                curSquare.toggleMarked();
            }
        }
    }
    
    public boolean isAllOpenedOrMarked(List<Square> squares) {    
        for (Square square : squares) {
            if (square.checkHasMine() && !square.checkIsMarked()) {
                return false;
            }
            if (!square.checkIsOpened() && !square.checkHasMine()) {
                return false;
            }
        }
        return true;
    }

    

    public List<Square> fillSquares(Game game) {
        List<Square> squares = createSquares(game.getRows(), game.getColumns());
        squares = fillMines(game, squares);
        squares = placeNumbers(squares, game);
        return squares;
    }
    
    private List<Square> fillMines(Game game, List<Square> squares) {
        List<Mine> mines = game.getMines();
        for (int i  = 0; i < mines.size(); i++) {
            Mine mine = mines.get(i);
            int x = mine.getX();
            int y = mine.getY();
            squares.get(x * game.getColumns() + y);    
        }
        return squares;
    }
    
    private List<Square> createSquares(int rows, int columns) {
        List<Square> squares = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Square square = new Square();
                squares.add(square);
            }
        }
        return squares;
    }
    //Pro zobrazení pro uživatele...
    private List<Square> placeNumbers(List<Square> squares, Game game) {
        int neighbourCount = Square.getNeighbourCount();
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
    
    private boolean shouldOpen(List<Square> squares, Game game, int currentRow, int currentColumn) {
        int columns = game.getColumns();
        return currentRow >= 0 &&
               currentRow < game.getRows() &&
               currentColumn >= 0 &&
               currentColumn < columns &&
               !squares.get(currentRow * columns + currentColumn).checkIsMarked() &&
               !squares.get(currentRow * columns + currentColumn).checkIsOpened();
    }
    
    private void openNeighboursRecursively(List<Square> squares, Game game, int currentRow, int currentColumn) {
        int columns = game.getColumns();
        squares.get(currentRow * columns + currentColumn).setIsOpened();
        int neighbourCount = Square.getNeighbourCount();
        for(int k = 0; k < neighbourCount; k++) {
            int newRow = currentRow + di[k];
            int newCol = currentColumn + dj[k];
    
            if (shouldOpen(squares, game, newRow, newCol)) {
                if (squares.get(newRow * columns + newCol).getMineCount() > 0) {
                    squares.get(newRow * columns + newCol).setIsOpened();
                }
                else {
                    openNeighboursRecursively(squares, game, newRow, newCol);
                }
            }
        }
    }

    private static final int[] di = { -1, -1, -1, 0, 0, 1, 1, 1 };
    private static final int[] dj = { -1, 0, 1, -1, 1, -1, 0, 1 };

}

