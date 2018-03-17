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

    public ArrayList<Session> getSession() {
        return session;
    }

    public void setSession(ArrayList<Session> session) {
        this.session = session;
    }

    public int getCinemaNumber() {
        return cinemaNumber;
    }

    public void setCinemaNumber(int cinemaNumber) {
        this.cinemaNumber = cinemaNumber;
    }



    public void addRowToCinema(char rowname, int numberOfSeats){
        Rows newRow = new Rows(rowname,numberOfSeats);
        //add new row to our cinema
        rows.add(newRow);
        addRowToAllSessions(newRow);
    }

    public void addSessionToCurrentCinema(Session s){
        session.add(s);
    }


    //add new row to the session
    public void addRow(Rows r){
        rows.add(r);
    }



    public int getSessionTimeIndex(Date time){
        int index = -1;
        for(int i = 0; i < session.size();i++){
            if(session.get(i).getMovietime().equals(time))
                return i;
        }
        return index;
    }

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

    public void addSession(int cinemaNumberShowing, Date timeShowing, String movieShowing) {
        Session newSession = new Session(movieShowing, timeShowing, cinemaNumberShowing);
        //want to make deep copies of the rows for session so all unique
        int numberOfRows = rows.size();
        ArrayList<Rows> deepCopyRows = new ArrayList<Rows>();
        for(int i = 0; i<numberOfRows; i++)
            deepCopyRows.add(new Rows(rows.get(i).getRowname(),rows.get(i).getNumberOfSeatsInRow()));


        for(int i = 0;i<rows.size();i++)
            newSession.addRowToSession(deepCopyRows.get(i));

        session.add(newSession);
    }

    public void addRowToAllSessions(Rows r){
        Rows deepCopyRow = new Rows(r.getRowname(),r.getNumberOfSeatsInRow());
        for(int i = 0;i<session.size();i++)
            session.get(i).addRowToSession(deepCopyRow);
    }


}
