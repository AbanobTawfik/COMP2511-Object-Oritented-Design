import java.util.Objects;

public class BackEndCar {
    private int row;
    private int col;
    private int size;
    private boolean goalCar;
    private boolean horizontal;


    public BackEndCar(GridVehicle gridVehicle) {
        this.row = gridVehicle.getRow();
        this.col = gridVehicle.getCol();
        this.goalCar = gridVehicle.isGoalCar();
        this.horizontal = gridVehicle.getVehicle().isHorizontal();
        this.size = gridVehicle.getVehicle().getSize();
    }

    public BackEndCar(BackEndCar backEndVehicles) {
        this.row = backEndVehicles.getRow();
        this.col = backEndVehicles.getCol();
        this.goalCar = backEndVehicles.isGoalCar();
        this.horizontal = backEndVehicles.isHorizontal();
        this.size = backEndVehicles.getSize();
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getSize() {
        return size;
    }

    public boolean isGoalCar() {
        return goalCar;
    }

    public boolean isHorizontal() {
        return horizontal;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BackEndCar)) return false;
        BackEndCar that = (BackEndCar) o;
        return getRow() == that.getRow() &&
                getCol() == that.getCol() &&
                getSize() == that.getSize() &&
                isGoalCar() == that.isGoalCar() &&
                isHorizontal() == that.isHorizontal();
    }
}
