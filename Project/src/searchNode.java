import java.util.ArrayList;

public class searchNode {
    private BackEndState current;
    private int score;
    private int previousStates;
    private searchNode parent;

    public searchNode(BackEndState current,int previousStates , searchNode parent) {
        this.current = current;
        this.parent = parent;
        this.previousStates = previousStates;
    }

    public BackEndState getCurrent() {
        return current;
    }

    public int getScore() {
        return score;
    }

    public void setScore() {
        int sum = 0;
        ArrayList<BackEndCar> cars = current.getBackEndVehicles();
        BackEndCar goal = null;
        for (BackEndCar bec : cars) {
            if (bec.isGoalCar()) {
                goal = bec;
                break;
            }
        }
        sum += (GridVariables.BOARD_SIZE - 1) - goal.getCol();
        for (int i = 0; i < GridVariables.BOARD_SIZE; i++) {
            if (current.getBackendGrid()[i][current.getGoalRow()] == 0)
                sum++;
        }

        score = sum;
    }

    public boolean solved() {
        if (current.getBackendGrid()[GridVariables.BOARD_SIZE - 1][current.getGoalRow()] == -1)
            return false;
        return (current.getBackendGrid()[GridVariables.BOARD_SIZE - 1][current.getGoalRow()] == current.getGoalCar());
    }

    public int getPreviousStates() {
        return previousStates;
    }

}
