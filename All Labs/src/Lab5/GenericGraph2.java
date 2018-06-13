package Lab5;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class GenericGraph2<E> {
    private HashMap<E, ArrayList<Pair<Integer, E>>> edges = new HashMap<>();

    public int size() {
        return edges.size();
    }

    public boolean removeNode(E node) {
        if (!edges.containsKey(node))
            return false;
        edges.remove(node);
        Iterator<E> keys = (Iterator<E>) edges.keySet().iterator();
        for (E list : edges.keySet())
            edges.get(list).remove(node);
        return true;
    }

    public ArrayList<Pair<Integer, E>> getNeighbours(E node) {
        return edges.get(node);
    }


    public boolean addEdge(int weight,E node1, E node2) {
        if (!(edges.containsKey(node1) || edges.containsKey(node2)))
            return false;
        if (node1.equals(node2))
            return false;
        edges.get(node1).add(new Pair<>(weight,node2));
        edges.get(node2).add(new Pair<>(weight,node1));
        return false;
    }

    public boolean addNode(E node) {
        edges.put(node, new ArrayList<Pair<Integer, E>>());
        return true;
    }

    public boolean removeEdge(E node1, E node2){
        if(!edges.containsKey(node1) || !edges.containsKey(node2))
            return false;
        for(int i = 0; i < edges.get(node1).size();i++) {
            if(edges.get(node1).get(i).getValue().equals(node2)) {
                edges.get(node1).remove(i);
                break;
            }
        }
        for(int i = 0; i < edges.get(node2).size();i++) {
            if(edges.get(node2).get(i).getValue().equals(node1)) {
                edges.get(node2).remove(i);
                break;
            }
        }
        return true;
    }

    public void print() {
        Iterator<E> it = (Iterator<E>) edges.keySet().iterator();
        while (it.hasNext()) {
            E ee = it.next();
            System.out.println("Node - " + ee.toString() + " Neighbours" + edges.get(ee));
        }
    }
}
