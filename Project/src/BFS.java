import javafx.scene.Group;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * This class will be used as the solver for the board combined with board generation.
 * This class will be responsible for creating a state with a certain amount of vehicles,
 * and then solving the state in the lowest number of moves possible (single moves 1 space moves)
 * if the state is not solvable then a new state will be generated till a valid state can be played
 */
public class BFS {
    //this is the difficulty of the current state
    private Difficulty difficulty;
    //the number of vehicles on the grid
    private int numberOfVehicles;
    /**
     * The front end grid to update provided the state created is valid.
     * this will be reset if invalid and cleared
     */
    Grid grid;
    /**
     * The Vehicles for the front end (grid vehicles), these will be updated provided
     * the state is valid, this will be reset if invalid and cleared
     */
    Group vehicles;
    /**
     * The vehicle which represents goal state. this will remain the same and readded upon clears.
     */
    GridVehicle goalCar;

    /**
     * This method will attempt to create a state from a given number of vehicles. and also attempt
     * to solve the state to see if the given state has a possible solution, we don't want to create
     * impossible states for our user. This method will attempt to make a valid configuration and
     * upon creation will attempt the breadth first search to solve it (keeps track of all the parent states)
     * and this method guarantees to return true if the created configuration is solveable and false otherwise
     * provided it receives, a valid difficulty, a positive integer displaying number of vehicles on state,
     * the front end grid which will be created, the group of vehicles for the front end grid and non null goal car
     *
     * @param difficulty       the difficulty of the board being created
     * @param numberOfVehicles the number of vehicles to be added to the board (changes)
     * @param grid             the grid which will be the game state created
     * @param vehicles         the vehicles which will be the group of grid vehicles being created
     * @param goalCar          the goal car which is the car required to reach goal state
     * @return true if the created state is solvable, false otherwise
     */
    public boolean createState(Difficulty difficulty, int numberOfVehicles, Grid grid, Group vehicles, GridVehicle goalCar) {
        //set the required difficulty for the state
        this.difficulty = difficulty;
        //set the number of vehicles in the state
        this.numberOfVehicles = numberOfVehicles;
        //set the grid for the state
        this.grid = grid;
        //set the group of vehicles for the grid
        this.vehicles = vehicles;
        //set the goal car for the state
        this.goalCar = goalCar;
        //creating a random generator
        Random generator = new Random();
        //creating a temporary group of vehicles representing good configuration
        Group tempVehicles = new Group();
        //add the goal car to the temp group
        tempVehicles.getChildren().add(goalCar);
        //update the goal car's state
        goalCar.initialShift();
        //counter for adding the given number of vehicles
        int count = 0;
        //to avoid huge loops we want to reset if failed additions get out of hand
        //to avoid infinite loops
        int fails = 0;
        //we want to place random vehicles on the grid till our count
        //is greater than the number of vehicles
        //adding timeout for board gen reset so not too much time is spend generating boards
        long startTime = System.currentTimeMillis();
        while (count < numberOfVehicles) {
            //if the attempted addition of the vehicle counter has exceeded 100 or more than 5s has passed
            //to avoid infinite loops we reset the creation process
            if (fails > 100 || System.currentTimeMillis() - startTime > 5000) {
                startTime = System.currentTimeMillis();
                //reset counter
                count = 0;
                //reset the grid
                grid.resetNewGrid();
                //clear the front end groups
                vehicles.getChildren().clear();
                tempVehicles.getChildren().clear();
                //re-add the goal car into the group
                vehicles.getChildren().add(goalCar);
                tempVehicles.getChildren().add(goalCar);
                //initally shift to update the goal car's row and column
                goalCar.initialShift();
                //randomise the number of vehicles to be added again
                numberOfVehicles = generator.nextInt(17) + 4;
                //reset fail counter
                fails = 0;
            }
            //create a new vehicle with random size and orientation (2-3) and (true/false)
            Vehicle v = new Vehicle(generator.nextBoolean(), generator.nextInt(2) + 2);
            //make a random row for the vehicle to be in between 0-(6-vehicle size)
            int row = generator.nextInt(GridVariables.BOARD_SIZE + 1 - v.getSize());
            //if the vehicle is horizontal it can be placed from 0-6 since row doesnt effect horizontal vehicle
            //placement allows for more combinations
            if (v.isHorizontal())
                row = generator.nextInt(GridVariables.BOARD_SIZE);
            //make a random column for the vehicle to be in between 0-(6-vehicle size)
            int col = generator.nextInt(GridVariables.BOARD_SIZE + 1 - v.getSize());
            //if the vehicle is vertical it can be placed from 0-6 since column doesnt effect vertical vehicle
            //placement which allows for more combinations
            if (!v.isHorizontal())
                col = generator.nextInt(GridVariables.BOARD_SIZE);
            //create a grid vehicle which is not the goal car with the given randomized vehicle
            GridVehicle gv = new GridVehicle(false, v, row, col, grid);
            //if the vehicle placed was valid add to bhe grid, and add the vehicle to both vehicle group sets
            if (grid.validPlacement(v, row, col, gv)) {
                //add the vehicle to the vehicle set
                vehicles.getChildren().add(gv);
                //update the grid to include the board (front end grid)
                gv.initialShift();
                //add the vehicle to the second vehicle set
                tempVehicles.getChildren().add(gv);
                //increment the counter
                count++;
            }
            //increment the number of fails regardless to avoid infinite loops
            fails++;
        }
        //create a back end state suitable for search using the grid constructor
        BackEndState initalState = new BackEndState(grid);

        //attempt to solve the board and see if solution
        boolean check = BFSearch(initalState);
        //clear the group of gridvehicles
        vehicles.getChildren().clear();
        //add all the secondary grid vehicles to restore initial state
        vehicles.getChildren().addAll(tempVehicles.getChildren());
        //return true if created state is solvable
        //false if otherwise
        return check;
    }


