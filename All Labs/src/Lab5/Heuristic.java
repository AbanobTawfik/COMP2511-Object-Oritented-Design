package Lab5;

public interface Heuristic<E> {
    int score(E node);
    int compare(E node1, E node2);
}
