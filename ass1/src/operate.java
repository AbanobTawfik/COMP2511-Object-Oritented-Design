import java.io.*;
import java.text.*;
import java.util.*;

public class operate {
    /*
     * for reading input from the file i want to take out the important keywords and their format
     * when scanner detects keywords as
     * Cinema it will read the next input as cinema number to alter
     * it will read the input after that as the row to add
     * it will read the input after that as the number of seats in that row
     * when the # char is detected it will cease to use that input rather as comments
     * then it will continue till the next keyword is found
     * list of keywords
     * Cinema, Session, Request, Change, Cancel, Print
     */

    /**
     * This function runs the testFile and prints output to stdout.
     * All testing is done here.
     * @param FileInput the file which is going to be read in.
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
                if (checker.length == 0 || checker.equals(null)) {
                    line = sc.nextLine();                                      //get next line for input
                    continue;
                }
                if (checker[0] == '#') {
                    line = sc.nextLine();                                      //get next line for input
                    continue;
                }
                String[] inputs = data[0].split(" ");
                int inputcommands = inputs.length;
                if (inputcommands == 0) {
                    line = sc.nextLine();                                      //get next line for input
                    continue;
                }
                printOutcome(inputs, cinema);                 //run the outcome function which prints outcome based on the input
                line = sc.nextLine();                                      //get next line for input
            }

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } finally {
            if (sc != null) sc.close();
        }
    }

    /**
     * This function attempts to find the index in the ArrayList of cinemas, which contains the cinema number
     * @param cinema takes in array list of all cinemas
     * @param cinemaNumber the cinemaNumber we are trying to locate
     * @return -1 if it does not exist, or index of the cinema
     */
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

    /**
     *
     * @param cinema takes in array list of all cinemas
     * @param id the booking id we are searching for
     * @return -1 if the id doesn't exist or the index in the cinema arraylist which contains that booking
     */
    public int getBookingIdCinema(ArrayList<Cinema> cinema, int id) {
        int index = -1;
        for (int i = 0; i < cinema.size(); i++) {
            for (int j = 0; j < cinema.get(i).getSession().size(); j++) {
                for (int k = 0; k < cinema.get(i).getSession().get(j).getAllBookings().size(); k++) {
                    if (cinema.get(i).getSession().get(j).getAllBookings().get(k).getId() == id) {
                        return i;
                    }
                }
            }
        }

        return index;
    }

