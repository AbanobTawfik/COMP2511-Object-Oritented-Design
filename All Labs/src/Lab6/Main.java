package Lab6;


import com.sun.org.apache.xpath.internal.SourceTree;

public class Main {
    public static void main(String args[]){
        Assembly assembly = new Assembly();
        BasicItem item = new BasicItem(50);
        System.out.println("--------------------------------------------------------------------");
        System.out.println("Adding a single wheel");
        item.showPrice();
        assembly.addItem(item);
        DiscountItem discountedItem = new DiscountItem(item);
        discountedItem = new DiscountItem(discountedItem);
        System.out.println("--------------------------------------------------------------------");
        System.out.println("Adding 3 discounted wheels");
        discountedItem.showPrice();
        assembly.addItem(discountedItem);
        assembly.addItem(discountedItem);
        assembly.addItem(discountedItem);
        Assembly wheels = new Assembly();
        wheels.addItem(assembly);
        System.out.println("--------------------------------------------------------------------");
        item = new BasicItem(1000);
        System.out.println("Adding the Engine");
        item.showPrice();
        Assembly engine = new Assembly();
        engine.addItem(item);
        System.out.println("--------------------------------------------------------------------");
        item = new BasicItem(5000);
        System.out.println("Adding car frame");
        item.showPrice();
        Assembly frame = new Assembly();
        frame.addItem(item);
        System.out.println("--------------------------------------------------------------------");
        item = new BasicItem(1000);
        discountedItem = new DiscountItem(item);
        System.out.println("Adding car interior with discount");
        discountedItem.showPrice();
        Assembly interior = new Assembly();
        interior.addItem(discountedItem);
        System.out.println("--------------------------------------------------------------------");
        System.out.println("Assembling Car...");
        DiscountItem Wheels = new DiscountItem(wheels);
        Assembly car = new Assembly();
        car.addItem(wheels);
        car.addItem(engine);
        car.addItem(frame);
        car.addItem(interior);

        System.out.println("\n--------------------------------------------------------\nFINAL TAB\n--------------------------------------------------------");
        car.showPrice();
        System.out.println("Total price -> " + car.getPrice());
    }
}
