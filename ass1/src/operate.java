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
     * This method will guarantee to perform all testing and output the appropriate returns from those requests
     * @param FileInput the file which is going to be read in, expected to be a valid file which can be read
     */
    public void runTests(String FileInput)

    {
        //initialise an ArrayList to hold all cinemas in the "Building"
        ArrayList<Cinema> cinema = new ArrayList<Cinema>();
        //file input
        Scanner sc = null;
        try {
            //opens the file and allows to read all text in file
            sc = new Scanner(new File(FileInput));
            //reading file line by line
            String line = sc.nextLine();
            while (line != null) {
                //split our keywords and input info from comment character '#'
                String[] data = line.split("#");
                //if the line is just a comment to avoid this issue
                if (data.length == 0) {
                    if (sc.hasNextLine()) {
                        line = sc.nextLine();
                        continue;
                    }
                    break;
                }
                char checker[] = data[0].toCharArray();

                //if the line was a comment then we will have a new String with length 0
                //if the split caused data[0] to be null then we want to skip to next line
                //example if our line is "#ThisIsAComment" it will be split into ["", "#thisIsAComment"]
                //since the first index contains null "", we want to skip to the next else this will cause errors
                if (checker.length == 0 || checker.equals(null)) {
                    //get next line for input
                    if (sc.hasNextLine()) {
                        line = sc.nextLine();
                        continue;
                    }
                    break;
                }

                //split the request data into an array that has different indexing
                //example -> ["Cinema 1 x 5"] -> ["Cinema", "1", "x", "5"]
                //this will allow us to proccess our request properly
                String[] inputs = data[0].split(" |\t");
                //get the number of inputs for the request so for our cinema example -> return 4
                int inputcommands = inputs.length;
                //if the size of the split array is 0 we want to skip else this request would cause null errors
                if (inputcommands == 0) {
                    if (sc.hasNextLine()) {
                        line = sc.nextLine();
                        continue;
                    }
                    break;
                }
                //process the request
                //run the outcome function which prints outcome based on the input
                printOutcome(inputs, cinema);
                //get next line for input for iteration through whole file
                //if there is a next line retrieve that
                if (sc.hasNextLine())
                    line = sc.nextLine();
                    //otherwise break
                else
                    break;
            }

        } catch (FileNotFoundException e) {
            //catch exceptions example if invalid file and print them out
            System.out.println(e.getMessage());
        } finally {
            //close file after we reached the end of file
            if (sc != null) sc.close();
        }
        //close file
        sc.close();
    }

    /**
     * This function attempts to find the index in the ArrayList of cinemas, which contains the cinema number
     * This method guarantees to return either the index which has the cinema number we are searching for or -1 if no such cinema exists
     * @param cinema       takes in array list of all cinemas, expected to be non null
     * @param cinemaNumber the cinemaNumber we are trying to locate, expected to be a valid integer
     * @return -1 if it does not exist, or index of the cinema
     */
    public int getCinemaIndex(ArrayList<Cinema> cinema, int cinemaNumber) {
        //initalise the return as false incase it doesn't exist
        int index = -1;
        //scan through the arrayList of cinemas to find the index which has the corresponding cinema number
        for (int i = 0; i < cinema.size(); i++) {
            //if we found the correct index i, return i
            if (cinema.get(i).getCinemaNumber() == cinemaNumber) {
                return i;
            }
        }
        //else we want to return the false
        return index;
    }

    /**
     * This method guarantees to either return the cinema index which contains the booking id we are scanning for, or -1 if no booking exists
     * @param cinema takes in array list of all cinemas, expected to be non null
     * @param id     the booking id we are searching for, expected to be a valid integer
     * @return -1 if the id doesn't exist or the index in the cinema arraylist which contains that booking
     */
    public int getBookingIdCinema(ArrayList<Cinema> cinema, int id) {

        //initalise our return as false initially incase the bookingId in the cinema doesn't exist
        int index = -1;
        //first we want to scan through EVERY cinema in the arrayList since any cinema can contain this booking id
        for (int i = 0; i < cinema.size(); i++) {
            //now we want to scan through EVERY session in the cinema we are scanning since all sessions could contain the booking id
            for (int j = 0; j < cinema.get(i).getSession().size(); j++) {
                //now we want to scan through EVERY booking in that session since all bookings could have that booking id
                for (int k = 0; k < cinema.get(i).getSession().get(j).getAllBookings().size(); k++) {
                    //if we found a match, we want to return i, the cinema index which contains that booking
                    if (cinema.get(i).getSession().get(j).getAllBookings().get(k).getId() == id) {
                        return i;
                    }
                }
            }
        }

        //otherwise return false if the booking doesn't exist
        return index;
    }

    /**
     * Proccesses The request, will print outcome of the request
     * This method will guarantee to proccess the request and print an outcome or just go to next line of input if no outcome to print
     * @param inputs the requests from user (split from the '#' comments), expected to be a non null string
     * @param cinema takes in array list of all cinemas, expected to be non null
     */
    public void printOutcome(String inputs[], ArrayList<Cinema> cinema) {
        //if our request is Cancel request we want to perform a cancel
        if (inputs[0].equals("Cancel"))
            outcomeCancel(inputs, cinema);
            //if our request is a Print request we want to print all bookings in the session
        else if (inputs[0].equals("Print"))
            outcomePrint(inputs, cinema);
            //if the request is a Cinema request we want to add rows or create cinema with rows
        else if (inputs[0].equals("Cinema"))
            outcomeCinema(inputs, cinema);
            //if the request is a Session request we want to add a session to a cinema
        else if (inputs[0].equals("Session"))
            outcomeSession(inputs, cinema);
            //if the request is a Request request we want to add a booking to a session
        else if (inputs[0].equals("Request"))
            outcomeBooking(inputs, cinema);
            //if the request is a Change request we want to change a booking
        else if (inputs[0].equals("Change"))
            outcomeChange(inputs, cinema);
        else
            //otherwise get nextline and proccess next request
            return;
    }

    /**
     * if the request was a "Cancel" request, this will attempt to cancel the booking from the session. <br>
     * it will remove the booking from the assosciated session and free up seats
     * This method will guarantee to either cancel a request and print out the cancelled booking, OR print out cancel rejected always output
     * @param inputs request from the input file, expected to be non null string
     * @param cinema takes in array list of all cinemas, expected to be non null
     */
    public void outcomeCancel(String inputs[], ArrayList<Cinema> cinema) {
        //checking if valid input for request if invalid print error message and return
        try {   //try to parse the input as an integer, if successful continue
            Integer.parseInt(inputs[1]);
        }   //catch the exception if it is not an integer, and print error message
        catch (Exception e) {
            System.out.println("Cancel rejected");
            return;
        }

        //proccess the booking ID from the input parseinteger from string
        int bookingId = Integer.parseInt(inputs[1]);
        //get the cinema which contains the booking ID requested to cancel
        int cinemaWithBookingIdIndex = getBookingIdCinema(cinema, bookingId);
        //if the cinema doesn't exist we want to print failed cancellation
        if (cinemaWithBookingIdIndex == -1) {
            System.out.println("Cancel rejected");
            return;
        }
        //find the session which contains that booking
        int sessionindex = cinema.get(cinemaWithBookingIdIndex).sessionIndexWithBooking(bookingId);
        //now we need to find the index of the booking in the cinema's list of bookings
        int bookingIndex = cinema.get(cinemaWithBookingIdIndex).getSession().get(sessionindex).getBookingIdIndex(bookingId);
        //if the booking somehow doesnt exist ??? print error message and continue
        if (bookingIndex == -1) {
            System.out.println("Cancel rejected");
            return;
        }
        //now we want to add a check for our cancellation
        boolean cancelled;
        //attempt to cancel the booking, check cancelbooking function if need more info
        //will cancel the booking in the cinema, for the booking in that session and remove it from the list
        cancelled = cinema.get(cinemaWithBookingIdIndex).getSession().get(sessionindex).getAllBookings().get(bookingIndex).cancelBooking(cinema, sessionindex);
        //if the cancellation failed we want to print out our error message
        if (cancelled == false) {
            System.out.println("Cancel rejected");
            return;
        } else {
            //otherwise we want to print successful cancellation information
            System.out.println("Cancel " + bookingId);
        }
        //sort our bookings by rowname to allow easy printing
        cinema.get(cinemaWithBookingIdIndex).getSession().get(sessionindex).sortAllBookingsByRowName();

    }

    /**
     * if the request was a "Print" request, this will attempt to print all booking from the session
     * This method will guarantee to print the movie name for that session, and all the bookings (even if they are null no print), or if no session exists just return
     * @param inputs request from the input file, expected to be non null string
     * @param cinema takes in array list of all cinemas, expected to be non null
     */
    public void outcomePrint(String inputs[], ArrayList<Cinema> cinema) {
        //checking if valid request input and format
        //create the format for our date in HOURS:MINUTES also for checking format
        DateFormat formatting = new SimpleDateFormat("HH:mm");
        try {
            //if the cinemanumber asked to print is not an integer return
            Integer.parseInt(inputs[1]);
        } catch (Exception e) {
            return;
        }
        try {
            //if the date format doesnt follow HH:MM strictly return
            formatting.parse(inputs[2]);
        } catch (Exception e) {
            return;
        }
        //printing we print all bookings for the cinema number + time of session
        //get the cinemaNumber of the cinema we are trying to print booking for
        int cinemaN = Integer.parseInt(inputs[1]);
        //find the index in the arraylist of cinemas for that cinema
        int cinemaNumber = getCinemaIndex(cinema, cinemaN);
        //if the cinema is not in the arraylist of cinemas print error message and return
        if (cinemaNumber == -1) {
            return;
        }

        //parse the date from input
        //initialise our date as null
        Date movieTime = null;
        try {
            //try to format the date input from input
            movieTime = formatting.parse(inputs[2]);
        } catch (ParseException e) {
            //otherwise if invalid date format input we want to print error
            e.printStackTrace();
            return;
        }
        //now we want to find the session which is showing during that time
        int sessionindex = cinema.get(cinemaNumber).getSessionTimeIndex(movieTime);
        //checking if session exist
        if (sessionindex == -1)
            //if the session doesn't exist return
            return;
        //now that we have found the session we want to print the movie showing in that session time
        System.out.println(cinema.get(cinemaNumber).getSession().get(sessionindex).getMovie());
        //now print all the bookings in that session see printAllBookings function
        cinema.get(cinemaNumber).getSession().get(sessionindex).printAllBookings();

    }

    /**
     * if the request was a "Cancel" request, it will either add rows to the existing cinema. <br>
     * Or it will create the cinema with the cinema number and add the row to that cinema.
     * This method guarantees to add a row to a cinema or create a cinema with row requested
     * @param inputs request from the input file, expected to be non null string
     * @param cinema takes in array list of all cinemas, expected to be non null
     */
    public void outcomeCinema(String inputs[], ArrayList<Cinema> cinema) {
        //checking if input has correct formula
        try {
            //if the first input is not an integer return
            Integer.parseInt(inputs[1]);
        } catch (Exception e) {
            return;
        }
        try {
            //if the number of seats is not an integer return
            Integer.parseInt(inputs[3]);
        } catch (Exception e) {
            return;
        }

        //get the cinemaNumber we want to create or add rows to
        int cinemaN = Integer.parseInt(inputs[1]);
        //find the index of thaat cinemaNumber we are adding rows to
        int cinemaNumber = getCinemaIndex(cinema, cinemaN);
        //creating row name for that row
        String rowName = inputs[2].toString();
        //get number of seats we are adding to that row
        int numberOfSeats = Integer.parseInt(inputs[3]);
        if (numberOfSeats < 1) {
            //if there isnt any amount of seats requested just return
            return;
        }
        //want to create cinema out of this
        //if the cinema doesnt exist create the cinema else
        //find the cinema and add the row to the cinema
        if (cinemaNumber == -1) {
            //create the new cinema
            Cinema createCinema = new Cinema(cinemaN);
            //ad the row to the cinema
            createCinema.addRowToCinema(rowName, numberOfSeats);
            //add the cinema to our list of cinemas
            cinema.add(createCinema);

        } else {
            //add the row to the cinema at the correct index
            cinema.get(cinemaNumber).addRowToCinema(rowName, numberOfSeats);
        }
    }

    /**
     * if the request was a "Session" request, it will add a session to that cinema number. <br>
     * the session is a movie with a time.
     * this method guarantees to add a valid session to a cinema's session list based on input
     * @param inputs request from the input file, expected to be non null string
     * @param cinema takes in array list of all cinemas, expected to be non null
     */
    public void outcomeSession(String inputs[], ArrayList<Cinema> cinema) {
        //setting up the formatting for date
        DateFormat formatting = new SimpleDateFormat("HH:mm");
        //request format check
        try {
            //check if the first part of the request is an integer
            Integer.parseInt(inputs[1]);
        } catch (Exception e) {
            //just return if it isn't
            return;
        }
        try {
            //check if correct date format
            formatting.parse(inputs[2]);
        } catch (Exception e) {
            //return if it isn't
            return;
        }

        //get cinemaNumber for our session from standard input
        int cinemaN = Integer.parseInt(inputs[1]);
        //find the index of that cinema in our arrayList
        int cinemaNumber = getCinemaIndex(cinema, cinemaN);
        //if the cinema doesn't exist print out error
        if (cinemaNumber == -1) {
            return;
        }

        //gathering the date from the input
        //initialise our date as null
        Date movieTime = null;
        try {
            //attempt to parse date from input into the format
            movieTime = formatting.parse(inputs[2]);
        } catch (ParseException e) {
            //print error if incorrect format
            e.printStackTrace();
            return;
        }
        //want to get the name of the movie for our session
        //initialise moviename as null
        String movieShowing = inputs[3];
        //find the amount of inputs incase name has space
        int inputSize = inputs.length;
        //if no movie is in session input return
        if (inputSize <= 3)
            return;
        //otherwise add the name of the movie with spaces to our string
        for (int i = 4; i < inputSize; i++)
            //add each word of movie to our movie string
            movieShowing = movieShowing + " " + inputs[i];
        //want to now add the session to the cinema
        cinema.get(cinemaNumber).addSession(cinemaNumber, movieTime, movieShowing);
    }

    /**
     * if the request was a "Request" request, it will attempt to make a booking with specified inputs. <br>
     * It will either print the successful booking or Booking rejected.
     * This method guarantees to either, create a booking and print the booking details to stdout, or print booking rejected if impossible always output
     * @param inputs request from the input file, expected to be non null string
     * @param cinema takes in array list of all cinemas, expected to be non null
     */
    public void outcomeBooking(String inputs[], ArrayList<Cinema> cinema) {
        //setting up the formatting for date
        DateFormat formatting = new SimpleDateFormat("HH:mm");
        //request format check
        try {
            //check if the first part of the request is an integer
            Integer.parseInt(inputs[1]);
        } catch (Exception e) {
            //just print error message and return if it isn't
            System.out.println("Booking rejected");
            return;
        }
        try {
            //check if the next part of the request is an integer
            Integer.parseInt(inputs[2]);
        } catch (Exception e) {
            //just return if it isn't
            System.out.println("Booking rejected");
            return;
        }
        try {
            //check if correct date format
            formatting.parse(inputs[3]);
        } catch (Exception e) {
            //return if it isn't
            System.out.println("Booking rejected");
            return;
        }
        try {
            //check if the tickets part of the request is an integer
            Integer.parseInt(inputs[4]);
        } catch (Exception e) {
            //just return if it isn't
            System.out.println("Booking rejected");
            return;
        }
        //get the booking id for our request
        int bookingId = Integer.parseInt(inputs[1]);
        //get the cinemaNumber for our booking id
        int cinemaN = Integer.parseInt(inputs[2]);
        //trying to find the index for our cinemaNumber to create our booking
        int cinemaNumber = getCinemaIndex(cinema, cinemaN);
        //to avoid accessing negative array index if cinema doesn't exist we want to print error message
        if (cinemaNumber == -1) {
            System.out.println("Booking rejected");
            return;
        }
        //initialise our movietime as null
        Date movieTime = null;
        try {
            //try to parse the date in from standard input
            movieTime = formatting.parse(inputs[3]);
        } catch (ParseException e) {
            //if there is error print stack trace
            e.printStackTrace();
            return;
        }
        //get the number of tickets in our booking
        int numberOfTickets = Integer.parseInt(inputs[4]);
        //dont request negative tickets
        if (numberOfTickets < 1) {
            //if there isnt any amount of tickets requested just return
            System.out.println("Booking rejected");
            return;
        }
        //now we want to find the session which has the time fo that showing
        int sessionindex = cinema.get(cinemaNumber).getSessionTimeIndex(movieTime);
        //to avoid accessing negative array index's we want to print error message if session doesn't exist
        if (sessionindex == -1) {
            System.out.println("Booking rejected");
            return;
        }
        //create a new bookings blueprint to allow us to create our booking
        bookings b = new bookings(0, null, 0, 0);
        //now we want to create that booking in our blueprint, return true is successful or false if unable to create booking
        boolean check = b.createBooking(numberOfTickets, movieTime, bookingId, cinemaNumber, cinema, sessionindex, cinemaN);
        //if the booking couldn't be created we want to print our error message and go to the next request
        if (check == false) {
            System.out.println("Booking rejected");
            return;
        }
        //if our booking was successful we want to add that booking to the bookings list in the session
        cinema.get(cinemaNumber).getSession().get(sessionindex).addBookingToSession(b);

        //information for our print to allow easy to read sout print
        //get the row index for our booking
        int rowindex = cinema.get(cinemaNumber).getSession().get(sessionindex).getBookingIdIndex(bookingId);
        //get the booking index in our bookings list for our booking
        int bookingIndex = cinema.get(cinemaNumber).getSession().get(sessionindex).getBookingIdIndex(bookingId);
        //now we want to get the row name for our booking using rowindex and bookingindex
        String rowchar = cinema.get(cinemaNumber).getSession().get(sessionindex).getAllBookings().get(bookingIndex).getRowName();
        //now we want to get our starting seat for our booking
        int seatStart = cinema.get(cinemaNumber).getSession().get(sessionindex).getAllBookings().get(rowindex).getStartSeat();
        //get the last seat in row for our booking
        int seatEnd = seatStart + numberOfTickets;
        //print Booking bookingID ax-ax+y
        if (numberOfTickets == 1)
            System.out.println("Booking " + bookingId + " " + rowchar + (seatStart + 1));
        else
            System.out.println("Booking " + bookingId + " " + rowchar + (seatStart + 1) + "-" + rowchar + seatEnd);
        //sort all our bookings by row name allows easier printing
        cinema.get(cinemaNumber).getSession().get(sessionindex).sortAllBookingsByRowName();
    }

    /**
     * if the request was a "Change" request, it will attempt to change the booking with the booking id requested. <br>
     * if the change is not possible the old booking will remain the same.
     * this method guarantees to change a booking and print out the changed details to sdout or print change rejected if impossible, ALWAYS OUTPUT
     * @param inputs request from the input file, expected to be non null string
     * @param cinema takes in array list of all cinemas, expected to be non null
     */
    public void outcomeChange(String inputs[], ArrayList<Cinema> cinema) {
        //creating our formatting for our date in hours:minutes
        DateFormat formatting = new SimpleDateFormat("HH:mm");
        try {
            //checking if the booking id is infact a number
            Integer.parseInt(inputs[1]);
        } catch (Exception e) {
            //print error message and return if it isn't
            System.out.println("Change rejected");
            return;
        }
        try {
            //checking if the cinema number is infact a number
            Integer.parseInt(inputs[2]);
        } catch (Exception e) {
            //print error message and return if it isn't
            System.out.println("Change rejected");
            return;
        }
        try {
            //checking if the date follows the HH:MM format
            formatting.parse(inputs[3]);
        } catch (Exception e) {
            //print error message and return if it isn't
            System.out.println("Change rejected");
            return;
        }
        try {
            //checking if the number of tickets for the new booking is infact a number
            Integer.parseInt(inputs[4]);
        } catch (Exception e) {
            //print error message and return if it isn't
            System.out.println("Change rejected");
            return;
        }
        //get the booking id for the change request
        int bookingId = Integer.parseInt(inputs[1]);
        //get the cinemaNumber for our new booking request (change->new)
        int cinemaN = Integer.parseInt(inputs[2]);
        //get the old cinemaNumber index
        int oldCinemaNumber = getBookingIdCinema(cinema, bookingId);
        //want to check if old session even exists
        if (oldCinemaNumber == -1) {
            System.out.println("Change rejected");
            return;
        }
        //get the index for the new cinema we are making our new booking for
        int cinemaNumber = getCinemaIndex(cinema, cinemaN);
        //to avoid issues with accessing negative array index we want to print an error message
        if (cinemaNumber == -1) {
            System.out.println("Change rejected");
            return;
        }
        //initialise our date as null
        Date movieTime = null;
        try {
            //attempt to parse the date in from the string input
            movieTime = formatting.parse(inputs[3]);
        } catch (ParseException e) {
            //if there is an error with parsing the date in print stack trace
            e.printStackTrace();
        }
        //get the number of tickets for our new booking
        int numberOfTickets = Integer.parseInt(inputs[4]);
        //dont try making a new booking with less than 1 ticket
        if (numberOfTickets < 1) {
            System.out.println("Change rejected");
            return;
        }
        //get the session index in our session list from cinema that has our new request's movietime
        int sessionindex = cinema.get(cinemaNumber).getSessionTimeIndex(movieTime);
        //to avoid issues with accessing negative array index we want to print an error message
        if (sessionindex == -1) {
            System.out.println("Change rejected");
            return;
        }
        //want to keep track of our old bookings session index so we can perform the change
        int oldSessionIndex = cinema.get(oldCinemaNumber).sessionIndexWithBooking(bookingId);
        if (oldSessionIndex == -1) {
            System.out.println("Change rejected");
            return;
        }
        //want to get the bookingindex in that session for our old booking
        int bookingIndexId = cinema.get(oldCinemaNumber).getSession().get(oldSessionIndex).getBookingIdIndex(bookingId);
        //now we want to get our old booking (create new booking object that holds our old booking)
        bookings b = cinema.get(oldCinemaNumber).getSession().get(oldSessionIndex).getAllBookings().get(bookingIndexId);
        //attempt to change that booking (return true is if successful or false if unsuccessful
        boolean check = b.changeBooking(cinemaNumber, movieTime, numberOfTickets, cinema, sessionindex, cinemaN);
        //if change was unsuccessful we want to print our error message
        if (check == false) {
            System.out.println("Change rejected");
            return;
        }
        //add the changed booking to our new session
        cinema.get(cinemaNumber).getSession().get(sessionindex).addBookingToSession(b);
        //now we want to print our successful change
        //get the rowname, start seat - end seat
        String rowchar = b.getRowName();
        int seatStart = b.getStartSeat();
        int seatEnd = seatStart + numberOfTickets;
        //print Change bId rx-rx+y
        System.out.println("Change " + bookingId + " " + rowchar + (seatStart + 1) + "-" + rowchar + seatEnd);
        //sort all bookings by row name
        cinema.get(cinemaNumber).getSession().get(sessionindex).sortAllBookingsByRowName();
    }
}
