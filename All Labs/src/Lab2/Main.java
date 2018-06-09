package Lab2;

public class Main {
    public static void main(String args[]){
        BankAccount ia = new InternetAccount();
        ia.deposit(300);
        System.out.println(ia);
        ia.withdraw(200);
        System.out.println(ia);
    }
}
