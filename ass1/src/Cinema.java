import java.util.*;
public class Cinema {
    private ArrayList<bookings> allBookings;
    private int cinemaNumber;
    private ArrayList<Rows> rows;

    public Cinema(ArrayList<bookings> allBookings) {
        this.allBookings = allBookings;
        this.cinemaNumber = cinemaNumber;
    }

    public ArrayList<bookings> getAllBookings() {
        return allBookings;
    }

    public void setAllBookings(ArrayList<bookings> allBookings) {
        this.allBookings = allBookings;
    }

    public int getCinemaNumber() {
        return cinemaNumber;
    }

    public void setCinemaNumber(int cinemaNumber) {
        this.cinemaNumber = cinemaNumber;
    }

    public ArrayList<bookings> getallBookings() {
        return allBookings;
    }

    public void setallBookings(ArrayList<bookings> allBookings) {
        this.allBookings = allBookings;
    }
}
