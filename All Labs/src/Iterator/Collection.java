package Iterator;

import java.util.ArrayList;

public class Collection {
    private ArrayList<String> elements = new ArrayList<>();

    public static void main(String args[]){
        Collection c = new Collection();
        c.run();
    }

    public void run(){
        ArrayHasHLinkList h = new ArrayHasHLinkList();
        h.addAllTypes();
        h.showCollection1();
    }
}
