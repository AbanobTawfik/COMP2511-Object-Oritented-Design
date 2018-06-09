package Lab5;

public class SizeSort<E> implements Heuristic<E> {
    @Override
    public int score(E node) {
        return (int) node.toString().length();
    }

    @Override
    public int compare(E node1, E node2) {
        if (score(node1) < score(node2))
            return -1;
        if (score(node1) > score(node2))
            return 1;
        return 0;
    }
}
