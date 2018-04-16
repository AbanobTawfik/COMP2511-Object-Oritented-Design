import java.util.*;

public class ASearch {
    private LinkedList<DirectedEdge> path;
    private int timeTaken;
    private int nodesExpanded;

    public LinkedList<DirectedEdge> Search(GraphOfPorts g, LinkedList<DirectedEdge> schedule) {
        //if the graph only has 1 node there is no search required
        if (g.getnV() <= 1)
            return null;
        //if there is no schedule or only 1 port then return there is no search required
        if (schedule.size() <= 1)
            return null;
        nodesExpanded = 0;
        timeTaken = 0;
        //now we want to make two queues, one for the search and one for the final return path
        LinkedList<Node> closed = new LinkedList<Node>();
        //the only queue which requires to be ordered by the heuristic is the open one
        Comparator<Node> comparator = new nodeComparator(schedule,g);
        PriorityQueue<Node> open = new PriorityQueue<Node>(g.getnV(), comparator);
        //assuming ship will start in Sydney each time we want Sydney to be the first Node in our queue for search
        Node initial = (Node) g.getNodeByString("Sydney");
        //if the node Sydney is not existant in the graph we return null error
        if (initial.equals(null))
            return null;
        //add the initial node to our queue
        open.add(initial);
        //similair to BFS search till either the search queue is empty or the route has been completed
        while (!open.isEmpty() && !isCompletedSchedule(closed, schedule)) {
            Node curr = open.poll();
            timeTaken += curr.getRefuellingTime();
            nodesExpanded++;
            closed.add(curr);
            open.clear();
            LinkedList<Node> neighbours = g.getNeighbours(curr);
            int neighbourDE = 0;
            int neighbourOnSchedule = 0;
            for (Node c : neighbours) {
                for (DirectedEdge d : schedule) {
                    if (d.getTo().equals(c) && d.getFrom().equals(curr)) {
                        open.add(c);
                        neighbourDE++;
                    }
                }
            }
            //if there are no neighbour ports on schedule from the current node
            //try to go to the closest port on schedule
            if (neighbourDE == 0) {
                for (Node c : neighbours) {
                    for (DirectedEdge d : schedule) {
                        if (d.getFrom().equals(c)) {
                            open.add(c);
                            neighbourOnSchedule++;
                        }
                    }
                }
            }
            //if there are no neighbour ports leading to a scheduled delivery from current node
            //then add the closest node which leads to a scheduled port to find it we use a DFS
            //DFS and add any node which links to a node on schedule FROM to the queue or has a path to it
            //dont want to add dead end which dont lead to our schedule
            if (neighbourOnSchedule == 0) {
                for (Node c : neighbours) {
                    if (BFS(c, g, schedule))
                        open.add(c);
                }
            }
            updateSchedule(closed,schedule);
        }
        LinkedList<DirectedEdge> path = createPath(closed);
        return path;
    }

    //want to scan through LinkedList of nodes and create a Directed EGe
    //check if the schedule has been completed by checking remaining schedule with the current one
    public boolean isCompletedSchedule(LinkedList<Node> route, LinkedList<DirectedEdge> schedule) {
        //if there is only 1 node in the list of nodes return need atleast 2 for a directed edge
        if(route.size() == 1)
            return false;
        LinkedList<DirectedEdge> check = new LinkedList<DirectedEdge>();
        for (int i = 0; i < route.size() - 1; i++) {
            DirectedEdge d = new DirectedEdge(route.get(i), route.get(i + 1));
            check.add(d);
        }
        return route.containsAll(schedule);
    }

    public void updateSchedule(LinkedList<Node> route, LinkedList<DirectedEdge> schedule){
        if(route.size() == 1)
            return;
        LinkedList<DirectedEdge> check = new LinkedList<DirectedEdge>();
        for (int i = 0; i < route.size() - 1; i++) {
            DirectedEdge d = new DirectedEdge(route.get(i), route.get(i + 1));
            check.add(d);
        }

        for(int i = 0; i < schedule.size(); i++){
            if(check.contains(schedule.get(i))){
                schedule.remove(i);
            }
        }
    }

    public LinkedList<DirectedEdge> createPath(LinkedList<Node> route){
        LinkedList<DirectedEdge> path = new LinkedList<DirectedEdge>();
        for (int i = 0; i < route.size() - 1; i++) {
            DirectedEdge d = new DirectedEdge(route.get(i), route.get(i + 1));
            path.add(d);
        }
        return path;
    }

    //we want to do a BFS that starts at a node and checks if there is a path from it to a scheduled node
    //if it encounters a node which is a FROM port on the remaining schedule we want to return true
    //return false otherwise
    public boolean BFS(Node n, GraphOfPorts g, LinkedList<DirectedEdge> schedule) {
        int index = g.getNodeIndex(n);
        //initalise our visited array with false
        boolean visited[] = new boolean[g.getnV()];
        Arrays.fill(visited, false);
        //simple to
        LinkedList<Node> pathcheck = new LinkedList<Node>();
        pathcheck.add(n);
        visited[index] = true;
        int edges[][] = g.getEdges();
        while (!pathcheck.isEmpty()) {
            Node tmp = pathcheck.poll();
            if (BFSConditionCheck(tmp, schedule))
                return true;
            for (int i = 0; i < g.getnV(); i++) {
                if (edges[index][i] > 0 && !visited[i]) {
                    visited[i] = true;
                    Node nodeToAdd = g.getNodeByIndex(i);
                    pathcheck.add(nodeToAdd);
                }
            }
        }
        return false;
    }

    public boolean BFSConditionCheck(Node n, LinkedList<DirectedEdge> schedule) {
        for (DirectedEdge d : schedule) {
            if (d.getFrom().equals(n))
                return true;
        }
        return false;
    }
}
