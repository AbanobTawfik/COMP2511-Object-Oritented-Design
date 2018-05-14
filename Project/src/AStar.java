import javafx.scene.Group;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;

public class AStar {
    private Difficulty difficulty;
    private int numberOfVehicles;
    Grid grid;
    Group vehicles;
    GridVehicle goalCar;

    public boolean Search(Difficulty difficulty, int numberOfVehicles, Grid grid, Group vehicles, GridVehicle goalCar) {
        this.difficulty = difficulty;
        this.numberOfVehicles = numberOfVehicles;
        this.grid = grid;
        this.vehicles = vehicles;
        this.goalCar = goalCar;
        Grid copy = new Grid();
        Random generator = new Random();
        Group tempVehicles = new Group();
        int count = 0;
        //to avoid huge loops
        int fails = 0;
        tempVehicles.getChildren().add(goalCar);
        goalCar.initialShift();
        //we want to place random vehicles on the grid
        while (count < numberOfVehicles) {
            if (fails > 25) {
                count = 0;
                grid.resetNewGrid();
                vehicles.getChildren().clear();
                tempVehicles.getChildren().clear();
                vehicles.getChildren().add(goalCar);
                tempVehicles.getChildren().add(goalCar);
                goalCar.initialShift();
                numberOfVehicles = generator.nextInt(17) + 4;
                fails = 0;
            }
            Vehicle v = new Vehicle(generator.nextBoolean(), generator.nextInt(2) + 2);
            int row = generator.nextInt(7 - v.getSize());
            int col = generator.nextInt(7 - v.getSize());
            GridVehicle gv = new GridVehicle(false, v, row, col, grid);
            GridVehicle v2 = new GridVehicle(false, v, row, col, copy);
            //if the vehicle placed was valid add to both grids
            if (grid.validPlacement(v, row, col, gv) && (!((v.isHorizontal()) && gv.getRow() == grid.getGoalRow() && gv.getCol() > goalCar.getCol()))) {

                vehicles.getChildren().add(gv);
                gv.initialShift();
                v2.initialShift();
                tempVehicles.getChildren().add(gv);
                count++;
            }
            fails++;
        }

        boolean check = solveAble(copy);
        vehicles.getChildren().clear();
        vehicles.getChildren().addAll(tempVehicles.getChildren());
        return check;
    }


    public boolean solveAble(Grid grid) {
        GridComparator gridComparator = new GridComparator();
        PriorityQueue<Grid> open = new PriorityQueue<>(gridComparator);
        ArrayList<Grid> closed = new ArrayList<>();
        grid.setGridScore(gridScoreSystem(grid));
        open.add(grid);
        closed.add(grid);
        //A*
        int count = 0;
        while (!open.isEmpty()) {
            Grid current = open.poll();
            closed.add(current);
            count++;
            if (current.solved(goalCar))
                return true;

            for (GridVehicle gv : current.getGridVehicles()) {
                Grid newState = null;
                try {
                    newState = grid.makeCopy(grid);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (null == newState)
                    continue;
                newState.tryMoveVehicle(gv);
                if (closed.contains(newState))
                    continue;
                open.add(newState);
                closed.add(newState);

            }
            //to stop checking way too many
            if (count > 2000) {
                return true;
            }
            //System.out.println("Counter - " + count);
        }

        grid.resetNewGrid();

        return false;
    }

    public int gridScoreSystem(Grid grid) {
        int score = 0;

        return score;
    }

}
