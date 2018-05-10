public class Vehicle {
    //if it's horizontal -> true, if vertical -> false (converse)
    private boolean horizontal;
    //this will be 2 for cars, 3 for trucks
    private int size;
    //index of the car on grid in row col form
    private int rowIndex;
    private int columnIndex;

    public Vehicle(boolean horizontal, int size) {
        //can set the orientation of car in constructor and size (to say if its a car or truck)
        this.horizontal = horizontal;
        this.size = size;
    }

    public boolean isHorizontal() {
        return horizontal;
    }

    public int getSize() {
        return size;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }
}
