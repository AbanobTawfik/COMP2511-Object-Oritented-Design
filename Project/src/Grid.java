import javafx.util.Pair;
import java.util.ArrayList;

/**
 * This is the back end grid which will be used to keep track
 * of all the vehicles in the board. and update the board state
 * . This class is the class for representing the board state
 */
public class Grid {
    //a 2d matrix of gridvehicles used to keep track of the back end
    private GridVehicle[][] grid = new GridVehicle[GridVariables.BOARD_SIZE][GridVariables.BOARD_SIZE];
    //this is the index for the row where the goal car has to pass through (always at last column)
    private final int goalRow = 2;
    //this is a counter for counting how many drags in the board
    private int numberOfMoves = 0;
    //this will be all the gridvehicles allocated on the grid
    private ArrayList<GridVehicle> gridVehicles = new ArrayList<>();

    /**
     * this method will create a 2d matrix initalised as null (empty board)
     */
    public Grid() {
        //scan through the rows and columns and initalise every index as null
        for (int i = 0; i < GridVariables.BOARD_SIZE; i++)
            for (int j = 0; j < GridVariables.BOARD_SIZE; j++)
                //null means nothing is in the panel
                grid[i][j] = null;
    }

    /**
     * This method will guarantee to return the row which contains the goal tile
     *
     * @return the row which defines "victory"
     */
    public int getGoalRow() {
        return goalRow;
    }

    /**
     * This method will guarantee to return the 2d matrix (the back end board) which represents
     * the grid.
     *
     * @return a 2d matrix with contains all the gridvehicles
     */
    public GridVehicle[][] getGrid() {
        return grid;
    }

    /**
     * This method will update the back end on an attempted move, once the mouse is released and the vehicle
     * is snapped into the row and column, it will free the old row + column (backend) where the object was previously
     * and it will store the object in the new row and column provided.
     * This method guarantees to free the current coordinates occupied from the grid by nulling the row column pair index
     * and it will guarantee to place the object in the grid at the row and column given, provided it is supplied
     * with a valid non zero integer for row and column, and the vehicle + grid vehicle pair which make the front end vehicle class
     *
     * @param column             the column where the object is located at after it is snapped
     * @param row                the roww where the object is lovated at after it is snapped
     * @param coordinatesBlocked the coordinates blocked by the gridvehicle currently (has to be dynamically updated)
     * @param vehicle            the vehicle core with vehicle size and orientation (Backend vehicle)
     * @param gridVehicle        the grid vehicle which is displayed (the front end vehicle)
     */
    /*
     * this function will update the back end grid based on where the row and column of the object. this is called
     * initially when the object is shifted from the top left origin, and when the cursor is released
     */
    public void updateGrid(int column, int row, ArrayList<Pair<Integer, Integer>> coordinatesBlocked, Vehicle vehicle, GridVehicle gridVehicle) {
        //scan through the coordinates blocked by the current object
        for (Pair<Integer, Integer> coordinates : coordinatesBlocked) {
            //clear up on the back end grid teh column + row index in matrix where the object is located
            grid[coordinates.getKey()][coordinates.getValue()] = null;
        }
        //clear the list of coordinates blocked so we can properly updated
        coordinatesBlocked.clear();
        //if the vehicle is vertical/horizontal the update will be different so,
        //if the vehicle is hoirzontal
        if (vehicle.isHorizontal()) {
            //scan through the based on vehicle size
            for (int i = 0; i < vehicle.getSize(); i++) {
                //add the column + row index into the coordinate blocked pair
                coordinatesBlocked.add(new Pair<>(column + i, row));
                //set the index at the column + row to be equal to the current object to update board
                //the row is always same since it doesnt change since the object is horizontal only horizontal movement
                grid[column + i][row] = gridVehicle;
            }
        }
        //if the vehicle is vertical
        else {
            //scan through the based on vehicle size
            for (int i = 0; i < vehicle.getSize(); i++) {
                //add the column + row index into the coordinate blocked pair
                coordinatesBlocked.add(new Pair<>(column, row + i));
                //set the index at the column + row to be equal to the current object to update board
                //the column is always same since it doesnt change since vertical, only moves vertically
                grid[column][row + i] = gridVehicle;
            }
        }
    }

