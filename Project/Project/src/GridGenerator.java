import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GridGenerator {
    private Grid grid = new Grid();

    public Parent generateGrid(){
        Pane finalBoard = new Pane();
        finalBoard.setStyle("-fx-background-color: black");
        finalBoard.setPrefSize(GridVariables.gridWidth,GridVariables.gridHeight);


        //this is the 2d grid we will be using for GUI
        GridPane gridpane = new GridPane();
        //this is the 2d grid we will be using for GUI
        Group vehicles = new Group();
        //initalising the rows for this grid (adds a slot at each row index)
        for(int i = 0; i < GridVariables.boardSize; i++) {
            RowConstraints row = new RowConstraints();
            //this lets the nodes fill the grid tile
            row.setFillHeight(true);
            gridpane.getRowConstraints().add(row);
        }
        //initalising the columns for this grid (adds a slot at each col index)
        for(int i = 0; i < GridVariables.boardSize; i++) {
            ColumnConstraints col = new ColumnConstraints();
            //this lets the nodes fill the grid tile
            col.setFillWidth(true);
            gridpane.getColumnConstraints().add(col);
        }

        //filling our grid with tiles for the game
        for(int i = 0; i < GridVariables.boardSize; i++){
            for(int j = 0; j < GridVariables.boardSize; j++){
                //tiles are represented by a rectangle that is white with black border
                Rectangle tile = new Rectangle(GridVariables.tileSizeWidth-1.15,GridVariables.tileSizeHeight-1.15);
                if(i == GridVariables.boardSize - 1 && j == GridVariables.grid.getGoalRow()) {
                    tile.setStroke(Color.BLACK);
                    tile.setFill(Color.GREEN);
                }
                else {
                    tile.setFill(Color.WHITE);
                    tile.setStroke(Color.BLACK);
                }

                gridpane.add(tile, i, j);

            }
        }
        //vehicles.setManaged(true);
        Vehicle v = new Vehicle(true, 2);
        GridVehicle car = new GridVehicle(GridVariables.tileSizeWidth,GridVariables.tileSizeHeight,true,v,grid);
        car.setCol(1);
        car.setRow(2);
        vehicles.getChildren().add(car);
        car.initialShift();

        Vehicle v1 = new Vehicle(false,3);
        GridVehicle car1 = new GridVehicle(GridVariables.tileSizeWidth,GridVariables.tileSizeHeight,false,v1,grid);
        car1.setCol(5);
        car1.setRow(1);
        vehicles.getChildren().add(car1);
        car1.initialShift();

        Vehicle v2 = new Vehicle(true,2);
        GridVehicle car2 = new GridVehicle(GridVariables.tileSizeWidth,GridVariables.tileSizeHeight,false,v2,grid);
        car2.setCol(3);
        car2.setRow(4);
        vehicles.getChildren().add(car2);
        car2.initialShift();


        finalBoard.getChildren().addAll(gridpane,vehicles);
        return finalBoard;
    }
}
