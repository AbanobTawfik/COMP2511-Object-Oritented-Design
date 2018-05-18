import java.util.ArrayList;
import java.util.Objects;

/**
 * <p>
 * This class will be representitive of the back end board state simplfiied to just integers
 * -1 is representitive of empty space on the board
 *  0 is representitive of a obstacle (non goal) vehicle on the board
 * 36 is representitive of the goal vehicle on the board
 * </p>
 * <p>
 * this is a much more compact way of storing and making multiple states as attempting to make deep copeis
 * of grid vehicles and grid causes a recursive memory issue, since grid vehicles use a grid which also uses grid vehicles
 * causing a nested problem and making a huge amount of objects.
 * </p>
 *
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
     * Instantiates a new Back end state.
     *
     * @param grid the grid
     */
    public BackEndState(Grid grid) {
        for (int i = 0; i < GridVariables.BOARD_SIZE; i++) {
            for (int j = 0; j < GridVariables.BOARD_SIZE; j++) {
                if (null == grid.getGrid()[i][j]) {
                    backendGrid[i][j] = -1;
                    continue;
                } else {
                    if (grid.getGrid()[i][j].isGoalCar()) {
                        //since goal car WILL ALWAYS BE HORIZONTAL
                        for (int k = 0; k < grid.getGrid()[i][j].getVehicle().getSize(); k++) {
                            backendGrid[grid.getGrid()[i][j].getCol() + k][grid.getGrid()[i][j].getRow()] = goalCar;
                        }
                        continue;
                    } else {
                        if (grid.getGrid()[i][j].getVehicle().isHorizontal()) {
                            for (int k = 0; k < grid.getGrid()[i][j].getVehicle().getSize(); k++) {
                                backendGrid[grid.getGrid()[i][j].getCol()+k][grid.getGrid()[i][j].getRow()] = 0;
                            }
                            continue;
                        }
                        if (!grid.getGrid()[i][j].getVehicle().isHorizontal()) {
                            for (int k = 0; k < grid.getGrid()[i][j].getVehicle().getSize(); k++) {
                                backendGrid[grid.getGrid()[i][j].getCol()][grid.getGrid()[i][j].getRow() + k] = 0;
                            }
                            continue;
                        }
                    }
                }
            }
        }
        for(int i = 0; i < grid.getGridVehicles().size(); i++){
            backEndVehicles.add(new BackEndCar(grid.getGridVehicles().get(i)));
        }
    }

    /**
     * Instantiates a new Back end state.
     *
     * @param backEndState the back end state
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
     * Get backend grid int [ ] [ ].
     *
     * @return the int [ ] [ ]
     */
    public int[][] getBackendGrid() {
        return backendGrid;
    }

    /**
     * Gets back end vehicles.
     *
     * @return the back end vehicles
     */
    public ArrayList<BackEndCar> getBackEndVehicles() {
        return backEndVehicles;
    }

    /**
     * Gets goal row.
     *
     * @return the goal row
     */
    public int getGoalRow() {
        return goalRow;
    }

    /**
     * Gets goal car.
     *
     * @return the goal car
     */
    public int getGoalCar() {
        return goalCar;
    }

    /**
     * Try move vehicle.
     *
     * @param v the v
     */
    public void tryMoveVehicle(BackEndCar v) {
        if (v.isHorizontal()) {
            if (canRight(v) && !v.isGoalCar()) {
                for (int i = 0; i < v.getSize(); i++)
                    backendGrid[v.getCol() + i][v.getRow()] = -1;
                for (int i = 0; i < v.getSize(); i++)
                    backendGrid[v.getCol() + i + 1][v.getRow()] = 0;
                v.setCol(v.getCol() + 1);
                return;
            }
            if (canLeft(v) && !v.isGoalCar()) {
                for (int i = 0; i < v.getSize(); i++)
                    backendGrid[v.getCol() + i][v.getRow()] = -1;
                for (int i = 0; i < v.getSize(); i++)
                    backendGrid[v.getCol() + i - 1][v.getRow()] = 0;
                v.setCol(v.getCol() - 1);
                return;
            }
            if (canRight(v) && v.isGoalCar()) {
                for (int i = 0; i < v.getSize(); i++)
                    backendGrid[v.getCol() + i][v.getRow()] = -1;
                for (int i = 0; i < v.getSize(); i++)
                    backendGrid[v.getCol() + i + 1][v.getRow()] = goalCar;
                v.setCol(v.getCol() + 1);
                return;
            }
            if (canLeft(v) && v.isGoalCar()) {
                for (int i = 0; i < v.getSize(); i++)
                    backendGrid[v.getCol() + i][v.getRow()] = -1;
                for (int i = 0; i < v.getSize(); i++)
                    backendGrid[v.getCol() + i - 1][v.getRow()] = goalCar;
                v.setCol(v.getCol() - 1);
                return;
            }
        } else {
            if (canDown(v)) {
                for (int i = 0; i < v.getSize(); i++)
                    backendGrid[v.getCol()][v.getRow() + i] = -1;
                for (int i = 0; i < v.getSize(); i++)
                    backendGrid[v.getCol()][v.getRow() + i + 1] = 0;
                v.setRow(v.getRow() + 1);
                return;
            }
            if (canUp(v)) {
                for (int i = 0; i < v.getSize(); i++)
                    backendGrid[v.getCol()][v.getRow() + i] = -1;
                for (int i = 0; i < v.getSize(); i++)
                    backendGrid[v.getCol()][v.getRow() + i - 1] = 0;
                v.setRow(v.getRow() - 1);
                return;
            }
        }
        return;
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
                    if(backendGrid[j][i] == goalCar)
                        s+= "G, ";
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
        for(int i = 0; i < GridVariables.BOARD_SIZE; i++) {
            for (int j = 0; j < GridVariables.BOARD_SIZE; j++) {
                if (that.getBackendGrid()[i][j] != backendGrid[i][j]) {
                    check = false;
                    break;
                }
            }
        }
        boolean check2 = true;
        for(int i = 0; i < getBackEndVehicles().size();i++) {
            if (!getBackEndVehicles().get(i).equals(that.getBackEndVehicles().get(i))){
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
}
