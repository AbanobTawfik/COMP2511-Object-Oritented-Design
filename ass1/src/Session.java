import java.util.*;

public class Session {
    //info for the session
    private String movie;
    private Date movietime;
    private int cinemaNumber;
    private ArrayList<bookings> allBookings;

    public Session(String movie, Date movietime, int cinemaNumber) {
        this.movie = movie;
        this.movietime = movietime;
        this.cinemaNumber = cinemaNumber;
        this.allBookings = new ArrayList<bookings>();
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

    public boolean bookingIdInCinema(int bookingId) {
        for (int i = 0; i < allBookings.size(); i++) {
            if (allBookings.get(i).getId() == bookingId) {
                return true;
            }
        }
        return false;
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
                    System.out.print(allBookings.get(i).getStartseat() + ", ");
                }else {
                    System.out.print(allBookings.get(i).getStartseat() + "-" + (allBookings.get(i).getStartseat() + allBookings.get(i).getNumberOfTickets() + ", "));
                }
                currentrowname = allBookings.get(i).getRowname();
            }else if(allBookings.get(i+1).getRowname() != currentrowname && i<allBookings.size()-1){
                if(allBookings.get(i).getNumberOfTickets() == 1){
                    System.out.print(allBookings.get(i).getStartseat() + ", ");
                }else {
                    System.out.print(allBookings.get(i).getStartseat() + "-" + (allBookings.get(i).getStartseat() + allBookings.get(i).getNumberOfTickets()));
                }
                System.out.print("\n");
            }else{
                if(allBookings.get(i).getNumberOfTickets() == 1){
                    System.out.print(allBookings.get(i).getStartseat() + ", ");
                }else {
                    System.out.print(allBookings.get(i).getStartseat() + "-" + (allBookings.get(i).getStartseat() + allBookings.get(i).getNumberOfTickets() + ", "));
                }
            }
        }
    }

}

