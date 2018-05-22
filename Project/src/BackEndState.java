import java.util.ArrayList;
import java.util.Objects;

/**
 * <p>
 * This class will be representitive of the back end board state simplfiied to just integers
 * -1 is representitive of empty space on the board
 * 0 is representitive of a obstacle (non goal) vehicle on the board
 * 36 is representitive of the goal vehicle on the board
 * </p>
 * <p>
 * this is a much more compact way of storing and making multiple states as attempting to make deep copeis
 * of grid vehicles and grid causes a recursive memory issue, since grid vehicles use a grid which also uses grid vehicles
 * causing a nested problem and making a huge amount of objects.
 * </p>
 * <p>
 * The state stores a list of back end vehicles (more compact vehicle storage). this will allow for updating board state
 * by moving vehicles based on row column index and updating it.
 * </p>
 */
public class BackEndState {
    //2d matrix of integer (very cost effective method for representing grid state)
    //rather than using grid vehicles and grids (causes recursive memory issue when trying to make new states
    private int[][] backendGrid = new int[GridVariables.BOARD_SIZE][GridVariables.BOARD_SIZE];
    //the row representitive of victory
    private int goalRow = 2;
    //this is a counter for counting how many drags in the board
    private int numberOfMoves = 0;
    //not possible to get over 36 cars
    private int goalCar = 36;
    //a list of grid vehicles being represented in a more compact form rather than using the gridvehicles itself
    //only stores information required to solve the board
    private ArrayList<BackEndCar> backEndVehicles = new ArrayList<>();

    /**
     * This method will create a back end representation of the state using the front end grid. this is so
     * the search will use alot less memory and be quicker. a back end representation of just integers rather than
     * objects drastically improves performance. this method performs the following
     * <p>
     * <p> null spaces on the board are represented as -1 </p>
     * <p> obstacle cars (cars that aren't the goal car) are represented as 0</p>
     * <p> the goal car will be represented with 36 (board size) as integer (the integer of choice doesnt matter)</p>
     * <p>
     * <p> in order to differentiate between the vehicles a list of vehicles containing the vital information needed in backend,
     * the row and column and orientation and size is stored within the class.</p>
     * <p> This method guarantees to return a state representation of the board provided it receives a non null grid.</p>
     *
     * @param grid the front end board grid used to represent the game state.
     */
    public BackEndState(Grid grid) {
        //scan through the grid
        for (int i = 0; i < GridVariables.BOARD_SIZE; i++) {
            for (int j = 0; j < GridVariables.BOARD_SIZE; j++) {
                //if the object in the index is null
                if (null == grid.getGrid()[i][j]) {
                    //store -1 (null -> -1) and go to next index
                    backendGrid[i][j] = -1;
                    continue;
                } else {
                    //if the object in the index is the goal vehicle (ALWAYS HORIZONTAL)
                    if (grid.getGrid()[i][j].isGoalCar()) {
                        //store 36 (goal variable) and go to the next index
                        backendGrid[grid.getGrid()[i][j].getCol()][grid.getGrid()[i][j].getRow()] = goalCar;
                        continue;
                    } else {
                        //if the object in the index is an obstacle vehicle (not goal vehicle)
                        //store 0 and go to the next index
                        backendGrid[grid.getGrid()[i][j].getCol()][grid.getGrid()[i][j].getRow()] = 0;
                        continue;
                    }
                }
            }
        }
        //now we also want to make back end vehicles representing the grid vehicle in the most basic form
        //scan through the list of grid vehicles
        for (int i = 0; i < grid.getGridVehicles().size(); i++) {
            //construct a back end vehicle from the front end vehicle
            backEndVehicles.add(new BackEndCar(grid.getGridVehicles().get(i)));
        }
    }

