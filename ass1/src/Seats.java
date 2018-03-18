
public class Seats {
    private int seatNumber;
    private boolean reserveStatus;

    //constructor for our class
    public Seats(int seatnumber, boolean reserveStatus) {
        this.seatNumber = seatnumber;
        this.reserveStatus = reserveStatus;
    }

    /**
     * This will check if the current seat is reserved or unreserved
     * @return True if the seat is reserved, or false if unreserved
     */
    public boolean checkReserveStatus(){
        //returns the reservation status of the seat
        return reserveStatus;
    }

    /**
     * changes the reserve status of a seat from unreserved to reserved (or leaves it same if already reserved)
     */
    public void reserveSeat(){
        //reserves a seat by changing its reservation status
        reserveStatus = true;
    }

    /**
     * changes the reserve status of a seat from reserved to unreserved (or leaves it same if already unreserved)
     */
    public void removeReservedSeat(){
        //removes reservation from seat by changing its reservation status
        reserveStatus = false;
    }
}
