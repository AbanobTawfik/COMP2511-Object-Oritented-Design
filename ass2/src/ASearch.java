import java.util.*;

/**
 * The type A search.
 */
public class ASearch {
    private int nodesExpanded;

    /**
     * Search linked list.
     *
     * @param g        the g
     * @param schedule the schedule
     * @return the linked list
     */
    public LinkedList<DirectedEdge> Search(GraphOfPorts g, LinkedList<DirectedEdge> schedule) {
        //if the graph only has 1 node there is no search required
        if (g.getnV() <= 1)
            return null;
        //if there is no schedule or only 1 port then return there is no search required
        if (schedule.size() <= 1)
            return null;
        nodesExpanded = 0;
        //now we want to make two queues, one for the search and one for the final return path
        LinkedList<Node> closed = new LinkedList<Node>();
        //the only queue which requires to be ordered by the heuristic is the open one
        Comparator<Node> comparator = new NodeComparator(closed, schedule, g);
        PriorityQueue<Node> open = new PriorityQueue<Node>(g.getnV(), comparator);
        //assuming ship will start in Sydney each time we want Sydney to be the first Node in our queue for search
        Node initial = g.getNodeByString("Sydney");
        //if the node Sydney is not existant in the graph we return null error
        if (initial.equals(null))
            return null;
        //add the initial node to our queue
        open.add(initial);
        //similair to BFS search till either the search queue is empty or the route has been completed
        while (!open.isEmpty() && !isCompletedSchedule(closed, schedule)) {
            Node curr = open.poll();
            nodesExpanded++;
            closed.add(curr);
            open.clear();
            LinkedList<Node> neighbours = g.getNeighbours(curr);
            for (Node c : neighbours) {
                open.add(c);
            }

            updateSchedule(closed, schedule);

            comparator = new NodeComparator(closed, schedule, g);
            PriorityQueue<Node> tmpQueue = open;
            open = new PriorityQueue<Node>(open.size(), comparator);
            for (Node n : tmpQueue) {
                open.add(n);
            }

        }
        LinkedList<DirectedEdge> path = createPath(closed);
        return path;
    }

    /**
     * Is completed schedule boolean.
     *
     * @param route    the route
     * @param schedule the schedule
     * @return the boolean
     */
//want to scan through LinkedList of nodes and create a Directed EGe
    //check if the schedule has been completed by checking remaining schedule with the current one
    public boolean isCompletedSchedule(LinkedList<Node> route, LinkedList<DirectedEdge> schedule) {
        //if there is only 1 node in the list of nodes return need atleast 2 for a directed edge
        if (route.size() == 1)
            return false;
        LinkedList<DirectedEdge> check = new LinkedList<DirectedEdge>();
        for (int i = 0; i < route.size() - 1; i++) {
            DirectedEdge d = new DirectedEdge(route.get(i), route.get(i + 1));
            check.add(d);
        }
        return route.containsAll(schedule);
    }

    /**
     * Update schedule.
     *
     * @param route    the route
     * @param schedule the schedule
     */
    public void updateSchedule(LinkedList<Node> route, LinkedList<DirectedEdge> schedule) {
        if (route.size() == 1)
            return;
        LinkedList<DirectedEdge> check = new LinkedList<DirectedEdge>();
        for (int i = 0; i < route.size() - 1; i++) {
            DirectedEdge d = new DirectedEdge(route.get(i), route.get(i + 1));
            check.add(d);
        }

        for (int i = 0; i < schedule.size(); i++) {
            for (int j = 0; j < check.size(); j++) {
                if (schedule.get(i).getFrom().equals(check.get(j).getFrom()) && schedule.get(i).getTo().equals(check.get(j).getTo()))
                    schedule.remove(i);
            }
        }
    }

    /**
     * Create path linked list.
     *
     * @param route the route
     * @return the linked list
     */
    public LinkedList<DirectedEdge> createPath(LinkedList<Node> route) {
        LinkedList<DirectedEdge> path = new LinkedList<DirectedEdge>();
        for (int i = 0; i < route.size() - 1; i++) {
            DirectedEdge d = new DirectedEdge(route.get(i), route.get(i + 1));
            path.add(d);
        }
        return path;
    }


    /**
     * Gets cost.
     *
     * @param edges the edges
     * @param g     the g
     * @return the cost
     */
    public int getCost(LinkedList<DirectedEdge> edges, GraphOfPorts g) {
        int cost = 0;
        for (int i = 0; i < edges.size(); i++) {
            //adding the refuelling time of the FROM node
            cost += edges.get(i).getFrom().getRefuellingTime();
            //now we want to add the weight aka time taken between the two nodes
            cost += g.getWeightOfEdge(edges.get(i).getFrom(), edges.get(i).getTo());

        }
        return cost;
    }

    /**
     * Gets nodes expanded.
     *
     * @return the nodes expanded
     */
    public int getNodesExpanded() {
        return nodesExpanded;
    }

}
