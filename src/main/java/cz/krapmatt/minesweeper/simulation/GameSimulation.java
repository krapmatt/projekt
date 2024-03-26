package cz.krapmatt.minesweeper.simulation;

import java.util.List;

import cz.krapmatt.minesweeper.entity.Game;
import cz.krapmatt.minesweeper.entity.GameState;
import cz.krapmatt.minesweeper.entity.Moves;
import cz.krapmatt.minesweeper.service.GameService;

public class GameSimulation {
    //stav hry -> si vytvoří hru
    private GameService gameService;
    
    private GameState SimulateAllRounds(int gameId) {
        Board board = new Board();
        board.setGameState(gameService.getGamestate(gameId));
        GameState gameState = board.getGameState();

        if (gameState == GameState.LOST_GAME) {
            return GameState.LOST_GAME;
        }

        Game game = gameService.getGame(gameId);
        List<Square> squares = gameService.fillSquares(game);
        board.setNumOfMines(game.getMines().size());
        board.setSquares(squares);

        gameState = SimulateOneRound(board, game);
        
        return gameState;
    }

    private GameState SimulateOneRound(Board board, Game game) {
        GameState gameState = board.getGameState();
        gameState = gameService.isAllOpenedOrMarked(board.getSquares()) ? GameState.WON_GAME : GameState.ONGOING;
        if (gameState == GameState.WON_GAME) {
            return GameState.WON_GAME;
        }

        List<Moves> moves = game.getMoves();
        Moves newestMove = moves.get(0);
        
        if (newestMove.getAction() == 0) {
            gameState = gameService.openSquare(board, game, newestMove.getSelectedRow(), newestMove.getSelectedColumn()) ? GameState.ONGOING : GameState.LOST_GAME;
        } else if (newestMove.getAction() == 1) {
            gameService.toggleMarkedSquare(board, game, newestMove.getSelectedRow(), newestMove.getSelectedColumn());    
        }

        return gameState;
    }
    
}
