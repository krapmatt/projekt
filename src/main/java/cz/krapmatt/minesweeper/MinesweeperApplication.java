
package cz.krapmatt.minesweeper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import cz.krapmatt.minesweeper.entity.Board;
import cz.krapmatt.minesweeper.entity.Game;
import cz.krapmatt.minesweeper.entity.GameState;
import cz.krapmatt.minesweeper.entity.Square;
import cz.krapmatt.minesweeper.repository.GameRepository;
import cz.krapmatt.minesweeper.service.GameService;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class MinesweeperApplication implements CommandLineRunner {
    @Autowired
    private GameRepository gameRepository;
    
    private final GameService gameService;
    private final Scanner scanner;

    public MinesweeperApplication(GameService gameService) {
        this.gameService = gameService;
        this.scanner = new Scanner(System.in);
    }
    
    private static final char filledSquareCharacter = '\u25A0';
    private static final char markedFlagCharacter = '\u2691';

    private enum Action {
        OPEN,
        MARK;

        public static Action fromInteger(int i) {
            if (i < Action.values().length) {
                return Action.values()[i]; 
            } else {
                return null;
            }
        }
    };

    public static void main(String[] args) {
        SpringApplication.run(MinesweeperApplication.class, args);
    }

    @Override
    public void run(String... args) {
        if (args.length != 3) {
            System.err.println("Usage: java -jar Minesweeper-1.jar <rows> <columns> <numOfMines>");
            return;
        }
        //Zadat id hry nebo x na vytvoření nové
        //pro odejití hry a připojení zpět no ní/jiné

        int rows = Integer.parseInt(args[0]);
        int columns = Integer.parseInt(args[1]);
        int numOfMines = Integer.parseInt(args[2]);
        
        Game game = gameService.createGame(rows, columns, numOfMines);
       
        
        if(gameService == null) {
            throw new RuntimeException("GameService is not properly injected.");
        }

        
        GameState curGameState = GameState.ONGOING;
        while (curGameState == GameState.ONGOING) {
            curGameState = playRound(game);
        }

        if (curGameState == GameState.LOST_GAME) {
            this.gameOver("Prohra");
        } else if (curGameState == GameState.WON_GAME) {
            this.gameOver("Výhra");
        }

        scanner.close();
        gameRepository.saveGame(game);
    }

    public void gameOver(String msg) {
        System.out.println("Stav hry: " + msg);
    }

    @Transactional
    public GameState playRound(Game game) {
        display(game);
        
        System.out.print("Akce (0 pro otevření políčka, 1 na označení miny): ");
        Action action = Action.fromInteger(scanner.nextInt());
        System.out.print("Řádek: ");
        int row = scanner.nextInt() - 1;
        System.out.print("Sloupec: ");
        int column = scanner.nextInt() - 1;

        if (row < 0 || row >= game.getRows() || column < 0 || column >= game.getColumns()) {
            System.out.println("Neplatné souřadnice.");
            return GameState.ONGOING;
        }

        GameState gameState = GameState.ONGOING;
        if (gameState == GameState.ONGOING) {
            gameState = gameService.isAllOpenedOrMarked(game) ? GameState.ONGOING : GameState.WON_GAME;
        }

        if(action == Action.OPEN) {
            gameState = gameService.openSquare(game, row, column) ? GameState.ONGOING : GameState.LOST_GAME;
        }
        else if (action == Action.MARK) {
            gameService.toggleMarkedSquare(game, row, column);
        }
        else {
            System.out.println("Invalid action");
        }

        return gameState;
        
    }

    public void display(Game game) {
        int columns = game.getColumns();
        int rows = game.getRows();
        List<Square> squares = game.getSquares();
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
                    System.out.printf("%3c", markedFlagCharacter);
                }
                else {
                    System.out.printf("%3c", filledSquareCharacter);
                }
            }
            System.out.println();
        }
    }
}