import java.io.*;
import java.util.*;

/**
 * The ShipmentPlanner class which handles are input parsing and output printing, this class will take in a file input
 * and creates a graph based on input, connect the edges on the graph by supplying weighting, then finding a shipment plan
 * for a given schedule.
 */
/* HEURISTIC RUNTIME ANALYSIS
 * (as quoted from Wikipedia)
 * Consider integer n which represents the number of shipments where n>=0
 * And consider integer m which represents the number of nodes on graph where m >=0
 * (outdegree of all nodes are m since they are all connected)
 * The runtime complexity analysis for the A*Search without a heuristic in worst case is O(m^n).
 * in best case it will be O(n) where n is the number of shipments on the schedule.
 * However, through the use of my heuristic I will be eliminating paths that are useless and
 * prioritising paths which have great scores. The way my heuristic will do this is take the path,
 * and add the remaining schedule weight to the path, and the closest distance to the nearest node
 * on shipment to the last node in the path (0 if the last node in the path is a shipment itself).
 * What this will do is, paths which have longer distance to complete shipment, or paths which are
 * further away from shipments are discarded, their scores will be high and towards the end of the
 * open set. This will get rid of paths which have longer distances to reach goal state since they
 * will not be optimal paths. In order to calculate the effectiveness of the heuristic I will be using
 * the formula supplied on Wikipedia that
 * N + 1 = 1 + k + k^2 + (all the inbetween) + k^n to solve for K,
 * where N are the number of nodes expanded.
 * In one of my searches 19 nodes expanded, and n = 7 m = 5
 * 20 = 1 + k + k^2 + k^3 + k^4 + k^5 + k^6 + k^7
 * 19 = k + k^2 + k^3 + k^4 + k^5 + k^6 + k^7
 * Using an online calculator
 * K = 1.25215
 * The heuristic I have used brings runtime costs to O(1.25215^n) rather than the O(5^n).
 * this is because the heuristic will get rid of all extra branching which will result in less suboptimal paths checked,
 * since suboptimal paths will not be expanded on further during search.

 */
public class ShipmentPlanner {
    //initalise the vertex list as a new list of nodes
    //this will be the list of nodes which comprise the graph
    private ArrayList<Node> vertices = new ArrayList<Node>();
    //initalise our schedule as a new list of directed edges(shipments)
    //this will be the shipments required on the schedule
    private ArrayList<DirectedEdge> schedule = new ArrayList<DirectedEdge>();
    //this will be the graph used in the search
    private GraphOfPorts g;
    //this will be the path returned from the A* search
    private ArrayList<DirectedEdge> path;
    //initalise flag as false (scan in node input)
    //this will be used as a flag to check when all the nodes have been scanned from input
    private boolean graphInput = false;

    /**
     * This is the main class where the program runs
     *
     * @param args the command line arguements read in
     */
    public static void main(String args[]) {
        //bootstrapping the class to run the main method
        ShipmentPlanner sp = new ShipmentPlanner();
        //read file input from args[0]
        sp.runTests(args[0]);
    }

    /**
     * this function will parse in the input from the file. this function will be handling parsing inputs.
     *
     * @param FileInput the file read in from command line
     */
    public void runTests(String FileInput) {
        //file input
        Scanner sc = null;
        try {
            //initalise a new scanner for reading the file
            sc = new Scanner(new File(FileInput));
            //read current line at start of file
            String lineInput = sc.nextLine();
            //while there line read is not end of file
            while (lineInput != null) {
                //split the request from the comment character '#'
                String[] dataInput = lineInput.split("#");
                //if the line was just a comment, or there was no line, we want to skip to the next line
                //as invalid lines will not yield any result (no valid request has length 0)
                //so just skip over
                if (dataInput.length == 0) {
                    //if there is a next line, go to next line
                    if (sc.hasNextLine()) {
                        lineInput = sc.nextLine();
                        continue;
                    }
                    //otherwise break since no next line
                    break;
                }

                //now we want to split up the request which was seperated from the comment '#'
                //for example ["refuelling 6 Sydney"] -> ["refuelling", "6", "Sydney"]
                //this is a check to see if it can be split
                char validInput[] = dataInput[0].toCharArray();

                //if the split request has no length, or its a null line
                //we want to skip over to the next line
                if (validInput.length == 0 || validInput.equals(null)) {
                    //skip and process next line  if exists
                    if (sc.hasNextLine()) {
                        lineInput = sc.nextLine();
                        continue;
                    }
                    //otherwise break
                    break;
                }

                //split the request dataInput into an array that has different indexing
                //example -> ["Refuelling 6 Sydney"] -> ["Refuelling", "6", "Sydney"]
                //this will allow us to proccess our request properly based on our takens " " " \t"
                String[] request = dataInput[0].split(" |\t");
                int requestLength = request.length;
                //if the request was nothing we want to skip to next line if one exists
                if (requestLength == 0) {
                    if (sc.hasNextLine()) {
                        lineInput = sc.nextLine();
                        continue;
                    }
                    break;
                }
                //proccess the line request
                process(request);
                //iterating over file line by line
                if (sc.hasNextLine())
                    lineInput = sc.nextLine();
                else
                    break;
            }

        } catch (FileNotFoundException e) {
            //print out any file not found exceptions
            System.out.println(e.getMessage());
        } finally {
            //close file after all scanning is complete
            if (sc != null) sc.close();
        }
        //close file once all scanning is complete
        sc.close();
        //once we have all the dataInput we want to perform the A* search
        ASearch a = new ASearch();
        //we want to set the schedule (goal state) of our a* search
        a.setSchedule(schedule);
        //we want to set the graph required for the A* search to workout costs
        a.setG(g);
        //now we want to perform the A* search and store the result in the path (array list of directed edges)
        path = a.Search();
        //nowe we print the path returned fromt he A* search
        printPath(path, a);


    }