    /**
     * Similair to above, this method will create a clone of the board state from an existing board state.
     * since the board state is represented as an integer a simple equals will allow for a low memory clone
     * (not as low memory use as character but integers are much easier to work with). This method will also create
     * new backend vehicles clones (simplified grid vehicles). This method guarantees to return a backend state representation
     * cloned from an initial state, provided it receives a valid back end state.
     *
     * @param backEndState the state representitive which will be cloned
     */
    public BackEndState(BackEndState backEndState) {
        for (int i = 0; i < GridVariables.BOARD_SIZE; i++) {
            for (int j = 0; j < GridVariables.BOARD_SIZE; j++) {
                this.backendGrid[i][j] = backEndState.getBackendGrid()[i][j];
            }
        }
        for (int i = 0; i < backEndState.getBackEndVehicles().size(); i++)
            this.backEndVehicles.add(new BackEndCar(backEndState.getBackEndVehicles().get(i)));
    }

    /**
     * This method will be used to get the back end representitive state of the board. This method guarantees to return
     * the back end state represented as a 2d array fo integers.
     *
     * @return the 2d matrix representation of the board state
     */
    public int[][] getBackendGrid() {
        return backendGrid;
    }

    /**
     * This method will be used to retreive the list of vehicles represntitive of all the vehicles on the board, in a simplified state.
     * This method guarantees to return the list of back end vehicles.
     *
     * @return the list of back end vehicles representitive of the board state.
     */
    public ArrayList<BackEndCar> getBackEndVehicles() {
        return backEndVehicles;
    }

    /**
     * This method will be used to retrieve the goal row (the winning row where the goal vehicles will be placed). This method
     * guarantees to return a positive integer representitive of the row where the exit is located.
     *
     * @return the row index where the goal vehicle needs to exit
     */
    public int getGoalRow() {
        return goalRow;
    }

    /**
     * This method will be used to retrieve the integer representation of the goal vehicle. This method will guarantee to
     * return the integer representation of the goal vehicle.
     *
     * @return integer representation of the goal car
     */
    public int getGoalCar() {
        return goalCar;
    }

