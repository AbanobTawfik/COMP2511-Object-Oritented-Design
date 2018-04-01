import java.util.*;

public class bookings {
    //info for the booking
    private int numberOfTickets;
    private Date bookingTime;
    private int id;
    private int cinemaNumber;
    //info for the booked movie
    private int rowNumber;               // <--- essential to keep track of our booked seats
    private int startSeat;
    private String rowName;

    /*
     * constructor for booking object initialise our rowNumber and start seat as false and on creation
     * of a booking we set those to different values corresponding to the booking request and session
     */
    public bookings(int numberOfTickets, Date bookingTime, int id, int cinemaNumber) {
        this.numberOfTickets = numberOfTickets;
        this.bookingTime = bookingTime;
        this.id = id;
        this.cinemaNumber = cinemaNumber;
        //initial creates booking with these false flags for the rownumber and start seat
        //this is so if we want to cancel/change a booking we check if the booking is already existing
        //or has just been created with constructor and hasn't actually booked any seats
        this.rowNumber = -1;
        this.startSeat = -1;
    }

    /**
     * This function returns the rowName for
     *
     * @return the rowname for the booking
     */
    public String getRowName() {
        //return the row name for our booking
        return rowName;
    }

    /**
     *
     * @return the rowNumber for the row which the booking is in, the return value should be >= 0 no negatives
     */
    public int getRowNumber(){
        //return the index in the rowlist where the row is found
        return rowNumber;
    }
    /**
     * @return the start seat in the row, the return value should be >= 0 no negatives
     */
    public int getStartSeat() {
        //return the first seat in the booking
        return startSeat;
    }

    /**
     * @return return the number of tickets in the booking, the return value should be >0  cant have booking with no tickets
     */
    public int getNumberOfTickets() {
        //return the number of tickets in the booking
        return numberOfTickets;
    }

    /**
     * @return the booking id, the return value should be >= 0 no negatives
     */
    public int getId() {
        //return our booking id
        return id;
    }


    //when we create a booking we want to 1.
    //check which session contains this data
    //check every row in this session if there is a row which contains those number of seats
    //return true if booking is made
    //else we just want to return false and no booking made
    //also check if the session even exists itself

    /**
     * This function creates a booking based on all of the parameter inputs. <br>
     * This function checks if it's a valid booking all along the way based on if there is a row or cinema or a booking. <br>
     * If booking is successful it will reserve seats in the row based on input and store the data into booking object.
     *
     * @param numberOfTicketsCreate the index of the cinema in the  cinemas ArrayList for the booking, this should be > 0
     * @param bookingTimeCreate     Time of the booking, this should be a validly formatted 24h string
     * @param idCreate              bookingid, this number should be >= 0 no negatives
     * @param cinemaNumberCreate    the cinema for the booking, this number should be >=0 no negatives
     * @param cinemas               ArrayList of all cinemas, must contain a cinema with session else booking cannot be made
     * @param sessionIndex          this is the index in the arraylist of sessions which contains that booking, this must be >= 0 else we cannot make a booking
     * @param cinemaN               the cinemanumber for the booking, this again should be >=0 has to be the cinema which the session is in not index!
     * @return true if valid booking or else false
     */
    public boolean createBooking(int numberOfTicketsCreate, Date bookingTimeCreate, int idCreate, int cinemaNumberCreate, ArrayList<Cinema> cinemas, int sessionIndex, int cinemaN) {
        //trying to find index of cinema in the cinema arraylist that contains that cinema number
        int cinema = checkCinemaAvailable(cinemas.get(cinemaNumberCreate).getCinemaNumber(), cinemas);
        //if the cinema number doesnt exist return false
        if (cinema == -1)
            return false;
        //we want to find the first available row in the session

        int getRowFree = cinemas.get(cinema).getSession().get(sessionIndex).availableRow(numberOfTicketsCreate);
        //if there are no rows in the booking that can withstand the booking request return false
        if (getRowFree == -1)
            return false;
        //get the first free seat in the row for the booking request so we can assign the correct seat number to each booking

        this.startSeat = cinemas.get(cinema).getSession().get(sessionIndex).getSessionRows().get(getRowFree).firstFreeSeatInRow();
        //now we are booking the seats by reserving each seat from first seat -> first seat + number of tickets
        cinemas.get(cinema).getSession().get(sessionIndex).bookSeats(numberOfTicketsCreate, getRowFree);

        //updating our booking info
        this.bookingTime = bookingTimeCreate;
        this.rowNumber = getRowFree;
        this.cinemaNumber = cinemaN;
        this.numberOfTickets = numberOfTicketsCreate;
        this.rowName = cinemas.get(cinema).getSession().get(sessionIndex).getSessionRows().get(rowNumber).getRowName();
        this.id = idCreate;

        //return succesful booking
        return true;
    }

