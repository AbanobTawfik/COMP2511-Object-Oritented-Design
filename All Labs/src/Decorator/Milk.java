package Decorator;

public class Milk implements Item {
    private String topping = "cream ";
    @Override
    public String composition() {
        return topping;
    }

    @Override
    public void display() {
        System.out.println(topping);
    }
}
