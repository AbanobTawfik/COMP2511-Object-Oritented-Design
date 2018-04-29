import java.util.*;

/**
 * A* search class which will be implementing a heuristic search as required by the stratergy pattern.
 * This search will take in a graph, and a list of shipments required on a schedule, and will return the optimal path
 * in the most efficient time possible based on the heuristic provided the heuristic is admissible, as an admissible heuristic
 * will increase performance while mainting optimal path
 */
public class ASearch implements Heuristic<searchNode> {
    private int nodesExpanded;
    private LinkedList<DirectedEdge> schedule;
    private GraphOfPorts g;

    /**
     * this method will set the schedule, it is going to be used to compare the path to the goal state
     * <br/> this method will accept any input
     * @param schedule the schedule which will contain all the required shipments
     */
    public void setSchedule(LinkedList<DirectedEdge> schedule) {
        this.schedule = schedule;
    }

    /**
     * Sets the graph for the class which will be used for the A* search.
     * <br/> this class will accept any graph type input
     * @param g the graph which is being searched
     */
    public void setG(GraphOfPorts g) {
        this.g = g;
    }


    /**
     * This method is the A* search function itself, it has no input as the case for this search is that it
     * ALWAYS starts at sydney and the goal state is based on the schedule. the method guarantees to return the optimal path
     * from sydney that completes the schedule in the optimal amount of time, whilst utilising an admissable heuristic in
     * order to make the search efficient
     * @return a linked list of directed edges which is a series of shipments that can be linked into a path
     */
    public LinkedList<DirectedEdge> Search() {
        if (g.getnV() <= 1)
            return null;
        if (schedule.size() < 1)
            return null;


        LinkedList<searchNode> closed = new LinkedList<searchNode>();
        Comparator<searchNode> comparator = new NodeComparator();
        PriorityQueue<searchNode> open = new PriorityQueue<searchNode>(g.getnV(), comparator);
        Node initial = g.getNodeByString("Sydney");
        DirectedEdge d1 = new DirectedEdge(initial, initial);
        searchNode initialSearchNode = new searchNode(d1);
        initialSearchNode.setGScore(0);
        initialSearchNode.setHScore(getShipmentScore(initialSearchNode));
        initialSearchNode.setFScore();
        if (initial.equals(null))
            return null;
        open.add(initialSearchNode);
        while (!open.isEmpty()) {
            searchNode curr = open.poll();
            nodesExpanded++;

            closed.add(curr);
            if (isCompletedSchedule(curr)) {
                return createPath(curr);
            }
            for (DirectedEdge d : schedule) {

                searchNode newPath = new searchNode(curr.getShipment());
                LinkedList<DirectedEdge> ships = new LinkedList<DirectedEdge>(curr.getChildren());
                newPath.setChildren(ships);
                if(newPath.getChildren().contains(d))
                    continue;
                if (ships.size() == 0) {
                    if(d.getFrom().equals(newPath.getShipment().getTo()))
                        newPath.addToChildren(new DirectedEdge(curr.getShipment().getTo(), d.getTo()));
                    else
                        newPath.addToChildren(new DirectedEdge(curr.getShipment().getTo(), d.getFrom()));
                }
                else
                    newPath.addToChildren(d);
                if(newPath.getChildren().getLast().getFrom().equals(newPath.getChildren().getLast().getTo()))
                    continue;
                newPath.setGScore(getWeightPath(newPath));
                newPath.setHScore(getShipmentScore(newPath));
                newPath.setFScore();
                ArrayList<Integer> onClosed = onClosed(newPath, closed);
                ArrayList<Integer> onOpen = onOpen(newPath, open);
                if (!onClosed.isEmpty()) {
                    for (int i = 0; i < onClosed.size(); i++) {
                        int FScoreClosed = closed.get(onClosed.get(i)).getFScore();
                        int FScoreNewPath = newPath.getFScore();
                        if (FScoreClosed > FScoreNewPath) {
                            closed.remove(onClosed.get(i));
                            open.add(newPath);
                        }
                    }
                } else if (!onOpen.isEmpty()) {

                    PriorityQueue<searchNode> copy = new PriorityQueue<searchNode>(open);
                    ArrayList<searchNode> openCopy = new ArrayList<searchNode>();
                    for (int i = 0; i < copy.size(); i++) {
                        openCopy.add(copy.poll());
                    }
                    for (int i = 0; i < onOpen.size(); i++) {
                        searchNode openNode = openCopy.get(onOpen.get(i));
                        int FScoreOpen = openNode.getFScore();
                        int FscoreNewPath = newPath.getFScore();
                        if (FScoreOpen > FscoreNewPath) {
                            open.remove(openNode);
                            open.add(newPath);
                        }
                    }
                } else {
                    open.add(newPath);
                }
            }
        }

        return null;

    }


