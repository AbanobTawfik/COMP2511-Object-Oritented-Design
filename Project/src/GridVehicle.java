import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Translate;

import static java.lang.Thread.sleep;

//col -> width
// row -> height
public class GridVehicle extends StackPane {
    private Grid grid;
    private Vehicle vehicle;
    private int row;
    private int col;
    private double offsetX;
    private double offsetY;
    private double oldXCoordinate = row * GridVariables.tileSizeHeight;
    private double oldYCoordinate = col * GridVariables.tileSizeWidth;
    private int rowinit;
    private int colinit;
    boolean reset = false;


    public GridVehicle(double tileSizeWidth, double tileSizeHeight, boolean goalCar, Vehicle vehicle, Grid grid) {
        this.grid = grid;
        this.vehicle = vehicle;
        double vehicleSizeX = tileSizeWidth;
        double vehicleSizeY = tileSizeHeight;
        if (vehicle.isHorizontal()) {
            vehicleSizeX = (tileSizeWidth) * vehicle.getSize();
        } else
            vehicleSizeY = (tileSizeHeight) * vehicle.getSize();

        Rectangle carBlock = new Rectangle(vehicleSizeX, vehicleSizeY);
        carBlock.setStroke(Color.BLACK);
        if (goalCar) {
            carBlock.setFill(Color.RED);
        } else
            carBlock.setFill(Color.GRAY);

        getChildren().add(carBlock);
        //want to now fix the grid status
        setOnMouseDragged(event -> {
            double deltaX = event.getSceneX();
            double deltaY = event.getSceneY();
            moveCar(deltaX, deltaY);
        });

        double finalTileSizeHeight = tileSizeHeight;
        double finalTileSizeWidth = tileSizeWidth;
        setOnMouseReleased(event -> {

            double lockX = col * finalTileSizeWidth + offsetX;
            double lockY = row * finalTileSizeHeight + offsetY;
            moveCar(lockX, lockY);
            offsetX = 0;
            offsetY = 0;
            reset = false;
        });
    }

    public void moveCar(double xNew, double yNew) {
        if (!reset) {
            double deltaX = xNew;
            double deltaY = yNew;
            double offsetXtemp = (col) * GridVariables.tileSizeWidth;
            double offsetYtemp = (row) * GridVariables.tileSizeHeight;
            offsetX = deltaX - offsetXtemp;
            offsetY = deltaY - offsetYtemp;
            reset = true;
        }

        int tempcol = col;
        int temprow = row;
        //want to store old coordinate on grid location
        if (vehicle.isHorizontal()) {
            if (oldXCoordinate < xNew)
                col = (int) Math.ceil(((float) (xNew - offsetX) / ((float) GridVariables.tileSizeWidth)));

            else{
                col = (int) Math.floor(((float) (xNew - offsetX) / ((float) GridVariables.tileSizeWidth)));

            }

        } else {
            if (oldYCoordinate < yNew)
                row = (int) Math.ceil(((float) (yNew - offsetY) / ((float) GridVariables.tileSizeHeight)));
            else
                row = (int) Math.floor(((float) (yNew - offsetY) / ((float) GridVariables.tileSizeHeight)));

        }

        double rowDiff = yNew;
        double colDiff = xNew;

        if (vehicle.isHorizontal())
            oldXCoordinate = xNew;
        else
            oldYCoordinate = yNew;

        rowDiff -= offsetY;
        colDiff -= offsetX;

        if (vehicle.isHorizontal()) {
            if(col > tempcol && col+vehicle.getSize() < GridVariables.boardSize && grid.getGrid()[row][col + vehicle.getSize()-1]){
                col = tempcol;
                setTranslateX(oldXCoordinate);
                return;
            }
            if(col < tempcol && col-vehicle.getSize() >= 0 && grid.getGrid()[row][col - vehicle.getSize()+1]){
                col = tempcol;
                setTranslateX(oldXCoordinate);
                return;
            }
            setTranslateX(colDiff);
            
            for(int i = 0; i < vehicle.getSize();i++) {
                grid.getGrid()[row][tempcol + i] = false;
            }

            if (col > (GridVariables.boardSize - vehicle.getSize())) {
                col = tempcol;
                return;
            }

            if (col < 0) {
                col = 0;
                return;
            }
        } else {
            setTranslateY(rowDiff);
            if (row > (GridVariables.boardSize - vehicle.getSize())) {
                row = temprow;
                return;
            }

            if (row < 0) {
                row = 0;
                return;
            }
        }
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

    public void initialShift() {
        double offsetXtemp = (colinit) * GridVariables.tileSizeWidth;
        double offsetYtemp = (rowinit) * GridVariables.tileSizeHeight;
        setTranslateX(offsetXtemp);
        setTranslateY(offsetYtemp);
        if (!vehicle.isHorizontal()) {
            for (int i = 0; i < vehicle.getSize(); i++)
                this.grid.getGrid()[rowinit+i][colinit] = true;
        }else{
            for (int i = 0; i < vehicle.getSize(); i++)
                this.grid.getGrid()[rowinit][colinit + i] = true;
        }
    }
}
