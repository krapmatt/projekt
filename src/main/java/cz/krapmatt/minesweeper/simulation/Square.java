package cz.krapmatt.minesweeper.simulation;

public class Square {
    private boolean hasMine;
    private boolean isOpened;
    private boolean isMarked;
    private int mineCount;
    
    private static final int neighbourCount = 8;

    public boolean checkIsOpened() {
        return isOpened;
    }

    public void setIsOpened() {
        isOpened = true;
        isMarked = false;
    }

    public void toggleMarked() {
        isMarked = !isMarked;
    }

    public boolean checkIsMarked() {
        return isMarked;
    }

    public void setHasMine() {
        hasMine = true;
    }

    public boolean checkHasMine() {
        return hasMine;
    }

    public void setMineCount(int mineCount) {
        this.mineCount = mineCount;
    }

    public int getMineCount() {
        return mineCount;
    }

    public static int getNeighbourCount() {
        return neighbourCount;
    }
}
