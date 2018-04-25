import java.util.*;

public class Node {
    private String portName;
    private int refuellingTime;
    private int indexOnGraph;


    public Node(String portName, int refuellingTime, int indexOnGraph) {
        this.portName = portName;
        this.refuellingTime = refuellingTime;
        this.indexOnGraph = indexOnGraph;
    }

    public String getPortName() {
        return portName;
    }

    public int getRefuellingTime() {
        return refuellingTime;
    }


    public int getIndexOnGraph() {
        return indexOnGraph;
    }

    @Override
    public String toString() {
        return  portName  ;
    }
}
