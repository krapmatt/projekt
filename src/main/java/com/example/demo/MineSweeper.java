package com.example.demo;

import java.util.*;
class Minesweeper {
    private Board board;
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
    private enum GameState {ONGOING, WON_GAME, LOST_GAME};

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

    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("java Minesweeper <radky> <sloupce> <pocetMin>");
            return;
        }
        
        Scanner sc = new Scanner(System.in);
        Minesweeper minesweeper = new Minesweeper(
            Integer.valueOf(args[0]),
            Integer.valueOf(args[1]),
            Integer.valueOf(args[2])
        );

        GameState curGameState = GameState.ONGOING;
        while (curGameState == GameState.ONGOING) {
            curGameState = minesweeper.playRound(sc);
        }

        if (curGameState == GameState.LOST_GAME) {
            minesweeper.gameOver("Prohra :(");
        }
        else if (curGameState == GameState.WON_GAME) {
            minesweeper.gameOver("Výhra :)");
        }
        sc.close();
    }
}
