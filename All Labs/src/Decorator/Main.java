package Decorator;

public class Main {
    public static void main(String args[]){
        Main m = new Main();
        m.run();
    }

    public void run(){
        Milk milk = new Milk();
        ChocolateSprinkles chocolateSprinkles = new ChocolateSprinkles();
        Coffee coffee = new Coffee();

        Decorator milkyCoffee = new Decorator(milk,"extra milky");
        milkyCoffee.display();
        milkyCoffee = new Decorator(milkyCoffee,"Coffee");
        milkyCoffee.display();

        Decorator fakeChocolate = new Decorator(chocolateSprinkles, "Anti-chocolate");
        fakeChocolate.display();

        Decorator WTFMilk = new Decorator(milk, "water, extra water, this is secretly water");
        WTFMilk.display();
    }
}