    /**
     * This method will be used to retrieve child states from a current state.
     * <p> This method will move the current selected vehicle in all the possible directions it can move,
     * for a horizontal vehicle, if it can move both left and right, it will create clone states, move the vehicle right
     * and add it to the list of child state. it will then go back to the original state and check if the vehicle can move right.
     * if the vehicle can be moved right a new state will be created with that move, and the state will be added to the list
     * of children state. The same is performed on vertical vehicles which can move both up and down. This is done so ALL
     * possible paths can be expanded, as if this is not done, the visited list will ignore the other path as it can never revisit it.</p>
     * <p> This method will create cloned states using the constructors for each new state that can be created.
     * </p>
     * <p>
     * This method guarantees to return a list of all child states which can be retrieved from moving the vehicle given, in all possible
     * directions the vehicle can be moved in, provided it receives a non null board state, and non null vehicle.
     * </p>
     *
     * @param copy the basis back end state which will be cloned when creating new states
     * @param v    the back end vehicle which will be moved in any direction it can be moved in
     * @return a list containing all states created when moving the vehicle in all directions it can be moved in
     */
    public ArrayList<BackEndState> tryMoveVehicle(BackEndState copy, BackEndCar v) {
        //creating a new arraylist to store all the child states
        ArrayList<BackEndState> states = new ArrayList<>();
        //if the vehicle is horizontal
        //we want to check if it can move both left and right
        //each direction it moves creates a new state and that state is added to the list
        if (v.isHorizontal()) {
            //if the vehicle can move to the right (and is not the goal vehicle)
            if (canRight(v) && !v.isGoalCar()) {
                //create a cloned state from the parent board state.
                BackEndState copyClone = new BackEndState(copy);
                //retrieve the vehicle we are altering in the list of vehicles on clone board, with the same
                //details as our vehicle we are moving v
                BackEndCar vv = clonedCar(copyClone, v);
                //scan through the vehicle size of the vehicle we are shifting right
                for (int i = 0; i < v.getSize(); i++)
                    //deallocate the vehicle in it's current state
                    copyClone.getBackendGrid()[vv.getCol() + i][vv.getRow()] = -1;
                for (int i = 0; i < v.getSize(); i++)
                    //update the board state by shifting its co-ordinates to the right by 1
                    copyClone.getBackendGrid()[vv.getCol() + i + 1][vv.getRow()] = 0;
                //update the vehicle's column (shifting to right -> +1)
                vv.setCol(vv.getCol() + 1);
                //add the updated state into our list of child states
                states.add(copyClone);
            }
            //if the vehicle can move to the left (and is not a goal car)
            if (canLeft(v) && !v.isGoalCar()) {
                //create a clone state from the parent board
                BackEndState copyClone = new BackEndState(copy);
                //retrieves the vehicle in the cloned state vehicle list that has the same
                //details as the given vehicle v
                BackEndCar vv = clonedCar(copyClone, v);
                //scan through the vehicle size
                for (int i = 0; i < vv.getSize(); i++)
                    //update the board state by nulling the current coordinates taken by the vehicle
                    copyClone.getBackendGrid()[vv.getCol() + i][vv.getRow()] = -1;
                for (int i = 0; i < vv.getSize(); i++)
                    //and move the vehicle back by 1, by setting the coordinates shifted back by 1 to 0
                    copyClone.getBackendGrid()[vv.getCol() + i - 1][vv.getRow()] = 0;
                //update the vehicles column
                vv.setCol(vv.getCol() - 1);
                //add the updated state into our list of child states
                states.add(copyClone);
            }
            //if the vehicle is both the goal car and can move to the right
            if (canRight(v) && v.isGoalCar()) {
                //create a clone state from the parent board state
                BackEndState copyClone = new BackEndState(copy);
                //retrieve the vehicle in the cloned state that has the same attributes as vehicle v
                BackEndCar vv = clonedCar(copyClone, v);
                //scan through the vehicle size
                for (int i = 0; i < vv.getSize(); i++)
                    //nullify the current coordinates blocked by the vehicle at the current state
                    copyClone.getBackendGrid()[vv.getCol() + i][vv.getRow()] = -1;
                for (int i = 0; i < vv.getSize(); i++)
                    //move the vehicle to the right by occuyping the old coordinates shift to the right by 1
                    copyClone.getBackendGrid()[vv.getCol() + i + 1][vv.getRow()] = goalCar;
                //update the vehicle column
                vv.setCol(vv.getCol() + 1);
                //add the updated new state into our list of child states
                states.add(copyClone);
            }
            //if the vehicle can move to the lift and is both the goal car
            if (canLeft(v) && v.isGoalCar()) {
                //create a cloned back end state from the current parent state
                BackEndState copyClone = new BackEndState(copy);
                //locate the vehicle in the list which has the same details as v
                BackEndCar vv = clonedCar(copyClone, v);
                //scan through the vehicle size
                for (int i = 0; i < vv.getSize(); i++)
                    //nullify the current coordinated occupied by the current vehicle in the current state
                    copyClone.getBackendGrid()[vv.getCol() + i][vv.getRow()] = -1;
                for (int i = 0; i < vv.getSize(); i++)
                    //update the current coordinates by shifting the vehicle to the left by 1
                    copyClone.getBackendGrid()[vv.getCol() + i - 1][vv.getRow()] = goalCar;
                //update the vehicles column
                vv.setCol(vv.getCol() - 1);
                //add the updated state into our list of child states
                states.add(copyClone);
            }
        }
        //the case where the vehicle is vertical
        else {
            //if the vehicle can move down
            if (canDown(v)) {
                //create a cloned state from the current parent state
                BackEndState copyClone = new BackEndState(copy);
                //retrieve the vehicle in the cloned state with the same attribute as vehicle v
                BackEndCar vv = clonedCar(copyClone, v);
                //scan through the current vehicle size
                for (int i = 0; i < vv.getSize(); i++)
                    //nullify the current coordinates occupied by the vehicle
                    copyClone.getBackendGrid()[vv.getCol()][vv.getRow() + i] = -1;
                for (int i = 0; i < vv.getSize(); i++)
                    //shift the vehicle down by updating the coordinated 1 below the old ones (row + 1)
                    copyClone.getBackendGrid()[vv.getCol()][vv.getRow() + i + 1] = 0;
                //update the vehicles row
                vv.setRow(vv.getRow() + 1);
                //add the updated state into the list of child states
                states.add(copyClone);
            }
            //if the vehicle can move up
            if (canUp(v)) {
                //create a cloned state from the current state
                BackEndState copyClone = new BackEndState(copy);
                //retrieve the vehicle in the cloned state with the same details as vehicle v
                BackEndCar vv = clonedCar(copyClone, v);
                //scan through the current vehicle coordinates
                for (int i = 0; i < vv.getSize(); i++)
                    //nullify the existing coordinates in the cloned state taken by the vehicle
                    copyClone.getBackendGrid()[vv.getCol()][vv.getRow() + i] = -1;
                for (int i = 0; i < vv.getSize(); i++)
                    //occupy the coordinates of the vehicle in the row above (row -1)
                    copyClone.getBackendGrid()[vv.getCol()][vv.getRow() + i - 1] = 0;
                //update the vehicles row
                vv.setRow(vv.getRow() - 1);
                //add the updated state into the list of children states
                states.add(copyClone);
            }
        }
        //return the list of child states created from the original parent state
        return states;
    }

