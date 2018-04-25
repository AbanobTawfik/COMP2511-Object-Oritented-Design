import java.util.*;

/**
 * The type A search.
 */
public class ASearch implements Heuristic<searchNode>{
    private int nodesExpanded;
    private LinkedList<DirectedEdge> schedule;
    private GraphOfPorts g;

    public void setSchedule(LinkedList<DirectedEdge> schedule) {
        this.schedule = schedule;
    }

    public void setG(GraphOfPorts g) {
        this.g = g;
    }


    public LinkedList<DirectedEdge> Search() {
        if (g.getnV() <= 1)
            return null;
        if (schedule.size() <= 1)
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
            if(curr.getChildren().size() > 1)
                System.out.println( curr + " " + curr.getGScore()
                );

            closed.add(curr);
            if (isCompletedSchedule(curr)) {
                return createPath(curr);
            }
            for(DirectedEdge d : schedule) {

                searchNode newPath = new searchNode(curr.getShipment());
                LinkedList<DirectedEdge> ships = new LinkedList<DirectedEdge>(curr.getChildren());
                newPath.setChildren(ships);
                if(ships.size() == 0){
                    newPath.addToChildren(new DirectedEdge(curr.getShipment().getTo(),d.getFrom()));
                }
                newPath.addToChildren(d);
                newPath.setGScore(getWeightPath(newPath));
                newPath.setHScore(getShipmentScore(newPath));
                newPath.setFScore();
                ArrayList<Integer> onClosed = onClosed(newPath, closed);
                ArrayList<Integer> onOpen = onOpen(newPath, open);
                if (!onClosed.isEmpty()) {
                    for(int i = 0; i < onClosed.size();i++) {
                        int FScoreClosed = closed.get(onClosed.get(i)).getFScore();
                        int FScoreNewPath = newPath.getFScore();
                        if (FScoreClosed >= FScoreNewPath) {
                            closed.remove(onClosed.get(i));
                            open.add(newPath);
                        }
                    }
                } else if (!onOpen.isEmpty()) {

                    PriorityQueue<searchNode> copy = new PriorityQueue<searchNode>(open);
                    ArrayList<searchNode> openCopy = new ArrayList<searchNode>();
                    for(int i = 0; i < copy.size();i++){
                        openCopy.add(copy.poll());
                    }
                    for(int i = 0; i < onOpen.size();i++){
                        searchNode openNode = openCopy.get(onOpen.get(i));
                        int FScoreOpen = openNode.getFScore();
                        int FscoreNewPath = newPath.getFScore();
                        if (FScoreOpen >= FscoreNewPath) {
                            open.remove(openNode);
                            open.add(newPath);
                        }
                    }
                }
                else{
                    open.add(newPath);
                }


            }
        }

