import javafx.scene.*;
import javafx.application.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;


public class GridLock extends Application{
    private Grid grid = new Grid();
    private int boardSize = grid.getBoardSize();
    private double gridWidth = 1440;
    private double gridHeight = 900;
    private double tileSizeWidth = gridWidth/boardSize;
    private double tileSizeHeight = gridHeight/boardSize;

    public static void main(String args[]){
        launch(args);
    }




    @Override
    public void start(Stage stage){
        Pane finalBoard = new Pane();
        finalBoard.setStyle("-fx-background-color: black");
        finalBoard.setPrefSize(gridWidth,gridHeight);


        //this is the 2d grid we will be using for GUI
        GridPane gridpane = new GridPane();
        //this is the 2d grid we will be using for GUI
        GridPane vehicles = new GridPane();
        //initalising the rows for this grid (adds a slot at each row index)
        for(int i = 0; i < boardSize; i++) {
            RowConstraints row = new RowConstraints();
            //this lets the nodes fill the grid tile
            row.setFillHeight(true);
            vehicles.getRowConstraints().add(row);
            gridpane.getRowConstraints().add(row);
        }
        //initalising the columns for this grid (adds a slot at each col index)
        for(int i = 0; i < boardSize; i++) {
            ColumnConstraints col = new ColumnConstraints();
            //this lets the nodes fill the grid tile
            col.setFillWidth(true);
            vehicles.getColumnConstraints().add(col);
            gridpane.getColumnConstraints().add(col);
        }
        //filling our grid with tiles for the game
        for(int i = 0; i < boardSize; i++){
            for(int j = 0; j < boardSize; j++){
                //tiles are represented by a rectangle that is white with black border
                Rectangle tile = new Rectangle(tileSizeWidth-2.5,tileSizeHeight-2.5);
                if(i == grid.getBoardSize()-1 && j == grid.getGoalRow()) {
                    tile.setStroke(Color.BLACK);
                    tile.setFill(Color.GREEN);
                }
                else {
                    tile.setFill(Color.WHITE);
                    tile.setStroke(Color.BLACK);
                }

                gridpane.add(tile, i, j);

                //tiles are represented by a rectangle that is white with black border
                tile = new Rectangle(tileSizeWidth-2.5,tileSizeHeight-2.5);
                if(i == grid.getBoardSize()-1 && j == grid.getGoalRow()) {
                    tile.setStroke(Color.TRANSPARENT);
                    tile.setFill(Color.TRANSPARENT);
                }
                else {
                    tile.setFill(Color.TRANSPARENT);
                    tile.setStroke(Color.TRANSPARENT);
                }

                vehicles.add(tile, i, j);
            }
        }

        Vehicle v = new Vehicle(true, 2);
        GridVehicle car = new GridVehicle(tileSizeWidth,tileSizeHeight,true,v);
        car.setCol(0);
        car.setRow(5);
        vehicles.add(car, car.getCol(), car.getRow());
        //car.moveCar(2,2);


        finalBoard.getChildren().addAll(gridpane,vehicles);
        //finalBoard.getChildren().addAll(vehicles);
        Scene scene = new Scene(finalBoard,gridWidth,gridHeight);
        stage.setScene(scene);
        stage.setTitle("GridLock Game");
        stage.show();
    }
}