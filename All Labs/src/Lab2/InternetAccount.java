package Lab2;

public class InternetAccount extends BankAccount {
    private int numberOfWithdraws = 0;
    private int withdrawLimit = 10;

    @Override
    public boolean withdraw(double amount){
        if(numberOfWithdraws >= withdrawLimit)
            return false;
        boolean b = super.withdraw(amount);
        if(b == true)
            numberOfWithdraws++;
        return b;
    }

    public boolean internetwithdraw(double amount){
        if(numberOfWithdraws >= withdrawLimit)
            return false;
        boolean b = super.withdraw(amount);
        if(b == true)
            numberOfWithdraws++;
        return b;
    }

    @Override
    public String toString() {
        return super.toString() + "InternetAccount{" +
                "numberOfWithdraws=" + numberOfWithdraws +
                ", withdrawLimit=" + withdrawLimit +
                '}';
    }
}
