package Lab1;

import java.util.Date;

public class Main {
    public static void main(String args[]) {
        Manager me = new Manager("Apple", 3, new Date());
        Employee me2 = new Manager("Apple", 4, new Date());
        System.out.println(me2);
    }

}
