/**
 * This is the back end grid which will be used to keep track
 * of all the vehicles in the board.
 */
public class Grid {
    //a 2d matrix of gridvehicles used to keep track of the back end
    private GridVehicle[][] grid = new GridVehicle[GridVariables.boardSize][GridVariables.boardSize];
    //this is the index for the row where the goal car has to pass through (always at last column)
    private final int goalRow = 2;

    /**
     * this method will create a 2d matrix initalised as null (empty board)
     */
    public Grid() {
        //scan through the rows and columns and initalise every index as null
        for (int i = 0; i < GridVariables.boardSize; i++) {
            for (int j = 0; j < GridVariables.boardSize; j++)
                //null means nothing is in the panel
                grid[i][j] = null;
        }
    }
    /**
     * This method will guarantee to return the row which contains the goal tile
     *
     * @return the row which defines "victory"
     */
    public int getGoalRow () {
        return goalRow;
    }

    /**
     * This method will guarantee to return the 2d matrix (the back end board) which represents
     * the grid.
     * @return a 2d matrix with contains all the gridvehicles
     */
    public GridVehicle[][] getGrid () {
        return grid;
    }
}
