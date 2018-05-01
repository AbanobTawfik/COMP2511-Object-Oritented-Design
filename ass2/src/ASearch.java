import java.util.*;

/**
 * A* search class which will be implementing a heuristic search as required by the stratergy pattern.
 * This search will take in a graph, and a list of shipments required on a schedule, and will return the optimal path
 * in the most efficient time possible based on the heuristic provided the heuristic is admissible, as an admissible heuristic
 * will increase performance while mainting optimal path
 */
public class ASearch implements Heuristic<searchNode> {
    //the number of paths searched in the final search
    private int nodesExpanded;
    //the schedule which is representative of the goal state, when a path has
    //completed all shipments on the schedule, the goal state is reached
    private LinkedList<DirectedEdge> schedule;
    //the graph which is being used to conduct the search
    private GraphOfPorts g;

    /**
     * this method will set the schedule, it is going to be used to compare the path to the goal state
     * <br/> this method will accept any input
     *
     * @param schedule the schedule which will contain all the required shipments
     */
    public void setSchedule(LinkedList<DirectedEdge> schedule) {
        //this sets the schedule for the A*Search class
        this.schedule = schedule;
    }

    /**
     * Sets the graph for the class which will be used for the A* search.
     * <br/> this class will accept any graph type input
     *
     * @param g the graph which is being searched
     */
    public void setG(GraphOfPorts g) {
        //this sets the graph for the A*Search class
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
        //if there are no vertices on the graph or there is only 1
        //(no shipments can be made from same port to same port) return nothing
        if (g.getnV() <= 1)
            return null;
        //if there are no shipments in the schedule return nothing
        if (schedule.size() < 1)
            return null;

        //initalise the closed list for the search
        LinkedList<searchNode> closed = new LinkedList<searchNode>();
        //this will be the comparator for the priority queue, it will compare
        //searchNodes based on the Fscore (final path score)
        Comparator<searchNode> comparator = new NodeComparator();
        //initalising the open list (priority queue) for the search
        PriorityQueue<searchNode> open = new PriorityQueue<searchNode>(comparator);
        //retrieving the first node which we start our search with, Sydney node
        Node initial = g.getNodeByString("Sydney");
        //if sydney is not on the graph then we want to return null since it wont be able to start
        if (null == initial)
            return null;
        //now we are initalising our search node root by bootstrapping sydney
        //since the searchNode works with shipments, and sydney is just a node
        //we are creating a bootstrapped directed edge from sydney to sydney which has
        //weight of 0 overall in the path (initalising)
        DirectedEdge d1 = new DirectedEdge(initial, initial);
        //initalising new searchNode with the root shipment, sydney to sydney (bootstrapped)
        searchNode initialSearchNode = new searchNode(d1);
        //since the path is at the start it's gscore is 0
        initialSearchNode.setGScore(0);
        //calculating the heuristic score for the current path (sydney-sydney == no path)
        initialSearchNode.setHScore(getShipmentScore(initialSearchNode));
        //setting the fscore which should be just the heuristic score since gscore = 0;
        initialSearchNode.setFScore();
        //add this search Node to the open list to initalise the search
        open.add(initialSearchNode);
        while (!open.isEmpty()) {
            //remove the path from the head of the open list which will be the path
            //with the lowest FScore as the priority queue is ordering nodes based on lowest FScore
            searchNode curr = open.poll();
            //since we are expanding a path, we want to increment the amount of paths expanded
            nodesExpanded++;
            //add this node to the closed set (a path which had been expanded)
            closed.add(curr);
            //this is the goal state we are checking our node for
            /*
             * the goal state is reached when all shipments in the current path constructed
             * contain all the shipments in the schedule of shipments. the constructed path can be
             * connected by joining the shipments, and compared to schedule, if the schedule is a subset
             * of the path, then the path has reached goal state -> return the constructed path
             */
            if (isCompletedSchedule(curr)) {
                return createPath(curr);
            }
            /*
             * now we want to add all shipments from the schedule onto the current path
             * creating a new path which can be added to the open or closed set.
             */
            for (DirectedEdge d : schedule) {
                //creating a new path from the old searchNode by appending
                //a shipment from schedule onto the end
                searchNode newPath = new searchNode(curr.getShipment());
                //now we add all the shipments from the previous path onto the new path as
                //the new path is an extension of the previous path
                LinkedList<DirectedEdge> ships = new LinkedList<DirectedEdge>(curr.getChildren());
                newPath.setChildren(ships);
                //if there are no shipments to append, we want to create the first one by
                //connecting sydney the first shipment start, to the scheduled shipment
                if (ships.size() == 0) {
                    //if the new shipment to add will have the same from node eg.
                    //adding sydney -> vancouver, onto the initial sydney->sydney shipment
                    if (d.getFrom().equals(newPath.getShipment().getTo()))
                        //we want to create the next shipment from sydney->vancouver
                        //attatching the last node in the initial shipment, to the last node in the scheduled shipment
                        newPath.addToChildren(new DirectedEdge(curr.getShipment().getTo(), d.getTo()));
                    else
                        //otherwise in example adding Shanghai->Singapore, onto the initial sydney->sydney shipment
                        //we want to create the new shipment, sydney->shanghai by attatching the last node from the shipment
                        //to the first node in the scheduled shipment
                        newPath.addToChildren(new DirectedEdge(curr.getShipment().getTo(), d.getFrom()));
                } else
                    //otherwise we just want to append the shipment onto the current path
                    //since we don't require sydney -> X to initalise our search
                    newPath.addToChildren(d);
                //if the last shipment in the path, has the same to and from node, we want to skip
                //dont want a bunch of shanghai -> shanghai nodes etc.
                if (newPath.getChildren().getLast().getFrom().equals(newPath.getChildren().getLast().getTo()))
                    continue;
                //now we want to set the GScore of the path, as the weight of the path from the start
                newPath.setGScore(getWeightPath(newPath));
                //we want to set the heuristic score of the path, as the estimated time remaining
                //to reach the goal state, calculated by the heuristic function
                newPath.setHScore(getShipmentScore(newPath));
                //this function will add the heuristic score to the GScore to set the FScore of the path
                newPath.setFScore();

                //Now we want to check if the path is both on open/closed
                //the arraylist will contain ALL paths that have the same state on open and on closed
                //if there are no paths with the same state then the lists will be empty
                ArrayList<Integer> onClosed = onClosed(newPath, closed);
                ArrayList<Integer> onOpen = onOpen(newPath, open);
                //if the path is on closed
                if (!onClosed.isEmpty()) {
                    //we want to compare all the paths on closed with the new paths
                    for (int i = 0; i < onClosed.size(); i++) {
                        //now we want to get the fscore for the path that was on closed
                        int FScoreClosed = closed.get(onClosed.get(i)).getFScore();
                        //we want to also get the FScore for the new path
                        int FScoreNewPath = newPath.getFScore();
                        //if the new path has a lower Fscore we want to remove the old path from closed,
                        //and put the new path on open
                        if (FScoreClosed > FScoreNewPath) {
                            closed.remove(onClosed.get(i));
                            open.add(newPath);
                        }
                    }
                }
                //if the path is on open
                else if (!onOpen.isEmpty()) {
                    //we first want to make a scanable arraylist, in order to retrieve the nodes
                    //to potentially remove from the priority queue open.
                    //make a disposable copy of open which is a deep copy
                    PriorityQueue<searchNode> copy = new PriorityQueue<searchNode>(open);
                    //create a copy of open as an arraylist by polling everything off
                    //the copied priority queue and storing them on the arraylist
                    ArrayList<searchNode> openCopy = new ArrayList<searchNode>();
                    for (int i = 0; i < copy.size(); i++) {
                        openCopy.add(copy.poll());
                    }
                    //now we want to compare all paths on open which have the same state as our new path
                    for (int i = 0; i < onOpen.size(); i++) {
                        //we want to retrieve the path from the open set
                        searchNode openNode = openCopy.get(onOpen.get(i));
                        //we want to get the score of the path that is on open
                        int FScoreOpen = openNode.getFScore();
                        //we also want to get the score of the new path
                        int FscoreNewPath = newPath.getFScore();
                        //if the new path has a lower score than the old path's score
                        //we want to remove the old path from open and replace it
                        //with the new path on open
                        if (FScoreOpen > FscoreNewPath) {
                            open.remove(openNode);
                            open.add(newPath);
                        }
                    }
                }
                //otherwise if the path shares no state with any of the paths on  either the open or closed set
                //we want to add the path onto open
                else {
                    open.add(newPath);
                }
            }
        }
        //otherwise if the search cannot find a path return null
        return null;

    }


