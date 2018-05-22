import javafx.scene.Group;

public class threadState {
    private Group vehicles;
    private Grid grid;
    private int estimatedMoves;

    public threadState(Group vehicles, Grid grid) {
        this.vehicles = vehicles;
        this.grid = grid;
        this.estimatedMoves = estimatedMoves;
    }

    public Group getVehicles() {
        return vehicles;
    }

    public Grid retGrid() {
        return grid;
    }

    public int getEstimatedMoves() {
        return estimatedMoves;
    }

    public void setEstimatedMoves(int estimatedMoves) {
        this.estimatedMoves = estimatedMoves;
    }
}
