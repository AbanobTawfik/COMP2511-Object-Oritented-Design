/**
 * The Vehicle class which will be used as a class in defining
 * the GridVehicle. it will only be required to supply the orientation of the vehicle,
 * and the size of the vehicle. This makes it so that GridVehicle has less cluster than it already
 * contains. this is similair to creating rows of seats, vs creating the seats in the row.
 */
public class Vehicle {
    //if it's horizontal -> true, if vertical -> false (converse)
    private boolean horizontal;
    //this will be the amount of tiles the car takes in the grid
    private int size;


    /**
     * creates a new Vehicle with an initial orientation which does not change
     * and a given size which also does not change
     * this method requires a valid integer, and a boolean. it will guarantee to return
     * a vehicle structure that holds the given size and integer. if the integer is less than or equal to 0
     * we force the vehicle to be size 1 which is the minimum size of a vehicle (cant have car size 0)
     *
     * @param horizontal the orientation of car, true = horizontal/false = vertical
     * @param size       the size of the vehicle aka how many tiles it will take in the orientation
     */
    //this method is used to create a vehicle with a given size and orientation
    public Vehicle(boolean horizontal, int size) {
        //if the vehicle is given a size that is invalid (less than or equal to 0) we force
        //the vehicle to have a size of 1. this is so we dont get wrongly sized cars
        if (size <= 0)
            //forcing the size of vehicle to be 1 if <= 0
            size = 1;

        //set the orientation of vehicle
        this.horizontal = horizontal;
        //set the size of the vehicle
        this.size = size;
    }

    /**
     * This method will check the orientation of the vehicle.
     * This method will guarantee to return a boolean referring to if the vehicle is horizontal and
     * uses converse. if the vehicle is horizontal, it returns true. if the vehicle is vertical, it
     * returns false.
     *
     * @return the orientation of the vehicle. true = horizontal. false = vertical
     */
    //this returns the orientation of car,
    //true -> horizontal (left/right)
    //false -> vertical (up/down)
    public boolean isHorizontal() {
        return horizontal;
    }

    /**
     * This method will return the size of the vehicle.
     * This method will guarantee to return a positive integer which represents the size of the vehicle in tiles
     *
     * @return the size of vehicle in tiles on grid
     */
    //this will return the size of the vehicle
    public int getSize() {
        return size;
    }
}
