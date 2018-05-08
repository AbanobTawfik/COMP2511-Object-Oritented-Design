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
        GridGenerator g = new GridGenerator();
        //finalBoard.getChildren().addAll(vehicles);
        Scene scene = new Scene(g.generateGrid(),gridWidth,gridHeight);
        stage.setScene(scene);
        stage.setTitle("GridLock Game");
        stage.show();
    }

}