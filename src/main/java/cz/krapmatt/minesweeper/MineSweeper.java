package cz.krapmatt.minesweeper;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;

import cz.krapmatt.minesweeper.controller.RoundController;
import cz.krapmatt.minesweeper.entity.Board;
import cz.krapmatt.minesweeper.entity.GameState;
import cz.krapmatt.minesweeper.service.RoundService;



class Minesweeper {
    public Board board;

    @Autowired
    private RoundService roundService;
    @Autowired
    private RoundController roundController;

    private enum Action {
        OPEN,
        MARK;

        public static Action fromInteger(int i) {
            switch(i) {
                case 0: return OPEN;
                case 1: return MARK;
            }
            return null;
        }
    };
    
    
    public Minesweeper(int rows, int columns, int numOfMines) {
        
        
        board = new Board(rows, columns, numOfMines);
        board.initBoard();
        
    }

    public GameState playRound(Scanner sc) {
        board.display();
        
        System.out.print("Akce (0 pro otevření políčka, 1 na označení miny): ");
        Action action = Action.fromInteger(sc.nextInt());
        System.out.print("Řádek Sloupec: ");
        int row    = sc.nextInt() - 1;
        int column = sc.nextInt() - 1;

        GameState gameState = GameState.ONGOING;
        if (gameState == GameState.ONGOING) {
            gameState = board.isAllOpenedOrMarked() ? GameState.ONGOING : GameState.WON_GAME;
        }

        if(action == Action.OPEN) {
            gameState = board.openSquare(row, column) ? GameState.ONGOING : GameState.LOST_GAME;
        }
        else if (action == Action.MARK) {
            board.toggleMarkedSquare(row, column);
        }
        else {
            System.out.println("Invalid action");
        }

        

        return gameState;
        
    }

    public void gameOver(String msg) {
        System.out.println("Stav hry: " + msg);
    }

}
