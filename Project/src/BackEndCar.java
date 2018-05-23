/**
 * This class will be used to perform back end solving with a more compact vehicle
 * representation. rather than using grid vehicles and all the extra memory associated with stack pane
 * and having to deal with reinitiallising the board, a more efficient and simpler solution would be
 * to create a back end vehicle that only stores information relevant to solving the board from initial state
 * since this information is very low memory usage (3 integers and 2 booleans) the search is much faster
 * and also allows for methods to be implemented relevant to the search without polluting the grid vehicle class
 */
public class BackEndCar {
    //the row where the vehicle occupies
    private int row;
    //the column where the vehicle occupies
    private int col;
    //the size of the vehicle
    private int size;
    //a boolean expressing if the vehicle is the goal car
    private boolean goalCar;
    //a boolean expressing if the vehicle is horizontal
    private boolean horizontal;

    /**
     * This method will be used to create a simplified vehicle containing all vital
     * information. this method will guarantee to return a back end vehicle containing
     * row index, column index, the size of the vehicle, a boolean expressing both if it is the goal car and the orientation fo the car
     * provided it receives valid non null grid vehicle it can retrieve details from. since all the fields are primitive type a simple = is sufficient to assign
     *
     * @param gridVehicle the grid vehicle we are creating our simplified vehicle from
     */
    public BackEndCar(GridVehicle gridVehicle) {
        //set the row as the grid vehicles row
        this.row = gridVehicle.getRow();
        //set the column as the grid vehicles column
        this.col = gridVehicle.getCol();
        //set the boolean expression for if its the goal car
        this.goalCar = gridVehicle.isGoalCar();
        //set the boolean expression for if the vehicle is horizontal
        this.horizontal = gridVehicle.getVehicle().isHorizontal();
        //set the size of the vehicle to be the same as the grid vehicle size
        this.size = gridVehicle.getVehicle().getSize();
    }

    /**
     * This method will be used to clone a simplified vehicle containing all vital
     * information from an existing vehicle very similair to the method above. this method will guarantee to return a back end vehicle containing
     * row index, column index, the size of the vehicle, a boolean expressing both if it is the goal car and the orientation fo the car
     * provided it receives valid a non null back end vehicle to clone, since like above all fields are pimitive data types a simple
     * equals method is sufficient for cloning.
     *
     * @param backEndVehicles the back end state we are cloning
     */
    public BackEndCar(BackEndCar backEndVehicles) {
        //set the row as the vehicles row
        this.row = backEndVehicles.getRow();
        //set the column as the grid vehicles column
        this.col = backEndVehicles.getCol();
        //set the boolean expression for if its the goal car
        this.goalCar = backEndVehicles.isGoalCar();
        //set the boolean expression for if the vehicle is horizontal
        this.horizontal = backEndVehicles.isHorizontal();
        //set the size of the vehicle to be the same as the vehicle size
        this.size = backEndVehicles.getSize();
    }

    /**
     * This method will be used to retrieve the row occupied by the current vehicle.
     * this method will guarantee to return an non zero positive integer representing the row occupied by the vehicle
     *
     * @return the row occupied by the vehicle
     */
    public int getRow() {
        return row;
    }

    /**
     * This method will be used to retrieve the column occupied by the current vehicle.
     * this method will guarantee to return an non zero positive integer representing the column occupied by the vehicle
     *
     * @return the column occupied by the vehicle
     */
    public int getCol() {
        return col;
    }

    /**
     * This method will be used to retrieve the size of the current vehicle.
     * this method will guarantee to return an non zero positive integer greater than 1 representing the size of the vehicle
     *
     * @return the size of the current vehicle
     */
    public int getSize() {
        return size;
    }

    /**
     * This method will be used to retrieve a boolean representing if the vehicle is the goal vehicle
     * this method will guarantee to return a boolean expression representing if the vehicle is the goal vehicle or not
     *
     * @return a boolean expression representing if the vehicle is the goal vehicle, false if otherwise
     */
    public boolean isGoalCar() {
        return goalCar;
    }

    /**
     * This method will be used to retrieve a boolean representing if the vehicle's orientation is horizontal
     * this method will guarantee to return a boolean expression representing if the vehicle has horizontal orientation
     *
     * @return a boolean expression representing if the vehicle is horizontal, false if otherwise
     */
    public boolean isHorizontal() {
        return horizontal;
    }

    /**
     * This method will be used to update the row occupied by the vehicle. This method will guarantee to update
     * the row field with the given integer, provided it receives a valid non zero integer that is greater than 1
     *
     * @param row the updated row value we are updating
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * This method will be used to update the column occupied by the vehicle. This method will guarantee to update
     * the column field with the given integer, provided it receives a valid non zero integer that is greater than 1
     *
     * @param col the updated column value we are updating
     */
    public void setCol(int col) {
        this.col = col;
    }

    /**
     * This method will be used to compare an object to see if it is identical to the current vehicle. this method
     * will compare all fields of the vehicle to the object (if it is a vehicle). if all fields are equivalent
     * then the method will guarantee to return true, provided it receives a non hull object
     *
     * @param o the object we are checking if it is equal
     * @return true if the object has the same fields as the vehicle, false if otherwise
     */
    @Override
    public boolean equals(Object o) {
        //if the object is itself -> return true (the object is itself duh)
        if (this == o) return true;
        //if the object is not even a vehicle, we want to return false
        if (!(o instanceof BackEndCar)) return false;
        //otherwise cast the object as a vehicle and compare all elements are equal to each other
        BackEndCar that = (BackEndCar) o;
        return getRow() == that.getRow() &&
                getCol() == that.getCol() &&
                getSize() == that.getSize() &&
                isGoalCar() == that.isGoalCar() &&
                isHorizontal() == that.isHorizontal();
    }
}
