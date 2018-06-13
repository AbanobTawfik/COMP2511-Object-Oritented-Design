package Stratergy;

public class Quarters implements Stratergy{

    @Override
    public void dispense(int amount) {
        System.out.println(amount + " Dollars Converts to " + amount*4 + " Quarters");
    }
}
