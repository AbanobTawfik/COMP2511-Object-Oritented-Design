import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Translate;

public class GridVehicle extends StackPane {
    private Vehicle vehicle;
    private boolean goalCar;
    private int row;
    private int col;
    private double mouseXCoordinate;
    private double mouseYCoordinate;


    public GridVehicle(double tileSizeWidth, double tileSizeHeight, boolean goalCar, Vehicle vehicle)
    {

        this.vehicle = vehicle;
        if(vehicle.isHorizontal()) {
            tileSizeWidth = (tileSizeWidth-1.5) * vehicle.getSize();
        }
        else
            tileSizeHeight = (tileSizeHeight-1.5) * vehicle.getSize();

        Rectangle carBlock = new Rectangle(tileSizeWidth,tileSizeHeight);
        carBlock.setStroke(Color.BLACK);
        if(goalCar)
            carBlock.setFill(Color.RED);
        else
            carBlock.setFill(Color.GRAY);

        getChildren().add(carBlock);
        setOnMouseClicked(event -> {
            mouseXCoordinate = event.getSceneX();
            mouseYCoordinate = event.getScreenY();
        });
        setOnMouseDragged(event ->{

        });
        setOnMouseDragReleased(event ->{

        });
    }

    public void moveCar(int rowNew, int colNew){

        //want to store old coordinate on grid location
        double rowDiff = (rowNew-row)*(tileSizeHeight);
        double colDiff = (colNew-col) * (tileSizeWidth);
        row = rowNew;
        col = colNew;
        setTranslateX(colDiff);
        setTranslateY(rowDiff);
    }

    public void moveVehicleOnClick(){

    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
        this.XCoordinate = row *
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }
}
