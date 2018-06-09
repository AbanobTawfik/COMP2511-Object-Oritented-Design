package Lab6;

import java.util.List;

public interface Item {
    double getPrice();
    Item clone();
    void showPrice();
    List<Item> getChildren();
}
