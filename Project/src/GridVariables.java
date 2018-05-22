import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * This class is designed to store all variables used by every file in the project
 * if any change to sboard is required regarding size and constants
 * it is changes here in the single variable in charge
 */
public class GridVariables {
    /**
     * This constant will show how many tiles in both directions required for the grid
     * grid size is BOARD_SIZE * BOARD_SIZE
     */
    //this is the size of all boards, kept constant in all the grid variables
    public static final int BOARD_SIZE = 6;

    /**
     * This constant will be the amount of pixels taken by the application when
     * the application is open in the X direction
     */
    //this is the resolution of the app on screen 1440pixelsX
    public static final double GRID_WIDTH = 1440;

    /**
     * This constant will be the amount of pixels taken by the application when
     * the application is open in the Y direction
     */
    //this is the resolution of the app on screen 900pixelsY
    public static final double GRID_HEIGHT = 800;

    /**
     * This is the size of the tile in the X direction in amount of pixels.
     * The tiles are evenly seperated based on board size and resolution.
     */
    //the size of each tile on board in X direction
    //divide the pixels in X direction by board size for evenly sized tiles
    public static final double TILE_SIZE_WIDTH = GRID_WIDTH / BOARD_SIZE;

    /**
     * This is the size of the tile in the Y direction in amount of pixels.
     * The tiles are evenly seperated based on board size and resolution.
     */
    //the size of each tile on board in Y direction
    //divide the pixels in Y direction by board size for evenly sized tiles
    public static final double TILE_SIZE_HEIGHT = GRID_HEIGHT / BOARD_SIZE;

}
