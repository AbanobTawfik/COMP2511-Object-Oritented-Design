package Decorator;

public class ChocolateSprinkles implements Item {
    private String topping = "Chocolate Sprinkeles ";
    @Override
    public String composition() {
        return topping;
    }

    @Override
    public void display() {
        System.out.println(topping);
    }
}
