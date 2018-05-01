import java.util.*;

/**
 * This class is a implementation from the graph interface
 * it is a graph constructed of vertices from type Node with a 2d array of weights for all the nodes
 * all the vertices on the graph are given in a list form, and have the same corresponding index's as the row/column index
 * on the graphs edges
 */
/*
 * this class utilises the stratergy pattern to create a graph of type Node which is an
 * adjacency matrix graph type
 */
public class GraphOfPorts implements Graph<Node> {
    //this is the number of nodes in the graph
    private int nV;
    //keep track of the vertices on graph
    private ArrayList<Node> vertices;
    //the edges assosciated with that graph
    private int[][] edges;

    /**
     * Instantiates a new Graph with a 2d matrix of weights, size nV (the number of nodes in graph).
     * <br /> this graph expects a non negative integer as the number of nodes, and will create a object
     * which contains edges[nV][nV]
     *
     * @param nV the number of nodes in the graph
     */
    public GraphOfPorts(int nV) {
        //when creating a graph
        this.nV = nV;
        //this will be used to se the edges for our graph
        int[][] graphEdges = new int[nV][nV];
        //initalise weighted graph with edges weighted -1 (non existant)
        //scan through the rows and columns of the matrix
        for (int i = 0; i < nV; i++) {
            for (int j = 0; j < nV; j++) {
                //the edgeweight to the same node sydney->Sydney = 0
                if (i == j)
                    graphEdges[i][j] = 0;
                //otherwise initalise the edge weight as not existant (-1)
                else
                    graphEdges[i][j] = -1;

            }
        }
        //set the edges we initalised for the graph
        setEdges(graphEdges);
    }

    /**
     * this method will set a 2d matrix as the adjacency matrix for the graph with or without corresponding weights
     *
     * @param edges the 2d matrix we are storing our weights in
     */
    @Override
    public void setEdges(int[][] edges) {
        //this method will set the edges for the graph
        this.edges = edges;
    }


    /**
     * This method will assosciate a list of vertices to the nodes of the graph, where the index of the vertex on the vertex list
     * is representitive of the graph matrix index for the node
     *
     * @param vertices the vertices
     */
    @Override
    public void setVertices(ArrayList<Node> vertices) {
        //this method will set a list of vertices assosciated with that graph (identifiers for the nodes)
        this.vertices = vertices;
    }

    /**
     * this method  will return the number of nodes in the graph, it is required for scanning through the adjacency matrix.
     * <br /> this is expected to always return the number of nodes in the graph
     *
     * @return the number of nodes in graph
     */
    @Override
    public int getnV() {
        //this method will return the number of vertices in graph (useful for scanning through the graph)
        return nV;
    }

    /**
     * this method will return the edges in the graph, the 2d adjacency matrix with all the weights stored inside
     * <br /> this method will always be guaranteed to return a 2d matrix with known graph weights
     *
     * @return the edges in the adjacency matrix
     */
    @Override
    public int[][] getEdges() {
        //this method will return the edges of the graph which will be used to work out edge weights
        return edges;
    }

    /**
     * will add a bi-directional edge between two Nodes with the same weighting. this method guarantees to
     * connect two nodes on a graph and assign a numerical weighting for that connection
     * <br /> this method expects two nodes and an integer output and will guarantee to connect those nodes with that weighting
     *
     * @param node1     the first node we are connecting, non null node
     * @param node2     the second node we are connecting, non null node
     * @param weighting a numberical weighting of the connection
     */
    @Override
    public void insertEdge(Node node1, Node node2, int weighting) {
        //since it is a bidirectional fraph inserting an edges has to be the same in both direction
        //Sydney -> Vancouver and Vancouver->Sydney have same travel time (different refuel times)
        //we want to first find the index where the nodes are located
        int index1 = getNodeIndex(node1);
        int index2 = getNodeIndex(node2);
        //then we went to set the the weight of the connection bi-directionally to the same weight input
        edges[index1][index2] = weighting;
        edges[index2][index1] = weighting;

    }

    /**
     * Gets the index for node on the graph, since this is an adjacancy list matrix, the index of a node will allow access to a node from it's identifier
     * <br /> this method accepts any input, and will guarantee to return the index of the node if it exists, or null if no node exists
     *
     * @param node the node we are locating
     * @return the node index or -1 if no node exists
     */
    @Override
    public int getNodeIndex(Node node) {
        //this method returns the index on the graph where the node is located
        return node.getIndexOnGraph();
    }

    /**
     * Gets node by string identifier.
     * <br /> this method will accept any String input, and will return the node with the respective string value
     *
     * @param s the string for the nodes identifier e.g node "Sydney".
     * @return the node with the respective identifier
     */
    @Override
    public Node getNodeByString(String s) {
        //this method will scan through the vertex list
        for (int i = 0; i < nV; i++) {
            //if the portName is equivalent, then return the node that has the same port name
            if (vertices.get(i).getPortName().equals(s))
                return vertices.get(i);
        }
        //return null if no node is found
        return null;
    }
}