    /**
     * this method will check if there is an obstruction (object) in the updated column and row for the attempted move.
     * This is the backend check for if there is an object obstruction. this method will guarantee to return true if there
     * are no obstructions, and false if otherwise, provided it receives valid integers for the row and column index to check,
     * and the vehicle pair (front and backend) representing the vehicle attempting to move.
     *
     * @param col         the column index for the attempted move
     * @param row         the row index for the attempted move
     * @param vehicle     the vehicle core with vehicle size and orientation (Backend vehicle)
     * @param gridVehicle the grid vehicle which is displayed (the front end vehicle)
     * @return true if the index is either null or the current object itself (since the vehicle spans), false if there is an object in that index
     */
    /*
     * there are 2 checks, one is if the vehicle is horizxontal, and the other is if the vehicle is vertical
     */
    public boolean isValidMove(int col, int row, Vehicle vehicle, GridVehicle gridVehicle) {
        //if the object is horizontal
        if (vehicle.isHorizontal()) {
            //scan through matrix based on vehicle size
            for (int i = 0; i < vehicle.getSize(); i++) {
                //to avoid mull pointer issue and bound the car to the board (so it doesnt go over boardsize)
                //if the attempted move is outside board RETURN FALSE
                if (col + i >= grid.length) {
                    return false;
                }
                //this is the main check
                //If ANY block of the current object moved across collides with another. i.e.
                //the updated coordinates dont contain any object, or the current object itself (cant collide with itself)
                //if the object slided across does not have any objects in the way beside itself is the main check
                if (grid[col + i][row] != gridVehicle && null != grid[col + i][row])
                    return false;
            }
        }
        //if the object is vertical
        else {
            //scan through matrix based on vehicle size
            for (int i = 0; i < vehicle.getSize(); i++) {
                //to avoid mull pointer issue and bound the car to the board (so it doesnt go over boardsize)
                //if the attempted move is outside board RETURN FALSE
                if (row + i >= grid.length) {
                    return false;
                }
                //similair to above
                //this is the main check
                //If ANY block of the current object moved across collides with another. i.e.
                //the updated coordinates dont contain any object, or the current object itself (cant collide with itself)
                //if the object slided across does not have any objects in the way beside itself is the main check
                if (grid[col][row + i] != gridVehicle && null != grid[col][row + i])
                    return false;
            }
        }
        //default return true, that means the scan didn't fail for the shifted block
        return true;
    }


    /**
     * This method will increment the counter showing number of moves player has performed.
     * This method guarantees to always increase the move counter by 1
     */
    public void incrementMoveCounter() {
        numberOfMoves++;
    }

    /**
     * This method will clear the entire grid, and all gridVehicles assosciated with that grid
     * in order to create a new one. This method will guarantee to null every object on the grid
     * and clear the list of grid vehicles effectively wiping it.
     */
    public void resetNewGrid() {
        //scan through the rows and columns and initalise every index as null
        for (int i = 0; i < GridVariables.BOARD_SIZE; i++)
            for (int j = 0; j < GridVariables.BOARD_SIZE; j++)
                //null means nothing is in the panel
                grid[i][j] = null;
        //reset the move counter
        numberOfMoves = 0;
        //clear the list of grid vehicles
        gridVehicles.clear();
    }

    /**
     * This method will check if the attempt to add a gridVehicle in a given row and column, is legal
     * this will be the same method mentioned above in isvalid move but upon initalisation. This method
     * will guarantee to return a boolean provided it receives, a row column index for the grid vehicle,
     * the vehicle with size and orientation for the grid vehicle, and the grid vehicle itself. it will check
     * the grid to see if there are any objects obstructing on insert.
     *
     * @param vehicle     the vehicle which contains size and orientation for the represented grid vehicle
     * @param row         the row index where the grid vehicle is located
     * @param col         the column index where the grid vehicle is located
     * @param gridVehicle the grid vehicle object itself attempting to be added
     * @return true if the vehicle can be placed in the given row and column, false if otherwise
     */
    public boolean validPlacement(Vehicle vehicle, int row, int col, GridVehicle gridVehicle) {
        //perform same function as isValidMove as it does the same check
        //it will check if the row and column attempting to be placed in will cause overlap with another
        //object in the grid
        return isValidMove(col,row,vehicle,gridVehicle);
    }

    /**
     * This method will be used to get the list of grid vehicles which are all the vehicles that are on the board.
     * This method will guarantee to return a list of grid vehicles which are representitive of the board state
     *
     * @return a list of grid vehicles that of which are all located on the board
     */
    public ArrayList<GridVehicle> getGridVehicles() {
        return gridVehicles;
    }
}
