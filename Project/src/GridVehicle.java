import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;

import java.util.ArrayList;

public class GridVehicle extends StackPane {
    private Grid grid;
    private Vehicle vehicle;
    private int row;
    private int col;
    private int rowinit;
    private int colinit;
    private double beginX;
    private double beginY;
    private int lastX;
    private int lastY;
    private ArrayList<Pair<Integer,Integer>> coordinatesBlocked = new ArrayList<>();
    private boolean flag = true;

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
        setOnMousePressed(event -> {
            beginX = event.getSceneX();
            beginY = event.getSceneY();
            lastValidPosition(getLayoutX(),getLayoutY());
        });
        setOnMouseDragged(event -> {
            moveCar(event.getSceneX(), event.getSceneY());
        });


        setOnMouseReleased(event -> {
            flag = true;
            if(vehicle.isHorizontal()) {
                relocate(lastX * GridVariables.tileSizeWidth, row * GridVariables.tileSizeHeight);
            }else{
                relocate(col*GridVariables.tileSizeWidth, lastY * GridVariables.tileSizeHeight);
            }

            GridVehicle[][] g = grid.getGrid();
            for(Pair<Integer,Integer> coordinates : coordinatesBlocked){
                g[coordinates.getKey()][coordinates.getValue()] = null;
            }
            coordinatesBlocked.clear();
            if (vehicle.isHorizontal()) {
                for (int i = 0; i < vehicle.getSize(); i++) {
                    coordinatesBlocked.add(new Pair<>(lastX+i,lastY));
                    this.grid.getGrid()[lastX + i][lastY] = this;
                }
            }else{
                for (int i = 0; i < vehicle.getSize(); i++) {
                    coordinatesBlocked.add(new Pair<>(lastX,lastY+i));
                    this.grid.getGrid()[lastX][lastY + i] = this;
                }
            }
        });
    }


    public boolean lastValidPosition(double x, double y){
        int tilewidth = (int) GridVariables.tileSizeWidth;
        int posI = (int) (x+GridVariables.tileSizeWidth/vehicle.getSize());
        int resultCol = posI/tilewidth;
        if(posI < 0){
            return false;
        }

        int tileHeight = (int) GridVariables.tileSizeHeight;
        int posI2 = (int) (y+GridVariables.tileSizeHeight/vehicle.getSize());
        int resultRow = posI2/tileHeight;
        if(posI2 < 0){
            return false;
        }
        System.out.println(isValidMove(resultCol,resultRow));
        if(isValidMove(resultCol,resultRow)){
            lastX = resultCol;
            lastY = resultRow;
            return true;
        }
        if(!isValidMove(resultCol,resultRow))
            flag = false;

        return false;
    }

    public boolean isValidMove(int col, int row){


        GridVehicle[][]  g = grid.getGrid();
        if (vehicle.isHorizontal()) {
            for (int i = 0; i < vehicle.getSize(); i++) {
                if(col+i >= g.length){
                    return false;
                }
                if(g[col+i][row] != this && null != g[col+i][row])
                    return false;
            }
        }else{
            for (int i = 0; i < vehicle.getSize(); i++) {
                if(row+i >= g.length){
                    return false;
                }
                if(g[col][row+i] != this && null != g[col][row+i])
                    return false;
            }
        }
        return true;
    }

    public void moveCar(double xNew, double yNew) {

        if(flag && vehicle.isHorizontal() && lastValidPosition(xNew-beginX + getLayoutX(),(row)*GridVariables.tileSizeHeight)) {
            relocate(xNew - beginX + getLayoutX(), (row) * GridVariables.tileSizeHeight);
            beginX = xNew;
        }
        else{
            if(flag && (!vehicle.isHorizontal()) && lastValidPosition((col)*GridVariables.tileSizeWidth,yNew-beginY + getLayoutY())){
               relocate((col)*GridVariables.tileSizeWidth,yNew - beginY + getLayoutY());
                beginY = yNew;
            }
        }

    }


    public void setRow(int row) {
        this.row = row;
        this.rowinit = row;
    }


    public void setCol(int col) {
        this.col = col;
        this.colinit = col;
    }

    public void initialShift() {
        double offsetXtemp = (colinit) * GridVariables.tileSizeWidth;
        double offsetYtemp = (rowinit) * GridVariables.tileSizeHeight;
        relocate(offsetXtemp,offsetYtemp);
        if (vehicle.isHorizontal()) {
            for (int i = 0; i < vehicle.getSize(); i++) {
                coordinatesBlocked.add(new Pair<>(colinit+i,rowinit));
                this.grid.getGrid()[colinit + i][rowinit] = this;
            }
        }else{
            for (int i = 0; i < vehicle.getSize(); i++) {
                coordinatesBlocked.add(new Pair<>(colinit,rowinit+i));

                this.grid.getGrid()[colinit][rowinit + i] = this;
            }
        }
    }
}
