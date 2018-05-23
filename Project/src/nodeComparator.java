import java.util.Comparator;

/**
 * This class will be used as a custom comparator for the priority queue. The
 * comparison is based on the search node score.
 */
public class nodeComparator implements Comparator<searchNode> {
    /**
     * This method will be used to compare two search node and order the priority queue.
     * this method will guarantee to return an integer representing the sort of the two objects
     *
     * @param sn1 the first search node we are comparing
     * @param sn2 the second search node we are comparing
     * @return integer representation of the order
     */
    @Override
    public int compare(searchNode sn1, searchNode sn2) {
        //if the first search node has a lower score than the second search node
        if (sn1.getScore() < sn2.getScore())
            //return -1 means sn1 sn2 is order in queue
            return -1;
        //if the first search node has a higher score than the first search node
        if (sn1.getScore() > sn2.getScore())
            //return 1 means sn2 sn1 is order in queue
            return 1;
        //else if the scores are equal we just leave it to natural ordering
        return 0;
    }
}
