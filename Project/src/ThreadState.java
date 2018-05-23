import javafx.scene.Group;

/**
 * This class is used as a container for holding the a state created from each thread.
 * to allow access to the queue states we need
 * 1. a grid
 * 2. a list of vehicles on that grid
 * 3. the estimated number of moves to solve the board state.
 */
public class ThreadState {
    //the list of vehicles in the state
    private Group vehicles;
    //the grid representing the current state
    private Grid grid;
    //the estimated number of moves to complete the state
    private int estimatedMoves;

    /**
     * This method will be used to create a new thread state which is a current board state.
     * This method guarantees to return a state representable container provided it receives
     * the information it needs for the state, a non null list of vehicles and non null board
     *
     * @param grid the board assosciated with the state
     */
    public ThreadState(Grid grid) {
        //set the list of vehicles for the class
        this.vehicles = new Group();
        //set the board for the class
        this.grid = grid;
    }

    /**
     * This method will be used to retrieve the list of vehicles assosciated with
     * the current state. This method guarantees to return a list of vehicles
     * assosciated with the state.
     *
     * @return the list of vehicles on the board state
     */
    public Group getVehicles() {
        vehicles.getChildren().clear();
        for (GridVehicle gv : grid.getGridVehicles()) {
            if (vehicles.getChildren().contains(gv)) {
                continue;
            }
            vehicles.getChildren().add(gv);
        }
        return vehicles;
    }

    /**
     * This method will be used to return the board assosciated with the current state.
     * This method guarantees to return the board in the current state. (getgrid was taken)
     *
     * @return the board in the current state
     */
    public Grid retGrid() {
        return grid;
    }

    /**
     * This method will retrieve the estimated number of moves to complete the current
     * board state. This method will guarantee to return the number of moves to finish the
     * board state.
     *
     * @return the estimated moves required to complete the board
     */
    public int getEstimatedMoves() {
        return estimatedMoves;
    }

    /**
     * This method will be used to set the number of estimated moves to complete the state.
     * This method guarantees to update the field giving the number of estimated moves, provided it
     * receives a valid non negative integer
     *
     * @param estimatedMoves the estimated number of moves to solve the board
     */
    public void setEstimatedMoves(int estimatedMoves) {
        this.estimatedMoves = estimatedMoves;
    }
}
