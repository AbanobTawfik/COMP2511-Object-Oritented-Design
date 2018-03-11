package com.company;
import java.util.Random;

/**
 * UML CLASS DIAGRAM
 * -------------------------------------------------------------------------------------------------
 * CLASS
 * bankaccount
 * -------------------------------------------------------------------------------------------------
 * ATTRIBUTES
 * -id:int
 * -currentBalance:double             (PRIVATE INT)
 * -daysPassed:int
 * -numberofwithdrawls
 * -dailyamountwithdrawn
 * -------------------------------------------------------------------------------------------------
 * METHODS
 * +bankAccount(int bal,int id,int dayspassed, int amountwithdrawn, int numberofwithdraws): bankaccount
 * +withdraw(double amount): boolean (true if success false if fail)
 * +deposit(double amount):boolean (true if success false if fail)
 * +showAccount(bankaccount account):string
 * -------------------------------------------------------------------------------------------------
 */

public class bankaccount{

    private double currentbalance;
    private int id;
    private static int daysPassed;
    private double amountWithdrawn;
    private int numberOfWithdraws;


    public double getCurrentbalance() {
        return currentbalance;
    }

    public void setCurrentbalance(double currentbalance) {
        this.currentbalance = currentbalance;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDaysPassed(int daysPassed) {
        this.daysPassed = daysPassed;
    }

    public void setAmountWithdrawn(double amountWithdrawn) {
        this.amountWithdrawn = amountWithdrawn;
    }

    public void setNumberOfWithdraws(int numberOfWithdraws) {
        this.numberOfWithdraws = numberOfWithdraws;
    }

    public int getId() {
        return id;
    }


    public int getDaysPassed() {
        return daysPassed;
    }

    public double getAmountWithdrawn() {
        return amountWithdrawn;
    }


    public int getNumberOfWithdraws() {
        return numberOfWithdraws;
    }


    public bankaccount(double currentbalance, int id) {
        if(id < 0){
            Random rand = new Random();
            int  n = rand.nextInt(50000) + 30;
            this.id = n;
        }else
            this.id = id;
        if(currentbalance < 0)
            currentbalance = 0;
        else
            this.currentbalance = currentbalance;

        this.daysPassed = 0;
        this.amountWithdrawn = 0;
        this.numberOfWithdraws = 0;
    }


    /**
     * deposit function asserts that you can't deposit negative money, post condition and a limitation
     * a expectation from the client
     * can make as many deposits as they want
     */

    public boolean deposit(double amount) {
        assert (amount >= 0);
        this.currentbalance += amount;
        System.out.println(this.showAccount() + "\n");
        return true;
    }

    /**
     * withdraw functions has many conditions and asserts
     * asserts that you cant withdraw more than 800 a day, this amountwithdrawn resets daily (for loop to implement day system)
     * asserts you cant withdraw more than what is in your balance
     */
    public boolean withdraw(double amount) {
        if(amount < 0 || this.amountWithdrawn + amount>= 800 || this.currentbalance < amount){
            return false;
        }else {
            this.currentbalance -= amount;
            this.amountWithdrawn += amount;
            this.numberOfWithdraws++;
            System.out.println(this.showAccount());
            return true;
        }
    }

    /**
     * shows current status of bankaccount
     */
    public String showAccount() {
        return "bankaccount{" +
                "currentbalance=" + currentbalance +
                ", id=" + id +
                ", daysPassed=" + daysPassed +
                ", amountWithdrawn=" + amountWithdrawn +
                ", numberOfWithdraws=" + numberOfWithdraws +
                '}';
    }
}

/**
 * UML CLASS DIAGRAM
 * -------------------------------------------------------------------------------------------------
 * CLASS
 * internetAccount
 * -------------------------------------------------------------------------------------------------
 * ATTRIBUTES
 * -id:int
 * -currentBalance:double             (PRIVATE INT)
 * -daysPassed:int
 * -monthsPassed:int
 * -numberofwithdrawls:int
 * -------------------------------------------------------------------------------------------------
 * METHODS
 * +bankAccount(int bal,int id,int dayspassed, int amountwithdrawn, int numberofwithdraws, int monthspassed, int numberofinternetpayments): bankaccount
 * +withdraw(double amount): boolean (true if success false if fail)
 * +deposit(double amount):boolean (true if success false if fail)
 * +internetPayment(double amount) - boolean (true if success false if fail)
 * +showAccount(internetAccount account): string
 * -------------------------------------------------------------------------------------------------
 */
class internetAccount extends bankaccount {
    private static int monthsPassed;
    private int numberOfInternetPayments;

