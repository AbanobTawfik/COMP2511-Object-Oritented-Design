import java.util.*;

/**
 * This class is representivie of a search Node
 * for this project, since our path will be consistent of a initial node sydney
 * and then it will consistent of multiple shipments, each path can be thought of as
 * a root node with a list of nodes that come from that path. in our case we are working
 * with shipments, rather than nodes to simplify the project down, however to do this properly for our case
 * we must bootstrap sydney as a directedEdge from sydney to sydney.
 */
public class searchNode {
    private DirectedEdge shipment;
    private LinkedList<DirectedEdge> children;
    private int GScore;
    private int HScore;
    private int FScore;

    /**
     * Instantiates a new Search node with a root shipment, this method will create a new search node
     * with a root shipment (shipment) and create an empty list of that shipment's path. it will also
     * start with a GSCORE of 0x7fffff, maximum integer value in order to make comparison possible. as initalising
     * with a maximum integer value will allow comparisons for gscore less than
     *
     * @param shipment root shipment, which in all cases will be sydney-sydney bootstrap for this method
     */
    public searchNode(DirectedEdge shipment) {
        this.shipment = shipment;
        this.children = new LinkedList<DirectedEdge>();
        this.GScore = Integer.MAX_VALUE;
    }

    /**
     * This method will return the root shipment for the searchNodes current path
     * <br /> this method will guarantee to return the root for a shipment.
     * @return the root of the path, where the path originiates
     */
    public DirectedEdge getShipment() {
        return shipment;
    }

    /**
     * This method will return the current path from the root shipment
     * <br /> this method will guarantee to return the "children" which are the shipments
     * which compose the current path
     * @return the shipments which compose the path
     */
    public LinkedList<DirectedEdge> getChildren() {
        return children;
    }

    /**
     * This method will be used for setting the previous path's shipments to a new search nodes path.
     * <br/> This method will guarantee to set the current searchnode's path as a different node's path provided
     * it receives a valid list (including empty) of shipments
     * @param children the path we are setting for our searchNode
     */
    public void setChildren(LinkedList<DirectedEdge> children) {
        this.children = children;
    }

    /**
     * This method will set the GScore, which is the cost to get from the last node in the path
     * from the very start node, it will be already calculated and this method will assign it to the searchNode
     * <br /> This method will guarantee to set a gscore for the path, provided it receives a integer
     * @param GScore the weight to get from the start aka root of path, to the last node of the path.
     */
    public void setGScore(int GScore) {
        this.GScore = GScore;
    }

    /**
     * This method will be used for setting the heuristic score of the path, the heuristic score is an estimate
     * which is calculated beforehand, and set with this method
     * <br/> this method will guarantee to set a heuristic score for the path, provided it receives a valid integer
     * @param HScore the heuristic score aka time estimated till end
     */
    public void setHScore(int HScore) {
        this.HScore = HScore;
    }

    /**
     * This method will retrieve the final score (sum of heuristic and gscore) for the current path
     * <br/> this method will guarantee to return the final score of the path
     * @return the f score
     */
    public int getFScore() {
        return FScore;
    }

    /**
     * This method will set the final score by summing the heuristic score and gscore, this method should be called
     * after the heuristic score and gscore is calculated, however it can be called whenever and it will guarantee to
     * sum the heuristic score and gscore.
     */
    public void setFScore() {
        this.FScore = HScore + GScore;
    }

    /**
     * This method will add a shipment (node pair from, to) to the current searchNode (path)
     * <br/> this method will guarantee to add a shipment to a path, provided a valid shipment
     * @param d the shipment we are appending to our path
     */
    public void addToChildren(DirectedEdge d) {
        this.children.add(d);
    }

    /**
     * this is a tostring method to print out the path visually during debugging (for easre
     * @return a string representation of the path
     */
    @Override
    public String toString() {
        return
                shipment + " " +
                        children;
    }
}