    /**
     * this method will check a search node with the goal state, which is that it has completed the shcedule
     * <br/> this method will expect a searchNode of any type and will guarantee to return true/false
     * @param path the path we are checking for a search node
     * @return true if the goal state is reached/otherwise false
     */
    public boolean isCompletedSchedule(searchNode path) {
        LinkedList<DirectedEdge> scheduleCopy = new LinkedList<DirectedEdge>();
        for (int i = 0; i < schedule.size(); i++)
            scheduleCopy.add(schedule.get(i));
        for (int i = 0; i < path.getChildren().size(); i++) {
            if (scheduleCopy.contains(path.getChildren().get(i)))
                scheduleCopy.remove(path.getChildren().get(i));
        }
        return scheduleCopy.isEmpty();
    }


    /**
     * this method will create a path from a search node which is a linked list of directed edges that can
     * be connected together in order to form a path, this will be called when a path has met the goal state
     * <br/> this method will guarantee to return the path provided it has received a valid search node
     * @param route the searchNode which contains the path that is most optimal
     * @return the linked list of directed edges which is a representation of the path used in the print function
     */
    public LinkedList<DirectedEdge> createPath(searchNode route) {
        LinkedList<DirectedEdge> path = new LinkedList<DirectedEdge>();
        for (int i = 0; i < route.getChildren().size(); i++) {
            path.add(route.getChildren().get(i));
        }
        return path;
    }


