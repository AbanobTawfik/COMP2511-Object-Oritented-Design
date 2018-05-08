import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.Arrays;


public class Grid {
    private int boardSize = 6;
    private boolean[][] grid = new boolean[boardSize][boardSize];
    //this is the index for thw row column where the goal has to pass through
    private final int goalColumn = 2;
    private final int goalRow = 2;

    public Grid() {
        for(int i = 0 ; i < boardSize; i++){
            for(int j = 0; j < boardSize; j++)
                //false means nothing is in the panel
                grid[i][j] = false;
        }

    }

    public int getGoalColumn() {
        return goalColumn;
    }

    public int getGoalRow() {
        return goalRow;
        }

    public boolean[][] getGrid() {
        return grid;
    }

    public int getBoardSize() {
        return boardSize;
    }

}