            return null;

    }



    public boolean isCompletedSchedule(searchNode path){
        LinkedList<DirectedEdge> scheduleCopy = new LinkedList<DirectedEdge>();
        for(int i = 0; i < schedule.size();i++)
            scheduleCopy.add(schedule.get(i));
        for(int i = 0; i < path.getChildren().size();i++){
            if(scheduleCopy.contains(path.getChildren().get(i)))
                scheduleCopy.remove(path.getChildren().get(i));
        }
        return scheduleCopy.isEmpty();
    }

    /**
     * Create path linked list.
     *
     * @param route the route
     * @return the linked list
     */
    public LinkedList<DirectedEdge> createPath(searchNode route) {
        LinkedList<DirectedEdge> path = new LinkedList<DirectedEdge>();
        for (int i = 0; i < route.getChildren().size(); i++) {
            path.add(route.getChildren().get(i));
        }
        return path;
    }


    public int getCost(LinkedList<DirectedEdge> edges, GraphOfPorts g) {
        ArrayList<Node> path = new ArrayList<Node>();
        for(int i = 0; i < edges.size();i++){
            path.add(edges.get(i).getFrom());
            path.add(edges.get(i).getTo());
        }
        int sum = 0;
        for(int i = 0; i < path.size()-1;i++){
            if(!path.get(i).equals(path.get(i+1))) {
                sum += path.get(i).getRefuellingTime();
                sum += g.getEdges()[path.get(i).getIndexOnGraph()][path.get(i + 1).getIndexOnGraph()];
            }
        }
        return sum;
    }


    public int getNodesExpanded() {
        return nodesExpanded;
    }

    @Override
    public int getShipmentScore(searchNode d) {
        int sum = getScheduleScoreRemaining(d);
        int minShipment = Integer.MAX_VALUE;
        boolean flag = false;
        if(d.getChildren().size()>1) {
            for (int i = 0; i < schedule.size() - 1; i++) {
                if (schedule.contains(new DirectedEdge(d.getChildren().getLast().getTo(), schedule.get(i).getFrom()))) {
                    int temp = g.getWeightOfEdge(schedule.get(i).getFrom(), d.getChildren().getLast().getTo());
                    if (temp < minShipment) {
                        flag = true;
                        minShipment = temp;
                    }
                }
            }
        }
        if(!flag) {
            minShipment = 0;
        }
        sum -= minShipment;
        for(int i = 0; i < d.getChildren().size() -1 ;i++){
            sum -= d.getChildren().get(i).getFrom().getRefuellingTime();
            sum -= g.getEdges()[d.getChildren().get(i).getFrom().getIndexOnGraph()][d.getChildren().get(i).getTo().getIndexOnGraph()];
        }
        if(d.getChildren().size() == 1){
            sum -= d.getChildren().get(0).getFrom().getRefuellingTime();
            sum -= g.getEdges()[d.getChildren().get(0).getFrom().getIndexOnGraph()][d.getChildren().get(0).getTo().getIndexOnGraph()];
        }
        sum = (Integer) sum/schedule.size();
        return sum;
    }


    public int getWeightPath(searchNode s){
        ArrayList<Node> path = new ArrayList<Node>();
        path.add(s.getShipment().getFrom());
        path.add(s.getShipment().getTo());
        for(int i = 0; i < s.getChildren().size();i++){
            path.add(s.getChildren().get(i).getFrom());
            path.add(s.getChildren().get(i).getTo());
        }

        int sum = 0;

        for(int i = 0; i < path.size()-1;i++){
            if(!path.get(i).equals(path.get(i+1))) {
                sum += path.get(i).getRefuellingTime();
                sum += g.getEdges()[path.get(i).getIndexOnGraph()][path.get(i + 1).getIndexOnGraph()];
            }
        }

        return sum;
    }


    public ArrayList<Integer> onClosed(searchNode p, LinkedList<searchNode> closed){
        ArrayList<Integer> ret = new ArrayList<Integer>();
        for(int i = 0; i < closed.size(); i++){
            if(p.getChildren().containsAll(closed.get(i).getChildren())
                    && closed.get(i).getChildren().containsAll(p.getChildren()))
                 ret.add(i);
        }
        return ret;
    }
    public ArrayList<Integer> onOpen(searchNode p, PriorityQueue<searchNode> open){
        PriorityQueue<searchNode> copy = new PriorityQueue<searchNode>(open);
        ArrayList<searchNode> openCopy = new ArrayList<searchNode>();
        for(int i = 0; i < copy.size();i++){
            searchNode curr = copy.poll();
            openCopy.add(curr);
        }
        ArrayList<Integer> ret = new ArrayList<Integer>();
        for(int i = 0; i < openCopy.size(); i++){
            if(p.getChildren().containsAll(openCopy.get(i).getChildren())
                    && openCopy.get(i).getChildren().containsAll(p.getChildren()))
                ret.add(i);;
        }
        return ret;
    }

    public int getScheduleScoreRemaining(searchNode s){
        int sum = 0;
        int count = 0;
        for(int i = 0;i < schedule.size();i++){
            if (s.getChildren().contains(schedule.get(i)))
                continue;
            sum += schedule.get(i).getFrom().getRefuellingTime();
            sum += g.getEdges()[schedule.get(i).getFrom().getIndexOnGraph()][schedule.get(i).getTo().getIndexOnGraph()];
            count++;
        }

        return sum;
    }

}




