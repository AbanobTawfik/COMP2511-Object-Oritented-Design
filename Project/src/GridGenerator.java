import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * The Grid Generator class, which will create a new board which contains
 * a grid with tiles (visual tiles)
 * and a group of vehicles that can be moved
 */
public class GridGenerator {
    /**
     * Generates a grid with a board of tiles, and a group of vehicles. This method
     * will guarantee to return a Parent which will be used as a root for the scene it is provided
     * to.
     * @return the root used for the scene
     */
    public Parent generateGrid() {
        //this will be the Parent used for the scene
        Pane finalBoard = new Pane();
        //sets the background to black
        finalBoard.setStyle("-fx-background-color: black");
        //set the size of the parent to the resolution in the global grid variables.
        finalBoard.setPrefSize(GridVariables.gridWidth, GridVariables.gridHeight);


        //this is the 2d grid we will be using for GUI
        GridPane gridpane = new GridPane();
        //this is the group which will hold the vehicles on the board
        Group vehicles = new Group();
        //initalising the rows for this grid (adds a slot at each row index)
        for (int i = 0; i < GridVariables.boardSize; i++) {
            //initalises rows for the grid pane
            RowConstraints row = new RowConstraints();
            //this lets the nodes fill the grid tile
            row.setFillHeight(true);
            gridpane.getRowConstraints().add(row);
        }
        //initalising the columns for this grid (adds a slot at each col index)
        for (int i = 0; i < GridVariables.boardSize; i++) {
            //initalises the columns for the grid pane
            ColumnConstraints col = new ColumnConstraints();
            //this lets the nodes fill the grid tile
            col.setFillWidth(true);
            gridpane.getColumnConstraints().add(col);
        }

        //filling our grid with tiles for the game
        for (int i = 0; i < GridVariables.boardSize; i++) {
            for (int j = 0; j < GridVariables.boardSize; j++) {
                //tiles are represented by a rectangle that is white with black border
                //the -1.15 is so the rectangle fits better with the vehicles
                Rectangle tile = new Rectangle(GridVariables.tileSizeWidth - 1.15, GridVariables.tileSizeHeight - 1.15);
                if (i == GridVariables.boardSize - 1 && j == GridVariables.g.getGoalRow()) {
                    //this sets the goal tile filled green so it stands out better
                    //the stroke is a black border around the grid
                    tile.setStroke(Color.BLACK);
                    tile.setFill(Color.PALEGREEN);
                    //this will slightly round the rectangles for smoother shapes but not as round as the vehicles
                    tile.setArcHeight(25);
                    tile.setArcWidth(25);
                }
                //otherwise every other tile will be filled white with a black border
                else {
                    //fill the rectangle with grey, has a nicer contrast to the vehicles
                    //add a black border for the rectangle
                    tile.setFill(Color.GREY);
                    tile.setStroke(Color.BLACK);
                    //this will slightly round the rectangles for smoother shapes but not as round as the vehicles
                    //the regular tiles arent as rounded as goal tile on purpose
                    tile.setArcHeight(15);
                    tile.setArcWidth(15);
                }
                //add the tile to the grid pane
                gridpane.add(tile, i, j);

            }
        }

        // THIS STUFF BELOW IS HARD CODED FIX
        //vehicles.setManaged(true);
        Vehicle v = new Vehicle(true, 2);
        GridVehicle car = new GridVehicle(true, v, 2, 1);
        vehicles.getChildren().add(car);
        car.initialShift();

        Vehicle v1 = new Vehicle(false, 3);
        GridVehicle car1 = new GridVehicle(false, v1, 1, 3);

        vehicles.getChildren().add(car1);
        car1.initialShift();

        Vehicle v2 = new Vehicle(true, 2);
        GridVehicle car2 = new GridVehicle(false, v2, 4, 3);

        vehicles.getChildren().add(car2);
        car2.initialShift();

        finalBoard.getChildren().addAll(gridpane, vehicles);

        //reset the number of moves when new board is created
        GridVariables.numberOfMoves = 0;
        //return the final board containing the grid and the group of vehicles
        return finalBoard;
    }
}
