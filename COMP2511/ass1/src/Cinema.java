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
        Session newSession = new Session(movieShowing, timeShowing, cinemaNumberShowing,rows);
        session.add(newSession);
    }

}