    /**
     * This function will be used in order to handle input processing, it will redirect the request
     * based on the request type, or just skip over it if the request is invalid
     *
     * @param request the request from the file
     */
    /*
     * proccess the line request from the file scan
     */
    public void process(String request[]) {
        //if the first word of the request IS "Refuelling" we want to go to the refuel handler
        if (request[0].equals("Refuelling"))
            outcomeRefuelling(request);
        //if the first word of the request IS "Time" we want to go to the Time handler
        if (request[0].equals("Time"))
            outcomeTime(request);
        //if the first word of the request IS "Shipment" we want to go to the Shipment handler
        if (request[0].equals("Shipment"))
            outcomeShipment(request);
        //otherwise no handle return to file scanning
    }

    /**
     * This function will handle the refuelling request, it will add a vertex, to the vertice list
     * and initalise the vertex with the port name and refuelling time
     *
     * @param request the line request from the input file
     */
    /*
     * handler for handling refuelling requests creates a node on our graph
     */
    public void outcomeRefuelling(String request[]) {
        try {
            //check if the first part of the request is an integer
            Integer.parseInt(request[1]);
        } catch (Exception e) {
            //if not return
            return;
        }
        //["Refuelling" "7" "Sydney"]
        //set the index of node as when it is placed
        int nodeIndex = vertices.size();
        //set the refuel time as the next part of the input for our case 7
        int refuelTime = Integer.parseInt(request[1]);
        //set the port name as the part of input after integer so in our case "Sydney"
        String portName = request[2];
        //now create a new node from the port name, index and refuel time and add it to the vertex list
        Node add = new Node(portName, refuelTime, nodeIndex);
        vertices.add(add);
    }

    /**
     * This function will handle the Time request, it will connect two nodes on the adjacency matrix graph
     * based on the user's time specified in the line and link the two nodes together on the graph with given weighting
     *
     * @param request the line request from the input file
     */
    /*
     * handler for handling Time requests creates adds edge weight on our graph
     */
    public void outcomeTime(String request[]) {
        //if this is the first time coming accross graph input we want to
        //make our graph and add the edge in the request
        try {
            //check if the first part of the request is an integer
            Integer.parseInt(request[1]);
        } catch (Exception e) {
            return;
        }
        //["Time" "6" "Sydney" "Manila"] as our reference
        //setting the weight of our path as the 2nd string, in our case 6
        int weight = Integer.parseInt(request[1]);
        //now we want to set the first and second port name in order to locate them (order isn't important since)
        //edges have same weight in both directions
        String port1Name = request[2];
        String port2Name = request[3];
        //now we want to find the index on the graph where the nodes with that string is located
        int indexOfPort1 = getNodeIndexByString(port1Name);
        int indexOfPort2 = getNodeIndexByString(port2Name);
        //if neither node exists in the graph we want to skip over
        if (indexOfPort1 == -1 || indexOfPort2 == -1)
            return;
        //now we want to retrieve those nodes from the vertex list
        Node port1 = vertices.get(indexOfPort1);
        Node port2 = vertices.get(indexOfPort2);
        //IF the graph has not been created yet (flag still false)
        if (!graphInput) {
            //we want to create our graph with the given vertex list
            //since we are no longer scanning in node input
            int nV = vertices.size();
            //create the instance of graph (object)
            g = new GraphOfPorts(nV);
            //set it's vertices as the current vertex list
            g.setVertices(vertices);
            //now we use the inbuilt function to create an edge between the two nodes
            g.insertEdge(port1, port2, weight);
            //set the flag as true (graph has been created) wont create a new graph again and reset
            graphInput = true;
        } else {
            //otherwise just add the edge weight to the graph
            g.insertEdge(port1, port2, weight);
        }
    }

