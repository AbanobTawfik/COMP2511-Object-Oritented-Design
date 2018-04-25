import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DirectedEdge)) return false;
        DirectedEdge that = (DirectedEdge) o;
        return Objects.equals(getFrom(), that.getFrom()) &&
                Objects.equals(getTo(), that.getTo());
    }

    @Override
    public String toString() {
        return
                 from + " -> " +
                 to ;
    }
}
