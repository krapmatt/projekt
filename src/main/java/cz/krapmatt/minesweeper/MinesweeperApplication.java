
package cz.krapmatt.minesweeper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import cz.krapmatt.minesweeper.controller.BoardController;
import cz.krapmatt.minesweeper.controller.GameController;
import cz.krapmatt.minesweeper.controller.RoundController;
import cz.krapmatt.minesweeper.entity.Board;
import cz.krapmatt.minesweeper.entity.Game;
import cz.krapmatt.minesweeper.entity.GameState;
import cz.krapmatt.minesweeper.entity.Round;
import cz.krapmatt.minesweeper.service.BoardService;
import cz.krapmatt.minesweeper.service.GameService;
import cz.krapmatt.minesweeper.service.RoundService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class MinesweeperApplication implements CommandLineRunner {
    @Autowired
    private GameService gameService;

    @Autowired
    private RoundService roundService;

    @Autowired
    private BoardService boardService;

    public static void main(String[] args) {
        SpringApplication.run(MinesweeperApplication.class, args);
    }

    @Override
    public void run(String... args) {
        if (args.length != 3) {
            System.err.println("Usage: java -jar Minesweeper-1.jar <rows> <columns> <numOfMines>");
            return;
        }
        
        Game game = new Game();
        if(gameService == null) {
            throw new RuntimeException("GameController is not properly injected.");
        }
        game = gameService.createGame(game);
        List<Round> rounds = new ArrayList<>();
        

        int rows = Integer.parseInt(args[0]);
        int columns = Integer.parseInt(args[1]);
        int numOfMines = Integer.parseInt(args[2]);

        Scanner scanner = new Scanner(System.in);
        Minesweeper minesweeper = new Minesweeper(rows, columns, numOfMines);
        

        GameState curGameState = GameState.ONGOING;
        while (curGameState == GameState.ONGOING) {
            
            Round round = new Round();
            round = roundService.saveRound(round);
            
            curGameState = minesweeper.playRound(scanner);
            
            //po každém kole uložit Round do listu...
            round.setGameState(curGameState);
            round.setGame(game);
            rounds.add(round);
            roundService.saveRound(round);
            
        }
        
        game.setRounds(rounds);
        
        //Uložení id hry... mělo by se zvyšovat
        gameService.saveGame(game);


        if (curGameState == GameState.LOST_GAME) {
            minesweeper.gameOver("Prohra");
        } else if (curGameState == GameState.WON_GAME) {
            minesweeper.gameOver("Výhra");
        }

        scanner.close();
    }
}