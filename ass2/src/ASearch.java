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
     *
     * @param schedule the schedule which will contain all the required shipments
     */
    public void setSchedule(LinkedList<DirectedEdge> schedule) {
        this.schedule = schedule;
    }

    /**
     * Sets the graph for the class which will be used for the A* search.
     * <br/> this class will accept any graph type input
     *
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
     *
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
                if (newPath.getChildren().contains(d))
                    continue;
                if (ships.size() == 0) {
                    if (d.getFrom().equals(newPath.getShipment().getTo()))
                        newPath.addToChildren(new DirectedEdge(curr.getShipment().getTo(), d.getTo()));
                    else
                        newPath.addToChildren(new DirectedEdge(curr.getShipment().getTo(), d.getFrom()));
                } else
                    newPath.addToChildren(d);
                if (newPath.getChildren().getLast().getFrom().equals(newPath.getChildren().getLast().getTo()))
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
     *
     * @param path the path we are checking for a search node
     * @return true if the goal state is reached/otherwise false
     */
    public boolean isCompletedSchedule(searchNode path) {
        LinkedList<DirectedEdge> scheduleCopy = new LinkedList<DirectedEdge>();
        for (int i = 0; i < schedule.size(); i++)
            scheduleCopy.add(schedule.get(i));
        LinkedList<DirectedEdge> d = new LinkedList<DirectedEdge>();
        if (path.getChildren().size() > 0) {
            for (int i = 0; i < path.getChildren().size() - 1; i++) {
                d.add(new DirectedEdge(path.getChildren().get(i).getFrom(), path.getChildren().get(i).getTo()));
                d.add(new DirectedEdge(path.getChildren().get(i).getTo(), path.getChildren().get(i + 1).getFrom()));
            }
            d.add(new DirectedEdge((path.getChildren().getLast().getFrom()), path.getChildren().getLast().getTo()));
        }
        if (path.getChildren().size() == 1) {
            d.add(new DirectedEdge(path.getChildren().get(0).getFrom(), path.getChildren().get(0).getTo()));
        }
        if (path.getChildren().size() == 0) {
            d.add(new DirectedEdge(path.getShipment().getFrom(), path.getShipment().getTo()));
        }
        for (int i = 0; i < path.getChildren().size(); i++) {
            if (d.contains(path.getChildren().get(i)))
                scheduleCopy.remove(path.getChildren().get(i));
        }
        return d.containsAll(schedule);
    }


    /**
     * this method will create a path from a search node which is a linked list of directed edges that can
     * be connected together in order to form a path, this will be called when a path has met the goal state
     * <br/> this method will guarantee to return the path provided it has received a valid search node
     *
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
     * returns the cost  of the path cost from the start to the last node in the path
     * <br/> this method will guarantee to return an integer which is the absolute distance
     * from the start to the current place, or 0 if no path
     *
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
     * will add the distance + time to the closest port on schedule to the last node in the path, this allows optimisation for the heuristic
     * whilst still remaining admissable. the heuristic is admissable as the heuristic will never allow over-estimation for a shipment
     * the heuristic adds the remaining shipment times left over with the current path, and this in alone will always admissable
     * as the cost will always less be than the shortest cost path
     * another trick which allows for greater optimisation is, picking the closest shipment to the node with a higher value
     * nodes which have further neighbouring shipments will be adding a larger sum, whereas a node which is in itself its own neighbour
     * aka you are on sydney and you have for example sydney -> (SOME RANDOM PORT), then the minimum distance to closest shipment is 0
     * since sydney itself is on a shipment. this will retain the admissable feature, as it cannot over-estimate the cost, when the path is
     * on the shipment, we can't add more than the cost of the shortest path like this
     * in every situation for this heuristic h(x) < H*(x)
     * <br /> this method is implemented from the heuristic interface which utilises the stratergy pattern for a generic
     * scoring system, it expects a path or for a general case an object, and will guarantee to return a non negative integer
     * representive of that object/path's estimated time/distance to the goal state.
     *
     * @param d the search node which contains the current path we are searching
     * @return a estimate of how close the path is to reaching goal state, this will be a non negative integer based on
     * the current path for the search node.
     */
    @Override
    public int getShipmentScore(searchNode d) {
        int sum = getScheduleScoreRemaining(d);
        int k = getScheduleScoreRemaining(d);

        int minShipmentRemaining = minShipmentLeft(d);
        //now i want to do same for schedule remaining
        sum += minShipmentRemaining;
        return sum;
    }


    /**
     * This method will return the G_SCORE of the current path, aka the time taken to reach the last node in the path
     * from the start port Sydney in this case. this method expects a search node which contains a "root" and a path from that root
     * <br /> this method expects a search node (or path) to calculate the gscore for and will guarantee to return a non negative integer
     *
     * @param s the searchNode (path) we are scoring
     * @return the cost of the path from the start to the last node in it's state
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
     * This method will check if a searchNode is on the closed set of our A* search, it will return every searchNode
     * which have the same STATE as the current search node, this is very important we say same state, as two search node
     * with the same shipments but in a jumbled order, are still in the same state since they both have the same shipments
     * however they will have different path costs, take for example <br/>
     * Sydney->(Vancouver->Manila, shanghai->Singapore) and <br />
     * Sydney->(Shanghai->Singapore, Vancouver->Manila) <br/>
     * these different paths are in the same state as they contain the same shipments HOWEVER they do not have the same score.
     * <br /> this method expects a valid path for its search node, and a list of search nodes on closed, and will guarantee
     * to return a list (could be empty list too) containing all nodes which share the same state as our valid path that are on closed.
     *
     * @param p      the search node aka the path we are checking
     * @param closed the closed set which contains all other searchnodes (paths)
     * @return An arraylist containing all the nodes which share the same state as our searchNode (path) P that are in the closed set
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
     * This method will check if a given searchNode, aka path is on the open set. this checks if the path has the same state
     * as other search nodes (paths) on the open set. This method will then return a list containing all paths that share the same state
     * as the path we are checking. Similair to onClosed, we are checking if the paths have the same state as this means they have the same
     * shipments, in a different order but vastly different costs for that path. take for example <br/>
     * Sydney->(Vancouver->Manila, shanghai->Singapore) and <br />
     * Sydney->(Shanghai->Singapore, Vancouver->Manila) <br/>
     * these two path have the same state as they have the same shipments in a different order, however their costs vary depending
     * on the edge weights and refuelling times.
     * <br /> his method expects a valid path for its search node, and a list of search nodes on open, and will guarantee
     * to return a list (could be empty list too) containing all nodes which share the same state as our valid path that are on open.
     *
     * @param p    the search node aka the path we are checking
     * @param open the open set which contains all other searchnodes (paths)
     * @return An arraylist containing all the nodes which share the same state as our searchNode (path) P that are in the open set
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
     * This method will return the amount of time remaining to complete the direct schedule path remaining. it will first
     * check which shipments have not been made, it will calculate the cost of those remaining shipments (sum them together).
     * <br /> this method will expect a path of any kind, and will guarantee to return a non negative integer of how much time is required
     * to complete the schedule in the current state
     *
     * @param s the path we are calculating remaining time needed to reach goal state
     * @return the time remaining to finish the schedule directly (not accounting for linking)
     */
    public int getScheduleScoreRemaining(searchNode s) {
        int sum = 0;
        LinkedList<DirectedEdge> d = new LinkedList<DirectedEdge>();
        if (s.getChildren().size() > 0) {
            for (int i = 0; i < s.getChildren().size() - 1; i++) {
                d.add(new DirectedEdge(s.getChildren().get(i).getFrom(), s.getChildren().get(i).getTo()));
                d.add(new DirectedEdge(s.getChildren().get(i).getTo(), s.getChildren().get(i + 1).getFrom()));
            }
            d.add(new DirectedEdge((s.getChildren().getLast().getFrom()), s.getChildren().getLast().getTo()));
        }
        if (s.getChildren().size() == 1) {
            d.add(new DirectedEdge(s.getChildren().get(0).getFrom(), s.getChildren().get(0).getTo()));
        }
        for (int i = 0; i < schedule.size(); i++) {
            if (d.contains(schedule.get(i)))
                continue;
            sum += schedule.get(i).getFrom().getRefuellingTime();
            sum += g.getEdges()[schedule.get(i).getFrom().getIndexOnGraph()][schedule.get(i).getTo().getIndexOnGraph()];
        }

        return sum;
    }


    /**
     * This method will return the closest shipment to the last node in our path if one exists. for example <br />
     * if our schedule was <br/>
     * Sydney -> vancouver <br/>
     * Sydney -> Manila <br/>
     * Shanghai -> Sydney <br/>
     * and the last node in our path was sydney, since the schedule remaining has sydney -> vancouver/manila, the closest
     * node on schedule is itself, so it would return 0, it will return the CLOSEST DISTANCE TO A NODE ON SCHEDULE.
     * This method expects a valid path and will guarantee to return a non negative integer representing the closest distance to a node on path if one exists
     * @param s the path
     * @return the closest distance to another node that is on shipment, or 0 otherwise
     */
    public int minShipmentLeft(searchNode s) {
        int temp1 = Integer.MAX_VALUE;
        Node last = null;
        if (s.getChildren().size() == 0)
            last = s.getShipment().getTo();
        else
            last = s.getChildren().getLast().getTo();
        if (null == last)
            return 0;
        boolean flag = false;
        for (int i = 0; i < schedule.size(); i++) {
            int temp = g.getEdges()[schedule.get(i).getFrom().getIndexOnGraph()][last.getIndexOnGraph()];
            if (!schedule.get(i).getFrom().equals(last))
                temp += schedule.get(i).getFrom().getRefuellingTime();
            if (!s.getChildren().contains(schedule.get(i)) && temp < temp1) {
                temp1 = temp;
                flag = true;
            }

        }

        if (flag == false)
            temp1 = 0;
        return temp1;
    }

}


