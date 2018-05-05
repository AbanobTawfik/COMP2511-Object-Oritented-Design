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
    private double offsetX = 0;
    private double offsetY = 0;
    int boardSize = 6;
    double gridWidth = 1440;
    double gridHeight = 900;
    double tileSizeWidth = gridWidth/boardSize;
    double tileSizeHeight = gridHeight/boardSize;
    private double oldXCoordinate = row*tileSizeHeight;
    private double oldYCoordinate = col*tileSizeWidth;
    private int rowinit;
    private int colinit;


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


        double finalTileSizeWidth = tileSizeWidth;
        double finalTileSizeHeight = tileSizeHeight;
        setOnMouseClicked(event -> {
            try{
                sleep(100);
            }catch (Exception e){
                e.printStackTrace();
            }

            double deltaX = event.getSceneX();
            double deltaY = event.getSceneY();
            double offsetXtemp = col * finalTileSizeWidth;
            double offsetYtemp = row * finalTileSizeHeight;
            offsetX = deltaX - offsetXtemp;
            offsetY = deltaY - offsetYtemp;
            System.out.println("Offset x - " + offsetX + "Offset y - " + offsetY);
        });

        setOnMouseDragged(event ->{
            double deltaX = event.getSceneX();
            double deltaY = event.getSceneY();
            moveCar(deltaX,deltaY);
        });

        double finalTileSizeHeight1 = tileSizeHeight;
        double finalTileSizeWidth1 = tileSizeWidth;
        setOnMouseReleased(event -> {
            double lockX = col * finalTileSizeWidth1;
            double lockY = row * finalTileSizeHeight1;
            moveCar(lockX,lockY);
            offsetX = 0;
            offsetY = 0;
        });
    }

    public void moveCar(double xNew, double yNew){

        //want to store old coordinate on grid location
        if(vehicle.isHorizontal())
            col = Math.round(((float)(xNew)/((float)tileSizeWidth)));
        else
            row = Math.round(((float)(yNew)/((float)tileSizeHeight)));


        double rowDiff = yNew;
        double colDiff = xNew;

        if(vehicle.isHorizontal())
            oldXCoordinate = xNew;
        else
            oldYCoordinate = yNew;

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
