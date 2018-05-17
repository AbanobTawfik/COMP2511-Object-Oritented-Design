import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class BackEndState {
    private int[][] backendGrid = new int[GridVariables.BOARD_SIZE][GridVariables.BOARD_SIZE];
    private int goalRow = 2;
    //this is a counter for counting how many drags in the board
    private int numberOfMoves = 0;
    //not possible to get over 36 cars
    private int goalCar = 36;
    private ArrayList<BackEndCar> backEndVehicles = new ArrayList<>();

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

    public BackEndState(BackEndState backEndState) {
        for (int i = 0; i < GridVariables.BOARD_SIZE; i++) {
            for (int j = 0; j < GridVariables.BOARD_SIZE; j++) {
                this.backendGrid[i][j] = backEndState.getBackendGrid()[i][j];
            }
        }
        for (int i = 0; i < backEndState.getBackEndVehicles().size(); i++)
            this.backEndVehicles.add(backEndState.getBackEndVehicles().get(i));
    }

    public int[][] getBackendGrid() {
        return backendGrid;
    }

    public ArrayList<BackEndCar> getBackEndVehicles() {
        return backEndVehicles;
    }

    public int getGoalRow() {
        return goalRow;
    }

    public int getGoalCar() {
        return goalCar;
    }

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

    public boolean canLeft(BackEndCar v) {
        if (!v.isHorizontal())
            return false;
        if (v.getCol() == 0)
            return false;
        if (backendGrid[v.getCol() - 1][v.getRow()] != -1)
            return false;
        return true;
    }

    public boolean canRight(BackEndCar v) {
        if (!v.isHorizontal())
            return false;
        if (v.getCol() + v.getSize() >= GridVariables.BOARD_SIZE)
            return false;
        if (backendGrid[v.getCol() + v.getSize()][v.getRow()] != -1)
            return false;
        return true;
    }

    public boolean canUp(BackEndCar v) {
        if (v.isHorizontal())
            return false;
        if (v.getRow() == 0)
            return false;
        if (backendGrid[v.getCol()][v.getRow() - 1] != -1)
            return false;
        return true;
    }

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
        return getGoalRow() == that.getGoalRow() &&
                numberOfMoves == that.numberOfMoves &&
                getGoalCar() == that.getGoalCar() &&
                check &&
                Objects.equals(getBackEndVehicles(), that.getBackEndVehicles());
    }
}
