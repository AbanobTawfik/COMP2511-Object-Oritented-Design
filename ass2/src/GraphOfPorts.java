import java.util.*;
public class GraphOfPorts implements Graph<Node> {
    private int nV;
    //keep track of the vertices on graph
    private LinkedList<Node> vertices;
    private int[][] edges;


    public GraphOfPorts(int nV) {
        this.nV = nV;
        int[][] graphEdges = new int[nV][nV];
        //initalise weighted graph with no edges
        for (int i = 0; i < nV; i++) {
            for (int j = 0; j < nV; j++) {
                if(i == j)
                    graphEdges[i][j] = 0;

                graphEdges[i][j] =-1;

            }
        }
        setEdges(graphEdges);
    }

    public void setEdges(int[][] edges) {
        this.edges = edges;
    }

    public void setVertices(LinkedList<Node> vertices) {
        this.vertices = vertices;
    }

    public LinkedList<Node> getVertices() {
        return vertices;
    }

    public int getnV() {
        return nV;
    }

    public int[][] getEdges() {
        return edges;
    }


    @Override
    public void insertEdge(Node node1, Node node2, int weighting) {
        //since it is a bidirectional fraph
        int index1 = getNodeIndex(node1);
        int index2 = getNodeIndex(node2);
        edges[index1][index2] = weighting;
        edges[index2][index1] = weighting;

    }

    @Override
    public boolean isConnected(Node node1, Node node2) {
        int index1 = node1.getIndexOnGraph();
        int index2 = node2.getIndexOnGraph();

        return (edges[index1][index2] > 0 && edges[index2][index1] >= 1);
    }


    @Override
    public int getNodeIndex(Node node) {
        return node.getIndexOnGraph();
    }

    @Override
    public Node getNodeByIndex(int index) {
        return vertices.get(index);
    }

    @Override
    public Node getNodeByString(String s) {
        for (int i = 0; i < nV; i++) {
            if (vertices.get(i).getPortName().equals(s))
                return vertices.get(i);
        }
        return null;
    }

    public int getWeightOfEdge(Node n1, Node n2) {
        int index1 = n1.getIndexOnGraph();
        int index2 = n2.getIndexOnGraph();
        return edges[index1][index2];
    }

    public LinkedList<Node> getNeighbours(Node node) {
        LinkedList<Node> resultant = new LinkedList<>();
        for (int i = 0; i < nV; i++) {
            Node node2 = getNodeByIndex(i);
            if (isConnected(node, node2))
                resultant.add(node2);
        }

        return resultant;
    }


}
