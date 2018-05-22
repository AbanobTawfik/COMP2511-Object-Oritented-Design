import javafx.scene.Group;

/**
 * The type Thread state.
 */
public class ThreadState {
    private Group vehicles;
    private Grid grid;
    private int estimatedMoves;

    /**
     * Instantiates a new Thread state.
     *
     * @param vehicles the vehicles
     * @param grid     the grid
     */
    public ThreadState(Group vehicles, Grid grid) {
        this.vehicles = vehicles;
        this.grid = grid;
        this.estimatedMoves = estimatedMoves;
    }

    /**
     * Gets vehicles.
     *
     * @return the vehicles
     */
    public Group getVehicles() {
        return vehicles;
    }

    /**
     * Ret grid grid.
     *
     * @return the grid
     */
    public Grid retGrid() {
        return grid;
    }

    /**
     * Gets estimated moves.
     *
     * @return the estimated moves
     */
    public int getEstimatedMoves() {
        return estimatedMoves;
    }

    /**
     * Sets estimated moves.
     *
     * @param estimatedMoves the estimated moves
     */
    public void setEstimatedMoves(int estimatedMoves) {
        this.estimatedMoves = estimatedMoves;
    }
}