    /**
     * Proccesses The request, will print outcome of the request
     * @param inputs the requests from user (split from the '#' comments)
     * @param cinema takes in array list of all cinemas
     */
    public void printOutcome(String inputs[], ArrayList<Cinema> cinema) {
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

    /**
     * if the request was a "Cancel" request, this will attempt to cancel the booking from the session. <br>
     * it will remove the booking from the assosciated session and free up seats
     * @param inputs request from the input file
     * @param cinema takes in array list of all cinemas
     */
    public void outcomeCancel(String inputs[], ArrayList<Cinema> cinema) {
        int bookingId = Integer.parseInt(inputs[1]);                                //get the cinema which contains the booking ID requested to cancel
        int cinemaWithBookingIdIndex = getBookingIdCinema(cinema, bookingId);
        if (cinemaWithBookingIdIndex == -1) {
            System.out.println("Cancel Rejected");                                  //if the booking id doesn't exist in any cinema then we print error message
            return;
        }
        int sessionindex = cinema.get(cinemaWithBookingIdIndex).sessionIndexWithBooking(bookingId);
        //now we need to find the index of the booking in the cinema's list of bookings
        int bookingIndex = cinema.get(cinemaWithBookingIdIndex).getSession().get(sessionindex).getBookingIdIndex(bookingId);
        if (bookingIndex == -1) {
            System.out.println("Cancel Rejected");                                  //if the booking somehow doesnt exist ??? print error message and continue
            return;
        }
        boolean cancelled;                                                          //now we have the bookingIndex and the CinemaIndex lets remove that booking
        cancelled = cinema.get(cinemaWithBookingIdIndex).getSession().get(sessionindex).getAllBookings().get(bookingIndex).cancelBooking(cinema, sessionindex);
        if (cancelled == false) {
            System.out.println("Cancel Rejected");
            return;
        } else {
            System.out.println("Cancel " + bookingId);
        }

    }

    /**
     * if the request was a "Print" request, this will attempt to print all booking from the session
     * @param inputs request from the input file
     * @param cinema takes in array list of all cinemas
     */
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

    /**
     * if the request was a "Cancel" request, it will either add rows to the existing cinema. <br>
     * Or it will create the cinema with the cinema number and add the row to that cinema.
     * @param inputs request from the input file
     * @param cinema takes in array list of all cinemas
     */
    public void outcomeCinema(String inputs[], ArrayList<Cinema> cinema) {
        int cinemaN = Integer.parseInt(inputs[1]);
        int cinemaNumber = getCinemaIndex(cinema, cinemaN);
        String rowName = inputs[2].toString();
        int numberOfSeats = Integer.parseInt(inputs[3]);
        //want to create cinema out of this
        //if the cinema doesnt exist create the cinema else
        //find the cinema and add the row to the cinema
        if (cinemaNumber == -1) {

            Cinema createCinema = new Cinema(cinemaN);
            createCinema.addRowToCinema(rowName, numberOfSeats);
            cinema.add(createCinema);

        } else {
            cinema.get(cinemaNumber).addRowToCinema(rowName, numberOfSeats);
        }
    }

    /**
     * if the request was a "Session" request, it will add a session to that cinema number. <br>
     * the session is a movie with a time.
     * @param inputs request from the input file
     * @param cinema takes in array list of all cinemas
     */
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

    /**
     * if the request was a "Request" request, it will attempt to make a booking with specified inputs. <br>
     * It will either print the successful booking or booking rejected.
     * @param inputs request from the input file
     * @param cinema takes in array list of all cinemas
     */
    public void outcomeBooking(String inputs[], ArrayList<Cinema> cinema) {
        int bookingId = Integer.parseInt(inputs[1]);
        int cinemaN = Integer.parseInt(inputs[2]);
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
        bookings b = new bookings(0, null, 0, 0);
        boolean check = b.createBooking(numberOfTickets, movieTime, bookingId, cinemaNumber, cinema, sessionindex, cinemaN);
        if (check == false) {
            System.out.println("Booking Rejected");
            return;
        }
        cinema.get(cinemaNumber).getSession().get(sessionindex).addBookingToSession(b);
        int rowindex = cinema.get(cinemaNumber).getSession().get(sessionindex).getBookingIdIndex(bookingId);
        int bookingIndex = cinema.get(cinemaNumber).getSession().get(sessionindex).getBookingIdIndex(bookingId);
        String rowchar = cinema.get(cinemaNumber).getSession().get(sessionindex).getAllBookings().get(bookingIndex).getRowName();
        int seatStart = cinema.get(cinemaNumber).getSession().get(sessionindex).getAllBookings().get(rowindex).getStartSeat();
        int seatEnd = seatStart + numberOfTickets;
        System.out.println("Booking " + bookingId + " " + rowchar + (seatStart + 1) + "-" + rowchar + seatEnd);
    }

    /**
     * if the request was a "Change" request, it will attempt to change the booking with the booking id requested. <br>
     * if the change is not possible the old booking will remain the same.
     * @param inputs request from the input file
     * @param cinema takes in array list of all cinemas
     */
    public void outcomeChange(String inputs[], ArrayList<Cinema> cinema) {
        int bookingId = Integer.parseInt(inputs[1]);
        int cinemaN = Integer.parseInt(inputs[2]);
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

        int oldSessionIndex = cinema.get(cinemaNumber).sessionIndexWithBooking(bookingId);
        int bookingIndexId = cinema.get(cinemaNumber).getSession().get(oldSessionIndex).getBookingIdIndex(bookingId);
        bookings b = cinema.get(cinemaNumber).getSession().get(oldSessionIndex).getAllBookings().get(bookingIndexId);
        boolean check = b.changeBooking(cinemaNumber, movieTime, numberOfTickets, cinema, sessionindex, cinemaN);
        if (check == false) {
            System.out.println("Change rejected");
            return;
        }
        //add the booking to our new session
        cinema.get(cinemaNumber).getSession().get(sessionindex).addBookingToSession(b);
        String rowchar = b.getRowName();
        int seatStart = b.getStartSeat();
        int seatEnd = seatStart + numberOfTickets;
        System.out.println("Change " + bookingId + " " + rowchar + (seatStart + 1) + "-" + rowchar + seatEnd);
    }
}
