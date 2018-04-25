import java.util.*;

public class searchNode {
    private DirectedEdge shipment;
    private LinkedList<DirectedEdge> children;
    private int GScore;
    private int HScore;
    private int FScore;

    public searchNode(DirectedEdge shipment) {
        this.shipment = shipment;
        this.children = new LinkedList<DirectedEdge>();
        this.GScore = Integer.MAX_VALUE;
    }

    public DirectedEdge getShipment() {
        return shipment;
    }

    public void setShipment(DirectedEdge shipment) {
        this.shipment = shipment;
    }

    public LinkedList<DirectedEdge> getChildren() {
        return children;
    }

    public void setChildren(LinkedList<DirectedEdge> children) {
        this.children = children;
    }

    public int getGScore() {
        return GScore;
    }

    public void setGScore(int GScore) {
        this.GScore = GScore;
    }

    public int getHScore() {
        return HScore;
    }

    public void setHScore(int HScore) {
        this.HScore = HScore;
    }

    public int getFScore() {
        return FScore;
    }

    public void setFScore() {
        this.FScore = HScore + GScore;
    }

    public void addToChildren(DirectedEdge d){
        this.children.add(d);
    }

    @Override
    public String toString() {
        return
                 shipment +" " +
                 children ;
    }
}
