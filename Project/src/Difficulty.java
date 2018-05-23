/**
 * This enumeration is representitive of the difficulty of the board state on initialisation.
 * Boards have 3 varying difficulties
 * 1. easy - take at most 6 FULL moves to solve
 * 2. medium - take at most 12 FULL moves to solve
 * 3. HaRd - take at most 28 full moves to solve (any harder is cruelty)
 */
public enum Difficulty {
    /**
     * Easy difficulty, needs at most 6 full moves to solve
     */
    EASY,
    /**
     * Medium difficulty, needs at most 12 full moves to solve
     */
    MEDIUM,
    /**
     * Hard difficulty, needs at most 28 full moves to solve
     */
    HARD
}
