package cz.krapmatt.minesweeper.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cz.krapmatt.minesweeper.entity.Board;
import cz.krapmatt.minesweeper.entity.Game;
import cz.krapmatt.minesweeper.entity.Square;
import cz.krapmatt.minesweeper.service.GameService;

@Controller
public class GameController {
    private static final char filledSquareCharacter = '\u25A0';

    private static final String markedFlagCharacter = "\u2690";

    private final GameService gameService;
    private final int gameId = 1;
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }
    
    @RequestMapping("/hello")
    @ResponseBody
    
    public String showGameAsString() {
        Game game = gameService.getGame(gameId);
        return generateGameString(game);
    }

    private String generateGameString(Game game) {
        Board board = gameService.findNewestBoard(game);
        int columns = game.getColumns();
        int rows = game.getRows();

        StringBuilder gameStringBuilder = new StringBuilder();
        gameStringBuilder.append("<div style=\"font-family: monospace;\">");

        gameStringBuilder.append("Game ID: ").append(game.getId()).append("<br>").append("Rows: ").append(rows).append("<br>")
        .append("Columns: ").append(columns).append("<br>").append("Number of mines: ").append(game.getNumOfMines()).append("<br>");

        
        List<Square> squares = board.getSquares();
        // Add board data
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
                    gameStringBuilder.append(String.format("%3s" ,markedFlagCharacter));
                }
                else {
                    gameStringBuilder.append(String.format("%3s",filledSquareCharacter));
                }
            }
            gameStringBuilder.append("<br>");
        }
        gameStringBuilder.append("</div>");

        return gameStringBuilder.toString();
    }
}

