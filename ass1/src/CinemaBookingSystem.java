import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class CinemaBookingSystem {
    public static void main(String args[]){
        /**
         * for reading input from the file i want to take out the important keywords and their format
         * when scanner detects keywords as
         * Cinema it will read the next input as cinema number to alter
         * it will read the input after that as the row to add
         * it will read the input after that as the number of seats in that row
         *
         * when the # char is detected it will cease to use that input rather as comments
         * then it will continue till the next keyword is found
         * list of keywords
         * Cinema, Session, Request, Change, Cancel, Print
         */
        ArrayList<Cinema> cinema = new ArrayList<Cinema>();

        Scanner sc = null;
        try
        {
            sc = new Scanner(new File(args[0]));    // args[0] is the first command line argument
            // Read input from the scanner here
            String line = sc.nextLine();
            while(sc.hasNextLine()){

                //split our keywords and input info from comment
                String[] data = line.split("#");
                //if a line is a comment skip
                char checker[] = data[0].toCharArray();
                if(checker[0] == '#')
                    continue;

                String[] inputs = data[0].split(" ");
                int inputcommands = inputs.length;

                if(inputcommands == 2 && inputs[0] == "Cancel"){

                }
                if(inputcommands == 3 && inputs[0] == "Print"){

                }

                if(inputcommands == 4 && inputs[0] == "Cinema"){
                    int cinemaNumber = Integer.parseInt(inputs[1]);
                    char rowName[] = inputs[2].toCharArray();
                    int numberOfSeats = Integer.parseInt(inputs[3]);
                    //want to create cinema out of this

                }
                if(inputcommands == 4 && inputs[0] == "Session"){

                }
                if(inputcommands == 5 && inputs[0] == "Request"){

                }
                if(inputcommands == 5 && inputs[0] == "Change"){

                }
                //get next line for input
                line = sc.nextLine();
            }

        }
        catch (FileNotFoundException e)
        {
            System.out.println(e.getMessage());
        }

        finally
        {
            if (sc != null) sc.close();
        }

    }



}
