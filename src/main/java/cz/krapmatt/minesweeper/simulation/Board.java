package cz.krapmatt.minesweeper.simulation;

import java.util.List;

import cz.krapmatt.minesweeper.entity.GameState;

public class Board {
    private List<Square> squares;
    private GameState gameState;
    private int numOfMines;

    /**
     * @return List<Square> return the squares
     */
    public List<Square> getSquares() {
        return squares;
    }

    /**
     * @param squares the squares to set
     */
    public void setSquares(List<Square> squares) {
        this.squares = squares;
    }

    /**
     * @return GameState return the gameState
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * @param gameState the gameState to set
     */
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }


    /**
     * @return int return the numOfMines
     */
    public int getNumOfMines() {
        return numOfMines;
    }

    /**
     * @param numOfMines the numOfMines to set
     */
    public void setNumOfMines(int numOfMines) {
        this.numOfMines = numOfMines;
    }

}