    //to change booking all we want to do is
    //check if the cinema number exists
    //check if available row is there
    //check if available row has seats
    //if the row has seats change the booking and MAKE SURE TO FREE THE OLD SEATS TO UNRESERVED
    //want to also make sure we can access bookings
    //also check if booking exists in first place xD

    /**
     * This function will change an existing booking if possible. <br>
     * It will clear the seats and check if a new booking is available, if not cancel and reassign the old seats
     * It will first check if the new booking is possible so it doesnt just delete a booking for no reason. <br>
     * The function will then free the seats to the old session, clearing the booking. <br>
     * It will then create the new booking requested. <br>
     * Then afterwards, the Booking will be removed from the bookingList in the old session. <br>
     * There aren't really any preconditions for this case as a user can request any sort of input and it will all be handled accordingly.
     * <br>The post conditions must be that all requests are handled appropriately, and a return value of true/false is returned
     * @param cinemaNumbernew    the index of the cinema in the  cinemas ArrayList for the booking
     * @param bookingTimenew     Time of the booking
     * @param numberOfTicketsnew number of tickets in the booking
     * @param cinemas            ArrayList of all cinemas
     * @param sessionIndex       this is the index in the arraylist of sessions which contains that booking
     * @param cinemaN            the cinemanumber for the booking
     * @return True if successful change, false if unsuccessful
     */
    public boolean changeBooking(int cinemaNumbernew, Date bookingTimenew, int numberOfTicketsnew, ArrayList<Cinema> cinemas, int sessionIndex, int cinemaN) {
        /*
         * gathering information from our old booking so we can free our old booking properly
         */
        //index of cinema in the cinema array which contains our old booking's cinema number
        int cinemaOld = checkCinemaAvailable(cinemaNumber, cinemas);
        //keep track of our old session index from the old booking
        int oldSessionIndex = cinemas.get(cinemaOld).sessionIndexWithBooking(id);

        //now we want to check if the pre-existing booking existed
        //if there was no pre-exisiting booking then these fields would remain unchanged from initialised state
        //return false if there was no previously assigned booking
        if (rowNumber == -1 || startSeat == -1)
            return false;


        //first checks if valid booking for change before we remove old booking
        //find the index of the cinema in the arraylist of cinemas that contains the new cinema number
        int cinemaChange = checkCinemaAvailable(cinemas.get(cinemaNumbernew).getCinemaNumber(), cinemas);
        //if the cinema isn't available or exist in the array return false
        if (cinemaChange == -1)
            return false;

        //now free up our old booking which can be conviently done with the row + start seat of old booking
        //free up the old seats
        cinemas.get(cinemaOld).getSession().get(oldSessionIndex).freeSeats(numberOfTickets, rowNumber, startSeat);

        //get the updated first seat and free row since we did just free up a booking to check
        int getRowFree = cinemas.get(cinemaChange).getSession().get(sessionIndex).availableRow(numberOfTicketsnew);
        //just incase
        if(getRowFree == -1) {
            //if there are no free rows w ewant to rebook the seats we just cancelled
            cinemas.get(cinemaOld).getSession().get(oldSessionIndex).bookSeats(numberOfTickets,rowNumber);
            return false;
        }
        //get the first seat in the first free row to create booking and keep track of seat number of bookings
        this.startSeat = cinemas.get(cinemaChange).getSession().get(sessionIndex).getSessionRows().get(getRowFree).firstFreeSeatInRow();

        //now that old bookings have been reverted and fixed up assign new booking with new booking information
        cinemas.get(cinemaChange).getSession().get(sessionIndex).bookSeats(numberOfTicketsnew, getRowFree);

        //now we want to go to our sessions, delete the old booking from our old session first need old booking index
        //old booking index is the index in the bookings list for that session that has the old booking id
        int oldBookingIndex = cinemas.get(cinemaOld).getSession().get(oldSessionIndex).getBookingIdIndex(id);
        //remove the old booking from old booking list since we are adding new one to new booking list
        cinemas.get(cinemaOld).getSession().get(oldSessionIndex).getAllBookings().remove(oldBookingIndex);

        //update booking information with the new booking information
        this.rowNumber = getRowFree;
        this.numberOfTickets = numberOfTicketsnew;
        this.cinemaNumber = cinemaN;
        this.bookingTime = bookingTimenew;
        this.rowName = cinemas.get(cinemaChange).getSession().get(sessionIndex).getSessionRows().get(rowNumber).getRowName();

        //return succesful booking if no checks have failed
        return true;
    }

