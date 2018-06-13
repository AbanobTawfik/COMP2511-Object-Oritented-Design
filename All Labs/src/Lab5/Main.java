package Lab5;

import java.util.ArrayList;

public class Main {
    public static void main(String args[]){
        GenericGraph<String> s = new GenericGraph<>();
        String hi = "hi";
        s.addNode(hi);
        String bye = "bye";
        s.addNode(bye);
        String k = "k";
        s.addNode(k);
        String lol = "lol";
        s.addNode(lol);
        String imnot = "im not";
        s.addNode(imnot);

        s.addEdge(hi,bye);
        s.addEdge(k,lol);
        s.addEdge(hi,lol);

        s.removeNode(hi);
        s.print();

        GenericGraph2<Integer> i = new GenericGraph2<Integer>();
        Integer one = 1;
        i.addNode(one);
        Integer two = 2;
        i.addNode(two);
        Integer three = 3;
        i.addNode(three);
        Integer four = 4;
        i.addNode(four);
        Integer five = 5;
        i.addNode(five);

        i.addEdge(3,one,two);
        i.addEdge(4,three,one);
        i.addEdge(5,four,three);
        i.addEdge(6,one,one);
        i.removeEdge(one,two);
        i.print();
        AlphabetSort<String> n = new AlphabetSort<String>();
        NodeComparator<String> compa = new NodeComparator<>(n);
        System.out.println(i.getNeighbours(three));
    }
}
