public class DirectedEdge {
    private Node from;
    private Node to;

    public DirectedEdge(Node from, Node to) {
        this.from = from;
        this.to = to;
    }

    public Node getFrom() {
        return from;
    }

    public Node getTo() {
        return to;
    }
}
