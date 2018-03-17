import java.util.*;

public class Session {
    //info for the session
    private String movie;
    private Date movietime;
    private int cinemaNumber;
    private ArrayList<bookings> allBookings;
    //since each session is a different instance of cinema
    private ArrayList<Rows> sessionRows = new ArrayList<Rows>();;

    public Session(String movie, Date movietime, int cinemaNumber) {
        this.movie = movie;
        this.movietime = movietime;
        this.cinemaNumber = cinemaNumber;
        this.allBookings = new ArrayList<bookings>();
    }

    public ArrayList<Rows> getSessionRows() {
        return sessionRows;
    }

    public void setSessionRows(ArrayList<Rows> sessionRows) {
        this.sessionRows = sessionRows;
    }

    public ArrayList<bookings> getAllBookings() {
        return allBookings;
    }

    public void setAllBookings(ArrayList<bookings> allBookings) {
        this.allBookings = allBookings;
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


    public int getCinemaNumber() {
        return cinemaNumber;
    }

    public void setCinemaNumber(int cinemaNumber) {
        this.cinemaNumber = cinemaNumber;
    }


    public void addBookingToSession(bookings b) {
        allBookings.add(b);
    }


    public int getBookingIdIndex(int id) {
        int index = -1;
        for (int i = 0; i < allBookings.size(); i++) {
            if (allBookings.get(i).getId() == id) {
                return i;
            }
        }
        return index;
    }

    public void printAllBookings() {
        if (allBookings.size() == 0) {
            System.out.println("No bookings for this session");
            return;
        }
        char currentrowname = '\0';
        //how i want to print out
        //check row character, if the next booking has a different row character print deets no comma
        //
        for (int i = 0; i < allBookings.size(); i++) {
            //if the current booking hasn't got the same rowname as the previous rowname we want to print the new rowname, and a : character
            //eg next line print B: 3-4,
            if (allBookings.get(i).getRowname() != currentrowname) {
                System.out.print(allBookings.get(i).getRowname() + ": ");
                if(allBookings.get(i).getNumberOfTickets() == 1){
                    System.out.print((allBookings.get(i).getStartseat()+1) + ", ");
                }
                else if( i+1 >= allBookings.size()){
                    System.out.println((allBookings.get(i).getStartseat()+1) + "-" + (allBookings.get(i).getStartseat() + allBookings.get(i).getNumberOfTickets()));
                }
                else {
                    System.out.print((allBookings.get(i).getStartseat()+1) + "-" + (allBookings.get(i).getStartseat() + allBookings.get(i).getNumberOfTickets() + ","));
                }
                currentrowname = allBookings.get(i).getRowname();
            }else if(allBookings.get(i+1).getRowname() != currentrowname && i<allBookings.size()){
                if(allBookings.get(i).getNumberOfTickets() == 1){
                    System.out.print((allBookings.get(i).getStartseat()+1) + ",");
                }
                else if( i+1 >= allBookings.size()){
                    System.out.println((allBookings.get(i).getStartseat()+1) + "-" + (allBookings.get(i).getStartseat() + allBookings.get(i).getNumberOfTickets()));
                }
                else {
                    System.out.print((allBookings.get(i).getStartseat()+1) + "-" + (allBookings.get(i).getStartseat() + allBookings.get(i).getNumberOfTickets()));
                }
                System.out.print("\n");
            }else{
                if( i+1 >= allBookings.size()){
                    System.out.println((allBookings.get(i).getStartseat()+1) + "-" + (allBookings.get(i).getStartseat() + allBookings.get(i).getNumberOfTickets()));
                }
                else if(allBookings.get(i).getNumberOfTickets() == 1){
                    System.out.print(allBookings.get(i).getStartseat() + ",");
                }else {
                    System.out.print((allBookings.get(i).getStartseat() +1) + "-" + (allBookings.get(i).getStartseat() + allBookings.get(i).getNumberOfTickets() + ","));
                }
            }
        }
    }


    //returns the first available row for the number of tickets else
    // -1 indicating unsuccesful
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

    public void bookSeats(int numberOfTickets,int rownumber){
        int startIndex = sessionRows.get(rownumber).firstFreeSeatInRow();
        for(int i = startIndex; i < startIndex + numberOfTickets; i++)
            this.sessionRows.get(rownumber).getChairs().get(i).reserveSeat();
        this.sessionRows.get(rownumber).setNumberOfAvailableSeatsInRow(sessionRows.get(rownumber).getNumberOfAvailableSeatsInRow() - numberOfTickets);
    }

    public void freeSeats(int numberOfTickets, int rownumber, int startSeat){
        for(int i = startSeat; i<startSeat + numberOfTickets; i++)
            this.sessionRows.get(rownumber).getChairs().get(i).removeReservedSeat();
        this.sessionRows.get(rownumber).setNumberOfAvailableSeatsInRow(sessionRows.get(rownumber).getNumberOfAvailableSeatsInRow() + numberOfTickets);

    }

    public void addRowToSession(Rows r){
        this.sessionRows.add(r);
    }



}

