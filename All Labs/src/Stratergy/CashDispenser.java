package Stratergy;

public class CashDispenser {
    private Stratergy stratergy;

    public CashDispenser(Stratergy stratergy) {
        this.stratergy = stratergy;
    }

    public void dispense(int amount){
        stratergy.dispense(amount);
    }
}
