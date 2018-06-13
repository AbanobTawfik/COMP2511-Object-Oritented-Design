package Decorator;

public class Coffee implements Item {
    private String topping = "Covfefe ";
    @Override
    public String composition() {
        return topping;
    }

    @Override
    public void display() {
        System.out.println(topping);
    }
}