    /**
     * This function will attempt to remove a booking from the bookingList in the session.
     * If possible it will free all the seats then remove the booking
     * The preconditions are that the session we are cancelling the booking for is >=1 and we have a cinema in our arraylist.
     * <br> the post conditions are that when we cancel we are expected to return true if successful or false if unsuccessful
     * @param cinemas      the Arraylist of all cinemas, must have atleast 1 cinema in the arraylist
     * @param sessionIndex the session index of the booking that is requesting to be cancelled, the index must be >=0
     * @return true if succesful, false if unsuccessful must handle all input appropriately
     */
    public boolean cancelBooking(ArrayList<Cinema> cinemas, int sessionIndex) {
        //if there is no booking to cancel return false
        if (rowNumber == -1 || startSeat == -1)
            return false;
        //if the session is invalid return false
        if(sessionIndex < 0)
            return false;
        //now want to just free our booking and remove it from session
        //finding the index of the cinema in the arraylist of cinemas that has the cinema number for our booking
        int cinemaN = checkCinemaAvailable(cinemaNumber, cinemas);
        //if the cinema is not there return false
        if(cinemaN == -1)
            return false;

        //find the booking index in the session which contains our booking we want to cancel
        int bookingIndex = cinemas.get(cinemaN).getSession().get(sessionIndex).getBookingIdIndex(id);
        //free up the seats from our old session
        cinemas.get(cinemaN).getSession().get(sessionIndex).freeSeats(numberOfTickets, rowNumber, startSeat);
        //now remove the old booking from the old booking list
        cinemas.get(cinemaN).getSession().get(sessionIndex).getAllBookings().remove(bookingIndex);
        //return true because successful cancellation
        return true;
    }

    /**
     * @param cinemaNumber the cinemaNumber we are tying to lcoate must be a valid integer
     * @param cinemas      the ArrayList which could contain our cinemaNumber must contain atleast 1 cinema
     * @return the index in the ArrayList that contains the cinemaNumber, or -1 if it doesn't exist, post condition ensure tbat we return a integer
     *
     */
    public int checkCinemaAvailable(int cinemaNumber, ArrayList<Cinema> cinemas) {
        //initalise as false return incase the cinemanumber is not in the array
        int cinema = -1;
        //scan through the cinema aarray and check each cinema number

        for (int i = 0; i < cinemas.size(); i++) {
            //if the cinema at index i contains that cinema number then return the index i
            if (cinemaNumber == cinemas.get(i).getCinemaNumber()) {
                return i;
            }
        }
        //if we have gone through our loop and our if statement was unsuccesful return the initialised false
        return cinema;
    }


}