    /**
     * This method will solve the board in a state breadth first heuristic search. This method will
     * guarantee to return true if the given initial configuration contains a solution at goal state, provided
     * it receives a valid non null configuration. this BFS will explore every possible child state from the original state
     * until one of the following
     * 1. the solution is found
     * 2. no more states left to search
     * 3. over 4000 states have been expanded to avoid really large unsolvable searches
     * <p>
     * child states will be created by moving all vehicles which can move in all the directions they can move and creating
     * a new state for each move made. these moves are added to the open queue for further expansion. states popped from the open queue
     * are added to the visited queue so we do not expand previously visited states.
     * </p>
     * <p>
     * the heuristic used in place will be used as a scoring system for the state. the score works by taking the distance
     * of the goal vehicle from the end state, and also 1 for each block infront of the goal vehicle in the goal row.
     * </p>
     *
     * @param initialState the initial state we are attempting to search
     * @return the boolean
     */
    public boolean BFSearch(BackEndState initialState) {
        //creating the comparator for the priority queue based on heuristic score
        Comparator searchNodeComparator = new nodeComparator();
        //creating the open list of states
        PriorityQueue<searchNode> open = new PriorityQueue<>(searchNodeComparator);
        //creating the list of visited states
        ArrayList<BackEndState> visited = new ArrayList<>();
        //the initial search node contains the initial state, boards checked = 0, and no previous states
        searchNode initialNode = new searchNode(initialState, 0, null);
        //sets the heuristic score of the current state
        initialNode.setScore();
        //add the initial node to the queue
        open.add(initialNode);
        //initial states expanded = 0
        int count = 0;
        //while there are still states to explore
        while (!open.isEmpty()) {
            //pop the state with the lowest heuristic score from the list of states
            searchNode curr = open.poll();
            //if the amount of states expanded is 4000
            if (count > 2000)
                //return false as we don't want to expand way too many states (too hard)
                return false;
            //if the visited list contains the popped state, we want to skip and go to next iteration
            //dont want to expand already visited states
            if (visited.contains(curr.getCurrent()))
                continue;
            //if the current state is thje goal state (solved)
            if (curr.solved()) {
                //we want to return true -> there is a solution
                System.out.println(curr.getPreviousStates());
                return true;
            }
            //otherwise add the current state board to the visited list
            visited.add(curr.getCurrent());
            //increment the counter by 1
            count++;
            System.out.println("count - " + count);
            //scan through the list of vehicles in the state
            for (BackEndCar bac : curr.getCurrent().getBackEndVehicles()) {
                //create a cloned state from the current state
                BackEndState copy = new BackEndState(curr.getCurrent());
                //get the vehicle in the list with the same attributes as the current vehicle
                BackEndCar bec = clonedCars(bac, copy);
                //now attempt to move the vehicle in all directions it can move in creating a new state each time
                //these states will be stored in a list
                ArrayList<BackEndState> states = copy.tryMoveVehicle(copy, bec);
                //if the list is empty (no new states) we want to skip (dont want to dereference null pointers xD ^_^
                if (states.isEmpty())
                    continue;
                    //otherwise
                else {
                    //scan through the list of all child states
                    for (int i = 0; i < states.size(); i++) {
                        //create a new search node with the parent being the current search node, and the count being the parents count + 1
                        searchNode add = new searchNode(states.get(i), curr.getPreviousStates() + 1, curr);
                        //if the search node created is the solution
                        if (add.solved()) {
                            //we want to return true! we go our solution!
                            System.out.println(add.getPreviousStates());
                            return true;
                        }
                        //otherwise we want to set the score for the search node
                        add.setScore();
                        //and if the search node has not been visited (Expanded) before
                        if (!visited.contains(add.getCurrent())) {
                            //add it to our open list
                            open.add(add);
                        }
                    }
                }

            }
        }
        //return false if all states have been exhausted and no solution
        return false;
    }

