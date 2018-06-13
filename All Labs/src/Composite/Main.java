package Composite;

public class Main {

    public static void main(String args[]){
        Main m = new Main();
        m.run();
    }

    public void run(){
        Item[] mochaarray = new Item[1];
        mochaarray[0] = new Item(20,"Mocha *sprinked chocolate ontop*");
        mochaarray[0].display();
        System.out.println("EWFWE");
        Item mocha = new Item(20,"Mocha *sprinked chocolate ontop*");
        Item Cappacino = new Item(5000,"Cappacino  *shit coffee*");
        MenuCollection shitCofee = new MenuCollection();
        shitCofee.add(mocha);
        shitCofee.add(Cappacino);

        Item water = new Item(500,"Water *tap water*");
        Item FancyWater = new Item(600000, "Fancy Water *finest tap water in town*");
        MenuCollection goodShit = new MenuCollection();
        goodShit.add(water);
        goodShit.add(FancyWater);

        MenuCollection actualMenu = new MenuCollection();
        actualMenu.add(shitCofee);
        actualMenu.add(goodShit);

        actualMenu.display();
    }
}
