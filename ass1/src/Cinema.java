import java.util.*;
public class Cinema {
    private int cinemaNumber;
    private ArrayList<Rows> rows;
    private ArrayList<Session> session;

    public Cinema(int cinemaNumber) {
        this.cinemaNumber = cinemaNumber;
        this.rows = new ArrayList<Rows>();
        this.session = new ArrayList<Session>();
    }

    /**
     *
     * @return ArrayList of all sessions in that cinema
     */
    public ArrayList<Session> getSession() {
        return session;
    }

    /**
     *
     * @return the cinemaNumber of that cinema
     */
    public int getCinemaNumber() {
        return cinemaNumber;
    }

    /**
     * creates a row based on specified input, and adds it to the cinema and all current sessions in the cinema.
     * @param rowName identifier for row
     * @param numberOfSeats number of seats to add to the row
     */
    public void addRowToCinema(String rowName, int numberOfSeats){
        Rows newRow = new Rows(rowName,numberOfSeats);
        //add new row to our cinema
        rows.add(newRow);
        addRowToAllSessions(newRow);
    }

    /**
     * Returns the index in the ArrayList of sessions, which corresponds to the time requested. <br>
     * Finds which session based on time.
     * @param time the time of the session we are searching for
     * @return the index in the arrayLift of sessions that corresponds to that time
     */
    public int getSessionTimeIndex(Date time){
        int index = -1;
        for(int i = 0; i < session.size();i++){
            if(session.get(i).getMovieTime().equals(time))
                return i;
        }
        return index;
    }

    /**
     * This function is designed to get the index in the ArrayList of Sessions which contains the booking id requested.
     * @param id the booking id we are searching for
     * @return the index in the Arraylist of Sessions which contains our booking Id we are trying to locate
     */
    public int sessionIndexWithBooking(int id){
        int index = -1;

        for(int i = 0;i < session.size(); i++ ){
            for(int j = 0; j<session.get(i).getAllBookings().size(); j++){
                if(session.get(i).getAllBookings().get(j).getId() == id){
                    return i;
                }
            }
        }

        return index;
    }

    /**
     * Adds a new session to the current cinema. <br>
     * Making sure to make deep copies of the rows in the session else making a booking will effect
     * the rows in this cinema blueprint and all existing sessions.
     * @param cinemaNumberShowing cinema number for the session
     * @param timeShowing the time of the session
     * @param movieShowing the movie which will be displayed during that session
     */
    public void addSession(int cinemaNumberShowing, Date timeShowing, String movieShowing) {
        Session newSession = new Session(movieShowing, timeShowing, cinemaNumberShowing);
        //want to make deep copies of the rows for session so all unique
        int numberOfRows = rows.size();
        ArrayList<Rows> deepCopyRows = new ArrayList<Rows>();
        for(int i = 0; i<numberOfRows; i++)
            deepCopyRows.add(new Rows(rows.get(i).getRowName(),rows.get(i).getNumberOfSeatsInRow()));


        for(int i = 0;i<rows.size();i++)
            newSession.addRowToSession(deepCopyRows.get(i));

        session.add(newSession);
    }

    /**
     * This function adds a row to all sessions in that Cinema. <br>
     * This is useful because when we add a row to the Cinema later on, we want to also add it to all
     * existing sessions.
     * @param r the row to add
     */
    public void addRowToAllSessions(Rows r){
        Rows deepCopyRow = new Rows(r.getRowName(),r.getNumberOfSeatsInRow());
        for(int i = 0;i<session.size();i++)
            session.get(i).addRowToSession(deepCopyRow);
    }


}
