import javafx.scene.Group;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;

public class BFS {
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
        Random generator = new Random();
        Group tempVehicles = new Group();
        int count = 0;
        //to avoid huge loops
        int fails = 0;
        tempVehicles.getChildren().add(goalCar);
        goalCar.initialShift();
        //we want to place random vehicles on the grid
        while (count < numberOfVehicles) {
            if (fails > 100) {
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
            //if the vehicle placed was valid add to both grids
            if (grid.validPlacement(v, row, col, gv) && (!((v.isHorizontal()) && gv.getRow() == grid.getGoalRow() && gv.getCol() > goalCar.getCol()))) {
                vehicles.getChildren().add(gv);
                gv.initialShift();
                tempVehicles.getChildren().add(gv);
                count++;
            }
            fails++;
        }

        BackEndState initalState = new BackEndState(grid);

        boolean check = BFSearch(initalState);
        vehicles.getChildren().clear();
        vehicles.getChildren().addAll(tempVehicles.getChildren());
        return check;
    }


    public boolean BFSearch(BackEndState initialState) {

        Comparator searchNodeComparator = new nodeComparator();
        PriorityQueue<searchNode> open = new PriorityQueue<>(searchNodeComparator);
        ArrayList<BackEndState> visited = new ArrayList<>();
        searchNode initialNode = new searchNode(initialState,null);
        initialNode.setScore();
        open.add(initialNode);
        int count = 0;
        while(!open.isEmpty()){
            searchNode curr = open.poll();
            if(visited.contains(curr.getCurrent()))
                continue;
            if(curr.solved())
                return true;
            visited.add(curr.getCurrent());
            count++;
            for(BackEndCar bac: curr.getCurrent().getBackEndVehicles()){
                BackEndState copy = new BackEndState(curr.getCurrent());
                copy.tryMoveVehicle(bac);
                searchNode add = new searchNode(copy,curr);
                add.setScore();
                if(visited.contains(add.getCurrent()))
                    continue;
                open.add(add);
            }
                System.out.println(count);
        }
        return false;
    }


}
