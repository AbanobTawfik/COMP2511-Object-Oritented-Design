import java.util.*;
public class nodeComparator implements Heuristic<Node>, Comparator<Node>{
    private LinkedList<Node> currentpath;
    private LinkedList<DirectedEdge> schedule;
    private GraphOfPorts g;

    public nodeComparator(LinkedList<Node> currentpath, LinkedList<DirectedEdge> schedule, GraphOfPorts g) {
        this.currentpath = currentpath;
        this.schedule = schedule;
        this.g = g;
    }

    public void setSchedule(LinkedList<DirectedEdge> schedule) {
        this.schedule = schedule;
    }

    @Override
    public int compare(Node e1, Node e2){
        if(getNodeDegree(e1) < getNodeDegree(e2))
            return 1;
        if(getNodeDegree(e1) > getNodeDegree(e2))
            return -1;
        if(getNodeDegree(e1) == getNodeDegree(e2)){
            if(getNodeTime(e1) < getNodeTime(e2))
                return -1;
            if(getNodeTime(e1) > getNodeTime(e2))
                return 1;
            if(getNodeTime(e1) == getNodeTime(e2))

                return 0;
        }
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
        int degree = 0;
        Node tmp = obj;
        LinkedList<Node> toSearch = new LinkedList<Node>();
        toSearch.add(tmp);
        boolean addedNode = false;
        //now we want to check the degree of the node to the schedule so we scan through the schedule
        while(!toSearch.isEmpty()) {
            tmp = toSearch.poll();
            addedNode = false;
            for (DirectedEdge d : schedule) {
                //if the node is a FROM node in the schedule
                //we want to add it to our list of seen nodes
                //then we want to update our tracking node to the to node
                if (d.getFrom().equals(tmp)) {
                    toSearch.add(d.getTo());
                    addedNode = true;
                }
            }
            if(addedNode)
                degree++;
        }


        return degree;
    }

    //secondary if the nodes all have same degree i.e all lead to dead ends, pick the least time effort
    //i.e port To refuel time + time taken to get to it is lowest
    @Override
    public int getNodeTime(Node obj){
        if(currentpath.size() < 1)
            return 0;
        int refuel = obj.getRefuellingTime();
        //now we want to just assign the weight of the refuel time for the obj and the weight between
        //the object and the last node in the path
        Node lastVisited = currentpath.getLast();
        int indexOfLastVisitedNode = g.getNodeIndex(lastVisited);
        int indexOfNode = g.getNodeIndex(obj);
        int matrix[][] = g.getEdges();
        int timeBetweenPorts = matrix[indexOfLastVisitedNode][indexOfNode];
        return refuel + timeBetweenPorts;
    }


}
