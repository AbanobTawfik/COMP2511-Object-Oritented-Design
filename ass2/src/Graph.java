import java.util.*;

/**
 * The interface Graph. This is a generic graph implemented using the stratergy pattern, where the method can vary
 * based on the task or type of input. the interface is designed for adjacency matrix graphs. the methods
 * supplid in this interface are all the core methods required to create a graph besides the add node function, since i chose
 * to run with adjacency matrix, i chose not to include an add node function instead, create it all in the class constructor
 * a list of vertices is created along with the graph in adjacency matrix form in order to give node identification to the
 * index on the matrix, eg. arraylist [sydney,shanghai,manila,singapore,vancouver] index 0 = sydney and edge[0][2] is the
 * edge weight from sydney to manila
 *
 * @param <E> the type parameter
 */
public interface Graph<E> {

    /**
     * will add a bi-directional edge between e1, e2 with the same weighting. this method guarantees to
     * connect two nodes on a graph and assign a numerical weighting for that connection
     * <br /> this method expects two nodes and an integer output and will guarantee to connect those nodes with that weighting
     *
     * @param node1     the first node we are connecting, non null node
     * @param node2     the second node we are connecting, non null node
     * @param weighting a numberical weighting of the connection
     */
    void insertEdge(E node1, E node2, int weighting);

    /**
     * Gets the index for node on the graph, since this is an adjacancy list matrix, the index of a node will allow access to a node from it's identifier
     * <br /> this method accepts any input, and will guarantee to return the index of the node if it exists, or null if no node exists
     *
     * @param node the node we are locating
     * @return the node index or -1 if no node exists
     */
    int getNodeIndex(E node);


    /**
     * Gets node by string identifier.
     * <br /> this method will accept any String input, and will return the node with the respective string value
     *
     * @param s the string for the nodes identifier e.g node "Sydney".
     * @return the node with the respective identifier
     */
    E getNodeByString(String s);


    /**
     * This method will assosciate a list of vertices to the nodes of the graph, where the index of the vertex on the vertex list
     * is representitive of the graph matrix index for the node
     * @param vertices the vertices
     */
    void setVertices(ArrayList<E> vertices);


    /**
     * this method  will return the number of nodes in the graph, it is required for scanning through the adjacency matrix.
     * <br /> this is expected to always return the number of nodes in the graph
     * @return the number of nodes in graph
     */
    int getnV();

    /**
     * this method will return the edges in the graph, the 2d adjacency matrix with all the weights stored inside
     * <br /> this method will always be guaranteed to return a 2d matrix with known graph weights
     * @return the edges in the adjacency matrix
     */
    int[][] getEdges();

    /**
     * this method will set a 2d matrix as the adjacency matrix for the graph with or without corresponding weights
     *
     * @param edges the 2d matrix we are storing our weights in
     */
    void setEdges(int[][] edges);
}
