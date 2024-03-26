
package cz.krapmatt.minesweeper;

import java.util.List;
import java.util.Scanner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import cz.krapmatt.minesweeper.entity.Moves;
import cz.krapmatt.minesweeper.entity.Game;
import cz.krapmatt.minesweeper.entity.GameState;
import cz.krapmatt.minesweeper.service.GameService;
import cz.krapmatt.minesweeper.simulation.Board;
import cz.krapmatt.minesweeper.simulation.GameSimulation;
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
    private static final char filledSquareCharacter = '\u25A0';

    private static final String markedFlagCharacter = "\u2690";
    
    public static void main(String[] args) {
        SpringApplication.run(MinesweeperApplication.class, args);
    }

    private final GameService gameService;
    private GameSimulation gameSimulation;
    private final Scanner scanner;

    public MinesweeperApplication(GameService gameService, GameSimulation gameSimulation) {
        this.gameService = gameService;
        this.scanner = new Scanner(System.in);
        this.gameSimulation = gameSimulation;

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

        Board board = new Board();
        List<Square> squares = gameService.fillSquares(game);
        board.setNumOfMines(game.getMines().size());
        board.setSquares(squares);
        System.out.println(board);
        GameState curGameState = GameState.ONGOING;
        while (curGameState == GameState.ONGOING) {
            /*display(board, game);
            curGameState = gameSimulation.SimulateOneRound(board, game);*/

        }

        gameService.saveGame(game);

        if (curGameState == GameState.LOST_GAME) {
            this.gameOver("Prohra");
        } else if (curGameState == GameState.WON_GAME) {
            this.gameOver("Výhra");
        }
        scanner.close();
    }

    private void gameOver(String msg) {
        System.out.println("Stav hry: " + msg);
    }
    

    private void display(Board board, Game game) {
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
}