    /**
     * this method will check a search node with the goal state, which is that it has completed the shcedule
     * <br/> this method will expect a searchNode of any type and will guarantee to return true/false
     *
     * @param path the path we are checking for a search node
     * @return true if the goal state is reached/otherwise false
     */
    /*
     * we want to compare the current path and check if all the shipments
     * in the current path contains all the shipments on the schedule.
     * we want to return true else we return false
     */
    public boolean isCompletedSchedule(searchNode path) {
        //this will be used to construct our parth
        LinkedList<DirectedEdge> d = new LinkedList<DirectedEdge>();
        //if there are more than one shipments on the path we want to
        //add them all by connecting the path
        if (path.getChildren().size() > 1) {
            //scan through all the shipments in the path (children) connecting all shipments
            for (int i = 0; i < path.getChildren().size() - 1; i++) {
                //add the current shipment into the path
                d.add(new DirectedEdge(path.getChildren().get(i).getFrom(), path.getChildren().get(i).getTo()));
                //now we want to add the last node from the previous shipment to the first node in the next shipment
                //in order to connect the path from its previous point and keep the path consistent
                d.add(new DirectedEdge(path.getChildren().get(i).getTo(), path.getChildren().get(i + 1).getFrom()));
            }
            //since the above loop only goes to the second last node (to avoid null pointer errors)
            //add the last shipment in the path
            d.add(new DirectedEdge((path.getChildren().getLast().getFrom()), path.getChildren().getLast().getTo()));
        }
        //if there is only one shipment on the path, to avoid null pointer errors in the above
        //we only add that shipment to the constructed path
        if (path.getChildren().size() == 1) {
            d.add(new DirectedEdge(path.getChildren().get(0).getFrom(), path.getChildren().get(0).getTo()));
        }
        //if the path has no children aka no shipments from the root, we only want to add the root shipment
        //(bootstrapped Sydney->Sydney)
        if (path.getChildren().size() == 0) {
            d.add(new DirectedEdge(path.getShipment().getFrom(), path.getShipment().getTo()));
        }
        //now we want to return that the path we constructed contains all the shipments on the schedule
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
        //in this function we want to add all the shipments in the path in our return
        //our print function will print out the connected nodes

        //initalise the new path
        LinkedList<DirectedEdge> path = new LinkedList<DirectedEdge>();
        //add all the children shipments (the path itself) to the path
        for (int i = 0; i < route.getChildren().size(); i++) {
            path.add(route.getChildren().get(i));
        }
        //return this path
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
    /*
     * this function will return the cost of the path from start to end (when it finds a succesful path)
     */
    public int getCost(LinkedList<DirectedEdge> edges, GraphOfPorts g) {
        //if there are no edges (shipments) in the path
        //we return 0
        if (null == edges)
            return 0;
        //otherwise we want to make an arraylist of nodes in order to find the cost of the path
        ArrayList<Node> path = new ArrayList<Node>();
        //we want to add all the shipments in as nodes into our path of nodes
        for (int i = 0; i < edges.size(); i++) {
            path.add(edges.get(i).getFrom());
            path.add(edges.get(i).getTo());
        }
        int sum = 0;
        //now we want to sum all these shipments connected
        for (int i = 0; i < path.size() - 1; i++) {
            //if the path is not a duplicate of itself eg. sydney -> sydney (skip those)
            if (!path.get(i).equals(path.get(i + 1))) {
                //add the refuelling time of the first node
                sum += path.get(i).getRefuellingTime();
                //add the edge weight of the first node to the node after (linking them together)
                sum += g.getEdges()[path.get(i).getIndexOnGraph()][path.get(i + 1).getIndexOnGraph()];
            }
        }
        //return the summed up cost of the path (very similair to gcost)
        return sum;
    }


    /**
     * this method will return the number of paths expanded in the search. which is the number of iterations
     * required to complete the search
     *
     * @return the number of paths investigated for the search
     */
    public int getNodesExpanded() {
        //returns the number of nodes expanded in the search
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
    /*
     * this method will implement the generic heuristic interface, this utilises the stratergy pattern to find
     * a heuristic score for generic type depending on how it is being implemented
     */
    @Override
    public int getShipmentScore(searchNode d) {
        //the first part of this heuristic will add the weights of the remaining shipments left over
        int sum = getScheduleScoreRemaining(d);
        //now we add the distance from the current node to the closest node on schedule
        int minShipmentRemaining = minShipmentLeft(d);
        //now i want to add the distance to the closest shipment if one exists
        sum += minShipmentRemaining;
        //return the sum of the schedule remaining + closest shipment distance
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
    /*
     * this function will return the GScore of the current path
     */
    public int getWeightPath(searchNode s) {
        //we want to construct the path similair to the code above, and workout the current weight of the path
        //in it's current state. which will return the GScore
        ArrayList<Node> path = new ArrayList<Node>();
        //adding the path of the root from the searchNode,
        path.add(s.getShipment().getFrom());
        path.add(s.getShipment().getTo());
        //now we want to add all the nodes in the shipments in the path
        for (int i = 0; i < s.getChildren().size(); i++) {
            path.add(s.getChildren().get(i).getFrom());
            path.add(s.getChildren().get(i).getTo());
        }

        int sum = 0;
        //now we want to sum up all the shipments connected
        for (int i = 0; i < path.size() - 1; i++) {
            //if the first node and next node are identical i.e sydney->sydney skip
            if (!path.get(i).equals(path.get(i + 1))) {
                //otherwise add the current nodes refuelling time
                sum += path.get(i).getRefuellingTime();
                //and add the edge weight from the current node to next node (linking the path)
                sum += g.getEdges()[path.get(i).getIndexOnGraph()][path.get(i + 1).getIndexOnGraph()];
            }
        }
        //return the GScore
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
    /*
     * this function will return all the paths that are on closed with the same state as our searchnode P
     */
    public ArrayList<Integer> onClosed(searchNode p, LinkedList<searchNode> closed) {
        //initalise a arraylist of integers to store the INDEX's of the nodes
        //where the path is located on closed
        ArrayList<Integer> ret = new ArrayList<Integer>();
        //scan through the entire closed list
        for (int i = 0; i < closed.size(); i++) {
            //if the path on closed contains all the shipments in the path p, and if the
            //path p contains all shipments on the closed path, return 1 since it means
            //they have the same state
            if (p.getChildren().containsAll(closed.get(i).getChildren())
                    && closed.get(i).getChildren().containsAll(p.getChildren()))
                //add the nodes index in the path
                ret.add(i);
        }
        //return an arraylist of integers, that contain the index of the searchnodes with the same state
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
    /*
     * this function will return all the paths that are on open with the same state as our searchnode P
     */
    public ArrayList<Integer> onOpen(searchNode p, PriorityQueue<searchNode> open) {
        //since we cannot acces priority queue member's index we need to first store all the elements from our
        //open priority queue into an arrayList, and then access the elements from there
        //we want to make a deep copy of open so we can poll all the paths from the copy into the list
        PriorityQueue<searchNode> copy = new PriorityQueue<searchNode>(open);
        ArrayList<searchNode> openCopy = new ArrayList<searchNode>();
        for (int i = 0; i < copy.size(); i++) {
            //poll the node off the priority queue, and add it to the list
            searchNode curr = copy.poll();
            openCopy.add(curr);
        }
        //nowe we want to initalise a return array which contains all the index's of the paths location
        //on the open set that have the exact same state as our path p
        ArrayList<Integer> ret = new ArrayList<Integer>();
        for (int i = 0; i < openCopy.size(); i++) {
            //if the path p contains all the same shipments as the path on index i, and the same other way around
            //it means they both have the same state
            if (p.getChildren().containsAll(openCopy.get(i).getChildren())
                    && openCopy.get(i).getChildren().containsAll(p.getChildren()))
                //add the index of the path's location on open into our return array.
                ret.add(i);

        }
        //return our array of integers containing the index where the paths with the same state as path p
        //on the open set
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
    /*
     * this method will return the smallest distance to the closest shipment on schedule.
     * if the shipment itself is on schedule, then it will return 0, this will help optimise the heuristic
     */
    public int minShipmentLeft(searchNode s) {
        //initalise the return as max in order to compare for maximum (will be set to 0 if no result)
        int temp1 = Integer.MAX_VALUE;
        Node last = null;
        //we want the last node to check the closest shipment to that node
        if (s.getChildren().size() == 0)
            //if there are no shipments in the path, use the last node in the root (shipment of the searchnode)
            last = s.getShipment().getTo();
        else
            //otherwise take the very last node in the path
            last = s.getChildren().getLast().getTo();
        //if for some reason the last node is null (no last node) return 0
        if (null == last)
            return 0;
        //initalise a flag as false whichw ill check if there is a shipment nearby on schedule
        boolean flag = false;
        for (int i = 0; i < schedule.size(); i++) {
            //get the edgeweight of the lasst node to the current scheduled shipment FROM node we are checking
            int temp = g.getEdges()[schedule.get(i).getFrom().getIndexOnGraph()][last.getIndexOnGraph()];
            //if the nodes are the same aka. sydney is last node and sydney->vancouver is schedule we are checking
            //we dont want to account for refuelling time since its already there
            if (!schedule.get(i).getFrom().equals(last))
                //otherwise add the refuelling time of the last node
                temp += last.getRefuellingTime();
            //if the shipment has not been made and its less than the smallest distance so far
            if (!s.getChildren().contains(schedule.get(i)) && temp < temp1) {
                //overwrite the new smallest distance with temp (the new lowest)
                temp1 = temp;
                //set the flag as true
                flag = true;
            }

        }
        //if no nodes found
        if (flag == false)
            //set the return as 0
            temp1 = 0;
        //return the smallest distance
        return temp1;
    }

}