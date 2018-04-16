import java.util.*;
public class nodeComparator implements Heuristic<Node>, Comparator<Node>{

    LinkedList<DirectedEdge> schedule;
    GraphOfPorts g;

    public nodeComparator(LinkedList<DirectedEdge> schedule, GraphOfPorts g) {
        this.schedule = schedule;
        this.g = g;
    }

    @Override
    public int compare(Node e1, Node e2){
        if(getNodeDegree(e1) < getNodeDegree(e2))
            return -1;
        if(getNodeDegree(e1) > getNodeDegree(e2))
            return 1;
        return 0;
    }

    //we want to give it a heuristic score based on which node has a better outdegree on schedule
    //for example say we have the situation in this graph where all these nodes are linked and we have the following schedule
    // Sydney->Vancouver
    // Singapore-> Vancouver
    // Shanghai -> Singapore
    //primarily when we make the choice the first thing we do is look for how long can we follow the chain on schedule
    // in this example shanghai is the optimal node with outdegree as 2, since
    // sh-> si -> V
    // where as the other nodes go to vancouver which is a dead end

    //we want to check for this degree

    @Override
    public int getNodeDegree(Node obj){
        int score = obj.getRefuellingTime();
        int i = g.getNodeIndex(obj);

        return score;
    }

    //secondary if the nodes all have same degree i.e all lead to dead ends, pick the least time effort
    //i.e port To refuel time + time taken to get to it is lowest
    @Override
    public int getNodeDegree(Node obj)
}
