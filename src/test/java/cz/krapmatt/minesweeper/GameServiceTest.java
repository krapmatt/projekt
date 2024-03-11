package cz.krapmatt.minesweeper;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cz.krapmatt.minesweeper.entity.Board;
import cz.krapmatt.minesweeper.entity.Game;
import cz.krapmatt.minesweeper.entity.Square;
import cz.krapmatt.minesweeper.repository.GameRepository;
import cz.krapmatt.minesweeper.service.GameService;



public class GameServiceTest {

    private final GameService gameService = new GameService();

    @BeforeEach
    public void setUp() {
        // Create GameService instance
        if(gameService == null) {
            throw new RuntimeException("GameService is not properly injected.");
        } 
        
    }

    @Test
    public void testCreateGame() {
        int rows = 5;
        int columns = 5;
        int numOfMines = 5;

        // Create a game
        Game game = gameService.createGame(rows, columns, numOfMines);

        // Verify that the game has been created
        assertNotNull(game);
        assertEquals(rows, game.getRows());
        assertEquals(columns, game.getColumns());
        assertEquals(numOfMines, game.getNumOfMines());

        // Verify that the game has a board with the correct number of squares
        List<Board> boards = game.getBoards();
        assertNotNull(boards);
        assertEquals(1, boards.size());
        Board board = boards.get(0);
        assertNotNull(board);
        List<Square> squares = board.getSquares();
        assertNotNull(squares);
        assertEquals(rows * columns, squares.size());
    }

    
}