    /**
     * returns the Gscore aka the path cost from the start to the last node in the path
     * <br/> this method will guarantee to return an integer which is the absolute distance
     * from the start to the current place, or 0 if no path
     * @param edges the current path that is having its cost calculated
     * @param g     the graph in order to workout the weight of each connection
     * @return the cost required to get to the current position from the start Port "Sydney"
     */
    public int getCost(LinkedList<DirectedEdge> edges, GraphOfPorts g) {
        if (null == edges)
            return 0;
        ArrayList<Node> path = new ArrayList<Node>();
        for (int i = 0; i < edges.size(); i++) {
            path.add(edges.get(i).getFrom());
            path.add(edges.get(i).getTo());
        }
        int sum = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            if (!path.get(i).equals(path.get(i + 1))) {
                sum += path.get(i).getRefuellingTime();
                sum += g.getEdges()[path.get(i).getIndexOnGraph()][path.get(i + 1).getIndexOnGraph()];
            }
        }
        return sum;
    }


    /**
     * this method will return the number of paths expanded in the search. which is the number of iterations
     * required to complete the search
     *
     * @return the number of paths investigated for the search
     */
    public int getNodesExpanded() {
        return nodesExpanded;
    }

    /**
     * this is the heuristic used in the search, it is an admissable heuristic as what it does is first, check the
     * path for which shipments it has completed on schedule, then it will add the remaining schedule to initalise the heuristic score
     * . when path is complete score = 0 for heuristic. the heuristic in order to find which path is better to follow
     * will add the smallest remaining shipment remaining on schedule to the score. for example if the shipments remaining has
     * scores [15,28,32] it will add 15. a path which has larger shipments remaining will be a worse path overall.
     *
     * The heuristic is admissible in all cases as the cost estimated + the score
     * @param d
     * @return
     */
    @Override
    public int getShipmentScore(searchNode d) {
        int sum = getScheduleScoreRemaining(d);
        int k = getScheduleScoreRemaining(d);

        int minShipmentRemaining = minShipmentLeft(d);
        //now i want to do same for schedule remaining
        //sum -= minShipment ;
        sum += minShipmentRemaining;
        return sum;
    }


    /**
     * Gets weight path.
     *
     * @param s the s
     * @return the weight path
     */
    public int getWeightPath(searchNode s) {
        ArrayList<Node> path = new ArrayList<Node>();
        path.add(s.getShipment().getFrom());
        path.add(s.getShipment().getTo());
        for (int i = 0; i < s.getChildren().size(); i++) {
            path.add(s.getChildren().get(i).getFrom());
            path.add(s.getChildren().get(i).getTo());
        }

        int sum = 0;

        for (int i = 0; i < path.size() - 1; i++) {
            if (!path.get(i).equals(path.get(i + 1))) {
                sum += path.get(i).getRefuellingTime();
                sum += g.getEdges()[path.get(i).getIndexOnGraph()][path.get(i + 1).getIndexOnGraph()];
            }
        }
        return sum;
    }


    /**
     * On closed array list.
     *
     * @param p      the p
     * @param closed the closed
     * @return the array list
     */
    public ArrayList<Integer> onClosed(searchNode p, LinkedList<searchNode> closed) {
        ArrayList<Integer> ret = new ArrayList<Integer>();
        for (int i = 0; i < closed.size(); i++) {
            if (p.getChildren().containsAll(closed.get(i).getChildren())
                    && closed.get(i).getChildren().containsAll(p.getChildren()))
                ret.add(i);
        }
        return ret;
    }

    /**
     * On open array list.
     *
     * @param p    the p
     * @param open the open
     * @return the array list
     */
    public ArrayList<Integer> onOpen(searchNode p, PriorityQueue<searchNode> open) {
        PriorityQueue<searchNode> copy = new PriorityQueue<searchNode>(open);
        ArrayList<searchNode> openCopy = new ArrayList<searchNode>();
        for (int i = 0; i < copy.size(); i++) {
            searchNode curr = copy.poll();
            openCopy.add(curr);
        }
        ArrayList<Integer> ret = new ArrayList<Integer>();
        for (int i = 0; i < openCopy.size(); i++) {
            if (p.getChildren().containsAll(openCopy.get(i).getChildren())
                    && openCopy.get(i).getChildren().containsAll(p.getChildren()))
                ret.add(i);
            ;
        }
        return ret;
    }

    /**
     * Gets schedule score remaining.
     *
     * @param s the s
     * @return the schedule score remaining
     */
    public int getScheduleScoreRemaining(searchNode s) {
        int sum = 0;
        for (int i = 0; i < schedule.size(); i++) {
            if (s.getChildren().contains(schedule.get(i)))
                continue;
            sum += schedule.get(i).getFrom().getRefuellingTime();
            sum += g.getEdges()[schedule.get(i).getFrom().getIndexOnGraph()][schedule.get(i).getTo().getIndexOnGraph()];
        }
        return sum;
    }

    /**
     * Min shipment left int.
     *
     * @param s the s
     * @return the int
     */
    public int minShipmentLeft(searchNode s) {
        int sum = Integer.MAX_VALUE;
        boolean flag = false;

        for (int i = 0; i < schedule.size(); i++) {
            int temp = g.getEdges()[schedule.get(i).getFrom().getIndexOnGraph()][schedule.get(i).getTo().getIndexOnGraph()];
            temp += schedule.get(i).getFrom().getRefuellingTime();
            if (s.getChildren().contains(schedule.get(i)) && temp < sum) {
                sum = temp;
                flag = true;
            }

        }
        if (flag == false)
            sum = 0;
        return sum;
    }

}

    /*public int minShipmentLeft(searchNode s) {
        int sum = Integer.MAX_VALUE;
        boolean flag = false;
        int temp1 = s.getChildren().getLast().getTo().getRefuellingTime();
        int temp1 = g.getEdges()[schedule.getFirst().getFrom().getIndexOnGraph()][sc]
        /*for (int i = 0; i < schedule.size(); i++) {
            int temp = g.getEdges()[schedule.get(i).getFrom().getIndexOnGraph()][schedule.get(i).getTo().getIndexOnGraph()];
            temp += schedule.get(i).getFrom().getRefuellingTime();
            if (s.getChildren().contains(schedule.get(i)) && temp < sum) {
                sum = temp;
                flag = true;
            }

        }
        for (int i = 0; i < schedule.size(); i++) {
            int temp = g.getEdges()[schedule.get(i).getFrom().getIndexOnGraph()][schedule.get(i).getTo().getIndexOnGraph()];
            temp += schedule.get(i).getFrom().getRefuellingTime();
            if (!s.getChildren().contains(schedule.get(i)) && temp < sum) {
                sum = temp;
                flag = true;
            }

        }
        if (flag == false)
            sum = 0;
        return sum;
    }*/


