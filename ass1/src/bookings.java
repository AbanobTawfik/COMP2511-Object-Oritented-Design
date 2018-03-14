import java.util.*;

public class bookings {
    private int numberOfTickets;
    private Date bookingTime;
    private int id;
    private int cinemaNumber;
    private int rownumber;          // <--- essential to keep track of our booked seats
    private int startseat;
    private ArrayList<Session> session;



    public bookings(int numberOfTickets, Date bookingTime, int id, int cinemaNumber, ArrayList<Session> session) {
        this.numberOfTickets = numberOfTickets;
        this.bookingTime = bookingTime;
        this.id = id;
        this.cinemaNumber = cinemaNumber;
        this.session = session;
        this.rownumber = -1;
        this.startseat = -1;
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

    public ArrayList<Session> getSession() {
        return session;
    }

    public void setSession(ArrayList<Session> session) {
        this.session = session;
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
    public boolean createBooking(int numberOfTicketsCreate, Date bookingTimeCreate, int idCreate, int cinemaNumberCreate){
        //scan through every session to see if the cinema number requested exists
        //if the cinemanumber is there break can continue
        int cinema = checkCinemaAvailable(cinemaNumberCreate);

        //if the cinema number doesnt exist return false
        if(cinema == -1)
            return false;
        int getRowFree = session.get(cinema).availableRow(numberOfTicketsCreate);
        if(getRowFree == -1)
            return false;
        //since this booking has met our conditions we want to make this booking successful
        //we want to first make all the seats in the row that are taken reserved
        session.get(cinema).bookSeats(numberOfTicketsCreate,getRowFree);

        //updating our booking now
        bookingTime = bookingTimeCreate;
        rownumber = getRowFree;
        startseat = session.get(cinema).getRows().get(getRowFree).firstFreeSeatInRow();
        cinemaNumber = cinema;
        numberOfTickets = numberOfTicketsCreate;

        return true;
    }

    //to change booking all we want to do is
    //check if the cinema number exists
    //check if available row is there
    //check if available row has seats
    //if the row has seats change the booking and MAKE SURE TO FREE THE OLD SEATS TO UNRESERVED
    //want to also make sure we can access bookings
    //also check if booking exists in first place xD

    public boolean changeBooking(int id, int cinemaNumbernew, Date bookingTimenew, int numberOfTicketsnew){
        //now we want to check if the pre-existing booking existed
        //if there was no pre-exisiting booking then these fields would remain unchanged from initialised state

        if(rownumber == -1 || startseat == -1)
            return false;
        //first checks if valid booking for change
        int cinema = checkCinemaAvailable(cinemaNumbernew);
        if(cinema == -1)
            return false;
        //now checking if there are avilable rows for number of tickets requested
        int getRowFree = session.get(cinema).availableRow(numberOfTicketsnew);

        if(getRowFree == -1)
            return false;

        //now free up our old booking which can be conviently done with the row + start seat of old booking
        session.get(cinema).freeSeats(numberOfTickets, getRowFree, startseat);
        //now that old bookings have been reverted and fixed up assign new booking
        session.get(cinema).bookSeats(numberOfTicketsnew,getRowFree);

        rownumber = getRowFree;
        numberOfTickets = numberOfTicketsnew;
        startseat = session.get(cinema).getRows().get(getRowFree).firstFreeSeatInRow();
        cinemaNumber = cinema;
        bookingTime = bookingTimenew;
        return true;
    }

    public boolean cancelBooking(){
        //if there is no booking to cancel return false
        if(rownumber == -1 || startseat == -1)
            return false;
        //now want to just free our booking
        session.get(cinemaNumber).freeSeats(numberOfTickets,rownumber,startseat);
        return true;
    }


    public int checkCinemaAvailable(int cinemaNumber){
        int cinema = -1;
        for(int i = 0; i < session.size();i++){
            if(cinemaNumber == session.get(i).getCinemaNumber() && bookingTime == session.get(i).getMovietime()) {
                cinema = i;
                break;
            }
        }
        return cinema;
    }

}
