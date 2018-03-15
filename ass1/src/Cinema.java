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

    public ArrayList<Rows> getRows() {
        return rows;
    }

    public void setRows(ArrayList<Rows> rows) {
        this.rows = rows;
    }

    //returns the first available row for the number of tickets else
    // -1 indicating unsuccesful
    public int availableRow(int numberOfTickets){

        for(int i = 0; i < rows.size(); i++){
            if(rows.get(i).getNumberOfAvailableSeatsInRow() >= numberOfTickets)
                return i;
        }

        return -1;
    }
    //we want to go to our row
    //find the first available seat index
    //resereve numberOfTickets amount of seats
    //update number of available seats in the row

    public void bookSeats(int numberOfTickets,int rownumber){
        int startIndex = rows.get(rownumber).firstFreeSeatInRow();
        for(int i = startIndex; i < startIndex + numberOfTickets; i++) {
            rows.get(rownumber).getChairs().get(i).reserveSeat();
        }
    }

    public void freeSeats(int numberOfTickets, int rownumber, int startSeat){
        for(int i = startSeat; i<startSeat + numberOfTickets; i++)
            rows.get(rownumber).getChairs().get(i).removeReservedSeat();
        rows.get(rownumber).setNumberOfAvailableSeatsInRow(rows.get(rownumber).getNumberOfAvailableSeatsInRow() + numberOfTickets);

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
        session.add(newSession);
    }

}