    /**
     * This method will check if the current vehicle can move to the left by 1.
     * <p> A vehicle can move to the left by 1 if the following conditions are met</p>
     * <p> the vehicle is a horizontal vehicle (no vertical vehicles)</p>
     * <p> the vehicle is NOT in column 0 (somewhere between column 1 and 5) as can't move OUTSIDE the board</p>
     * <p> the vehicle in the same row but the column to the left (column - 1) is null (nothing blocking the way in the vehicle to the left)</p>
     * This method will guarantee to return a boolean representing if the vehicle can move to the left by 1 space, provided
     * it receives a non null vehicle
     *
     * @param v the vehicle which is being checked if it can be moved to the left by 1 space
     * @return true if the vehicle can move the left by 1 space, false if otherwise
     */
    public boolean canLeft(BackEndCar v) {
        //if the vehicle is vertical (not horizontal -> vertical)
        if (!v.isHorizontal())
            //return false, as vehicles can only move in their orientation
            return false;
        //if the vehicle is already at the left most position (column 0)
        //where shifting left would move it outside the board
        if (v.getCol() == 0)
            //return false, as vehicles are confined to stay within the board
            return false;
        //if the board state at the coordinates of the vehicles to the left by 1 (column -1)
        //is NOT empty, that means there is a vehicle (obstruction) there
        if (backendGrid[v.getCol() - 1][v.getRow()] != -1)
            //return false, as there is a vehicle to the left by 1
            return false;
        //otherwise if all other checks have been passed return true
        return true;
    }

    /**
     * This method will check if the current vehicle can move to the right by 1.
     * <p> A vehicle can move to the right by 1 if the following conditions are met</p>
     * <p> the vehicle is a horizontal vehicle (no vertical vehicles)</p>
     * <p> the vehicle is NOT in a column where shifting it to the right by 1 would move it outside the board
     * this happens when the vehicle column is at the board size - vehicle size (shifting to the right would be outside th eboard range</p>
     * <p> the vehicle in the same row but the column to the right (column + 1) is null (nothing blocking the way in the vehicle to the left)</p>
     * This method will guarantee to return a boolean representing if the vehicle can move to the right by 1 space, provided
     * it receives a non null vehicle
     *
     * @param v the vehicle which is being checked if it can be moved to the right by 1 space
     * @return true if the vehicle can move the right by 1 space, false if otherwise
     */
    public boolean canRight(BackEndCar v) {
        //if the vehicle is vertical (not horizontal -> vertical)
        if (!v.isHorizontal())
            //return false as vehicles can only move in the direction of their orientation
            return false;
        //if the end of the vehicle is already at the right most column
        if (v.getCol() + v.getSize() >= GridVariables.BOARD_SIZE)
            //return false, as vehicles can not move out of grid bounds
            return false;
        //if the board states at the coordinates of the vehicle, to the right by 1 (column +1)
        //is NOT NULL, that means there is a vehicle in the way
        if (backendGrid[v.getCol() + v.getSize()][v.getRow()] != -1)
            //return false, as vehicles cannot make illegal moves
            return false;
        //if all checks have been made, return true/ the vehicle can move to the right
        return true;
    }

