
package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.demo.Minesweeper.GameState;

import java.util.Scanner;

@SpringBootApplication
public class MinesweeperApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(MinesweeperApplication.class, args);
    }

    @Override
    public void run(String... args) {
        if (args.length != 3) {
            System.err.println("Usage: java -jar Minesweeper.jar <rows> <columns> <numOfMines>");
            return;
        }

        int rows = Integer.parseInt(args[0]);
        int columns = Integer.parseInt(args[1]);
        int numOfMines = Integer.parseInt(args[2]);

        Scanner scanner = new Scanner(System.in);
        Minesweeper minesweeper = new Minesweeper(rows, columns, numOfMines);

        GameState curGameState = GameState.ONGOING;
        while (curGameState == GameState.ONGOING) {
            
            curGameState = minesweeper.playRound(scanner);
        }

        if (curGameState == GameState.LOST_GAME) {
            minesweeper.gameOver("You lost :(");
        } else if (curGameState == GameState.WON_GAME) {
            minesweeper.gameOver("Congratulations! You won :)");
        }

        scanner.close();
    }
}