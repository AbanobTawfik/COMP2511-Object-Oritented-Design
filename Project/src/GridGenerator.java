import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
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
        //sets the background to black
        GridVariables.root.setStyle("-fx-background-color: black");
        //set the size of the parent to the resolution in the global grid variables.
        GridVariables.root.setPrefSize(GridVariables.GRID_WIDTH, GridVariables.GRID_HEIGHT);
        //this is the 2d grid we will be using for GUI
        GridPane gridpane = new GridPane();
        //this is the group which will hold the vehicles on the board
        Group vehicles = new Group();
        //this will be the main menu used for the game, i will set it up so it will open on 2 conditions
        //if the goal state is met
        //or the user presses the esc key
        //this will be the title for the menu
        GridVariables.menu.setId("Main Menu");
        GridVariables.menu.getChildren().addAll(new Button("Easy Level"), new Button("Not as Meme"), new Button("Turn Back"));
        //initalising the rows for this grid (adds a slot at each row index)
        for (int i = 0; i < GridVariables.BOARD_SIZE; i++) {
            //initalises rows for the grid pane
            RowConstraints row = new RowConstraints();
            //this lets the nodes fill the grid tile
            row.setFillHeight(true);
            gridpane.getRowConstraints().add(row);
        }
        //initalising the columns for this grid (adds a slot at each col index)
        for (int i = 0; i < GridVariables.BOARD_SIZE; i++) {
            //initalises the columns for the grid pane
            ColumnConstraints col = new ColumnConstraints();
            //this lets the nodes fill the grid tile
            col.setFillWidth(true);
            gridpane.getColumnConstraints().add(col);
        }

        //filling our grid with tiles for the game
        for (int i = 0; i < GridVariables.BOARD_SIZE; i++) {
            for (int j = 0; j < GridVariables.BOARD_SIZE; j++) {
                //tiles are represented by a rectangle that is white with black border
                //the -1.15 is so the rectangle fits better with the vehicles
                Rectangle tile = new Rectangle(GridVariables.TILE_SIZE_WIDTH - 1.15, GridVariables.TILE_SIZE_HEIGHT - 1.15);
                if (i == GridVariables.BOARD_SIZE - 1 && j == GridVariables.g.getGoalRow()) {
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

        Vehicle v3 = new Vehicle(true, 2);
        GridVehicle car3 = new GridVehicle(false, v3, 0, 0);

        vehicles.getChildren().add(car3);
        car3.initialShift();

        Vehicle v4 = new Vehicle(true, 2);
        GridVehicle car4 = new GridVehicle(false, v4, 0, 3);

        vehicles.getChildren().add(car4);
        car4.initialShift();

        Vehicle v5 = new Vehicle(true, 2);
        GridVehicle car5 = new GridVehicle(false, v5, 4, 0);

        vehicles.getChildren().add(car5);
        car5.initialShift();

        Vehicle v6 = new Vehicle(false, 2);
        GridVehicle car6 = new GridVehicle(false, v6, 1, 4);
        vehicles.getChildren().add(car6);
        car6.initialShift();

        GridVariables.root.getChildren().addAll(gridpane, vehicles);
        //reset the number of moves when new board is created
        GridVariables.NUMBER_OF_MOVES = 0;
        //return the final board containing the grid and the group of vehicles
        GridVariables.root.setManaged(false);
        return GridVariables.root;
    }


}
