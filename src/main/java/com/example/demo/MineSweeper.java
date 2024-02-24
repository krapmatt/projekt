package com.example.demo;

import java.util.*;
class Minesweeper {
    public Board board;
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
    public enum GameState {ONGOING, WON_GAME, LOST_GAME};

    public Minesweeper(int rows, int columns, int numOfMines) {
        board = new Board(rows, columns, numOfMines);
        board.initBoard();
    }

    public GameState playRound(Scanner sc) {
        board.display();

        System.out.print("Akce (0 pro otevření políčka, 1 na označení miny) : ");
        Action action = Action.fromInteger(sc.nextInt());
        System.out.print("Řádek Sloupec : ");
        int row    = sc.nextInt() - 1;
        int column = sc.nextInt() - 1;

        GameState gameState = GameState.ONGOING;

        if(action == Action.OPEN) {
            gameState = board.openSquare(row, column) ? GameState.ONGOING : GameState.LOST_GAME;
        }
        else if (action == Action.MARK) {
            board.toggleMarkedSquare(row, column);
        }
        else {
            System.out.println("Invalid action");
        }

        if (gameState == GameState.ONGOING) {
            gameState = board.isAllOpenedOrMarked() ? GameState.WON_GAME : GameState.ONGOING;
        }
        return gameState;
    }

    public void gameOver(String msg) {
        System.out.println("Prohra, umřel jsi!! " + msg);
    }

}