    /**
     * This method will check if the current vehicle can move up by 1.
     * <p> A vehicle can move up by 1 if the following conditions are met</p>
     * <p> the vehicle is a vertical vehicle (no horizontal vehicles)</p>
     * <p> the vehicle is NOT in a row where shifting it up by 1 would move it outside the board
     * this happens when the vehicle row is at 0 (shifting it up would be outside th eboard range</p>
     * <p> the vehicle in the same column but the row is moved up (row - 1) is null (nothing blocking the way in the vehicle above)</p>
     * This method will guarantee to return a boolean representing if the vehicle can move up by 1 space, provided
     * it receives a non null vehicle
     *
     * @param v the vehicle which is being checked if it can be moved up by 1 space
     * @return true if the vehicle can move up by 1 space, false if otherwise
     */
    public boolean canUp(BackEndCar v) {
        //if the vehicle is horizontal
        if (v.isHorizontal())
            //return false, as vehicles can only move in the direction of their orientation
            return false;
        //if the vehicle is in row 0, at the up most position possible
        //where moving it up would move it outside the board
        if (v.getRow() == 0)
            //return false, as vehicles can not move outside the board range
            return false;
        //if the board states at the coordinates of the vehicle, above by 1 (row - 1) (moving up goes back in the row)
        //is NOT NULL, that means there is a vehicle in the way
        if (backendGrid[v.getCol()][v.getRow() - 1] != -1)
            //return false as we cannot move vehicles into other vehicles
            return false;
        //if all checks have been passed, return true (vehicle can move up)
        return true;
    }

    /**
     * This method will check if the current vehicle can move down by 1.
     * <p> A vehicle can move down by 1 if the following conditions are met</p>
     * <p> the vehicle is a vertical vehicle (no horizontal vehicles)</p>
     * <p> the vehicle is NOT in a row where shifting it down by 1 would move it outside the board
     * this happens when the vehicle row is at the board size - vehicle size (shifting it down would be outside th eboard range</p>
     * <p> the vehicle in the same column but the row is moved down (row + 1) is null (nothing blocking the way in the vehicle above)</p>
     * This method will guarantee to return a boolean representing if the vehicle can move down by 1 space, provided
     * it receives a non null vehicle
     *
     * @param v the vehicle which is being checked if it can be moved down by 1 space
     * @return true if the vehicle can move down by 1 space, false if otherwise
     */
    public boolean canDown(BackEndCar v) {
        //if the vehicle is horizontal
        if (v.isHorizontal())
            //return false, as vehicles can only move in the direction of their orientation
            return false;
        //if the vehicle is in row (boardsize - vehicle size) that means the end of the vehicle is at the bottom most
        //row spot. moving the vehicle down would move it outside the board space
        if (v.getRow() + v.getSize() >= GridVariables.BOARD_SIZE)
            //return false, as vehicles can only move within the board itself
            return false;
        //if the board states at the coordinate of the vehicle below by 1 (row + 1) is NOT NULL, that means
        //there is a vehicle where it is attempting to move
        if (backendGrid[v.getCol()][v.getRow() + v.getSize()] != -1)
            //return false, as vehicles can not move into other vehicles
            return false;
        //if all checks have been passed, return true (vehicle can move down)
        return true;
    }

