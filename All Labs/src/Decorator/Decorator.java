package Decorator;

public class Decorator implements Item{
    private Item toBeDecorated;
    private String decorationTopping;
    private String FullItem;

    public Decorator(Item toBeDecorated, String decorationTopping) {
        this.toBeDecorated = toBeDecorated;
        this.decorationTopping = decorationTopping;
        this.FullItem = toBeDecorated.composition() + " adding " + decorationTopping;

    }

    @Override
    public String composition() {
        return toBeDecorated.composition() + " " + decorationTopping;
    }

    @Override
    public void display() {
        System.out.println(FullItem);
    }
}
