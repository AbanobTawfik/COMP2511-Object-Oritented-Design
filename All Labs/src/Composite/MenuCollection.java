package Composite;

import java.util.ArrayList;

public class MenuCollection implements MenuItem {
    private ArrayList<MenuItem> composite = new ArrayList<MenuItem>();

    public void add(MenuItem i){
        composite.add(i);
    }

    public void remove(MenuItem i){
        composite.add(i);
    }

    @Override
    public double getPrice() {
        double sum = 0;
        for(MenuItem m : composite)
            sum += m.getPrice();
        return sum;
    }

    @Override
    public String getDescription() {
        String s = " ";
        for(MenuItem i : composite)
            s += i.getDescription() + "\n";

        return s;
    }

    @Override
    public void display() {
        for(MenuItem i : composite)
            i.display();
    }
}
