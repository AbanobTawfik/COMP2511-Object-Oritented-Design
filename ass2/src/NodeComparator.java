import java.util.*;
/**
 * The type Node comparator.
 */
public class NodeComparator implements Heuristic<Node>, Comparator<Node>{
    private LinkedList<Node> currentPath;
    private LinkedList<DirectedEdge> schedule;
    private GraphOfPorts g;

    /**
     * Instantiates a new Node comparator.
     *
     * @param currentPath the current path
     * @param schedule    the schedule
     * @param g           the g
     */
    public NodeComparator(LinkedList<Node> currentPath, LinkedList<DirectedEdge> schedule, GraphOfPorts g) {
        this.currentPath = currentPath;
        this.schedule = schedule;
        this.g = g;
    }


    @Override
    public int compare(Node e1, Node e2){
        int score1 = getNodeScore(e1);
        int score2 = getNodeScore(e2);
        if(getNodeScore(e1) < getNodeScore(e2)){
            return -1;
        }
        if(getNodeScore(e1) > getNodeScore(e2)) {
            return 1;
        }
        return 0;
    }
    @Override
    public int getNodeScore(Node obj){
        Node previous = currentPath.getLast();
        //check if the node is not a dead end
        boolean uselessNode = true;
        for(DirectedEdge d : schedule){
            if((d.getFrom().equals(obj)) || (d.getTo().equals(obj) && d.getFrom().equals(previous)) && !previous.equals(obj))
                uselessNode = false;
        }

        if(uselessNode == true)
            return Integer.MAX_VALUE;
        int edges[][] = g.getEdges();
        int sum = 0;
        LinkedList<DirectedEdge> scheduleRemaining = new LinkedList<DirectedEdge>();

        sum += previous.getRefuellingTime();
        sum += edges[previous.getIndexOnGraph()][obj.getIndexOnGraph()];

        Node curr = obj;

        LinkedList<Node> scheduledNeighbours = new LinkedList<Node>();
        for (DirectedEdge d : schedule) {
            if (d.getFrom().equals(curr)) {
                scheduledNeighbours.add(d.getTo());
            }
        }
        for (int i = 0; i < scheduledNeighbours.size(); i++) {
            int index1 = curr.getIndexOnGraph();
            int index2 = scheduledNeighbours.get(i).getIndexOnGraph();
            DirectedEdge d = new DirectedEdge(curr, scheduledNeighbours.get(i));
            if(!scheduleRemaining.contains(d))
                scheduleRemaining.add(d);
        }
        for(int j = 0; j < scheduledNeighbours.size();j++) {
            LinkedList<Node> backtrack = backtrack(scheduledNeighbours.get(j),curr);
            if (backtrack.size() > 1) {
                for (int i = backtrack.size() - 1; i > 1; i--) {
                    sum += backtrack.get(i).getRefuellingTime();
                    sum += edges[backtrack.get(i).getIndexOnGraph()][backtrack.get(i - 1).getIndexOnGraph()];
                    sum += backtrack.get(i).getRefuellingTime();
                    sum += edges[backtrack.get(i).getIndexOnGraph()][backtrack.get(i - 1).getIndexOnGraph()];
                    DirectedEdge d = new DirectedEdge(backtrack.get(i), backtrack.get(i - 1));
                    scheduleRemaining.add(d);
                }
            }
        }

        for(int j = 0; j < scheduledNeighbours.size(); j++) {
            LinkedList<Node> chain = chain(scheduledNeighbours.get(j),curr);
            if (chain.size() > 1) {
                for (int i = 0; i < chain.size() - 1; i++) {
                    sum += chain.get(i).getRefuellingTime();
                    sum += edges[chain.get(i).getIndexOnGraph()][chain.get(i + 1).getIndexOnGraph()];
                    DirectedEdge d = new DirectedEdge(chain.get(i), chain.get(i + 1));
                    if (!scheduleRemaining.contains(d))
                        scheduleRemaining.add(d);
                }
            }
        }
        sum += compareSchedule(scheduleRemaining);
        return sum;
    }

