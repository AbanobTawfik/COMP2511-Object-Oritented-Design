import java.util.*;

public class Rows {
    private String rowName;
    private ArrayList<Seats> chairs;
    private int numberOfSeatsInRow;
    private int numberOfAvailableSeatsInRow;

    //basic class constructor
    public Rows(String rowName, int numberOfSeatsInRow) {
        this.rowName = rowName;
        this.numberOfSeatsInRow = numberOfSeatsInRow;
        //when initalising a row we want to create a new seat for every seat in row
        //create a new arraylist of seats
        chairs = new ArrayList<Seats>();
        //add numberOfSeats many seats in the row
        for(int i = 0;i<numberOfSeatsInRow;i++)
            this.addSeatsToRow(1);
    }

    /**
     *
     * @return the name of the row (name of row)
     */
    public String getRowName() {
        //returns the rowname of the row
        return rowName;
    }

    /**
     *
     * @return all seats in the row
     */
    public ArrayList<Seats> getChairs() {
        return chairs;
    }

    /**
     *
     * @return the number of seats in the row
     */
    public int getNumberOfSeatsInRow() {
        //returns the number of seats in a row
        return numberOfSeatsInRow;
    }

    /**
     *
     * @return the number of unreserved seats in the row
     */
    public int getNumberOfAvailableSeatsInRow() {
        //returns the number of AVAILABLE seats in the row
        return numberOfAvailableSeatsInRow;
    }

    /**
     * function name says it all
     * @param numberOfAvailableSeatsInRow number of seats
     */
    public void setNumberOfAvailableSeatsInRow(int numberOfAvailableSeatsInRow) {
        //this is for assigning/changing number of available seats in row
        //useful for when we make bookings or cancel bookings
        this.numberOfAvailableSeatsInRow = numberOfAvailableSeatsInRow;
    }

    /**
     * adds seats to a row expected to add that many seats to a row.
     * @param numberofseats number of seats to be added, expected to be an integer > 0
     */
    //adds a seat to the row
    public void addSeatsToRow(int numberofseats){
        if(numberofseats < 1)
            return;
        //add the amount of seats requested (loop through numberofseats time)
        for(int k = 0; k < numberofseats; k++)
            //this will add the seat with the correct seatnumber and the correct reservation status
            chairs.add(new Seats((chairs.size()+k), false));
        //increase the number of available seats in the row
        numberOfAvailableSeatsInRow += numberofseats;
    }

    /**
     * This function is designed to find the index of the first free seat in the row, it is expected to return the first free seat in the row
     * @return ths index of the first free seat in the row
     */
    //this function returns the first unreserved seat in the row
    public int firstFreeSeatInRow(){
        //initialise return as FALSE (-1 flag) incase there isn't a free seat in the row
        int ret = -1;
        //scan through each seat in the row
        for(int k = 0;k < numberOfSeatsInRow; k++ ){
            //when the first free seat is found (false means unreserved)
            //return that index k
            if(chairs.get(k).checkReserveStatus() == false) {
                return k;
            }
        }
        //otherwise return false aka no free seat in the row
        return ret;
    }

}
