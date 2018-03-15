import java.io.*;
import java.text.*;
import java.util.*;

public class operate {
    /**
     * for reading input from the file i want to take out the important keywords and their format
     * when scanner detects keywords as
     * Cinema it will read the next input as cinema number to alter
     * it will read the input after that as the row to add
     * it will read the input after that as the number of seats in that row
     * <p>
     * when the # char is detected it will cease to use that input rather as comments
     * then it will continue till the next keyword is found
     * list of keywords
     * Cinema, Session, Request, Change, Cancel, Print
     */


    public void runTests(String FileInput)

    {
        ArrayList<Cinema> cinema = new ArrayList<Cinema>();
        Scanner sc = null;
        try {
            sc = new Scanner(new File(FileInput));
            String line = sc.nextLine();
            while (sc.hasNextLine()) {

                String[] data = line.split("#");                      //split our keywords and input info from comment
                char checker[] = data[0].toCharArray();                    //if a line is a comment skip
                if(checker.length == 0 || checker.equals(null)){
                    line = sc.nextLine();                                      //get next line for input
                    continue;
                }
                if (checker[0] == '#') {
                    line = sc.nextLine();                                      //get next line for input
                    continue;
                }
                String[] inputs = data[0].split(" ");
                int inputcommands = inputs.length;
                if(inputcommands == 0) {
                    line = sc.nextLine();                                      //get next line for input
                    continue;
                }
                printoutcome(inputcommands, inputs, cinema);                 //run the outcome function which prints outcome based on the input
                line = sc.nextLine();                                      //get next line for input
            }

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } finally {
            if (sc != null) sc.close();
        }
    }

    public int getCinemaIndex(ArrayList<Cinema> cinema, int cinemaNumber) {
        int index = -1;
        for (int i = 0; i < cinema.size(); i++) {
            if (cinema.get(i).getCinemaNumber() == cinemaNumber) {
                index = i;
                break;
            }
        }

        return index;
    }

    public int getBookingIdCinema(ArrayList<Cinema> cinema, int id) {
        int index = -1;
        for (int i = 0; i < cinema.size(); i++) {
            for (int j = 0; j < cinema.get(i).getSession().size(); j++) {
                if (id == cinema.get(i).getSession().get(j).getBookingIdIndex(id)) {
                    return i;
                }
            }
        }

        return index;
    }

    public void printoutcome(int inputcommands, String inputs[], ArrayList<Cinema> cinema) {
        if (inputs[0].equals("Cancel"))
            outcomeCancel(inputs, cinema);

        else if (inputs[0].equals("Print"))
            outcomePrint(inputs, cinema);

        else if (inputs[0].equals("Cinema"))
            outcomeCinema(inputs, cinema);

        else if (inputs[0].equals("Session"))
            outcomeSession(inputs, cinema);

        else if (inputs[0].equals("Request"))
            outcomeBooking(inputs, cinema);

        else if (inputs[0].equals("Change"))
            outcomeChange(inputs, cinema);
        else
            return;
    }

    public void outcomeCancel(String inputs[], ArrayList<Cinema> cinema) {
        int bookingId = Integer.parseInt(inputs[1]);                                //get the cinema which contains the booking ID requested to cancel
        int cinemaWithBookingIdIndex = getBookingIdCinema(cinema, bookingId);
        int sessionindex = cinema.get(cinemaWithBookingIdIndex).sessionIndexWithBooking(bookingId);

        if (cinemaWithBookingIdIndex == -1) {
            System.out.println("Cancel Rejected");                                  //if the booking id doesn't exist in any cinema then we print error message
            return;
        }
        //now we need to find the index of the booking in the cinema's list of bookings
        int bookingIndex = cinema.get(cinemaWithBookingIdIndex).getSession().get(sessionindex).getBookingIdIndex(bookingId);
        if (bookingIndex == -1) {
            System.out.println("Cancel Rejected");                                  //if the booking somehow doesnt exist ??? print error message and continue
            return;
        }
        boolean cancelled;                                                          //now we have the bookingIndex and the CinemaIndex lets remove that booking
        cancelled = cinema.get(cinemaWithBookingIdIndex).getSession().get(sessionindex).getAllBookings().get(bookingIndex).cancelBooking(cinema);
        if (cancelled == false) {
            System.out.println("Cancel Rejected");
            return;
        } else {
            System.out.printf("Cancel " + bookingId);
        }

    }

