import java.util.*;
public class Session {
    private String movie;
    private Date movietime;
    private ArrayList<Rows> rows;
    private int cinemaNumber;

    public Session(String movie, Date movietime, ArrayList<Rows> rows, int cinemaNumber) {
        this.movie = movie;
        this.movietime = movietime;
        this.rows = rows;
        this.cinemaNumber = cinemaNumber;
    }

    public String getMovie() {
        return movie;
    }

    public void setMovie(String movie) {
        this.movie = movie;
    }

    public Date getMovietime() {
        return movietime;
    }

    public void setMovietime(Date movietime) {
        this.movietime = movietime;
    }

    public ArrayList<Rows> getRows() {
        return rows;
    }

    public void setRows(ArrayList<Rows> rows) {
        this.rows = rows;
    }

    public int getCinemaNumber() {
        return cinemaNumber;
    }

    public void setCinemaNumber(int cinemaNumber) {
        this.cinemaNumber = cinemaNumber;
    }

    //add new row to the session
    public void addRow(Rows r){
        rows.add(r);
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
        rows.get(rownumber).setNumberOfAvailableSeatsInRow(rows.get(rownumber).getNumberOfAvailableSeatsInRow() - numberOfTickets);
    }

    public void freeSeats(int numberOfTickets, int rownumber, int startSeat){
        for(int i = startSeat; i<startSeat + numberOfTickets; i++)
            rows.get(rownumber).getChairs().get(i).removeReservedSeat();
        rows.get(rownumber).setNumberOfAvailableSeatsInRow(rows.get(rownumber).getNumberOfAvailableSeatsInRow() + numberOfTickets);

    }
}