    /**
     * This function will handle the Shipment request, it will add to the schedule
     * the specified shipment which is required in the search.
     *
     * @param request the line request from the input file
     */
    public void outcomeShipment(String request[]) {
        //["Shipment" "Sydney" "Manila"]
        //now we want to set the first and second port name in order to locate them (order IS VERY VERY important since)
        //the shipment has specific direction it MUST be made from sydney TO manila, which is extremely different from manila to sydney
        String port1Name = request[1];
        String port2Name = request[2];
        //now we want to find the index on the graph where the nodes with that string is located
        int indexOfPort1 = getNodeIndexByString(port1Name);
        int indexOfPort2 = getNodeIndexByString(port2Name);
        //if neither node exists in the graph we want to skip over
        if (indexOfPort1 == -1 || indexOfPort2 == -1)
            return;
        //we want to now retrieve those nodes from the vertex list
        Node port1 = vertices.get(indexOfPort1);
        Node port2 = vertices.get(indexOfPort2);
        //now we want to create a directional edge FROM port1 TO port 2
        DirectedEdge add = new DirectedEdge(port1, port2);
        //add this to the schedule list
        schedule.add(add);
    }


    /**
     * This method is designed to locate nodes based on their stirng "port name". it will return the index
     * on the graph where the node is located, and this is used in order to link the graph for the project
     * as nodes are referenced by string.
     * <br> this method expects a string of any sort, and will guarantee to return the index where the node is located
     * or -1 if the node doesn't exist in the graph
     *
     * @param s the string which is used to identify the node
     * @return the index of the node in the graph, if it exists or -1 otherwise.
     */
    /*
     * returns the index of the node based on the string value
     */
    public int getNodeIndexByString(String s) {
        //this function will scan through the vertex list
        for (int i = 0; i < vertices.size(); i++) {
            //it will check if the port name of the vertex at index i is the
            //same string as the one entered in input
            if (vertices.get(i).getPortName().equals(s))
                //return the index if it is found
                return i;
        }
        //return false otherwise
        return -1;
    }

    /**
     * This method will print out the path which is found during the A* search. if no path exists, it will output nothing
     * <br> this method requires a list of edges, the path and the aSearch class in order to call methods
     *
     * @param directedEdges the list of directed edges aka a from node directed to the to node, this is what our path consists of
     * @param a             the Asearch class, to retrieve the nodes expanded
     */
    /*
     * This method will print the path for the A* result
     */
    public void printPath(ArrayList<DirectedEdge> directedEdges, ASearch a) {
        //retrieving the number of nodes expanded search
        int nodesExpanded = a.getNodesExpanded();
        //retrieving the time taken in the search
        int timeTaken = a.getCost(directedEdges, g);

        //if the A* search returns a path then we want to print an output, otherwise we print nothing
        if (null != directedEdges && directedEdges.size() > 0) {
            //print out the number of nodes expanded
            System.out.println(nodesExpanded + " nodes expanded");
            //print out the cost of the path
            System.out.println("cost = " + timeTaken);
            //now we want to scan up until the last node
            for (int i = 0; i < directedEdges.size() - 1; i++) {
                //we want to (for simplicity of writing the code) get the port name of the from Node
                //we want to get the port name of the TO node
                String portNameFrom = directedEdges.get(i).getFrom().getPortName();
                String portNameTo = directedEdges.get(i).getTo().getPortName();
                //if the shipment is to itself, i.e sydney->sydney DO NOT print
                if (!portNameFrom.equals(portNameTo))
                    //now we print ship portFrom TO portTO
                    System.out.println("Ship " + portNameFrom + " to " + portNameTo);
                //if the shipment to the next node is not the same example
                //Sydney -> Vancouver
                //Vanoucver -> Manila
                // we dont want to print Vancouver -> Vancouver, just Vancouver -> Manila
                if (!portNameTo.equals(directedEdges.get(i + 1).getFrom().getPortName()))
                    //otherwise we want print the connection shipment eg
                    //Sydney ->Manila
                    //Vancouver ->Shanghai
                    //this will allow us to print Manila->Vancouver despite no direct connection
                    System.out.println("Ship " + directedEdges.get(i).getTo().getPortName() + " to " + directedEdges.get(i + 1).getFrom().getPortName());

            }
            //now we print the final shipment as it, from -> TO provided they are not the same node
            //eg sydney->Sydney
            if (directedEdges.size() > 1 && !directedEdges.get(directedEdges.size() - 2).equals(directedEdges.get(directedEdges.size() - 1).getFrom().getPortName())) {
                System.out.println("Ship " + directedEdges.get(directedEdges.size() - 1).getFrom() + " to " + directedEdges.get(directedEdges.size() - 1).getTo().getPortName());
            }
            if (directedEdges.size() == 1) {
                System.out.println("Ship " + directedEdges.get(0).getFrom() + " to " + directedEdges.get(0).getTo().getPortName());

            }

        }
    }
}
