package Lab5;

import java.util.List;

public interface Graph<E> {
    int[][] getEdges();
    boolean addNode(E node);
    boolean addEdge(E node1, E node2);
    int size();
    List<E> getVertices();
    boolean isConnected(E node1, E node2);
    List<E> getNeighbours(E node);
    boolean removeNode(E node);
    int nodeIndex(E node);
    E indexNode(int index);
    void print();
}
