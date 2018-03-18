import java.io.IOException;

public class CinemaBookingSystem {

    /**
     * @param args command line arguement
     */
    public static void main(String args[]){
        //run the tests
        //i did not want to pollute my main function with multiple functions i preffered to
        //split the functions accross different classes and a operation class to allow for easier
        //debugging
        operate run = new operate();
        run.runTests(args[0]);

    }
}
