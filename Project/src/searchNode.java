import java.util.ArrayList;

/**
 * This class will be used as a node class for our search. it will be a path like class where it contains
 * the previous state (which can be unwinded) and the current state, and the number of states to reach that state.
 * this will be used for search as it allows for the result to be traced back to the initial state, allowing a
 * solution to be traced from the initial state to the goal state
 */
public class searchNode {
    //this field will be the current state of expansion
    private BackEndState current;
    //this field will be the heuristic score of the search node
    private int score;
    //this field will be the number of states leading to the current state
    private int previousStates;
    //this field will be the previous state leading to this expansion
    private searchNode parent;

    /**
     * This method will be used to create a Node which can be expanded in the search. a node consists of three elements
     * 1. the current state of the node
     *
     * 2. the number of states expanded to reach this state
     *
     * 3. the list of previous nodes leading to this state
     * This method guarantees to return a search node which can be expanded in search, provided it receives a non null stale,
     * a valid integer greater than zero for the number of previous states, and a valid searchnode as parent.
     *
     * @param current        the current state for the node
     * @param previousStates the number of previous states leading to this state
     * @param parent         the previous search node leading to this state (allows for backtracing)
     */
    public searchNode(BackEndState current, int previousStates, searchNode parent) {
        //set the current state as the given state
        this.current = current;
        //set the current number of expanded states to reach this to give integer
        this.parent = parent;
        //set the previous search node parent aas the given search node
        this.previousStates = previousStates;
    }

    /**
     * This method will be used to retrieve the current state for the search node.
     * this method will guarantee to return a non null, state representing the state of the node
     *
     * @return the state of the current node
     */
    public BackEndState getCurrent() {
        return current;
    }

    /**
     * This method will be used to retrieve the number of parent nodes to reach the current node.
     * this method will guarantee to return a non negative positive integer representing the number of
     * previous states leading to this state
     *
     * @return the number of previous states leading to this state
     */
    public int getScore() {
        return score;
    }

    /**
     * this method will be used to set the heuristic score for the current search node. this will be done in the following way
     * <p>an initial sum counter is initalised at 0</p>
     * <p>the column difference from the right most row, to the current goal column is added to the sum </p>
     * <p>now we scan at the goal row and check for all grid spaces obstructing.
     * add 1 to the sum for every obstructed column till the right most column.
     * </p>
     *
     * This method will guarantee to update the current score of the node.
     */
    public void setScore() {
        //initialise sum counter as 0
        int sum = 0;
        //we want to retrieve the list of vehicles from the current state
        ArrayList<BackEndCar> cars = current.getBackEndVehicles();
        //placeholder to avoid initalisation errors (trying to retrieve the goal car)
        BackEndCar goal = null;
        //scan through the vehicle list to find where the goal vehicle is located
        for (BackEndCar bec : cars) {
            //if the current vehicle being expected is the goal caar
            if (bec.isGoalCar()) {
                //set the goal to be that vehicle
                goal = bec;
                //stop scanning no need to continue
                break;
            }
        }
        //add to the sum the distance from the goal vehicle to the end state
        sum += (GridVariables.BOARD_SIZE - 1) - goal.getCol();
        //to further initialise the heuristic
        //scan through the goal row
        for (int i = 0; i < GridVariables.BOARD_SIZE; i++) {
            //add 1 to the counter everytime there is an obstruction in goal row that is not the goal car itself
            if (current.getBackendGrid()[i][current.getGoalRow()] == 0)
                sum++;
        }
        //set the score = to the running sum that was calculated
        score = sum;
    }

    /**
     * This method will be used to check if the current node has met goal state. This occurs when the goal row at the very end
     * which is the exit, has the goal car value.
     * This method guarantees to return a boolean expressing if the node is at goal state.
     *
     * @return true if the index at row BOARDSIZE - 1 and goal row, has the goal car value, false if otherwise (true if goal state)
     */
    public boolean solved() {
        //if the goal index is empty/null
        if (current.getBackendGrid()[GridVariables.BOARD_SIZE - 1][current.getGoalRow()] == -1)
            //return false already not goal state
            return false;
        //return the expression that the goal index contains the goal car value
        return (current.getBackendGrid()[GridVariables.BOARD_SIZE - 1][current.getGoalRow()] == current.getGoalCar());
    }

    /**
     * This method will be used to return the number of previous states that have led to the current node state.
     * This method will guarantee to return the number of previous states leading to the current state.
     *
     * @return the number of states leading to the current state
     */
    public int getPreviousStates() {
        return previousStates;
    }
}
