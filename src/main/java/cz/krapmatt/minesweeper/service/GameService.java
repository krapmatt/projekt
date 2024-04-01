package cz.krapmatt.minesweeper.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
    private static final char filledSquareCharacter = '\u25A0';

    private static final String markedFlagCharacter = "\u2690";

    private static final int[] di = { -1, -1, -1, 0, 0, 1, 1, 1 };

    
    private static final int[] dj = { -1, 0, 1, -1, 1, -1, 0, 1 };
    
    @Autowired
    private GameRepository gameRepository;

private int markedCount;

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
        gameRepository.saveGame(game);
        List<Mine> mines = createMines(game, numOfMines);
        game.setMines(mines);
        return game;      
    }

    //RESTAPI
    public GameState ClickOnSquare(int id, int selectedRow, int selectedColumn) {
        Game game = getGame(id);
        Moves move = new Moves(selectedRow, selectedColumn, 0, game);
        gameRepository.addAndSaveMoves(move);
        GameState gameState = SimulateAllRounds(game).getGameState();

        if (gameState == GameState.LOST_GAME || gameState == GameState.WON_GAME) {
            game.setGameState(gameState);
        }
        return gameState;
    }

    public GameState FlagBomb(int id, int selectedRow, int selectedColumn) {
        Game game = getGame(id);
        Moves move = new Moves(selectedRow, selectedColumn, 1, game);
        gameRepository.addAndSaveMoves(move);

        return SimulateAllRounds(game).getGameState();   
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

    public void display(Board board, Game game) {
        List<Square> squares = board.getSquares();
        int columns = game.getColumns();
        int rows = game.getRows();
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
                    System.out.printf("%3s", markedFlagCharacter);
                }
                else {
                    System.out.printf("%3s", filledSquareCharacter);
                }
            }
            System.out.println();
        }
    }
    
    public String generateGameString(int id) {
        Game game = getGame(id);
        Board board = SimulateAllRounds(game);
        int columns = game.getColumns();
        int rows = game.getRows();
        GameState gameState = board.getGameState();

        StringBuilder gameStringBuilder = new StringBuilder();
        gameStringBuilder.append("<div style=\"font-family: monospace;\">");

        gameStringBuilder.append("Game ID: ").append(game.getId()).append("<br>").append("Rows: ").append(rows).append("<br>")
        .append("Columns: ").append(columns).append("<br>").append("Number of mines: ").append(board.getNumOfMines()).append("<br>").append(gameState).append("<br>");

        
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
    }
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
    
    public Board createBoard(Game game) {
        Board board = new Board();
        board.setGameState(game.getGameState());
        List<Square> squares = fillSquares(game);
        board.setNumOfMines(game.getMines().size());
        board.setSquares(squares);
    
        return board;
    }

    @Cacheable(value = "boardCache", key = "#game.id")
    public Board getBoard(Game game) {
        Board board = createBoard(game);
        return board;
    }

    @CachePut(value = "boardCache", key = "#game.id")
    public Board updateBoardCache(Game game, Board board) {
        return board;
    }

    public Board SimulateAllRounds(Game game) {
        Board board = new Board();
        board.setGameState(game.getGameState());
        GameState gameState = board.getGameState();

        if (gameState == GameState.LOST_GAME) {
            board.setGameState(gameState);
            return board;
        }
        List<Square> squares = fillSquares(game);
        
        board.setNumOfMines(game.getMines().size());
        board.setSquares(squares);
        List<Moves> moves = game.getMoves();
        for (int i = 0; i < moves.size(); i++) {
            Moves move = moves.get(i);
            board = SimulateOneRound(board, game, move);
        }
        return board;
        
    }

    public Board SimulateOneRound(Board board, Game game, Moves newestMove) {
        GameState gameState = board.getGameState();
        
        System.out.println("\n");
        gameState = isAllOpenedOrMarked(board.getSquares()) ? GameState.WON_GAME : GameState.ONGOING;
        if (gameState == GameState.WON_GAME) {
            board.setGameState(GameState.WON_GAME);
            return board;
        }
        
        if (newestMove.getAction() == 0) {
            gameState = openSquare(board, game, newestMove.getSelectedRow() - 1, newestMove.getSelectedColumn() - 1) ? GameState.ONGOING : GameState.LOST_GAME;
        } else if (newestMove.getAction() == 1) {
            toggleMarkedSquare(board, game, newestMove.getSelectedRow() - 1, newestMove.getSelectedColumn() - 1);    
        }
        board.setGameState(gameState);
        display(board, game);
        return board;
    }

    private List<Mine> createMines(Game game, int numOfMines) {
        int minesPlaced = 0;
        Random rand = new Random();
        List<Mine> mines = new ArrayList<>();

        while (minesPlaced < numOfMines) {
            int x = rand.nextInt(game.getRows());
            int y = rand.nextInt(game.getColumns());

            if (!gameRepository.existsMine(x, y, game.getId())) {
                Mine mine = new Mine(x, y, game);
                mines.add(mine);
                gameRepository.saveMine(mine);
                minesPlaced++;
            }
        }
        return mines;
    }
    
    private List<Square> fillMines(Game game, List<Square> squares) {
        List<Mine> mines = game.getMines();
        for (int i  = 0; i < mines.size(); i++) {
            Mine mine = mines.get(i);
            int x = mine.getX();
            int y = mine.getY();
            squares.get(x * game.getColumns() + y).setHasMine();   
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
}

