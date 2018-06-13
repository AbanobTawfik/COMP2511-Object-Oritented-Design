package Composite;

public class Item implements MenuItem {
    private double price;
    private String description;

    public Item(double price, String description) {
        this.price = price;
        this.description = description;
    }
    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void display() {
        System.out.println(description + " $" + price);
    }
}
