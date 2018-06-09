package Lab5;

import java.util.Comparator;

public class NodeComparator<E> implements Comparator<E> {
    private Heuristic heuristic;

    public NodeComparator(Heuristic heuristic) {
        this.heuristic = heuristic;
    }

    @Override
    public int compare(E node1, E node2) {
        return heuristic.compare(node1, node2);
    }
}
