import java.io.*;
import java.util.*;

/**
 * The ShipmentPlanner class which handles are input parsing and output printing, this class will take in a file input
 * and creates a graph based on input, connect the edges on the graph by supplying weighting, then finding a shipment plan
 * for a given schedule.
 */
public class ShipmentPlanner {
    //this will be the list of nodes which comprise the graph
    private LinkedList<Node> vertices;
    //this will be the shipments required on the schedule
    private LinkedList<DirectedEdge> route;
    //this will be the graph used in the search
    private GraphOfPorts g;
    //this will be the path returned from the A* search
    private LinkedList<DirectedEdge> path;
    //this will be used as a flag to check when all the nodes have been scanned from input
    private boolean graphInput;

    /**
     * This is the main class where the program runs
     *
     * @param args the command line arguements read in
     */
    public static void main(String args[]){
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
    public void runTests(String FileInput)
    {
        //initalise flag as false (scan in node input)
        graphInput = false;
        //initalise the vertex list as a new list of nodes
        vertices = new LinkedList<Node>();
        //initalise our schedule as a new list of directed edges(shipments)
        route = new LinkedList<DirectedEdge>();
        //file input
        Scanner sc = null;
        try {
            //opens the file and allows to read all text in file
            sc = new Scanner(new File(FileInput));
            //reading file line by line
            String line = sc.nextLine();
            while (line != null) {
                //split our keywords and input info from comment character '#'
                String[] data = line.split("#");
                //if the line is just a comment to avoid this issue
                if (data.length == 0) {
                    if (sc.hasNextLine()) {
                        line = sc.nextLine();
                        continue;
                    }
                    break;
                }
                char checker[] = data[0].toCharArray();

                //if the line was a comment then we will have a new String with length 0
                //if the split caused data[0] to be null then we want to skip to next line
                //example if our line is "#ThisIsAComment" it will be split into ["", "#thisIsAComment"]
                //since the first index contains null "", we want to skip to the next else this will cause errors
                if (checker.length == 0 || checker.equals(null)) {
                    //get next line for input
                    if (sc.hasNextLine()) {
                        line = sc.nextLine();
                        continue;
                    }
                    break;
                }

                //split the request data into an array that has different indexing
                //example -> ["Refuelling 6 Sydney] -> ["Refuelling", "6", "Sydney"]
                //this will allow us to proccess our request properly
                String[] inputs = data[0].split(" |\t");
                //get the number of inputs for the request so for our cinema example -> return 4
                int inputcommands = inputs.length;
                //if the size of the split array is 0 we want to skip else this request would cause null errors
                if (inputcommands == 0) {
                    if (sc.hasNextLine()) {
                        line = sc.nextLine();
                        continue;
                    }
                    break;
                }

                //process the request
                process(inputs);
                //get next line for input for iteration through whole file
                //if there is a next line retrieve that
                if (sc.hasNextLine())
                    line = sc.nextLine();
                    //otherwise break
                else
                    break;
            }

        } catch (FileNotFoundException e) {
            //catch exceptions example if invalid file and print them out
            System.out.println(e.getMessage());
        } finally {
            //close file after we reached the end of file
            if (sc != null) sc.close();
        }
        //close file once all scanning is complete
        sc.close();
        //once we have all the data we want to perform the A* search
        ASearch a = new ASearch();
        //we want to set the schedule (goal state) of our a* search
        a.setSchedule(route);
        //we want to set the graph required for the A* search to workout costs
        a.setG(g);
        //now we want to perform the A* search and store the result in the path (linked list of directed edges)
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
    public void process(String request[]){
        if(request[0].equals("Refuelling"))
            outcomeRefuelling(request);
        if(request[0].equals("Time"))
            outcomeTime(request);
        if(request[0].equals("Shipment"))
            outcomeShipment(request);
    }

    /**
     * This function will handle the refuelling request, it will add a vertex, to the vertice list
     * and initalise the vertex with the port name and refuelling time
     *
     * @param request the line request from the input file
     */
    public void outcomeRefuelling(String request[]){
        try {
            //check if the first part of the request is an integer
            Integer.parseInt(request[1]);
        } catch (Exception e) {
            return;
        }
        int nodeIndex = vertices.size();
        int refuelTime = Integer.parseInt(request[1]);
        String portName = request[2];
        Node add = new Node(portName,refuelTime,nodeIndex);
        vertices.add(add);
    }

    /**
     * This function will handle the Time request, it will connect two nodes on the adjacency matrix graph
     * based on the user's time specified in the line and link the two nodes together on the graph with given weighting
     *
     * @param request the line request from the input file
     */
    public void outcomeTime(String request[]){
        //if this is the first time coming accross graph input we want to
        //make our graph and add the edge in the request
        try {
            //check if the first part of the request is an integer
            Integer.parseInt(request[1]);
        } catch (Exception e) {
            return;
        }

        int weight = Integer.parseInt(request[1]);
        String port1Name = request[2];
        String port2Name = request[3];
        int indexOfPort1 = getNodeIndexByString(port1Name);
        int indexOfPort2 = getNodeIndexByString(port2Name);
        if(indexOfPort1 == -1 || indexOfPort2 == -1)
            return;
        Node port1 = vertices.get(indexOfPort1);
        Node port2 = vertices.get(indexOfPort2);
        if(!graphInput){
            int nV = vertices.size();
            g = new GraphOfPorts(nV);
            g.setVertices(vertices);
            g.insertEdge(port1,port2,weight);
            graphInput = true;
        }else {//otherwise just add the request
            g.insertEdge(port1,port2,weight);
        }
    }

    /**
     * This function will handle the Shipment request, it will add to the schedule
     * the specified shipment which is required in the search.
     *
     * @param request the line request from the input file
     */
    public void outcomeShipment(String request[]){
        String port1Name = request[1];
        String port2Name = request[2];
        int indexOfPort1 = getNodeIndexByString(port1Name);
        int indexOfPort2 = getNodeIndexByString(port2Name);
        if(indexOfPort1 == -1 || indexOfPort2 == -1)
            return;
        Node port1 = vertices.get(indexOfPort1);
        Node port2 = vertices.get(indexOfPort2);
        DirectedEdge add = new DirectedEdge(port1, port2);
        route.add(add);
    }


    /**
     * This method is designed to locate nodes based on their stirng "port name". it will return the index
     * on the graph where the node is located, and this is used in order to link the graph for the project
     * as nodes are referenced by string.
     * <br/> this method expects a string of any sort, and will guarantee to return the index where the node is located
     * or -1 if the node doesn't exist in the graph
     * @param s the string which is used to identify the node
     * @return the index of the node in the graph, if it exists or -1 otherwise.
     */
    public int getNodeIndexByString(String s){
        for(int i = 0; i < vertices.size(); i++){
            if(vertices.get(i).getPortName().equals(s))
                return i;
        }
        return -1;
    }

    /**
     * This method will print out the path which is found during the A* search. if no path exists, it will output nothing
     * <br/> this method requires a list of edges, the path and the aSearch class in order to call methods
     *
     * @param directedEdges the list of directed edges aka a from node -> to node, this is what our path consists of
     * @param a             the Asearch class, to retrieve the nodes expanded
     */
    public void printPath(LinkedList<DirectedEdge> directedEdges, ASearch a){
        int nodesExpanded = a.getNodesExpanded();
        int timeTaken = a.getCost(directedEdges, g);

        if(null != directedEdges && directedEdges.size() > 0) {
            System.out.println(nodesExpanded + " nodes expanded");
            System.out.println("cost = " + timeTaken);
            for (int i = 0; i < directedEdges.size()-1; i++) {
                String portNameFrom = directedEdges.get(i).getFrom().getPortName();
                String portNameTo = directedEdges.get(i).getTo().getPortName();
                if(!portNameFrom.equals(portNameTo))
                System.out.println("Ship " + portNameFrom + " to " + portNameTo);
                if(!portNameTo.equals(directedEdges.get(i+1).getFrom().getPortName()))
                System.out.println("Ship " + directedEdges.get(i).getTo().getPortName() + " to " + directedEdges.get(i+1).getFrom().getPortName());

            }
            if(!directedEdges.get(directedEdges.size()-2).equals(directedEdges.get(directedEdges.size()-1).getFrom().getPortName())){
                System.out.print("Ship " + directedEdges.get(directedEdges.size()-1).getFrom() + " to " + directedEdges.get(directedEdges.size()-1).getTo().getPortName());
            }

        }
    }
}
