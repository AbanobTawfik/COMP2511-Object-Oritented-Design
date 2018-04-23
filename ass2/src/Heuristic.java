/*
 * My heuristic for the project is based off the goal to reach the final port
 * Whilst the search will reach every port listed
 */
public interface Heuristic<E> {

    /**
     * @param obj, a non null object which we are comparing it's position to the destination
     * @return the time remaining from the node till the final destination
     */
     int getNodeScore(E obj);
}