    /**
     * Chain linked list.
     *
     * @param obj the obj
     * @return the linked list
     */
    public LinkedList<Node> chain(Node obj, Node parent){
        LinkedList<Node> chain = new LinkedList<Node>();
        chain.add(parent);
        LinkedList<Node> toSearch = new LinkedList<Node>();
        Node curr = obj;
        chain.add(curr);
        toSearch.add(curr);
        while(!toSearch.isEmpty()) {
            curr = toSearch.poll();
            for (DirectedEdge d : schedule) {
                if (d.getFrom().equals(curr) && !chain.contains(d.getTo())){
                    toSearch.add(d.getTo());
                    chain.add(d.getTo());
                    break;
                }
            }

        }
        return chain;
    }

    /**
     * Backtrack linked list.
     *
     * @param obj the obj
     * @return the linked list
     */
    public LinkedList<Node> backtrack(Node obj,Node parent){
        LinkedList<Node> backtracking = new LinkedList<Node>();
        backtracking.add(parent);
        LinkedList<Node> toSearch = new LinkedList<Node>();
        Node curr = obj;
        backtracking.add(curr);
        toSearch.add(curr);
        while(!toSearch.isEmpty()) {
            curr = toSearch.poll();
            for (DirectedEdge d : schedule) {
                if (d.getTo().equals(curr) && !backtracking.contains(d.getFrom())){
                    toSearch.add(d.getFrom());
                    backtracking.add(d.getFrom());
                    break;
                }
            }
        }
        return backtracking;
    }

    /**
     * Compare schedule int.
     *
     * @param d the d
     * @return the int
     */
    public int compareSchedule(LinkedList<DirectedEdge> d){

        int sum = 0;
        int edges[][] = g.getEdges();
        boolean flag = false;
        for (int i = 0; i < schedule.size(); i++) {
            flag = false;
            for (int j = 0; j < d.size(); j++) {
                if (schedule.get(i).getFrom().equals(d.get(j).getFrom()) && schedule.get(i).getTo().equals(d.get(j).getTo()))
                {
                    flag = true;
                    break;
                }

            }
            if(flag == false) {
                sum += schedule.get(i).getFrom().getRefuellingTime();
                sum += edges[schedule.get(i).getTo().getIndexOnGraph()][schedule.get(i).getFrom().getIndexOnGraph()];
            }
        }
        return sum;
    }
}

/*
Refuelling 6 Sydney             # Refuelling time is 6 days in Sydney
Refuelling 4 Shanghai           # Refuelling time is 4 days in Shanghai
Refuelling 4 Singapore          # Refuelling time is 4 days in Singapore
Refuelling 6 Vancouver          # Refuelling time is 6 days in Vancouver
Refuelling 8 Manila             # Refuelling time is 8 days in Manila
Time 18 Sydney Shanghai         # Travel time is 18 days from Sydney to Shanghai
Time 24 Sydney Singapore        # Travel time is 24 days from Sydney to Singapore
Time 18 Sydney Vancouver        # Travel time is 18 days from Sydney to Vancouver
Time 10 Sydney Manila           # Travel time is 10 days from Sydney to Manila
Time 10 Shanghai Singapore      # Travel time is 10 days from Shanghai to Singapore
Time 24 Shanghai Vancouver      # Travel time is 24 days from Shanghai to Vancouver
Time 12 Shanghai Manila         # Travel time is 12 days from Shanghai to Manila
Time 24 Singapore Vancouver     # Travel time is 24 days from Singapore to Vancouver
Time 22 Singapore Manila        # Travel time is 22 days from Singapore to Manila
Time 20 Vancouver Manila        # Travel time is 20 days from Vancouver to Manila
Shipment Singapore Vancouver    # Shipment is required from Singapore to Vancouver
Shipment Shanghai Singapore     # Shipment is required from Shanghai to Singapore
Shipment Sydney Vancouver       # Shipment is required from Sydney to Vancouver
Shipment Sydney Manila          # Shipment is required from Sydney to Manila
 */