    public internetAccount(double currentbalance, int id) {
        super(currentbalance, id);
        /*
        pre conditions in assert
         */
        assert(monthsPassed == 0);
        assert(numberOfInternetPayments == 0);
        this.monthsPassed = 0;
        this.numberOfInternetPayments = 0;
    }

    public void setMonthsPassed(int monthsPassed) {
        this.monthsPassed = monthsPassed;
    }

    public void setNumberOfInternetPayments(int numberOfInternetPayments) {
        this.numberOfInternetPayments = numberOfInternetPayments;
    }

    public int getMonthsPassed() {
        return monthsPassed;
    }


    public int getNumberOfInternetPayments() {
        return numberOfInternetPayments;
    }


    public String showInternetAccount() {

        return this.showAccount() + "internetAccount{" +
                "monthsPassed=" + monthsPassed +
                ", numberOfInternetPayments=" + numberOfInternetPayments +
                '}';
    }

    public boolean internetPayment(double amount){
        if(this.getCurrentbalance() < amount || this.getAmountWithdrawn() + amount > 800 || this.numberOfInternetPayments >= 10 ){
            return false;
        }else {
            this.withdraw(amount);
            this.numberOfInternetPayments++;
            System.out.println(this.showInternetAccount() + "\n");
            return true;
        }
    }

    /**
     * my code is consistent with my pre and post conditions that
     * 0. pre condition that you cannot start a bank account with a negative balance >=0
     * 1. you cannot withdraw or make payments once you have exceeded $800
     * 2. cannot withdraw or make payments if there aren't sufficient funds
     * 3. cannot make more than 10 internetpayments per month
     *
     * my code has 1. asserted all these conditions
     * the way i have made days and months wor k is that, when i do testing in my loop
     * i can make my number of payments/amount withdrawn reset on each counter defined on days/month
     * i will also make 1 month = 30 days for consistancy sake
     *
     * each condition is met in my code with my asserts
     */

    /**
     * Verify that your pre- and postconditions respect contravariance and covariance (respectively)
     * A good way i like to think of
     * Narrowing a reference (covariance)
     * Widening a reference (contravariance)
     * animal - super class, cat - subclass
     *
     * covariance - a cat is a animal           / a list of apples is a subtype of a list of fruits
     * contravariance - an animal is a cat      / a list of fruits is a supertype of a list of apples
     *
     * preconditions - contravariance
     * the preconditions are contravariant as the subclass internetAccount is a type of bankAccount
     * with the initalising, it has all the attributes of bankAccount with a few extra details
     * or another way to think of it is that the interAccount is a buclass of bankaccount
     * preconditions state that a bank account and an interaccount can be looked at as the same thing
     * they both have 0 balance, the only difference being the extra attribute of the internet account
     * however their state are basically the same
     *
     * A list of pre conditions met bank accounts can be seen as a supertype of a list of post conditions met
     * on a internet account, so
     *
     *  List <InternetAccount> internet = new bankList<InternetAccount>()
     *  with the sub list
     *  List <bankaccount super internetaccount> banklist = internet
     *
     * postonditions - covariance
     * post conditions are covariant as after post conditions are met, a internet account can be seen
     * as a subclass of a bank account, it cannot be seen in same way as contravariant as their states
     * are different, however an internet account can be seen as a subclass having the same states as a bank account
     * however having more features and attributes,
     *
     * a list of post condition met internet accounts can be seen as a subtype of a list of post condition
     * met bank accounts so
     *
     *  List <bankAccount> banks = new bankList<bankAccount>()
     *  with the sub list
     *  List <internetAccount extends bankaccount> internet = bankList
     *
     * /
    /**
     * balance >=0 must be a class invariant for both of them as pre conditions says u cant start
     * a bank account with negative balance must be >=0
     * and also states u cant withdraw over your limit thus meaning that the statement balance>=0 IS ALWAYS TRUE
     */

    /**
     * my class definitions satisfy LSP
     * the definition of LSP being that a subclass of class b then objects of class b may be replaced
     * with objects of class a
     *
     * in my classes bankaccounts can be replaced on internet accounts due to the following reasons
     * internetaccounts are dependant on bankaccounts, infact they implement most methods from bankaccounts
     * internetaccounts can withdraw/deposit/displayaccountdetails
     * however internetaccount have more features or states such as
     * can view the months passed/how many internet payments have been made
     * the contracts of bankaccounts can all be met by the contracts of internetaccount
     * and all the restrictions on internetaccounts are inherited from bankaccount
     * since the conditions/states are passed down from bankaccount, an internetaccount can replace
     * a bankaccount
     * there is nothing a bankaccount can do that an internetaccount can't is what im trying to say
     *
     */

}
