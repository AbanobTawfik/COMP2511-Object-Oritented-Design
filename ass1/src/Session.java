import java.util.*;

public class Session {
    //info for the session
    private String movie;
    private Date movieTime;
    private int cinemaNumber;
    private ArrayList<bookings> allBookings;
    //since each session is a different instance of cinema
    private ArrayList<Rows> sessionRows = new ArrayList<Rows>();;

    //constructor for our class
    public Session(String movie, Date movieTime, int cinemaNumber) {
        this.movie = movie;
        this.movieTime = movieTime;
        this.cinemaNumber = cinemaNumber;
        //initalise each new session with an empty bookings list which can be added to
        this.allBookings = new ArrayList<bookings>();
    }

    /**
     *
     * @return Arraylist of rows in the current session
     */
    public ArrayList<Rows> getSessionRows() {
        //return the ArrayList of rows for the session
        return sessionRows;
    }

    /**
     *
     * @return Arraylist of all bookings in the current session
     */
    public ArrayList<bookings> getAllBookings() {
        //return ArrayList of bookings for the session
        return allBookings;
    }

    /**
     *
     * @return name of the movie
     */
    public String getMovie() {
        //return the name of the movie showing at this session
        return movie;
    }

    /**
     *
     * @return the Date in which the movie is showing
     */
    public Date getMovieTime() {
        //return the HH:MM time for the session
        return movieTime;
    }


    /**
     * This function adds a booking to the ArrayList of all bookings in the current session
     * @param b the booking which is to be added to the session
     */
    public void addBookingToSession(bookings b) {
        //adds a booking to the current session
        allBookings.add(b);

    }

    /**
     * This method will sort all bookings by rowName to allow print bookings to display
     * each booking row by row rather than having a situation of a booking in row a, booking in row b
     * then another booking in row a, will make printing properly imposisble. This will be called everytime
     * a new booking is added/changed/removed
     */
    public void sortAllBookingsByRowName(){
        //sort the bookings by row name each time to allow for easier print
        Collections.sort(allBookings, new Comparator<bookings>() {
            @Override
            public int compare(bookings o1, bookings o2) {
                //comparing rownames
                return o1.getRowName().compareTo(o2.getRowName());
            }
        });
    }


    /**
     * This function returns the index in the ArrayList of all bookings which corresponds to the booking id attempting to be located.
     *
     * @param id id which is attempting to be located
     * @return the index in the Arraylist of bookings which has that id, or -1 if unsuccessful (doesn't exist)
     */
    public int getBookingIdIndex(int id) {
        //initialise the return as FALSE
        int index = -1;
        //now we want to scan through the list of bookings to find the index witht he booking id
        for (int i = 0; i < allBookings.size(); i++) {
            //if the booking at index i in the arraylist of bookings has the same id as the id we are looking for
            //return that index
            if (allBookings.get(i).getId() == id) {
                return i;
            }
        }
        //otherwise return the initalised FALSE return
        return index;
    }

    /**
     * This function displays all the bookings in the current sessions <br>
     * This function displays all the rows which contain a booking and the seat range of booked seats.
     */
    /**
     * rules for print
     * 1. it will print bookings row by row.
     * 2. if there are no more bookings on a row, no comma is placed and new line
     * 3. if the booking is the last booking print new line no comma end it
     * 4. once new row is accessed we want to print the row name and all the booked seats
     */
    public void printAllBookings() {
        //if there are no bookings for the session we print no bookings for this session
        if (allBookings.size() == 0) {
            System.out.println("No bookings for this session");
            return;
        }
        //have an initialised null comparator to initially compare our row name with the null character
        String currentrowName = "\0";
        //how i want to print out
        //check row character, if the next booking has a different row character print out the row range, new line no comma
        //check row character, if the next booking is the last booking print it out on new line with new row character no comma
        //check row character, if the booking is on the same row, print out booking with comma
        //going to go through each check


        for (int i = 0; i < allBookings.size(); i++) {
            //if the current booking hasn't got the same rowName as the previous rowName we want to print the new rowName
            // and a : character
            // eg next line print B: 3-4,
            if (allBookings.get(i).getRowName() != currentrowName) {
                System.out.print(allBookings.get(i).getRowName() + ": ");
                //if its a single ticket dont need row range just print the seat and comma
                if(allBookings.get(i).getNumberOfTickets() == 1){
                    if(allBookings.get(i+1).getRowName() != allBookings.get(i).getRowName())
                        System.out.println((allBookings.get(i).getStartSeat()+1));
                    else
                        System.out.print((allBookings.get(i).getStartSeat()+1) + ", ");
                }
                //if this is the last booking we dont want to print out comma at end of line
                //OR if the next booking is on a different row want to print it and go to new line
                else if( i+1 >= allBookings.size() || allBookings.get(i+1).getRowName() != allBookings.get(i).getRowName()){
                    //if the booking is a single ticket we want to just print the seat not range
                    if(allBookings.get(i).getNumberOfTickets() == 1)
                        System.out.println(allBookings.get(i).getStartSeat()+1);
                    else
                        //otherwise print out the range without comma
                        System.out.println((allBookings.get(i).getStartSeat()+1) + "-" + (allBookings.get(i).getStartSeat() + allBookings.get(i).getNumberOfTickets()));
                }

                //otherwise we want to just print out the range with the comma
                else {
                    System.out.print((allBookings.get(i).getStartSeat()+1) + "-" + (allBookings.get(i).getStartSeat() + allBookings.get(i).getNumberOfTickets() + ","));
                }
                //update our currentrowname checker
                currentrowName = allBookings.get(i).getRowName();
            }
            //if the next row haa a different character we want to print our seat range and a new line
            else if(i<allBookings.size()-1 && allBookings.get(i+1).getRowName() != currentrowName  ){
                //if there is only 1 seat in the booking print the seat at end of the row
                if(allBookings.get(i).getNumberOfTickets() == 1){
                    System.out.print((allBookings.get(i).getStartSeat()+1));
                }
                //otherwise we want to print the seat range
                else {
                    System.out.print((allBookings.get(i).getStartSeat()+1) + "-" + (allBookings.get(i).getStartSeat() + allBookings.get(i).getNumberOfTickets()));
                }
                //print out new line at end to start new row
                System.out.print("\n");
            }else{
                //otheriwse if its the last booking
                if( i+1 >= allBookings.size()){
                    //if its a single seat print out just the seat itself no comma new line
                    if(allBookings.get(i).getNumberOfTickets() == 1){
                        System.out.print((allBookings.get(i).getStartSeat()+1));
                    }else {
                        //or print out the seat range no comma new line
                        System.out.println((allBookings.get(i).getStartSeat() + 1) + "-" + (allBookings.get(i).getStartSeat() + allBookings.get(i).getNumberOfTickets()));
                    }
                }
                //otherwise we just want to append the new rage of tickets to the current row
                else if(allBookings.get(i).getNumberOfTickets() == 1){
                    //appending single ticket to current row comma for next booking
                    System.out.print(allBookings.get(i).getStartSeat() + ",");
                }else {
                    //appending range of ticket to current row comma for next booking
                    System.out.print((allBookings.get(i).getStartSeat() +1) + "-" + (allBookings.get(i).getStartSeat() + allBookings.get(i).getNumberOfTickets() + ","));
                }
            }

        }
    }



