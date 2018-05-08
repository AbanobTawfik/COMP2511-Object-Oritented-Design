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
    private boolean goalCar;
    private int row;
    private int col;
    private double offsetX;
    private double offsetY;
    int boardSize = 6;
    double gridWidth = 1440;
    double gridHeight = 900;
    double tileSizeWidth = gridWidth / boardSize;
    double tileSizeHeight = gridHeight / boardSize;
    private double oldXCoordinate = row * tileSizeHeight;
    private double oldYCoordinate = col * tileSizeWidth;
    private int rowinit;
    private int colinit;
    boolean reset = false;
    boolean shift = false;


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
        System.out.println("row - " + row);
        System.out.println("col - " + col);
        if (!reset) {
            double deltaX = xNew;
            double deltaY = yNew;
            double offsetXtemp = (col) * tileSizeWidth;
            double offsetYtemp = (row) * tileSizeHeight;
            offsetX = deltaX - offsetXtemp;
            offsetY = deltaY - offsetYtemp;
            reset = true;
        }

        int tempcol = col;
        int temprow = row;
        //want to store old coordinate on grid location
        if (vehicle.isHorizontal()) {
            if (oldXCoordinate < xNew)
                col = (int) Math.ceil(((float) (xNew - offsetX) / ((float) tileSizeWidth)));

            else
                col = (int) Math.floor(((float) (xNew - offsetX) / ((float) tileSizeWidth)));


        } else {
            if (oldYCoordinate < yNew)
                row = (int) Math.ceil(((float) (yNew - offsetY) / ((float) tileSizeHeight)));
            else
                row = (int) Math.floor(((float) (yNew - offsetY) / ((float) tileSizeHeight)));
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
            setTranslateX(colDiff);
            if (col > (boardSize - vehicle.getSize())) {
                col = tempcol;
                return;
            }
            if (col < 0) {
                col = 0;
                return;
            }
        } else {
            setTranslateY(rowDiff);
            if (row > (boardSize - vehicle.getSize())) {
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
        if (vehicle.isHorizontal()) {
            for (int i = 0; i < vehicle.getSize(); i++)
                this.grid.getGrid()[row][col + i] = true;
        }
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
        this.colinit = col;
        if (!vehicle.isHorizontal()) {
            for (int i = 0; i < vehicle.getSize(); i++)
                this.grid.getGrid()[col][row + i] = true;
        }

    }

    public void initialShift() {
        double offsetXtemp = (colinit) * tileSizeWidth;
        double offsetYtemp = (rowinit) * tileSizeHeight;
        setTranslateX(offsetXtemp);
        setTranslateY(offsetYtemp);


    }

}
