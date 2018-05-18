import java.util.Comparator;

public class nodeComparator implements Comparator<searchNode> {
    @Override
    public int compare(searchNode sn1, searchNode sn2) {
        if (sn1.getScore() < sn2.getScore())
            return -1;
        if (sn1.getScore() > sn2.getScore())
            return 1;
        return 0;
    }
}
