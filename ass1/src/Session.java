import java.util.*;

public class Session {
    //info for the session
    private String movie;
    private Date movieTime;
    private int cinemaNumber;
    private ArrayList<bookings> allBookings;
    //since each session is a different instance of cinema
    private ArrayList<Rows> sessionRows = new ArrayList<Rows>();;

    public Session(String movie, Date movieTime, int cinemaNumber) {
        this.movie = movie;
        this.movieTime = movieTime;
        this.cinemaNumber = cinemaNumber;
        this.allBookings = new ArrayList<bookings>();
    }

    /**
     *
     * @return Arraylist of rows in the current session
     */
    public ArrayList<Rows> getSessionRows() {
        return sessionRows;
    }

    /**
     *
     * @return Arraylist of all bookings in the current session
     */
    public ArrayList<bookings> getAllBookings() {
        return allBookings;
    }

    /**
     *
     * @return name of the movie
     */
    public String getMovie() {
        return movie;
    }

    /**
     *
     * @return the Date in which the movie is showing
     */
    public Date getMovieTime() {
        return movieTime;
    }


    /**
     * This function adds a booking to the ArrayList of all bookings in the current session
     * @param b the booking which is to be added to the session
     */
    public void addBookingToSession(bookings b) {
        allBookings.add(b);
    }

    /**
     * This function returns the index in the ArrayList of all bookings which corresponds to the booking id attempting to be located.
     *
     * @param id id which is attempting to be located
     * @return the index in the Arraylist of bookings which has that id, or -1 if unsuccessful (doesn't exist)
     */
    public int getBookingIdIndex(int id) {
        int index = -1;
        for (int i = 0; i < allBookings.size(); i++) {
            if (allBookings.get(i).getId() == id) {
                return i;
            }
        }
        return index;
    }

    /**
     * This function displays all the bookings in the current sessions <br>
     * This function displays all the rows which contain a booking and the seat range of booked seats.
     */
    public void printAllBookings() {
        if (allBookings.size() == 0) {
            System.out.println("No bookings for this session");
            return;
        }
        String currentrowName = "\0";
        //how i want to print out
        //check row character, if the next booking has a different row character print deets no comma
        //
        for (int i = 0; i < allBookings.size(); i++) {
            //if the current booking hasn't got the same rowName as the previous rowName we want to print the new rowName, and a : character
            //eg next line print B: 3-4,
            if (allBookings.get(i).getRowName() != currentrowName) {
                System.out.print(allBookings.get(i).getRowName() + ": ");
                if(allBookings.get(i).getNumberOfTickets() == 1){
                    System.out.print((allBookings.get(i).getStartSeat()+1) + ", ");
                }
                else if( i+1 >= allBookings.size()){
                    System.out.println((allBookings.get(i).getStartSeat()+1) + "-" + (allBookings.get(i).getStartSeat() + allBookings.get(i).getNumberOfTickets()));
                }
                else {
                    System.out.print((allBookings.get(i).getStartSeat()+1) + "-" + (allBookings.get(i).getStartSeat() + allBookings.get(i).getNumberOfTickets() + ","));
                }
                currentrowName = allBookings.get(i).getRowName();
            }else if(allBookings.get(i+1).getRowName() != currentrowName && i<allBookings.size()){
                if(allBookings.get(i).getNumberOfTickets() == 1){
                    System.out.print((allBookings.get(i).getStartSeat()+1) + ",");
                }
                else if( i+1 >= allBookings.size()){
                    System.out.println((allBookings.get(i).getStartSeat()+1) + "-" + (allBookings.get(i).getStartSeat() + allBookings.get(i).getNumberOfTickets()));
                }
                else {
                    System.out.print((allBookings.get(i).getStartSeat()+1) + "-" + (allBookings.get(i).getStartSeat() + allBookings.get(i).getNumberOfTickets()));
                }
                System.out.print("\n");
            }else{
                if( i+1 >= allBookings.size()){
                    System.out.println((allBookings.get(i).getStartSeat()+1) + "-" + (allBookings.get(i).getStartSeat() + allBookings.get(i).getNumberOfTickets()));
                }
                else if(allBookings.get(i).getNumberOfTickets() == 1){
                    System.out.print(allBookings.get(i).getStartSeat() + ",");
                }else {
                    System.out.print((allBookings.get(i).getStartSeat() +1) + "-" + (allBookings.get(i).getStartSeat() + allBookings.get(i).getNumberOfTickets() + ","));
                }
            }
        }
    }


    //returns the first available row for the number of tickets else
    // -1 indicating unsuccesful

    /**
     * This function returns the first available row which can handle the capacity of tickets requested
     * @param numberOfTickets number of tickets which are being booked
     * @return the index of the first row in the ArrayList of rows which can hold that many tickets
     */
    public int availableRow(int numberOfTickets){

        for(int i = 0; i < sessionRows.size(); i++){
            if(sessionRows.get(i).getNumberOfAvailableSeatsInRow() >= numberOfTickets)
                return i;
        }

        return -1;
    }
    //we want to go to our row
    //find the first available seat index
    //resereve numberOfTickets amount of seats
    //update number of available seats in the row

    /**
     * This function books seats in the session by reserving the amount of seats requested in the first available row.
     * <br> This function finds the first open seat and begins to book that many adjacent seats (reserves them).
     * @param numberOfTickets the number of seats being booked
     * @param rownumber the row in which we are booking the seats for
     */
    public void bookSeats(int numberOfTickets,int rownumber){
        int startIndex = sessionRows.get(rownumber).firstFreeSeatInRow();
        for(int i = startIndex; i < startIndex + numberOfTickets; i++)
            this.sessionRows.get(rownumber).getChairs().get(i).reserveSeat();
        this.sessionRows.get(rownumber).setNumberOfAvailableSeatsInRow(sessionRows.get(rownumber).getNumberOfAvailableSeatsInRow() - numberOfTickets);
    }

    /**
     * This function is the opposite of book seats. This function will unreserve the seats from the start seat till it has unreserved
     * numberOfTickets of seats in the row requested (used in conjunction with cancel/change requests)
     * @param numberOfTickets number of seats we are unreserving
     * @param rownumber the row in which we are unreserving seats
     * @param startSeat the first seat which we begin freeing
     */
    public void freeSeats(int numberOfTickets, int rownumber, int startSeat){
        for(int i = startSeat; i<startSeat + numberOfTickets; i++)
            this.sessionRows.get(rownumber).getChairs().get(i).removeReservedSeat();
        this.sessionRows.get(rownumber).setNumberOfAvailableSeatsInRow(sessionRows.get(rownumber).getNumberOfAvailableSeatsInRow() + numberOfTickets);

    }

    /**
     * This function will add a row to the session (used when we are adding row to cinema and then adding row to all sessions)
     * @param r the row we are adding
     */
    public void addRowToSession(Rows r){
        this.sessionRows.add(r);
    }



}

