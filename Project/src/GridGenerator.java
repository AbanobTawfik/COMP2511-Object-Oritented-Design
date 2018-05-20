import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.util.Random;

/**
 * The Grid Generator class, which will create a new board which contains
 * a grid with tiles (visual tiles)
 * and a group of vehicles that can be moved
 */
public class GridGenerator {
    private int streakCounter = 0;
    //this will be the grid representing the state (passed into vehicles)
    private Grid grid = new Grid();
    //this will be the main game
    private Pane pane = new Pane();
    private VBox menu = new VBox(30);
    private Group vehicles = new Group();
    private Label counter = new Label("Move count - " + grid.getNumberOfMoves());
    private Label streak = new Label("Current Streak - " + streakCounter);
    private Label EstimatedMoves = new Label();
    private GridPane gridPane = new GridPane();
    private VBox menubackground = new VBox();

    /**
     * Generates a grid with a board of tiles, and a group of vehicles. This method
     * will guarantee to return a Parent which will be used as a root for the scene it is provided
     * to.
     *
     * @return the root used for the scene
     */
    public Parent generateGrid() {
        //this will be the Parent used for the scene
        //sets the background to black
        menubackground.setStyle("-fx-background-color: rgba(50, 50, 50, 0.5); -fx-background-radius: 10;opacity: 0.5;");
        menubackground.setMinHeight(GridVariables.GRID_HEIGHT);
        menubackground.setMinWidth(GridVariables.GRID_WIDTH);
        pane.setStyle("-fx-background-color: black");
        //set the size of the parent to the resolution in the global grid variables.
        pane.setPrefSize(GridVariables.GRID_WIDTH, GridVariables.GRID_HEIGHT);
        //this is the 2d grid we will be using for GUI
        //this is the group which will hold the vehicles on the board
        //this will be the main menu used for the game, i will set it up so it will open on 2 conditions
        //if the goal state is met
        //or the user presses the esc key
        //this will be the title for the menu
        menu.setId("Main Menu");
        menu.relocate(GridVariables.GRID_WIDTH / 2 - 50, GridVariables.GRID_HEIGHT / 3 - 50);
        Button easy = new Button("Easy Level");
        easy.getStylesheets().add("Buttons.css");
        easy.setOnAction(e -> {
            grid.setDifficulty(Difficulty.EASY);
            generateEasyGrid();
            menubackground.toFront();
            menu.toFront();
            streakCounter = 0;
            updateStreakGUI();
        });
        Button medium = new Button("Medium Level");
        medium.getStylesheets().add("Buttons.css");
        medium.setOnAction(e -> {
            grid.setDifficulty(Difficulty.MEDIUM);
            generateMediumGrid();
            menubackground.toFront();
            menu.toFront();
            streakCounter = 0;
            updateStreakGUI();
        });
        Button hard = new Button("Hard Level");
        hard.getStylesheets().add("Buttons.css");
        hard.setOnAction(e -> {
            grid.setDifficulty(Difficulty.HARD);
            generateHardGrid();
            menubackground.toFront();
            menu.toFront();
            streakCounter = 0;
            updateStreakGUI();
        });
        Button exit = new Button("Exit");
        exit.getStylesheets().add("Buttons.css");
        exit.setOnAction(e -> {
            System.exit(0);
        });
        menu.getChildren().addAll(easy, medium, hard, exit);
        //menu.
        //initalising the rows for this grid (adds a slot at each row index)
        for (int i = 0; i < GridVariables.BOARD_SIZE; i++) {
            //initalises rows for the grid pane
            RowConstraints row = new RowConstraints();
            //this lets the nodes fill the grid tile
            row.setFillHeight(true);
            gridPane.getRowConstraints().add(row);
        }
        //initalising the columns for this grid (adds a slot at each col index)
        for (int i = 0; i < GridVariables.BOARD_SIZE; i++) {
            //initalises the columns for the grid pane
            ColumnConstraints col = new ColumnConstraints();
            //this lets the nodes fill the grid tile
            col.setFillWidth(true);
            gridPane.getColumnConstraints().add(col);
        }

        //filling our grid with tiles for the game
        for (int i = 0; i < GridVariables.BOARD_SIZE; i++) {
            for (int j = 0; j < GridVariables.BOARD_SIZE; j++) {
                //tiles are represented by a rectangle that is white with black border
                //the -1.15 is so the rectangle fits better with the vehicles
                Rectangle tile = new Rectangle(GridVariables.TILE_SIZE_WIDTH - 1.15, GridVariables.TILE_SIZE_HEIGHT - 1.15);
                if (i == GridVariables.BOARD_SIZE - 1 && j == grid.getGoalRow()) {
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
                gridPane.add(tile, i, j);

            }
        }
        counter.setTextFill(Color.CADETBLUE);
        counter.setFont(new Font("Impact", 50));
        counter.relocate(0, GridVariables.GRID_HEIGHT + 5);
        counter.setBorder(new Border(new BorderStroke(Color.WHITE,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        streak.setTextFill(Color.CADETBLUE);
        streak.setFont(new Font("Impact", 50));
        streak.relocate(GridVariables.GRID_WIDTH / 3 - (GridVariables.GRID_WIDTH / 12), GridVariables.GRID_HEIGHT + 5);
        streak.setBorder(new Border(new BorderStroke(Color.WHITE,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        EstimatedMoves.setTextFill(Color.CADETBLUE);
        EstimatedMoves.setFont(new Font("Impact", 50));
        EstimatedMoves.relocate(GridVariables.GRID_WIDTH / 3 + (GridVariables.GRID_WIDTH / 4.5), GridVariables.GRID_HEIGHT + 5);
        EstimatedMoves.setBorder(new Border(new BorderStroke(Color.WHITE,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        pane.getChildren().addAll(gridPane, counter, streak, EstimatedMoves);
        generateEasyGrid();
        //return the final board containing the grid and the group of vehicles
        return pane;
    }

    /**
     * Toggle menu.
     */
    public void toggleMenu() {
        if (pane.getChildren().contains(menu)) {
            pane.getChildren().remove(menu);
            pane.getChildren().remove(menubackground);
            gridPane.setStyle(null);
            vehicles.setStyle(null);
        } else if (!pane.getChildren().contains(menu)) {
            pane.getChildren().add(menubackground);
            menubackground.toFront();
            pane.getChildren().add(menu);
            menu.toFront();
        } else
            return;
    }

    /**
     * Clear grid.
     */
    public void clearGrid() {
        grid.setVictory(false);
        grid.resetNewGrid();
        vehicles.getChildren().clear();
    }

    /**
     * When generating a
     */
    /*
     * when generating a easy grid we want to
     * 1. place the goal car close towards the exit (random between col 3-4)
     * 2. for easy cars we will have between 7-10 other cars
     * 3. randomly place vehicles in random size and orientation on the board
     * 4. make sure placement is so that no block will overlap on placement
     * 5. perform djikstra's to try and solve the board state
     * 6. count number of moves from djikstra (MUST BE LESS THAN 6)
     * 7. if the djikstra returns a solution less than 6, we use that board
     * 8. otherwise we will keep generating board repeating step 3-7
     */
    public void generateEasyGrid() {
        grid.setNumberOfMoves(0);
        Difficulty difficulty = Difficulty.EASY;
        grid.setDifficulty(difficulty);
        pane.getChildren().remove(vehicles);
        clearGrid();
        vehicles.getChildren().clear();
        Random generator = new Random();
        Vehicle goalCar = new Vehicle(true, 2);
        GridVehicle goalVehicle = new GridVehicle(true, goalCar, 2, generator.nextInt(3), grid);
        vehicles.getChildren().add(goalVehicle);
        goalVehicle.initialShift();
        boolean result = false;
        int numberOfVehicles = generator.nextInt(17) + 4;
        BFS search = new BFS();
        while (!result) {
            clearGrid();
            result = search.createState(difficulty, numberOfVehicles, grid, vehicles, goalVehicle);
        }
        pane.getChildren().add(vehicles);
        EstimatedMoves.setText("max moves needed - " + Integer.toString(search.getNumberOfMovesEstimate()));

    }

    /**
     * Generate medium grid.
     */
    public void generateMediumGrid() {
        grid.setNumberOfMoves(0);
        Difficulty difficulty = Difficulty.MEDIUM;
        grid.setDifficulty(difficulty);
        pane.getChildren().remove(vehicles);
        clearGrid();

        Random generator = new Random();
        Vehicle goalCar = new Vehicle(true, 2);
        GridVehicle goalVehicle = new GridVehicle(true, goalCar, 2, generator.nextInt(3), grid);
        boolean result = false;
        int numberOfVehicles = generator.nextInt(17) + 4;
        BFS search = new BFS();
        while (!result) {
            clearGrid();
            result = search.createState(difficulty, numberOfVehicles, grid, vehicles, goalVehicle);
        }
        pane.getChildren().add(vehicles);
        EstimatedMoves.setText("max moves needed - " + Integer.toString(search.getNumberOfMovesEstimate()));
    }

    /**
     * Generate hard grid.
     */
    public void generateHardGrid() {
        grid.setNumberOfMoves(0);
        Difficulty difficulty = Difficulty.HARD;
        grid.setDifficulty(difficulty);
        pane.getChildren().remove(vehicles);
        clearGrid();

        Random generator = new Random();
        Vehicle goalCar = new Vehicle(true, 2);
        GridVehicle goalVehicle = new GridVehicle(true, goalCar, 2, generator.nextInt(3), grid);
        vehicles.getChildren().add(goalVehicle);
        boolean result = false;
        int numberOfVehicles = generator.nextInt(17) + 4;
        BFS search = new BFS();
        while (!result) {
            clearGrid();
            result = search.createState(difficulty, numberOfVehicles, grid, vehicles, goalVehicle);
        }
        EstimatedMoves.setText("max moves needed - " + Integer.toString(search.getNumberOfMovesEstimate()));
        pane.getChildren().add(vehicles);

    }

    public void reGenerate() {
        if (null == grid.getDifficulty()) {
            generateEasyGrid();
            return;
        }
        if (grid.getDifficulty().equals(Difficulty.EASY)) {
            generateEasyGrid();
            return;
        }
        if (grid.getDifficulty().equals(Difficulty.MEDIUM)) {
            generateMediumGrid();
            return;
        }
        if (grid.getDifficulty().equals(Difficulty.HARD)) {
            generateHardGrid();
            return;
        }

    }

    public Grid getGrid() {
        return grid;
    }

    public void updateCounter() {
        counter.setText("Move count - " + grid.getNumberOfMoves());
    }

    public void updateStreakGUI() {
        streak.setText("Current Streak - " + streakCounter);
    }

    public void updateStreak() {
        streakCounter++;
    }
}
