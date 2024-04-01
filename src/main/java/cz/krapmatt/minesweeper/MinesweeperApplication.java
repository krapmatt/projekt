
package cz.krapmatt.minesweeper;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import cz.krapmatt.minesweeper.entity.Game;
import cz.krapmatt.minesweeper.entity.GameState;
import cz.krapmatt.minesweeper.entity.Moves;
import cz.krapmatt.minesweeper.service.GameService;
import cz.krapmatt.minesweeper.simulation.Board;
import cz.krapmatt.minesweeper.simulation.Square;

@SpringBootApplication
public class MinesweeperApplication implements CommandLineRunner {
    private enum Action {
        OPEN,
        MARK,
        CLOSE;

        public static Action fromInteger(int i) {
            if (i < Action.values().length) {
                return Action.values()[i]; 
            } else {
                return null;
            }
        }
    }
    
    
    public static void main(String[] args) {
        SpringApplication.run(MinesweeperApplication.class, args);
    }

    private final GameService gameService;
    private final Scanner scanner;

    public MinesweeperApplication(GameService gameService) {
        this.gameService = gameService;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void run(String... args) {
        if (args.length != 4) {
            System.err.println("Usage: java -jar Minesweeper-1.jar <rows> <columns> <numOfMines> <new/gameid>");
            return;
        }
        //Zadat id hry nebo x na vytvoření nové
        //pro odejití hry a připojení zpět no ní/jiné
        //mělo by fungovat
        int rows = Integer.parseInt(args[0]);
        int columns = Integer.parseInt(args[1]);
        int numOfMines = Integer.parseInt(args[2]);
        String gameIdInput = args[3];
        int gameId;
        Game game;
        
        if(gameService == null) {
            throw new RuntimeException("GameService is not properly injected.");
        }

        if (gameIdInput.equalsIgnoreCase("new")) {
            game = gameService.createGame(rows, columns, numOfMines);
            gameId = game.getId();
        } else {
           gameId = Integer.parseInt(args[3]);
           game = gameService.getGame(gameId);
        }
        Board board = gameService.createBoard(game);
        List<Moves> moves = new ArrayList<>();
        GameState curGameState = GameState.ONGOING;
        while (curGameState == GameState.ONGOING) {

            System.out.print("Akce (0 pro označení pole/ 1 pro označení bomby): ");
            int action = scanner.nextInt();
            System.out.print("Řádek: ");
            int row = scanner.nextInt();
            System.out.print("Sloupec: ");
            int column = scanner.nextInt();
            Moves move = new Moves(row, column, action, game);
            moves.add(move);
            gameService.SimulateOneRound(board, game, move);
            gameService.updateBoardCache(game, board);

        }

        if (board.getGameState() == GameState.LOST_GAME) {
            gameOver("Prohra");
        } else if (board.getGameState() == GameState.WON_GAME) {
            gameOver("Výhra");
        }

        gameService.saveGame(game);
        

        scanner.close();
        
    }

    private void gameOver(String msg) {
        System.out.println("Stav hry: " + msg);
        
    }
    

    
}