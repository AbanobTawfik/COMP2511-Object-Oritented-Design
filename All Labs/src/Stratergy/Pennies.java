package Stratergy;

public class Pennies implements Stratergy{

    @Override
    public void dispense(int amount) {
        System.out.println(amount + " Dollars Converts to " + amount*100 + " pennies");
    }
}