package Lab6;

import java.util.ArrayList;

public class Assembly implements Item {

    private ArrayList<Item> assembly = new ArrayList<Item>();


    public void addItem(Item i){
        assembly.add(i.clone());
    }

    public void removeItem(Item i){
        i = new Assembly();
        assembly.remove(i);
    }

    @Override
    public double getPrice(){
        double sum = 0;
        int ii = assembly.size();
        for(Item i : assembly) {
            sum += i.getPrice();
            ii--;
        }
        return sum;
    }

    @Override
    public void showPrice(){
        for(Item i : assembly){
            i.showPrice();
        }
    }

    @Override
    public Assembly clone(){
        Assembly clone = new Assembly();
        for(Item i: assembly){
            clone.addItem(i.clone());
        }
        return clone;
    }

    @Override
    public ArrayList<Item> getChildren(){
        return assembly;
    }
}
