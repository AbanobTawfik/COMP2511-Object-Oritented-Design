import java.util.*;

public class Cinema {
    private int cinemaNumber;
    private ArrayList<Rows> rows;
    private ArrayList<Session> session;

    //class constructor for each theatre(cinema) in our movie theatre
    public Cinema(int cinemaNumber) {
        this.cinemaNumber = cinemaNumber;
        //create a new arrayList to hold rows in our cinema which can be added to
        this.rows = new ArrayList<Rows>();
        //creates a new Arraylist to hold sessions in our cinema which can be added to
        this.session = new ArrayList<Session>();
    }

    /**
     * @return ArrayList of all sessions in that cinema
     */
    public ArrayList<Session> getSession() {
        //returns arraylist of sessions in that cinema
        return session;
    }

    /**
     * @return the cinemaNumber of that cinema
     */
    public int getCinemaNumber() {
        //return the cinemaNumber for current cinema object
        return cinemaNumber;
    }

    /**
     * creates a row based on specified input, and adds it to the cinema and all current sessions in the cinema.
     * this function is expected to add the row to the rowlist for the cinema
     * @param rowName       identifier for row expected to be non null string
     * @param numberOfSeats number of seats to add to the row expcected to be valid integer > 0
     */
    //adds a row to the cinema blueprint which will be used for all sessions, it also adds
    //that row to all existing sessions, we want to make a new row to make deeper copies
    public void addRowToCinema(String rowName, int numberOfSeats) {
        //initialise a new row with name and number of seats
        if(numberOfSeats < 1)
            return;
        Rows newRow = new Rows(rowName, numberOfSeats);
        //add new row to our cinema
        rows.add(newRow);
        //add this new row to all our sessions (will be deep copies)
        addRowToAllSessions(newRow);
    }

    /**
     * Returns the index in the ArrayList of sessions, which corresponds to the time requested. <br>
     * Finds which session based on time.
     * This function is expected to return the index in the session list which has the movie time, or -1 if it doesnt exist
     * @param time the time of the session we are searching for, expected to be validly formatted HH:MM date
     * @return the index in the arrayLift of sessions that corresponds to that time
     */
    //will get the index of the session in the session list based on the session time
    public int getSessionTimeIndex(Date time) {
        //initialise return as false incase it is not there
        int index = -1;
        //scan through the session list
        for (int i = 0; i < session.size(); i++) {
            //if the session at index i has the same time as the time requested, return that index
            if (session.get(i).getMovieTime().equals(time))
                return i;
        }
        //otherwise return false unsuccesful
        return index;
    }

    /**
     * This function is designed to get the index in the ArrayList of Sessions which contains the booking id requested.
     * This function is expected to return the index in the session list which contains the booking id, or -1 if it doesnt exist
     * @param id the booking id we are searching for, expected to be a valid integer
     * @return the index in the Arraylist of Sessions which contains our booking Id we are trying to locate
     */
    //this will return the session index in the list of sessions that contains a booking ID
    public int sessionIndexWithBooking(int id) {
        //initialise return as false
        int index = -1;
        //scan through my list of sessions
        for (int i = 0; i < session.size(); i++) {
            //scan through my list of bookings in each session
            for (int j = 0; j < session.get(i).getAllBookings().size(); j++) {
                //if the booking in the session at the current booking index has the same booking id we are
                //trying to find
                //return the index of the sesison (i)
                if (session.get(i).getAllBookings().get(j).getId() == id) {
                    return i;
                }
            }
        }
        //otherwise return false
        return index;
    }

    /**
     * Adds a new session to the current cinema. <br>
     * Making sure to make deep copies of the rows in the session else making a booking will effect
     * the rows in this cinema blueprint and all existing sessions.
     * This function is expected to add the session to the session list for the cinema
     * @param cinemaNumberShowing cinema number for the session, this is expected to be a valid integer
     * @param timeShowing         the time of the session this is expected to be a validly formatted Date HH:MM
     * @param movieShowing        the movie which will be displayed during that session this is expected ot be a non null string
     */
    //adding new session to session list
    public void addSession(int cinemaNumberShowing, Date timeShowing, String movieShowing) {
        //initialise a new session with the movie/time of session and cinemanumber
        Session newSession = new Session(movieShowing, timeShowing, cinemaNumberShowing);

        //want to make deep copies of the rows for session so that
        //if a session is booked in 1 session it is not booked in all other sessions
        //i.e we dont want the rows for our sessions to share the address of our rows in our cinema
        //our cinema rows are only there for structure (scaffolding kind of blueprint)

        //get number of rows we are adding to our session
        int numberOfRows = rows.size();
        //create a new lsit of rows we are adding to our sessions
        ArrayList<Rows> deepCopyRows = new ArrayList<Rows>();
        //now add that many rows which the correct amount of seats and row name (can be found by indexing)
        //so this basically makes deep copies or brand new objects rather than sharing the memory
        //this is why i didnt just do sessions.setrows() == my rows or else it would share same memory adress
        for (int i = 0; i < numberOfRows; i++)
            deepCopyRows.add(new Rows(rows.get(i).getRowName(), rows.get(i).getNumberOfSeatsInRow()));

        //now we want to add each new row to our new session
        for (int i = 0; i < rows.size(); i++)
            //this is why i made the addrowtosession in my sessions class
            newSession.addRowToSession(deepCopyRows.get(i));

        //add this session to the list of all sessions in the cinema
        session.add(newSession);
    }

    /**
     * This function adds a row to all sessions in that Cinema. <br>
     * This is useful because when we add a row to the Cinema later on, we want to also add it to all
     * existing sessions.
     * this function is expected to add the row input to every session in the cinema
     * @param r the row to add, expected to be a non null row with seats
     */
    //this function is designed so that when we add a row to a cinema, we add it to all sessions aswell
    //only makes logical sense that all sessions get the extra row too because a session is just an
    //instance of that cinema at all times
    public void addRowToAllSessions(Rows r) {
        //create a new DEEP copy of row so that we can add to our session
        Rows deepCopyRow = new Rows(r.getRowName(), r.getNumberOfSeatsInRow());
        //scan through session list
        for (int i = 0; i < session.size(); i++)
            //add this row to every session in the session list
            session.get(i).addRowToSession(deepCopyRow);
    }


}
