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
        if (v.isHorizontal()) {
            // if the vehicle can move to the right (and is not the goal vehicle)
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

            if (canLeft(v) && !v.isGoalCar()) {
                BackEndState copyClone = new BackEndState(copy);
                BackEndCar vv = clonedCar(copyClone, v);
                for (int i = 0; i < vv.getSize(); i++)
                    copyClone.getBackendGrid()[vv.getCol() + i][vv.getRow()] = -1;
                for (int i = 0; i < vv.getSize(); i++)
                    copyClone.getBackendGrid()[vv.getCol() + i - 1][vv.getRow()] = 0;
                vv.setCol(vv.getCol() - 1);
                states.add(copyClone);
            }
            if (canRight(v) && v.isGoalCar()) {
                BackEndState copyClone = new BackEndState(copy);
                BackEndCar vv = clonedCar(copyClone, v);
                for (int i = 0; i < vv.getSize(); i++)
                    copyClone.getBackendGrid()[vv.getCol() + i][vv.getRow()] = -1;
                for (int i = 0; i < vv.getSize(); i++)
                    copyClone.getBackendGrid()[vv.getCol() + i + 1][vv.getRow()] = goalCar;
                vv.setCol(vv.getCol() + 1);
                states.add(copyClone);
            }
            if (canLeft(v) && v.isGoalCar()) {
                BackEndState copyClone = new BackEndState(copy);
                BackEndCar vv = clonedCar(copyClone, v);
                for (int i = 0; i < vv.getSize(); i++)
                    copyClone.getBackendGrid()[vv.getCol() + i][vv.getRow()] = -1;
                for (int i = 0; i < vv.getSize(); i++)
                    copyClone.getBackendGrid()[vv.getCol() + i - 1][vv.getRow()] = goalCar;
                vv.setCol(vv.getCol() - 1);
                states.add(copyClone);
            }
        } else {
            if (canDown(v)) {
                BackEndState copyClone = new BackEndState(copy);
                BackEndCar vv = clonedCar(copyClone, v);
                for (int i = 0; i < vv.getSize(); i++)
                    copyClone.getBackendGrid()[vv.getCol()][vv.getRow() + i] = -1;
                for (int i = 0; i < vv.getSize(); i++)
                    copyClone.getBackendGrid()[vv.getCol()][vv.getRow() + i + 1] = 0;
                vv.setRow(vv.getRow() + 1);
                states.add(copyClone);
            }
            if (canUp(v)) {
                BackEndState copyClone = new BackEndState(copy);
                BackEndCar vv = clonedCar(copyClone, v);
                for (int i = 0; i < vv.getSize(); i++)
                    copyClone.getBackendGrid()[vv.getCol()][vv.getRow() + i] = -1;
                for (int i = 0; i < vv.getSize(); i++)
                    copyClone.getBackendGrid()[vv.getCol()][vv.getRow() + i - 1] = 0;
                vv.setRow(vv.getRow() - 1);
                states.add(copyClone);
            }
        }
        return states;
    }

    /**
     * Can left boolean.
     *
     * @param v the v
     * @return the boolean
     */
    public boolean canLeft(BackEndCar v) {
        if (!v.isHorizontal())
            return false;
        if (v.getCol() == 0)
            return false;
        if (backendGrid[v.getCol() - 1][v.getRow()] != -1)
            return false;
        return true;
    }

    /**
     * Can right boolean.
     *
     * @param v the v
     * @return the boolean
     */
    public boolean canRight(BackEndCar v) {
        if (!v.isHorizontal())
            return false;
        if (v.getCol() + v.getSize() >= GridVariables.BOARD_SIZE)
            return false;
        if (backendGrid[v.getCol() + v.getSize()][v.getRow()] != -1)
            return false;
        return true;
    }

    /**
     * Can up boolean.
     *
     * @param v the v
     * @return the boolean
     */
    public boolean canUp(BackEndCar v) {
        if (v.isHorizontal())
            return false;
        if (v.getRow() == 0)
            return false;
        if (backendGrid[v.getCol()][v.getRow() - 1] != -1)
            return false;
        return true;
    }

    /**
     * Can down boolean.
     *
     * @param v the v
     * @return the boolean
     */
    public boolean canDown(BackEndCar v) {
        if (v.isHorizontal())
            return false;
        if (v.getRow() + v.getSize() >= GridVariables.BOARD_SIZE)
            return false;
        if (backendGrid[v.getCol()][v.getRow() + v.getSize()] != -1)
            return false;
        return true;
    }

    @Override
    public String toString() {
        String s = new String();
        for (int i = 0; i < GridVariables.BOARD_SIZE; i++) {
            s += "[";
            for (int j = 0; j < GridVariables.BOARD_SIZE; j++) {
                if (-1 == backendGrid[j][i])
                    s += "N,";
                else {
                    if (backendGrid[j][i] == goalCar)
                        s += "G, ";
                    else
                        s += "O, ";
                }
            }
            s += "]\n";
        }
        return s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BackEndState)) return false;
        BackEndState that = (BackEndState) o;
        boolean check = true;
        for (int i = 0; i < GridVariables.BOARD_SIZE; i++) {
            for (int j = 0; j < GridVariables.BOARD_SIZE; j++) {
                if (that.getBackendGrid()[i][j] != backendGrid[i][j]) {
                    check = false;
                    break;
                }
            }
        }
        boolean check2 = true;
        for (int i = 0; i < getBackEndVehicles().size(); i++) {
            if (!getBackEndVehicles().get(i).equals(that.getBackEndVehicles().get(i))) {
                check = false;
                break;
            }
        }
        return getGoalRow() == that.getGoalRow() &&
                numberOfMoves == that.numberOfMoves &&
                getGoalCar() == that.getGoalCar() &&
                check && check2 &&
                Objects.equals(getBackEndVehicles(), that.getBackEndVehicles());
    }

    /**
     * Cloned car back end car.
     *
     * @param bes the bes
     * @param bec the bec
     * @return the back end car
     */
    public BackEndCar clonedCar(BackEndState bes, BackEndCar bec) {
        for (int i = 0; i < bes.getBackEndVehicles().size(); i++) {
            if (bec.equals(bes.getBackEndVehicles().get(i)))
                return bes.getBackEndVehicles().get(i);
        }
        return bec;
    }
}
