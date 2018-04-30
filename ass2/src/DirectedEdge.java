import java.util.Objects;

/**
 * This class DirectedEdge, is a class which can create shipments as a pair of nodes which has
 * a node which is the start of shipment, the from node. and a node which is the end of the shipment the to node
 * this allows the direction of a shipment to be accounted for. it is a more advanced node pair for the graph.
 * A shipment that is for example Sydney -> vancouver
 * can be thought of as a directed edge <br/>
 * the shipment originates in sydney (the from node) <br/>
 * the shipment finishes at vancouver (the to node) <br/>
 * this class allows sydney-> vancouver to be represented as a node pair IN THE CORRECT ORDER (Sydney, vancouver)
 */
public class DirectedEdge {
    private Node from;
    private Node to;

    /**
     * Instantiates a new Directed edge which contains a shipment (directed edge) that represents direction as
     * start of shipment -> end of shipment.
     * <br/> this method will guarantee to create a shipment with two nodes, that is directional from -> to provided
     * it receives two valid nodes
     * @param from Node which the shipment starts in
     * @param to   Node which the shipment finishes at
     */
    public DirectedEdge(Node from, Node to) {
        this.from = from;
        this.to = to;
    }

    /**
     * This method will return the node where the shipment starts at
     * <br/> this method will guarantee to return the node where the shipment originates
     * @return the node which the shipment originates from
     */
    public Node getFrom() {
        return from;
    }

    /**
     * This method will return the node where the shipment finishes at
     * <br/> this method will guarantee to return the node where the shipment finishes
     * @return the node which the shipment finishes at
     */
    public Node getTo() {
        return to;
    }

    /**
     * This equals Overrides the equals method to compare directed edges, since this will be required to
     * compare two shipments to check if they are the same.
     * @param o the shipment we are comparing to our current shipment
     * @return true if the object we are comparing is equivalent in everyway or false if not.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DirectedEdge)) return false;
        DirectedEdge that = (DirectedEdge) o;
        return Objects.equals(getFrom(), that.getFrom()) &&
                Objects.equals(getTo(), that.getTo());
    }

    /**
     * This method will be used to represent the directed edge in string form (mainly for debugging)
     * @return a string representitive of the current directed edge
     */
    @Override
    public String toString() {
        return
                 from + " -> " +
                 to ;
    }
}
