import java.util.*;

public class Node {
    private String portName;
    private int refuellingTime;
    private int indexOnGraph;

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public int getRefuellingTime() {
        return refuellingTime;
    }

    public void setRefuellingTime(int refuellingTime) {
        this.refuellingTime = refuellingTime;
    }

    public int getIndexOnGraph() {
        return indexOnGraph;
    }

    public void setIndexOnGraph(int indexOnGraph) {
        this.indexOnGraph = indexOnGraph;
    }
}
