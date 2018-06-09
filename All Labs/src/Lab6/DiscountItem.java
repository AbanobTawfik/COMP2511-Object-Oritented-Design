package Lab6;

import java.util.List;

public class DiscountItem implements Item{
    Item item;

    public DiscountItem(Item item) {
        this.item = item;
    }


    @Override
    public double getPrice(){
        double sum = 0;
        for(int i = 0; i < item.getChildren().size(); i++){
            sum += item.getChildren().get(i).getPrice() * 0.9;
        }
        return sum;
    }

    @Override
    public DiscountItem clone(){
        return new DiscountItem(item.clone());
    }

    @Override
    public void showPrice(){
        for(int i = 0; i < item.getChildren().size(); i++){
            System.out.println("Price of compoenent -> " + getPrice());
        }
    }

    @Override
    public List<Item> getChildren(){
        return item.getChildren();
    }
}
