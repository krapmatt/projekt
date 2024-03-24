package cz.krapmatt.minesweeper.controller;

public class NewGameData {
    private int rows;
    private int columns;
    private int numOfMines;
    /**
     * @return int return the rows
     */
    public int getRows() {
        return rows;
    }

    /**
     * @param rows the rows to set
     */
    public void setRows(int rows) {
        this.rows = rows;
    }

    /**
     * @return int return the columns
     */
    public int getColumns() {
        return columns;
    }

    /**
     * @param columns the columns to set
     */
    public void setColumns(int columns) {
        this.columns = columns;
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
