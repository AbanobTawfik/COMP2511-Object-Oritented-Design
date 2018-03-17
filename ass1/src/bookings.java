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


    public bookings(int numberOfTickets, Date bookingTime, int id, int cinemaNumber) {
        this.numberOfTickets = numberOfTickets;
        this.bookingTime = bookingTime;
        this.id = id;
        this.cinemaNumber = cinemaNumber;
        this.rowNumber = -1;
        this.startSeat = -1;
    }

    /**
     * This function returns the rowName for
     * @return the rowname for the booking
     */
    public String getRowName() {
        return rowName;
    }

    /**
     *
     * @return the start seat in the row
     */
    public int getStartSeat() {
        return startSeat;
    }

    /**
     *
     * @return return the number of tickets in the booking
     */
    public int getNumberOfTickets() {
        return numberOfTickets;
    }

    /**
     *
     * @return the booking id
     */
    public int getId() {
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
     * @param numberOfTicketsCreate  the index of the cinema in the  cinemas ArrayList for the booking
     * @param bookingTimeCreate      Time of the booking
     * @param idCreate bookingid
     * @param cinemaNumberCreate the cinema for the booking
     * @param cinemas ArrayList of all cinemas
     * @param sessionIndex this is the index in the arraylist of sessions which contains that booking
     * @param cinemaN the cinemanumber for the booking
     * @return true if valid booking or else false
     */
    public boolean createBooking(int numberOfTicketsCreate, Date bookingTimeCreate, int idCreate, int cinemaNumberCreate, ArrayList<Cinema> cinemas, int sessionIndex, int cinemaN) {
        //scan through every session to see if the cinema number requested exists
        //if the cinemanumber is there break can continue
        int cinema = checkCinemaAvailable(cinemas.get(cinemaNumberCreate).getCinemaNumber(), cinemas);

        //if the cinema number doesnt exist return false
        if (cinema == -1)
            return false;
        int getRowFree = cinemas.get(cinema).getSession().get(sessionIndex).availableRow(numberOfTicketsCreate);
        if (getRowFree == -1)
            return false;
        this.startSeat = cinemas.get(cinema).getSession().get(sessionIndex).getSessionRows().get(getRowFree).firstFreeSeatInRow();
        //since this booking has met our conditions we want to make this booking successful
        //we want to first make all the seats in the row that are taken reserved
        cinemas.get(cinema).getSession().get(sessionIndex).bookSeats(numberOfTicketsCreate, getRowFree);

        //updating our booking now
        this.bookingTime = bookingTimeCreate;
        this.rowNumber = getRowFree;

        this.cinemaNumber = cinemaN;
        this.numberOfTickets = numberOfTicketsCreate;
        this.rowName = cinemas.get(cinema).getSession().get(sessionIndex).getSessionRows().get(rowNumber).getRowName();
        this.id = idCreate;
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
     * This function will change an existing booking if possible.
     * It will first check if the new booking is possible so it doesnt just delete a booking for no reason. <br>
     * The function will then free the seats to the old session, clearing the booking. <br>
     * It will then create the new booking requested. <br>
     * Then afterwards, the Booking will be removed from the bookingList in the old session. <br>
     * @param cinemaNumbernew the index of the cinema in the  cinemas ArrayList for the booking
     * @param bookingTimenew Time of the booking
     * @param numberOfTicketsnew  number of tickets in the booking
     * @param cinemas ArrayList of all cinemas
     * @param sessionIndex this is the index in the arraylist of sessions which contains that booking
     * @param cinemaN the cinemanumber for the booking
     * @return True if successful change, false if unsuccessful
     */
    public boolean changeBooking(int cinemaNumbernew, Date bookingTimenew, int numberOfTicketsnew, ArrayList<Cinema> cinemas, int sessionIndex, int cinemaN) {
        //freeing up our old cinema session seats
        int cinemaOld = checkCinemaAvailable(cinemaNumber, cinemas);
        //keep track of our old session index
        int oldSessionIndex = cinemas.get(cinemaOld).sessionIndexWithBooking(id);

        //now we want to check if the pre-existing booking existed
        //if there was no pre-exisiting booking then these fields would remain unchanged from initialised state

        if (rowNumber == -1 || startSeat == -1)
            return false;
        //first checks if valid booking for change

        int cinemaChange = checkCinemaAvailable(cinemas.get(cinemaNumbernew).getCinemaNumber(), cinemas);
        if (cinemaChange == -1)
            return false;

        //now checking if there are avilable rows for number of tickets requested
        int getRowFree = cinemas.get(cinemaChange).getSession().get(sessionIndex).availableRow(numberOfTicketsnew);
        if (getRowFree == -1)
            return false;
        this.startSeat = cinemas.get(cinemaChange).getSession().get(sessionIndex).getSessionRows().get(getRowFree).firstFreeSeatInRow();
        //now free up our old booking which can be conviently done with the row + start seat of old booking
        cinemas.get(cinemaOld).getSession().get(oldSessionIndex).freeSeats(numberOfTickets, rowNumber, startSeat);
        //now that old bookings have been reverted and fixed up assign new booking
        cinemas.get(cinemaChange).getSession().get(sessionIndex).bookSeats(numberOfTicketsnew, getRowFree);

        //now we want to go to our sessions, delete the booking from our old session first need old session index
        int oldBookingIndex = cinemas.get(cinemaOld).getSession().get(oldSessionIndex).getBookingIdIndex(id);
        cinemas.get(cinemaOld).getSession().get(oldSessionIndex).getAllBookings().remove(oldBookingIndex);

        this.rowNumber = getRowFree;
        this.numberOfTickets = numberOfTicketsnew;
        this.cinemaNumber = cinemaN;
        this.bookingTime = bookingTimenew;
        this.rowName = cinemas.get(cinemaChange).getSession().get(sessionIndex).getSessionRows().get(rowNumber).getRowName();
        return true;
    }

    /**
     * This function will attempt to remove a booking from the bookingList in the session.
     * If possible it will free all the seats then remove the booking
     * @param cinemas the Arraylist of all cinemas
     * @param sessionIndex the session index of the booking that is requesting to be cancelled
     * @return true if succesful, false if unsuccessful
     */
    public boolean cancelBooking(ArrayList<Cinema> cinemas, int sessionIndex) {
        //if there is no booking to cancel return false
        if (rowNumber == -1 || startSeat == -1)
            return false;
        //now want to just free our booking and remove it from session
        int cinemaN = checkCinemaAvailable(cinemaNumber, cinemas);
        int bookingIndex = cinemas.get(cinemaN).getSession().get(sessionIndex).getBookingIdIndex(id);
        cinemas.get(cinemaN).getSession().get(sessionIndex).getAllBookings().remove(bookingIndex);
        cinemas.get(cinemaN).getSession().get(sessionIndex).freeSeats(numberOfTickets, rowNumber, startSeat);
        return true;
    }

    /**
     *
     * @param cinemaNumber the cinemaNumber we are tying to lcoate
     * @param cinemas the ArrayList which could contain our cinemaNumber
     * @return the index in the ArrayList that contains the cinemaNumber, or -1 if it doesn't exist
     */
    public int checkCinemaAvailable(int cinemaNumber, ArrayList<Cinema> cinemas) {
        int cinema = -1;
        for (int i = 0; i < cinemas.size(); i++) {
            if (cinemaNumber == cinemas.get(i).getCinemaNumber()) {
                return i;
            }
        }
        return cinema;
    }


}