    /**
     * This method will be used to retrieve the vehicle in the list with the same attributed as a vehicle we are searching for.
     * This will be used so we can update the vehicle in the state rather than the previous state's vehicle.
     * This method guarantees to return the vehicle in the state's vehicle list that has the same attributes as the vehicle
     * passed in, provided it is passed in a non null vehicle.
     *
     * @param bec the vehicle which we are trying to locate in the current state
     * @param bes the current state we are trying to retrieve the vehicle for
     */
    public BackEndCar clonedCars(BackEndCar bec, BackEndState bes) {
        //scan through the list of vehicles in the current state
        for (int i = 0; i < bes.getBackEndVehicles().size(); i++) {
            //if the vehicle at the index i is identical in attributes to the vehicle passed in
            if (bec.equals(bes.getBackEndVehicles().get(i)))
                //return the vehicle at index i
                return bes.getBackEndVehicles().get(i);
        }
        //if search failed (god knows why i dont know how you can possibly fail it)
        //return the original car passed in (god forbid this happens)
        return bec;
    }
}
//        Grid someotherGrid = new Grid();
//
//        Vehicle horizontal2 = new Vehicle(true, 2);
//        Vehicle horizontal3 = new Vehicle(true,3);
//        Vehicle vertical2 = new Vehicle(false, 2);
//        Vehicle vertical3 = new Vehicle(false, 3);
//
//        GridVehicle v1 = new GridVehicle(true,horizontal2,2,3,someotherGrid);
//        v1.initialShift();
//        vehicles.getChildren().add(v1);
//        GridVehicle v2 = new GridVehicle(false, horizontal3,3,0,someotherGrid);
//        v2.initialShift();
//        vehicles.getChildren().add(v2);
//        GridVehicle v3 = new GridVehicle(false, horizontal2,5,3,someotherGrid);
//        v3.initialShift();
//        vehicles.getChildren().add(v3);
//        GridVehicle v4 = new GridVehicle(false, horizontal2,4,4,someotherGrid);
//        v4.initialShift();
//        vehicles.getChildren().add(v4);
//        GridVehicle v5 = new GridVehicle(false, horizontal2,5,0,someotherGrid);
//        v5.initialShift();
//        vehicles.getChildren().add(v5);
//        GridVehicle v6 = new GridVehicle(false, horizontal2,0,1,someotherGrid);
//        v6.initialShift();
//        vehicles.getChildren().add(v6);
//        GridVehicle v7 = new GridVehicle(false, vertical3,1,5,someotherGrid);
//        v7.initialShift();
//        vehicles.getChildren().add(v7);
//        GridVehicle v8 = new GridVehicle(false, vertical3,0,0,someotherGrid);
//        v8.initialShift();
//        vehicles.getChildren().add(v8);
//        GridVehicle v9 = new GridVehicle(false, vertical2,1,1,someotherGrid);
//        v9.initialShift();
//        vehicles.getChildren().add(v9);
//        GridVehicle v10 = new GridVehicle(false, vertical2,1,2,someotherGrid);
//        v10.initialShift();
//        vehicles.getChildren().add(v10);
//        GridVehicle v11 = new GridVehicle(false, vertical2,0,4,someotherGrid);
//        v11.initialShift();
//        vehicles.getChildren().add(v11);
//        GridVehicle v12 = new GridVehicle(false, vertical2,4,2,someotherGrid);
//        v12.initialShift();
//        vehicles.getChildren().add(v12);
//        GridVehicle v13 = new GridVehicle(false, vertical2,3,3,someotherGrid);
//        v13.initialShift();
//        vehicles.getChildren().add(v13);