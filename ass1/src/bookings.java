import java.util.*;

public class bookings {
    //info for the booking
    private int numberOfTickets;
    private Date bookingTime;
    private int id;
    private int cinemaNumber;
    //info for the booked movie
    private int rownumber;               // <--- essential to keep track of our booked seats
    private int startseat;
    private char rowname;


    public bookings(int numberOfTickets, Date bookingTime, int id, int cinemaNumber) {
        this.numberOfTickets = numberOfTickets;
        this.bookingTime = bookingTime;
        this.id = id;
        this.cinemaNumber = cinemaNumber;
        this.rownumber = -1;
        this.startseat = -1;
    }

    public char getRowname() {
        return rowname;
    }

    public void setRowname(char rowname) {
        this.rowname = rowname;
    }

    public int getRownumber() {
        return rownumber;
    }

    public void setRownumber(int rownumber) {
        this.rownumber = rownumber;
    }

    public int getStartseat() {
        return startseat;
    }

    public void setStartseat(int startseat) {
        this.startseat = startseat;
    }


    public int getNumberOfTickets() {
        return numberOfTickets;
    }

    public void setNumberOfTickets(int numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }

    public Date getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(Date bookingTime) {
        this.bookingTime = bookingTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCinemaNumber() {
        return cinemaNumber;
    }

    public void setCinemaNumber(int cinemaNumber) {
        this.cinemaNumber = cinemaNumber;
    }

    //when we create a booking we want to 1.
    //check which session contains this data
    //check every row in this session if there is a row which contains those number of seats
    //return true if booking is made
    //else we just want to return false and no booking made
    //also check if the session even exists itself
    public boolean createBooking(int numberOfTicketsCreate, Date bookingTimeCreate, int idCreate, int cinemaNumberCreate, ArrayList<Cinema> cinemas,int sessionIndex, int cinemaN) {
        //scan through every session to see if the cinema number requested exists
        //if the cinemanumber is there break can continue
        int cinema = checkCinemaAvailable(cinemas.get(cinemaNumberCreate).getCinemaNumber(), cinemas);

        //if the cinema number doesnt exist return false
        if (cinema == -1)
            return false;
        int getRowFree = cinemas.get(cinema).getSession().get(sessionIndex).availableRow(numberOfTicketsCreate);
        if (getRowFree == -1)
            return false;
        this.startseat = cinemas.get(cinema).getSession().get(sessionIndex).getSessionRows().get(getRowFree).firstFreeSeatInRow();
        //since this booking has met our conditions we want to make this booking successful
        //we want to first make all the seats in the row that are taken reserved
        cinemas.get(cinema).getSession().get(sessionIndex).bookSeats(numberOfTicketsCreate, getRowFree);

        //updating our booking now
        this.bookingTime = bookingTimeCreate;
        this.rownumber = getRowFree;

        this.cinemaNumber = cinemaN;
        this.numberOfTickets = numberOfTicketsCreate;
        this.rowname = cinemas.get(cinema).getSession().get(sessionIndex).getSessionRows().get(rownumber).getRowname();
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

    public boolean changeBooking(int cinemaNumbernew, Date bookingTimenew, int numberOfTicketsnew, ArrayList<Cinema> cinemas,int sessionIndex, int cinemaN) {
        //freeing up our old cinema session seats
        int cinemaOld = checkCinemaAvailable(cinemaNumber,cinemas);
        //keep track of our old session index
        int oldSessionIndex = cinemas.get(cinemaOld).sessionIndexWithBooking(id);

        //now we want to check if the pre-existing booking existed
        //if there was no pre-exisiting booking then these fields would remain unchanged from initialised state

        if (rownumber == -1 || startseat == -1)
            return false;
        //first checks if valid booking for change

        int cinemaChange = checkCinemaAvailable(cinemas.get(cinemaNumbernew).getCinemaNumber(), cinemas);
        if (cinemaChange == -1)
            return false;

        //now checking if there are avilable rows for number of tickets requested
        int getRowFree = cinemas.get(cinemaChange).getSession().get(sessionIndex).availableRow(numberOfTicketsnew);
        if (getRowFree == -1)
            return false;
        this.startseat = cinemas.get(cinemaChange).getSession().get(sessionIndex).getSessionRows().get(getRowFree).firstFreeSeatInRow();
        //now free up our old booking which can be conviently done with the row + start seat of old booking
        cinemas.get(cinemaOld).getSession().get(oldSessionIndex).freeSeats(numberOfTickets, rownumber, startseat);
        //now that old bookings have been reverted and fixed up assign new booking
        cinemas.get(cinemaChange).getSession().get(sessionIndex).bookSeats(numberOfTicketsnew, getRowFree);

        //now we want to go to our sessions, delete the booking from our old session first need old session index
        int oldBookingIndex = cinemas.get(cinemaOld).getSession().get(oldSessionIndex).getBookingIdIndex(id);
        cinemas.get(cinemaOld).getSession().get(oldSessionIndex).getAllBookings().remove(oldBookingIndex);

        this.rownumber = getRowFree;
        this.numberOfTickets = numberOfTicketsnew;
        this.cinemaNumber = cinemaN;
        this.bookingTime = bookingTimenew;
        this.rowname = cinemas.get(cinemaChange).getSession().get(sessionIndex).getSessionRows().get(rownumber).getRowname();
        return true;
    }

    public boolean cancelBooking(ArrayList<Cinema> cinemas,int sessionIndex) {
        //if there is no booking to cancel return false
        if (rownumber == -1 || startseat == -1)
            return false;
        //now want to just free our booking and remove it from session
        int cinemaN = checkCinemaAvailable(cinemaNumber,cinemas);
        int bookingIndex = cinemas.get(cinemaN).getSession().get(sessionIndex).getBookingIdIndex(id);
        cinemas.get(cinemaN).getSession().get(sessionIndex).getAllBookings().remove(bookingIndex);
        cinemas.get(cinemaN).getSession().get(sessionIndex).freeSeats(numberOfTickets, rownumber, startseat);
        return true;
    }


    public int checkCinemaAvailable(int cinemaNumber,ArrayList<Cinema> cinemas) {
        int cinema = -1;
        for (int i = 0; i < cinemas.size(); i++) {
            if (cinemaNumber == cinemas.get(i).getCinemaNumber()) {
                return i;
            }
        }
        return cinema;
    }


}
