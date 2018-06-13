package Stratergy;

public class DollarNotes implements Stratergy{

    @Override
    public void dispense(int amount) {
        System.out.println(amount + " Dollars Converts to " + amount + " Dollar Notes");
    }
}