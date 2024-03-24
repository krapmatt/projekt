package cz.krapmatt.minesweeper.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cz.krapmatt.minesweeper.entity.Board;
import cz.krapmatt.minesweeper.entity.Game;
import cz.krapmatt.minesweeper.entity.GameState;
import cz.krapmatt.minesweeper.entity.Square;
import cz.krapmatt.minesweeper.repository.GameRepository;
import jakarta.transaction.Transactional;

//pracování s daty
//Vytvoření entit, uložení, jak probíhají tahy - v MineSweeper starání se o zobrazení v konzoli a uživateli
@Service
public class GameService {
    @Autowired
    private GameRepository gameRepository;
    
    @Transactional
    public void saveGame(Game game) {
        gameRepository.saveGame(game);
    }

    @Transactional
    public void saveBoard(Board board) {
        gameRepository.saveBoard(board);
    }
    
    @Transactional
    public Game getGame(int id) {
        return gameRepository.findGameById(id);
    }
    
    @Transactional
    public Board findNewestBoard(int id) {
        return gameRepository.findLatestBoardByGameId(id);
    }

    @Transactional
    public Game createGame(int rows, int columns, int numOfMines)  {
        Game game = new Game();
        game.setRows(rows);
        game.setColumns(columns);
        game.setNumOfMines(numOfMines);

        Board board = new Board();
        board.setGame(game);

        List<Square> squares = createSquares(rows, columns, board);
        board.setSquares(squares);
        
        fillSquares(board, game);

        List<Board> boards = Arrays.asList(board);
        game.setBoards(boards);

        gameRepository.saveGame(game);
        
        return game;      
    }

//RESTAPI
    public void ClickOnSquare(int id, int selectedRow, int selectedColumn) {
        Game game = getGame(id);
        Board board = findNewestBoard(id).clone();
        
        GameState gameState = openSquare(board, game, selectedRow, selectedColumn) ? GameState.ONGOING : GameState.LOST_GAME;
        
        if (gameState == GameState.ONGOING) {
            gameState = isAllOpenedOrMarked(game) ? GameState.WON_GAME : GameState.ONGOING;
        }
        board.setGameState(gameState);

        saveBoard(board);
    }

    public void FlagBomb(int id, int selectedRow, int selectedColumn) {
        Board board = findNewestBoard(id).clone();
        GameState gameState = board.getGameState();
        Game game = getGame(id);
        toggleMarkedSquare(board, game, selectedRow, selectedColumn);
        if (gameState == GameState.ONGOING) {
            gameState = isAllOpenedOrMarked(game) ? GameState.WON_GAME : GameState.ONGOING;
        }
        board.setGameState(gameState);
        saveBoard(board);
    }

    public GameState getGamestate(int id) {
        return findNewestBoard(id).getGameState();
    }

    public List<Integer> getPlayebleGames() {
        List<Game> games = gameRepository.findAllPlayableGames();
        List<Integer> ids = new ArrayList<>();
        for(Game game : games) {
            ids.add(game.getId());
        }
        return ids;
    }

    public String generateGameString(int id) {
        Game game = getGame(id);
        Board board = findNewestBoard(id);
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
        openNeighboursRecursively(board, game, selectedRow, selectedColumn);
        return true;
    }

    public void toggleMarkedSquare(Board board, Game game, int selectedRow, int selectedColumn) {
        List<Square> squares = board.getSquares();
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
        List<Square> squares = findNewestBoard(game.getId()).getSquares();
    
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

    private int markedCount;
    public List<Square> createSquares(int rows, int columns, Board board) {
        List<Square> squares = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Square square = new Square();
                square.setBoard(board);
                squares.add(square);
            }
        }
        return squares;
    }

    private void fillSquares(Board board, Game game) {
        List<Square> squares = board.getSquares();
        squares = placeMines(board, game);
        squares = placeNumbers(board, game);
        board.setSquares(squares);
    }
    
    public List<Square> placeMines(Board board, Game game) {
        int minesPlaced = 0;
        int randRange = game.getRows() * game.getColumns();
        Random rand = new Random();
        List<Square> squares = board.getSquares();
        while (minesPlaced < game.getNumOfMines()) {
            int randomNumber = rand.nextInt(randRange);
            if (!squares.get(randomNumber).checkHasMine()) {
                squares.get(randomNumber).setHasMine();
                minesPlaced++;
            }
        }
        return squares;
    }

    private List<Square> placeNumbers(Board board, Game game) {
        int neighbourCount = Square.getNeighbourCount();
        List<Square> squares = board.getSquares();
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
    
    private boolean shouldOpen(Board board, Game game, int currentRow, int currentColumn) {
        List<Square> squares = board.getSquares();
        int columns = game.getColumns();
        return currentRow >= 0 &&
               currentRow < game.getRows() &&
               currentColumn >= 0 &&
               currentColumn < columns &&
               !squares.get(currentRow * columns + currentColumn).checkIsMarked() &&
               !squares.get(currentRow * columns + currentColumn).checkIsOpened();
    }
    
    private void openNeighboursRecursively(Board board, Game game, int currentRow, int currentColumn) {
        List<Square> squares = board.getSquares();
        int columns = game.getColumns();
        squares.get(currentRow * columns + currentColumn).setIsOpened();
        int neighbourCount = Square.getNeighbourCount();
        for(int k = 0; k < neighbourCount; k++) {
            int newRow = currentRow + di[k];
            int newCol = currentColumn + dj[k];
    
            if (shouldOpen(board, game, newRow, newCol)) {
                if (squares.get(newRow * columns + newCol).getMineCount() > 0) {
                    squares.get(newRow * columns + newCol).setIsOpened();
                }
                else {
                    openNeighboursRecursively(board, game, newRow, newCol);
                }
            }
        }
    }

    private static final int[] di = { -1, -1, -1, 0, 0, 1, 1, 1 };
    private static final int[] dj = { -1, 0, 1, -1, 1, -1, 0, 1 };

}

