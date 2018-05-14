import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Random;

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
    //
    private ArrayList<GridVehicle> gridVehicles = new ArrayList<>();
    private int gridScore;

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

    public Grid(Grid g){

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

    public void setGrid(GridVehicle[][] grid) {
        this.grid = grid;
    }

    public void setGridVehicles(ArrayList<GridVehicle> gridVehicles) {
        this.gridVehicles = gridVehicles;
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


    public void incrementMoveCounter() {
        numberOfMoves++;
    }

    public void resetNewGrid() {
        //scan through the rows and columns and initalise every index as null
        for (int i = 0; i < GridVariables.BOARD_SIZE; i++)
            for (int j = 0; j < GridVariables.BOARD_SIZE; j++)
                //null means nothing is in the panel
                grid[i][j] = null;
        numberOfMoves = 0;
        gridVehicles.clear();
    }

    public boolean validPlacement(Vehicle vehicle, int row, int col, GridVehicle gridVehicle) {
        if (vehicle.isHorizontal()) {
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
        return true;
    }

    public Grid makeCopy(Grid g){
        Grid copy = new Grid();
        copy.resetNewGrid();
        for(int i = 0; i < GridVariables.BOARD_SIZE; i++){
            for(int j = 0 ; j < GridVariables.BOARD_SIZE; j++){
                if(null == g.getGrid()[i][j])
                    copy.getGrid()[i][j] = null;
                else {
                    boolean goalcar = g.getGrid()[i][j].isGoalCar();
                    Vehicle newVehicle = new Vehicle(g.getGrid()[i][j].getVehicle().isHorizontal(), g.getGrid()[i][j].getVehicle().getSize());
                    copy.getGrid()[i][j] = new GridVehicle(goalcar, newVehicle,
                            g.getGrid()[i][j].getRow(), g.getGrid()[i][j].getCol(), g);
                }
            }
        }
        for(int i = 0; i < g.getGridVehicles().size();i++)
            copy.getGridVehicles().add(g.getGridVehicles().get(i));
        copy.setGridVehicles(g.gridVehicles);
        copy.setGrid(g.grid);
        return copy;
    }

    public boolean solved(GridVehicle goalCar) {
        return (goalCar.getCol() == GridVariables.BOARD_SIZE - 2);
    }

    public ArrayList<GridVehicle> getGridVehicles() {
        return gridVehicles;
    }

    public void tryMoveVehicle(GridVehicle v) {
        //if the vehicle is horizontal we want to try moving left/right
        Random generator = new Random();
        boolean left = generator.nextBoolean();
        boolean up = generator.nextBoolean();
        if (v.getVehicle().isHorizontal()) {
            //moving to the right ->
            if (!left && (v.getCol() > 0 && v.getCol() < GridVariables.BOARD_SIZE - 1 - v.getVehicle().getSize()) &&
                    grid[v.getCol() + v.getVehicle().getSize()][v.getRow()] == null) {
                //now we want to clear up where the car was
                for (int i = 0; i < v.getVehicle().getSize(); i++)
                    grid[v.getCol() + i][v.getRow()] = null;
                for (int i = 0; i < v.getVehicle().getSize(); i++)
                    grid[v.getCol() + 1 + i][v.getRow()] = v;
                v.setCol(v.getCol() + 1);
                return;

            }
            //moving to the left <-
            if (left && (v.getCol() > 0 && (v.getCol() < GridVariables.BOARD_SIZE - 1 - v.getVehicle().getSize()))
                    && grid[v.getCol() - 1][v.getRow()] == null) {
                //now we want to clear up where the car was
                for (int i = 0; i < v.getVehicle().getSize(); i++)
                    grid[v.getCol() + i][v.getRow()] = null;
                for (int i = 0; i < v.getVehicle().getSize(); i++)
                    grid[v.getCol() - 1 + i][v.getRow()] = v;
                v.setCol(v.getCol() + 1);
                return;
            }
        } else {
            //moving up
            if (up && (v.getRow()> 0 && (v.getRow() < GridVariables.BOARD_SIZE - v.getVehicle().getSize() - 1))&&
                    grid[v.getCol()][v.getRow() + v.getVehicle().getSize()] == null) {
                //now we want to clear up where the car was
                for (int i = 0; i < v.getVehicle().getSize(); i++)
                    grid[v.getCol()][v.getRow() + i] = null;
                for (int i = 0; i < v.getVehicle().getSize(); i++)
                    grid[v.getCol()][v.getRow() - 1 + i] = v;
                v.setRow(v.getRow() - 1);
                return;
            }
            //moving down
            if (!up && (v.getRow()> 0 && (v.getRow() < GridVariables.BOARD_SIZE - v.getVehicle().getSize() - 1))
                    && grid[v.getCol()][v.getRow() + v.getVehicle().getSize()] == null) {
                //now we want to clear up where the car was
                for (int i = 0; i < v.getVehicle().getSize(); i++)
                    grid[v.getCol()][v.getRow() + i] = null;
                for (int i = 0; i < v.getVehicle().getSize(); i++)
                    grid[v.getCol()][v.getRow()+ i + 1] = v;
                v.setRow(v.getRow() + 1);
                return;
            }
        }
        return;
    }

    public int getGridScore() {
        return gridScore;
    }

    public void setGridScore(int gridScore) {
        this.gridScore = gridScore;
    }
}
