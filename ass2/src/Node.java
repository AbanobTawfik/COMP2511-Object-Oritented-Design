import java.util.*;

/**
 * The type Node which will be used as the vertex's for the graph. it will contain the port Name which is
 * the identifier for the Node (vertex). it will contain the refuelling time which is how much time is required
 * before another shipment is set out. it will contain the index on the graph where the node is so that
 * the adjacency matrix can locate the nodes.
 */
public class Node {
    //the name of the port which is the node's identifier
    private String portName;
    //the time taken to refuel at the port
    private int refuellingTime;
    //the index on the graph where the node is located
    private int indexOnGraph;


    /**
     * Instantiates a new Node which contains a port name, the refuelling time and index on the graph.
     * <br/> this method will create a new node with the given details (port name, refuelling time, index on graph)
     * provided it is given a valid string, a non zero integer for refuelling time, and a non negative integer for the index on the graph
     *
     * @param portName       the name of the Node (Port)
     * @param refuellingTime the time required to refuel at a port between shipments
     * @param indexOnGraph   the index on graph where the node is located
     */
    public Node(String portName, int refuellingTime, int indexOnGraph) {
        //sets the name of the port (identifier for node)
        this.portName = portName;
        //sets the time ro refuel as integer value
        this.refuellingTime = refuellingTime;
        //sets the index on the graph where it is located on the graph
        this.indexOnGraph = indexOnGraph;
    }

    /**
     * This method will return the name of the Node(Port)
     * <br/> This method will guarantee to return the name of the port
     *
     * @return the port name
     */
    public String getPortName() {
        //return the string (port) identifier for the node
        return portName;
    }

    /**
     * This method will return the time taken to refuel for the Node(Port)
     * <br/> This method will guarantee to return the time taken to refuel for the port
     *
     * @return the time taken to refuel at the port
     */
    public int getRefuellingTime() {
        //return the refuelling time for that node
        return refuellingTime;
    }

    /**
     * This method will return the index on the graph where the Node(Port) is located
     * <br/> This method will guarantee to return the index on the graph where the Node(Port) is located
     *
     * @return the index on the graph where the Node is located
     */
    public int getIndexOnGraph() {
        //return the index on the graph where the node is located
        return indexOnGraph;
    }

    /**
     * This method is used as a method to represent a node in string form
     *
     * @return a string representive of the node
     */
    @Override
    public String toString() {
        //return the string (port) identifier for the node DEBUGGING USE
        return portName;
    }

    /**
     * This method will be used to compare different nodes together, all the components of the node
     * are compared
     *
     * @param o the other node we are comparing to
     * @return true if the nodes are equal, or false if they are not
     */
    @Override
    public boolean equals(Object o) {
        //if its equal to the current Node itself, then return true
        if (this == o) return true;
        //if the object is not a Node return false
        if (!(o instanceof Node)) return false;
        //otherwise cast the object as a Node
        Node node = (Node) o;
        //now we want to compare the casted objects field with the current nodes fields and make sure
        //return the boolean that
        //they are all equal
        return getRefuellingTime() == node.getRefuellingTime() &&
                getIndexOnGraph() == node.getIndexOnGraph() &&
                Objects.equals(getPortName(), node.getPortName());
    }

}
