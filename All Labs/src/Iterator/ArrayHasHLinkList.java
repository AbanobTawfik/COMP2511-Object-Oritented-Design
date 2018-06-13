package Iterator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

public class ArrayHasHLinkList{
    private Iterator iterator1;
    private Iterator iterator2;
    private Iterator iterator3;


    public void addAllTypes(){
        ArrayList<String> first = new ArrayList<>();
        first.add("HI");
        first.add("Bye");
        iterator1 = new IteratorImplement<>(first.toArray());
        HashSet<Integer> second = new HashSet<Integer>();
        second.add(4);
        iterator2 = new IteratorImplement<>(second.toArray());

        LinkedList<Boolean> third = new LinkedList<Boolean>();
        third.add(false);
        third.add(true);
        iterator3 = new IteratorImplement<>(third.toArray());
    }

    public void showCollection1(){
        System.out.println("-Collection 1-");
        while(iterator1.hasNext()){
            System.out.println(iterator1.next());
        }
        System.out.println(" ");
        System.out.println("-Collection 2-");
        while(iterator2.hasNext()){
            System.out.println(iterator2.next());
        }
        System.out.println(" ");

        System.out.println("-Collection 3-");
        while(iterator3.hasNext()){
            System.out.println(iterator3.next());
        }
    }

}
