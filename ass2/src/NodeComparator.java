import java.util.*;

/**
 * This node comparator class implements the comparator interface utilising the stratergy pattern,
 * it allows for the comparison of search nodes (paths) based on their respective Fscores, which is
 * the sum of their actual cost of path and an estimation to goal state
 */
/*
 * this will be used as a comparator  for our priority queue
 */
public class NodeComparator implements Comparator<searchNode> {
    /**
     * this method will be used as a linear comparator for the priority Queue, it will compare the nodes based on their
     * Fscore. and the nodes with lower fscore will be closer to the head, where the nodes with the higher fscore will be
     * closer to the tail
     *
     * @param e1 the first searchNode, aka path we are comparing scores
     * @param e2 the second searchNode, aka path we are comparing scores
     * @return this will return either -1, 1 or 0. it will return -1 if the first path has a lower score than the second path
     * <br /> it will return  1 if the first path has a higher score than the second path.
     * <br/>otherwise 0 if they are equal
     * the comparison is used to order the priority queue
     */
    /*
     * this is our comparator's comparison method, it will compare searchNodes (paths) based
     * on their FScore values.
     */
    @Override
    public int compare(searchNode e1, searchNode e2) {
        //if node 1 has a a lower FScore than node 2, return -1 which will put e1 BEFORE e2
        if (e1.getFScore() < e2.getFScore()) {
            return -1;
        }
        //if node 1 has a higher FScore than node 2, return 1 which will put e2 BEFORE e1
        if (e1.getFScore() > e2.getFScore()) {
            return 1;
        }
        //else return they are same whichw ill keep order same
        return 0;
    }
}