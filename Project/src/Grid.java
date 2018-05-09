import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.Arrays;


public class Grid {
    private int boardSize = 6;
    private GridVehicle[][] grid = new GridVehicle[boardSize][boardSize];
    //this is the index for thw row column where the goal has to pass through
    private final int goalColumn = 2;
    private final int goalRow = 2;

    public Grid() {
        for(int i = 0 ; i < boardSize; i++){
            for(int j = 0; j < boardSize; j++)
                //false means nothing is in the panel
                grid[i][j] = null;
        }

    }

    public int getGoalColumn() {
        return goalColumn;
    }

    public int getGoalRow() {
        return goalRow;
        }

    public GridVehicle[][] getGrid() {
        return grid;
    }

    public int getBoardSize() {
        return boardSize;
    }

    @Override
    public String toString() {
        String s = new String();
        for(int i = 0; i < boardSize; i ++){
            s += "[";
            for(int j = 0; j < boardSize; j++){
                s += grid[i][j];
                s += " ";
            }
            s += "]\n";
        }
        return "Grid{" +
                "grid=" + s +
                '}';
    }
}
