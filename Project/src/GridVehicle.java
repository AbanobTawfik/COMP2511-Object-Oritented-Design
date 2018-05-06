import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Translate;

import static java.lang.Thread.sleep;
//col -> width
// row -> height
public class GridVehicle extends StackPane {
    private Vehicle vehicle;
    private boolean goalCar;
    private int row;
    private int col;
    private double offsetX;
    private double offsetY;
    int boardSize = 6;
    double gridWidth = 1440;
    double gridHeight = 900;
    double tileSizeWidth = gridWidth/boardSize;
    double tileSizeHeight = gridHeight/boardSize;
    private double oldXCoordinate = row*tileSizeHeight;
    private double oldYCoordinate = col*tileSizeWidth;
    private int rowinit;
    private int colinit;
    boolean reset = false;


    public GridVehicle(double tileSizeWidth, double tileSizeHeight, boolean goalCar, Vehicle vehicle)
    {

        this.vehicle = vehicle;
        double vehicleSizeX = tileSizeWidth;
        double vehicleSizeY = tileSizeHeight;
        if(vehicle.isHorizontal()) {
            vehicleSizeX = (tileSizeWidth) * vehicle.getSize();
        }
        else
            vehicleSizeY = (tileSizeHeight) * vehicle.getSize();

        Rectangle carBlock = new Rectangle(vehicleSizeX,vehicleSizeY);
        carBlock.setStroke(Color.BLACK);
        if(goalCar)
            carBlock.setFill(Color.RED);
        else
            carBlock.setFill(Color.GRAY);

        getChildren().add(carBlock);


        setOnMouseDragged(event ->{
            double deltaX = event.getSceneX();
            double deltaY = event.getSceneY();
            moveCar(deltaX,deltaY);
        });

        double finalTileSizeHeight = tileSizeHeight;
        double finalTileSizeWidth = tileSizeWidth;
        setOnMouseReleased(event -> {

            double lockX = col * finalTileSizeWidth + offsetX;
            double lockY = row * finalTileSizeHeight + offsetY;
            moveCar(lockX,lockY);
            offsetX = 0;
            offsetY = 0;
            reset = false;
        });
    }

    public void moveCar(double xNew, double yNew){
        if(!reset) {
            double deltaX = xNew;
            double deltaY = yNew;
            double offsetXtemp = (col) * tileSizeWidth;
            double offsetYtemp = (row) * tileSizeHeight;
            offsetX = deltaX - offsetXtemp;
            offsetY = deltaY - offsetYtemp;
            reset = true;
        }
        //want to store old coordinate on grid location
        if(vehicle.isHorizontal()) {
            if(oldXCoordinate < xNew)
                col = (int) Math.ceil(((float) (xNew-offsetX) / ((float) tileSizeWidth)));
            else
                col = (int) Math.floor(((float) (xNew-offsetX) / ((float) tileSizeWidth)));
        }
        else {
            if(oldYCoordinate < yNew)
                row = (int) Math.ceil(((float) (yNew - offsetY) / ((float) tileSizeHeight)));
            else
                row = (int) Math.floor(((float) (yNew - offsetY) / ((float) tileSizeHeight)));
        }

        double rowDiff = yNew;
        double colDiff = xNew;

        if(vehicle.isHorizontal())
            oldXCoordinate = xNew;
        else
            oldYCoordinate = yNew;

        System.out.println(oldYCoordinate);
        rowDiff -= tileSizeHeight*rowinit;
        colDiff -= colinit*tileSizeWidth;


        rowDiff -= offsetY;
        colDiff -= offsetX;

        if(vehicle.isHorizontal())
            setTranslateX(colDiff);
        else
            setTranslateY(rowDiff);
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
        this.rowinit = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
        this.colinit = col;
    }
}
