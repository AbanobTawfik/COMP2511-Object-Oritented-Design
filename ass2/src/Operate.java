import java.io.*;
import java.util.*;

public class Operate {
    private LinkedList<Node> vertices;
    private LinkedList<DirectedEdge> route;
    private GraphOfPorts g;
    private LinkedList<DirectedEdge> path;
    boolean graphInput;
    /*
     * for reading input from the file i want to take out the important keywords and their format
     * when scanner detects keywords as
     * Cinema it will read the next input as cinema number to alter
     * it will read the input after that as the row to add
     * it will read the input after that as the number of seats in that row
     * when the # char is detected it will cease to use that input rather as comments
     * then it will continue till the next keyword is found
     * list of keywords
     * Cinema, Session, Request, Change, Cancel, Print
     */

    /**
     * This function runs the testFile and prints output to stdout.
     * All testing is done here.
     * This method will guarantee to perform all testing and output the appropriate returns from those requests
     * @param FileInput the file which is going to be read in, expected to be a valid file which can be read
     */
    public void runTests(String FileInput)

    {
        graphInput = false;
        vertices = new LinkedList<Node>();
        route = new LinkedList<DirectedEdge>();
        //initialise an ArrayList to hold all cinemas in the "Building"
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

                process(inputs);
                //process the request
                //run the outcome function which prints outcome based on the input
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
        //close file
        sc.close();
        ASearch a = new ASearch();
        path = a.Search(g,route);
        printPath(path, a);


    }

    public void process(String request[]){
        if(request[0].equals("Refuelling"))
            outcomeRefuelling(request);
        if(request[0].equals("Time"))
            outcomeTime(request);
        if(request[0].equals("Shipment"))
            outcomeShipment(request);
    }

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


    public int getNodeIndexByString(String s){
        for(int i = 0; i < vertices.size(); i++){
            if(vertices.get(i).getPortName().equals(s))
                return i;
        }
        return -1;
    }

    public void printPath(LinkedList<DirectedEdge> directedEdges, ASearch a){
        int nodesExpanded = a.getNodesExpanded();
        int timeTaken = a.getCost(directedEdges, g);
        System.out.println(nodesExpanded + " nodes expanded");
        System.out.println("cost = " + timeTaken);
        for(int i = 0; i < directedEdges.size(); i++){
            String portNameFrom = directedEdges.get(i).getFrom().getPortName();
            String portNameTo = directedEdges.get(i).getTo().getPortName();
            System.out.print("Ship " + portNameFrom + " to " + portNameTo);
            System.out.println();
        }

    }
}