    /**
     * This function returns the first available row which can handle the capacity of tickets requested
     * @param numberOfTickets number of tickets which are being booked
     * @return the index of the first row in the ArrayList of rows which can hold that many tickets
     */
    //returns the first available row for the number of tickets else
    // -1 indicating unsuccesful
    public int availableRow(int numberOfTickets){
        //scan through entire arraylist of rows in the session
        for(int i = 0; i < sessionRows.size(); i++){
            //if there is a row that has more or equal available seats compared to number of tickets requested
            if(sessionRows.get(i).getNumberOfAvailableSeatsInRow() >= numberOfTickets)
                //return the index of that row
                return i;
        }
        //otherwise return false (-1 flag false)
        return -1;
    }


    /**
     * This function books seats in the session by reserving the amount of seats requested in the first available row.
     * <br> This function finds the first open seat and begins to book that many adjacent seats (reserves them).
     * @param numberOfTickets the number of seats being booked
     * @param rownumber the row in which we are booking the seats for
     */
    //we want to go to our row
    //find the first available seat index
    //resereve numberOfTickets amount of seats
    //update number of available seats in the row
    public void bookSeats(int numberOfTickets,int rownumber){
        //find the first free seat in the row
        int startIndex = sessionRows.get(rownumber).firstFreeSeatInRow();
        //start at the first free seat and go till u've reserved the amount of seats == number of tickets
        for(int i = startIndex; i < startIndex + numberOfTickets; i++)
            //reserve the chair at the correct index
            this.sessionRows.get(rownumber).getChairs().get(i).reserveSeat();
        //update the number of available seats in the row
        this.sessionRows.get(rownumber).setNumberOfAvailableSeatsInRow(sessionRows.get(rownumber).getNumberOfAvailableSeatsInRow() - numberOfTickets);
    }

    /**
     * This function is the opposite of book seats. This function will unreserve the seats from the start seat till it has unreserved
     * numberOfTickets of seats in the row requested (used in conjunction with cancel/change requests)
     * @param numberOfTickets number of seats we are unreserving
     * @param rownumber the row in which we are unreserving seats
     * @param startSeat the first seat which we begin freeing
     */
    //this function is the opposite of book seats but requires startSeat because we are
    //de-assigning seats based on a booking
    public void freeSeats(int numberOfTickets, int rownumber, int startSeat){
        //from the start seat -> number of tickets worth seats we want to unreserve seats
        for(int i = startSeat; i<startSeat + numberOfTickets; i++)
            //unreserve the seat -> from true->false reservation status
            this.sessionRows.get(rownumber).getChairs().get(i).removeReservedSeat();
        //now we want to update the number of available seats
        this.sessionRows.get(rownumber).setNumberOfAvailableSeatsInRow(sessionRows.get(rownumber).getNumberOfAvailableSeatsInRow() + numberOfTickets);

    }

    /**
     * This function will add a row to the session (used when we are adding row to cinema and then adding row to all sessions)
     * @param r the row we are adding
     */
    public void addRowToSession(Rows r){
        //adds a row to the arraylist of row (useful when we add rows to our cinema object)
        this.sessionRows.add(r);
    }



}

