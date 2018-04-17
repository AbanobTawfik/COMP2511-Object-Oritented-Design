public interface Graph<E> {

    /**
     * will add a bi-directional edge between e1, e2 with the same weighting. this method guarantees to
     * connect two nodes on a graph and assign a numerical weighting based on a hueristic for the connection
     * @param node1 the first node we are connecting, non null node
     * @param node2 the second node we are connecting, non null node
     * @param weighting a numberical weighting of the connection
     */
    void insertEdge(E node1, E node2, int weighting);

    /**
     * this method will check if two nodes in a graph are connected
     * @param node1 the first node we are checking the connection for
     * @param node2 the second nude we are checking the connection for
     * @return true if there is a connection, false if no connection
     */
    boolean isConnected(E node1, E node2);

    /**
     * this method will check if a node exists in the graph
     * @param node the node we are checking
     * @return true if it exists in graph, false if it doesn't exist
     */
    boolean nodeExistsInGraph(E node);

    int getNodeIndex(E node);

    E getNodeByIndex(int index);

    E getNodeByString(String s);
}
