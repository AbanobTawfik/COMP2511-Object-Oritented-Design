import java.util.*;
/**
 * The type Node comparator.
 */
public class NodeComparator implements Comparator<searchNode> {
    @Override
    public int compare(searchNode e1, searchNode e2) {
        int score1 = e1.getFScore();
        int score2 = e2.getFScore();
        if (e1.getFScore() < e2.getFScore()) {
            return -1;
        }
        if (e1.getFScore() > e2.getFScore()) {
            return 1;
        }
        return 0;
    }
}