    public void outcomePrint(String inputs[], ArrayList<Cinema> cinema) {
        //printing we print all bookings for the cinema number + time of session
        int cinemaN = Integer.parseInt(inputs[1]);
        int cinemaNumber = getCinemaIndex(cinema, cinemaN);
        DateFormat formatting = new SimpleDateFormat("HH:mm");
        Date movieTime = null;
        try {
            movieTime = formatting.parse(inputs[2]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int sessionindex = cinema.get(cinemaNumber).getSessionTimeIndex(movieTime);
        System.out.println(cinema.get(cinemaNumber).getSession().get(sessionindex).getMovie());
        cinema.get(cinemaNumber).getSession().get(sessionindex).printAllBookings();

    }
    //Cinema input adds row to an existing cinema
    //if cinema doesn't exist create Cinema with cinema number
    //then add the row to it

    public void outcomeCinema(String inputs[], ArrayList<Cinema> cinema) {
        int cinemaN = Integer.parseInt(inputs[1]);
        int cinemaNumber = getCinemaIndex(cinema, cinemaN);
        char rowName[] = inputs[2].toCharArray();
        int numberOfSeats = Integer.parseInt(inputs[3]);
        //want to create cinema out of this
        //if the cinema doesnt exist create the cinema else
        //find the cinema and add the row to the cinema
        if (cinemaNumber == -1) {

            Cinema createCinema = new Cinema(cinemaN);
            createCinema.addRowToCinema(rowName[0], numberOfSeats);
            cinema.add(createCinema);

        } else {
            cinema.get(cinemaNumber).addRowToCinema(rowName[0], numberOfSeats);
        }
    }

    public void outcomeSession(String inputs[], ArrayList<Cinema> cinema) {
        int cinemaN = Integer.parseInt(inputs[1]);
        int cinemaNumber = getCinemaIndex(cinema, cinemaN);
        DateFormat formatting = new SimpleDateFormat("HH:mm");
        Date movieTime = null;
        try {
            movieTime = formatting.parse(inputs[2]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String movieShowing = inputs[3];

        //we want to create a booking for that cinema during that session so first we want to get the session
        cinema.get(cinemaNumber).addSession(cinemaNumber, movieTime, movieShowing);
    }

    public void outcomeBooking(String inputs[], ArrayList<Cinema> cinema) {
        int bookingId = Integer.parseInt(inputs[1]);
        int cinemaN = Integer.parseInt(inputs[1]);
        int cinemaNumber = getCinemaIndex(cinema, cinemaN);
        DateFormat formatting = new SimpleDateFormat("HH:mm");
        Date movieTime = null;
        try {
            movieTime = formatting.parse(inputs[3]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int numberOfTickets = Integer.parseInt(inputs[4]);
        int sessionindex = cinema.get(cinemaNumber).getSessionTimeIndex(movieTime);
        bookings b = new bookings(0,null,0,0);
        boolean check = b.createBooking(numberOfTickets, movieTime, bookingId, cinemaNumber, cinema);
        if (check == false) {
            System.out.println("Booking Rejected");
            return;
        }
        cinema.get(cinemaNumber).getSession().get(sessionindex).addBookingToSession(b);
        int rowindex = cinema.get(cinemaNumber).getSession().get(sessionindex).getBookingIdIndex(bookingId);
        int bookingIndex = cinema.get(cinemaNumber).getSession().get(sessionindex).getBookingIdIndex(bookingId);
        char rowchar = cinema.get(cinemaNumber).getSession().get(sessionindex).getAllBookings().get(bookingIndex).getRowname();
        int seatStart = cinema.get(cinemaNumber).getSession().get(sessionindex).getAllBookings().get(rowindex).getStartseat();
        int seatEnd = seatStart + numberOfTickets;
        System.out.println("Booking " + bookingId + " " + rowchar + (seatStart+1) + "-" +rowchar+seatEnd);
    }

    public void outcomeChange(String inputs[], ArrayList<Cinema> cinema) {
        int bookingId = Integer.parseInt(inputs[1]);
        int cinemaN = Integer.parseInt(inputs[1]);
        int cinemaNumber = getCinemaIndex(cinema, cinemaN);
        DateFormat formatting = new SimpleDateFormat("HH:mm");
        Date movieTime = null;
        try {
            movieTime = formatting.parse(inputs[2]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int numberOfTickets = Integer.parseInt(inputs[3]);
        int sessionindex = cinema.get(cinemaNumber).getSessionTimeIndex(movieTime);
        int bookingIndexId = cinema.get(cinemaNumber).getSession().get(sessionindex).getBookingIdIndex(bookingId);
        bookings b = cinema.get(cinemaNumber).getSession().get(sessionindex).getAllBookings().get(bookingIndexId);
        boolean check = b.changeBooking(cinemaNumber,movieTime,numberOfTickets,cinema);
        if(check == false){
            System.out.println("Cancel rejected");
            return;
        }
        int rowindex = cinema.get(cinemaNumber).getSession().get(sessionindex).getBookingIdIndex(bookingId);
        char rowchar = cinema.get(cinemaNumber).getSession().get(sessionindex).getAllBookings().get(rowindex).getRowname();
        int seatStart = cinema.get(cinemaNumber).getSession().get(sessionindex).getAllBookings().get(rowindex).getStartseat();
        int seatEnd = seatStart + numberOfTickets;
        System.out.println("Cancel " + bookingId + " " + rowchar + seatStart + "-" +seatEnd);
    }
}
