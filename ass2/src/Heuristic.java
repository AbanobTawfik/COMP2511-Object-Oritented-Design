/**
 * The interface Heuristic. implementing stratergy pattern so any form of search can implement the following methods
 *
 * @param <E> the type parameter
 */
public interface Heuristic<E> {


    /**
     * This function will return the estimated time remaining to reach goal state for a given path
     * This function is expected to return an admissable score aka not over-estimating in order to give
     * the optimal path for the search.
     * @param obj the path we are scoring
     * @return the estimated time remaining before goal state is reached
     */
    int getShipmentScore(E obj);
}
