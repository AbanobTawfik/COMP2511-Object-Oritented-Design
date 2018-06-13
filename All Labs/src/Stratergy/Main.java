package Stratergy;

public class Main {

    public static void main(String args[]) {
        Main m = new Main();
        m.run();
    }

    public void run() {
        CashDispenser pennyDispenser = new CashDispenser(new Pennies());
        System.out.println("Penny Dispenser...");
        CashDispenser quarterDispenser = new CashDispenser(new Quarters());
        System.out.println("Quarter Dispenser...");
        CashDispenser dollarDispenser = new CashDispenser(new DollarNotes());
        System.out.println("Dollar Dispenser...");

        pennyDispenser.dispense(400);
        quarterDispenser.dispense(400);
        dollarDispenser.dispense(400);


    }
}

