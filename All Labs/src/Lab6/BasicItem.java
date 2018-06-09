package Lab6;

import java.util.ArrayList;
import java.util.List;

public class BasicItem implements Item {
    private double price;

    public BasicItem(double price) {
        this.price = price;
    }

    @Override
    public double getPrice(){
        return price;
    }

    @Override
    public BasicItem clone(){
        return (new BasicItem(price));
    }

    @Override
    public void showPrice(){
        System.out.println("Price of compoenent -> " + getPrice());
    }

    @Override
    public List<Item> getChildren(){
        ArrayList<Item> item = new ArrayList<Item>();
        item.add(this);
        return item;
    }
}
