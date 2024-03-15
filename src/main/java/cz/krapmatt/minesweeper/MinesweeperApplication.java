
package cz.krapmatt.minesweeper;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import cz.krapmatt.minesweeper.entity.Board;
import cz.krapmatt.minesweeper.entity.Game;
import cz.krapmatt.minesweeper.entity.GameState;
import cz.krapmatt.minesweeper.entity.Square;
import cz.krapmatt.minesweeper.service.GameService;

import java.util.List;
import java.util.Scanner;

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

    private final Scanner scanner;;

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
        } else {
           gameId = Integer.parseInt(args[3]);
           game = gameService.getGame(gameId);
        }

        GameState curGameState = GameState.ONGOING;
        
        while (curGameState == GameState.ONGOING) {
            //Naklonování nejnovější board... musí se clonovat jinak nemá null id a přepisuje se jen
            Board board = gameService.findNewestBoard(game).clone();
            //hraní kola
            curGameState = playRound(board, game);
            //Nastavení id hry pro každý board
            gameService.saveBoard(board);
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
    //Hraní jednoho kola
    private GameState playRound(Board board, Game game) {
        display(board.getSquares(), game);
        
        System.out.print("Akce (0 pro otevření políčka, 1 na označení miny, 2 pro ukončení hry): ");
        Action action = Action.fromInteger(scanner.nextInt());
        if (action == Action.CLOSE) {
            gameService.saveGame(game);
            System.out.println("Id hry: " + game.getId());
            System.exit(1);
        } 
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
            gameState = gameService.isAllOpenedOrMarked(board, game) ? GameState.WON_GAME : GameState.ONGOING;
        }

        if(action == Action.OPEN) {
            gameState = gameService.openSquare(board, game, row, column) ? GameState.ONGOING : GameState.LOST_GAME;
        }
        else if (action == Action.MARK) {
            gameService.toggleMarkedSquare(board, game, row, column);
        } else {
            System.out.println("Invalid action");
        }
        return gameState;
    }

    private void display(List<Square> squares, Game game) {
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