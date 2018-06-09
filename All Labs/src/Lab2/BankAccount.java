package Lab2;

public class BankAccount {
    private double balance = 0;
    private double amountWithdrawn = 0;
    private double LIMIT = 800;

    /**
     * @param amount > 0
     */
    public void deposit(double amount){
        balance += amount;
    }

    /**
     *
     * @param amount > 0
     * @return true if successful/false otherwise
     */
    public boolean withdraw(double amount){
        if(amount > balance || amount + amountWithdrawn > LIMIT)
            return false;
        balance -= amount;
        amountWithdrawn += amount;
        return true;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "balance=" + balance +
                ", amountWithdrawn=" + amountWithdrawn +
                ", LIMIT=" + LIMIT +
                '}';
    }
}
