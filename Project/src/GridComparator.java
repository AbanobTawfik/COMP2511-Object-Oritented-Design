import java.util.Comparator;

public class GridComparator implements Comparator<Grid> {
    @Override
    public int compare(Grid grid1, Grid grid2) {
        if (grid1.getGridScore() < grid2.getGridScore())
            return -1;
        if (grid1.getGridScore() > grid2.getGridScore())
            return 1;
        return 0;
    }
}
