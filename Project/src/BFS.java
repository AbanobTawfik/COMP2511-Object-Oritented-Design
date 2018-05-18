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
//        Random generator = new Random();
//        Group tempVehicles = new Group();
//        int count = 0;
//        //to avoid huge loops
//        int fails = 0;
//        tempVehicles.getChildren().add(goalCar);
//        goalCar.initialShift();
//        //we want to place random vehicles on the grid
//        while (count < numberOfVehicles) {
//            if (fails > 1000) {
//                count = 0;
//                grid.resetNewGrid();
//                vehicles.getChildren().clear();
//                tempVehicles.getChildren().clear();
//                vehicles.getChildren().add(goalCar);
//                tempVehicles.getChildren().add(goalCar);
//                goalCar.initialShift();
//                numberOfVehicles = generator.nextInt(17) + 4;
//                fails = 0;
//            }
//            Vehicle v = new Vehicle(generator.nextBoolean(), generator.nextInt(2) + 2);
//            int row = generator.nextInt(7 - v.getSize());
//            int col = generator.nextInt(7 - v.getSize());
//            GridVehicle gv = new GridVehicle(false, v, row, col, grid);
//            //if the vehicle placed was valid add to both grids
//            if (grid.validPlacement(v, row, col, gv) && (!((v.isHorizontal()) && gv.getRow() == grid.getGoalRow() && gv.getCol() > goalCar.getCol()))) {
//                vehicles.getChildren().add(gv);
//                gv.initialShift();
//                tempVehicles.getChildren().add(gv);
//                count++;
//            }
//            fails++;
//        }
        Grid someotherGrid = new Grid();

        Vehicle horizontal2 = new Vehicle(true, 2);
        Vehicle horizontal3 = new Vehicle(true,3);
        Vehicle vertical2 = new Vehicle(false, 2);
        Vehicle vertical3 = new Vehicle(false, 3);

        GridVehicle v1 = new GridVehicle(true,horizontal2,2,3,someotherGrid);
        v1.initialShift();
        vehicles.getChildren().add(v1);
        GridVehicle v2 = new GridVehicle(false, horizontal3,3,0,someotherGrid);
        v2.initialShift();
        vehicles.getChildren().add(v2);
        GridVehicle v3 = new GridVehicle(false, horizontal2,5,3,someotherGrid);
        v3.initialShift();
        vehicles.getChildren().add(v3);
        GridVehicle v4 = new GridVehicle(false, horizontal2,4,4,someotherGrid);
        v4.initialShift();
        vehicles.getChildren().add(v4);
        GridVehicle v5 = new GridVehicle(false, horizontal2,5,0,someotherGrid);
        v5.initialShift();
        vehicles.getChildren().add(v5);
        GridVehicle v6 = new GridVehicle(false, horizontal2,0,1,someotherGrid);
        v6.initialShift();
        vehicles.getChildren().add(v6);
        GridVehicle v7 = new GridVehicle(false, vertical3,1,5,someotherGrid);
        v7.initialShift();
        vehicles.getChildren().add(v7);
        GridVehicle v8 = new GridVehicle(false, vertical3,0,0,someotherGrid);
        v8.initialShift();
        vehicles.getChildren().add(v8);
        GridVehicle v9 = new GridVehicle(false, vertical2,1,1,someotherGrid);
        v9.initialShift();
        vehicles.getChildren().add(v9);
        GridVehicle v10 = new GridVehicle(false, vertical2,1,2,someotherGrid);
        v10.initialShift();
        vehicles.getChildren().add(v10);
        GridVehicle v11 = new GridVehicle(false, vertical2,0,4,someotherGrid);
        v11.initialShift();
        vehicles.getChildren().add(v11);
        GridVehicle v12 = new GridVehicle(false, vertical2,4,2,someotherGrid);
        v12.initialShift();
        vehicles.getChildren().add(v12);
        GridVehicle v13 = new GridVehicle(false, vertical2,3,3,someotherGrid);
        v13.initialShift();
        vehicles.getChildren().add(v13);
        BackEndState initalState = new BackEndState(someotherGrid);


        boolean check = BFSearch(initalState);
        //vehicles.getChildren().clear();
        //vehicles.getChildren().addAll(tempVehicles.getChildren());
        return check;
    }


    public boolean BFSearch(BackEndState initialState) {

        Comparator searchNodeComparator = new nodeComparator();
        PriorityQueue<searchNode> open = new PriorityQueue<>(searchNodeComparator);
        ArrayList<BackEndState> visited = new ArrayList<>();
        searchNode initialNode = new searchNode(initialState, 0,null);
        initialNode.setScore();
        open.add(initialNode);
        int count = 0;
        while(!open.isEmpty()){
            searchNode curr = open.poll();
            if(visited.contains(curr.getCurrent()))
                continue;
            if(curr.solved()) {
                System.out.println(curr.getPreviousStates());
                return true;
            }
            visited.add(curr.getCurrent());
            count++;
            for(BackEndCar bac: curr.getCurrent().getBackEndVehicles()){
                BackEndState copy = new BackEndState(curr.getCurrent());
                BackEndCar bec = clonedCars(bac, copy);
                ArrayList<BackEndState> states = copy.tryMoveVehicle(copy,bec);

                if(states.isEmpty())
                    continue;
                else{
                    for(int i = 0; i < states.size(); i++){
                        searchNode add = new searchNode(states.get(i),curr.getPreviousStates() +1, curr);
                        if(add.solved()) {
                            System.out.println(add.getPreviousStates());
                            return true;
                        }
                        add.setScore();
                        if(!visited.contains(add)){
                            open.add(add);
                        }
                    }
                }

            }
        }
        return false;
    }

    public BackEndCar clonedCars(BackEndCar bec, BackEndState bes){
        for(int i = 0; i < bes.getBackEndVehicles().size(); i++){
            if(bec.equals(bes.getBackEndVehicles().get(i)))
                return bes.getBackEndVehicles().get(i);
        }
        return bec;
    }
}
