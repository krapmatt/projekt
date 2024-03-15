package cz.krapmatt.minesweeper;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cz.krapmatt.minesweeper.entity.Board;
import cz.krapmatt.minesweeper.entity.Game;
import cz.krapmatt.minesweeper.entity.Square;
import cz.krapmatt.minesweeper.service.GameService;



public class GameServiceTest {

    private GameService gameService;
    private Game game;
    private Board board;

    @BeforeEach
    public void setUp() {
        gameService = new GameService();
        game = new Game();
        game.setRows(5);
        game.setColumns(5);
        game.setNumOfMines(5);
        board = new Board();
        board.setGame(game);
        List<Square> squares = gameService.createSquares(5, 5, board);
        board.setSquares(squares);
    }

    @Test
    public void testCreateSquares() {
        List<Square> squares = board.getSquares();
        assertNotNull(squares);
        assertEquals(25, squares.size());
    }

    @Test
    public void testPlaceMines() {
        gameService.placeMines(board, game);
        List<Square> squares = board.getSquares();
        int mineCount = 0;
        for (Square square : squares) {
            if (square.checkHasMine()) {
                mineCount++;
            }
        }
        assertEquals(5, mineCount);
    }

    // Add more test methods here...

}