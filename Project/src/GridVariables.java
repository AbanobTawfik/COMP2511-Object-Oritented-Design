/**
 * This class is designed to store all variables used by every file in the project
 * if any change to sboard is required regarding size and constants
 * it is changes here in the single variable in charge
 */
public class GridVariables {
    /**
     * This will be a global counter reset on each new boardto calculate the number of drags
     * the user has performed in a game
     */
    //this is a counter for counting how many drags in the board
    public static int numberOfMoves = 0;

    /**
     * This will be the grid used accross all files to keep consistent access accross all
     * Grid Vehicles.
     */
    //this is the global grid for the project
    public static final Grid g = new Grid();

    /**
     * This will be the 2d matrix to hold all the objects to allow easier collision detection
     * this is global so that all grid vehicles don't require to store the grid in
     * make a more efficient and optimal program
     */
    //this is the global matrix used to hold all the vehicles
    public static final GridVehicle[][] grid = g.getGrid();
    /**
     * This constant will show how many tiles in both directions required for the grid
     * grid size is boardSize * boardSize
     */
    //this is the size of all boards, kept constant in all the grid variables
    public static final int boardSize = 6;

    /**
     * This constant will be the amount of pixels taken by the application when
     * the application is open in the X direction
     */
    //this is the resolution of the app on screen 1440pixelsX
    public static final double gridWidth = 1440;

    /**
     * This constant will be the amount of pixels taken by the application when
     * the application is open in the Y direction
     */
    //this is the resolution of the app on screen 900pixelsY
    public static final double gridHeight = 800;

    /**
     * This is the size of the tile in the X direction in amount of pixels.
     * The tiles are evenly seperated based on board size and resolution.
     */
    //the size of each tile on board in X direction
    //divide the pixels in X direction by board size for evenly sized tiles
    public static final double tileSizeWidth = gridWidth / boardSize;

    /**
     * This is the size of the tile in the Y direction in amount of pixels.
     * The tiles are evenly seperated based on board size and resolution.
     */
    //the size of each tile on board in Y direction
    //divide the pixels in Y direction by board size for evenly sized tiles
    public static final double tileSizeHeight = gridHeight / boardSize;

    /**
     * This is a global check condition to check if goal state is reached
     * that is, the goal car has been moved through the goal row
     */
    //this is the check flag if the goal state has been met
    public static final boolean victory
}