    /**
     * This method will be used to represent the board state visually based on the current vehicle layout on the board.
     * <p> The method will print the numeric representation in a object form of the following</p>
     * <p> empty spaces are assigned with N representing Null space</p>
     * <p> vehicles which are obstructions (not the goal car) are assigned with O representing object car</p>
     * <p> the goal car will be represented with a G representing goal </p>
     * <p> please note that the output will be row column form but work will be done in column - row form</p>
     * This method will guarantee to return a string representing the board state.
     *
     * @return string representing the current board state
     */
    @Override
    public String toString() {
        //create a new string
        String s = new String();
        //scan through entire board size
        for (int i = 0; i < GridVariables.BOARD_SIZE; i++) {
            //each new row add a container bracket
            s += "[";
            for (int j = 0; j < GridVariables.BOARD_SIZE; j++) {
                //if the space in the grid is empty -> add N with space to string
                if (-1 == backendGrid[j][i])
                    s += "N ";
                    //otherwise if there is a vehicle
                else {
                    //if the vehicle is the goal car
                    if (backendGrid[j][i] == goalCar)
                        //add G with space to string
                        s += "G ";
                    else
                        //otherwise it is an obstruction vehicle and add an O with a space
                        s += "O ";
                }
            }
            //at the end of the row close the container and have new line
            s += "]\n";
        }
        //return the string representing the board state as a string matrix
        return s;
    }

    /**
     * This method will be used to check if two board states are equivalent. this occurs on 2 conditions
     * <p> the first condition is that all grid elements are equivalent, this is check 1</p>
     * <p> the second condition is that all vehicles on the grid are the exact same</p>
     * upon meeting this this method guarantees to return a true false expression, representing if the two
     * states are equivalent, provided it receives a non null object
     *
     * @param o the object we are checking if it is an equivalent state
     * @return true if the object has the same state as the current state / false otherwisde
     */
    @Override
    public boolean equals(Object o) {
        //if the object is itself return true
        if (this == o) return true;
        //if the object is not a state return false
        if (!(o instanceof BackEndState)) return false;
        //cast the object to back end state
        BackEndState that = (BackEndState) o;
        //first check to see if the board has same values
        boolean check = true;
        //scan through both the boards
        for (int i = 0; i < GridVariables.BOARD_SIZE; i++) {
            for (int j = 0; j < GridVariables.BOARD_SIZE; j++) {
                //if the board value at the index are different
                if (that.getBackendGrid()[i][j] != backendGrid[i][j]) {
                    //set flag == false
                    check = false;
                    //stop scanning
                    break;
                }
            }
        }
        //second check to see if the board has the same vehicles
        boolean check2 = true;
        //scan through the list of vehicles
        for (int i = 0; i < getBackEndVehicles().size(); i++) {
            //if the vehicles at the same index are not equal
            if (!getBackEndVehicles().get(i).equals(that.getBackEndVehicles().get(i))) {
                //set the flag == false
                check = false;
                //stop scanning
                break;
            }
        }
        //return all conditions are true
        return getGoalRow() == that.getGoalRow() &&
                numberOfMoves == that.numberOfMoves &&
                getGoalCar() == that.getGoalCar() &&
                check && check2 &&
                Objects.equals(getBackEndVehicles(), that.getBackEndVehicles());
    }

    /**
     * This method will return the vehicle in the vehicle list at the current state, so that the vehicle
     * updated is the vehicle in the state rather than the throwaway vehicle. this will make sure the states
     * are fully updated, as updating the correct vehicle is the only way this search works.
     *
     * <p> this method guarantees to return the vehicle in the state with the same vehicle details provided it receives
     *     a non null back end vehicle</p>
     * @param bes the current state
     * @param bec the vehicle we are trying to find in the state
     * @return the vehicle in the current state with the same attributes as the vehicle we are searching for
     */
    public BackEndCar clonedCar(BackEndState bes, BackEndCar bec) {
        //scan through the list of back end vehicles in the current state
        for (int i = 0; i < bes.getBackEndVehicles().size(); i++) {
            //if the vehicle is equivalent to the one in the list
            if (bec.equals(bes.getBackEndVehicles().get(i)))
                //return that vehicle so we can update the board state
                return bes.getBackEndVehicles().get(i);
        }
        //if search unsuccessful return original car (avoid null objects)
        return bec;
    }
}
