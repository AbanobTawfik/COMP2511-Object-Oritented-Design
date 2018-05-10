import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;

import java.util.ArrayList;

/**
 * This class will be used for front end. it will be the movable "sprite" rectangle btw
 * which will keep track of where it is on the grid and where it is on the screen and which positions it is blocking
 * and regularly update it on the board.
 * A grid vehicle is given the light blue to signify it is the goal vehicle (stands out)
 * and other cars will be given black to signify obstacle (high contrast)
 * the rectangle for the vehicle on grid is based on the orientation of the vehicle supplied
 * it is x tiles wide, if its horizontal where x is the vehicle size.
 * it is y tiles wide, if its vertical where y is the vehicle size.
 */
public class GridVehicle extends StackPane {
    //the vehicle for the Grid
    private Vehicle vehicle;
    //the row where the car is located on the grid (backend)
    private int row;
    //the column where the car is located on the grid (backend)
    private int col;
    //the x coordinate of the initial click to offset in the X of the scene
    private double initialClickOffsetInX;
    //the y coordinate of the initial click to offset in the y of the scene
    private double initialClickOffsetInY;
    //the last valid row where the vehicle has moved while dragged (before collision or out of bounds)
    private int lastValidColumn;
    //the last valid column where the vehicle has moved while dragged (before collision or out of bounds)
    private int lastValidRow;
    //this list will store the pair i.e <row,col> where the car has occupied to allow to keep track
    //in the backend, where the vehicle is
    private ArrayList<Pair<Integer, Integer>> coordinatesBlocked = new ArrayList<>();
    //this flag will be used as a check for when the car initially collides with another, i.e invalid move
    private boolean flag = true;

    /**
     * This function will create a grid vehicle which handles both front and backend. this function will create
     * a rectangle based on the vehicle orientation (if its horizontal tall and skinny, if its vertical fat and short)
     * the rectangle will have properties on click/drag/release to move it around the grid, and these events will update
     * the back end whilst updating the front end.
     * this method will gurantee to place a vehicle on the grid which can be moved whilst still updating the back end,
     * provided that it is given a boolean for whether it is the goal car, a vehicle that is non null, and a valid integer
     * for the row and column it is placed in.
     *
     * @param goalCar true or false signifying that the gridvehicle is the goal car
     * @param vehicle the vehicle which contains the base orientation and size
     * @param row     the row which the vehicle is initally placed
     * @param col     the column where the vehicle is initially placed
     */
    public GridVehicle(boolean goalCar, Vehicle vehicle, int row, int col) {
        //sets the vehicle for the GridVehicle
        this.vehicle = vehicle;
        //sets the initial row for the GridVehicle
        this.row = row;
        //sets the initial column for the GridVehicle
        this.col = col;
        //initiallising the width of the rectangle (pixels in x direction)
        double vehicleSizeX = GridVariables.tileSizeWidth;
        //initiallising the height of the rectangle (pixels in y direction)
        double vehicleSizeY = GridVariables.tileSizeHeight;
        //if the vehicle is horizontal, we set it's pixel size in x direction to
        //vehicle size * tile size in x
        if (vehicle.isHorizontal()) {
            vehicleSizeX = (GridVariables.tileSizeWidth) * vehicle.getSize();
        }
        //otherwise it's vertical and do the converse, set it's pixel size in y direction to
        //vehicle size * tile size in y direction
        else
            vehicleSizeY = (GridVariables.tileSizeHeight) * vehicle.getSize();
        //creating the vehicle as rectangle with size as calculated above
        Rectangle carBlock = new Rectangle(vehicleSizeX, vehicleSizeY);
        //thicker stroke so the blocks are visually more seperated, white stroke on black vehicles
        carBlock.setStrokeWidth(2);
        carBlock.setStroke(Color.WHITE);
        //if the car is the goal car want to set it to more light blueish colour (idk personal prefernece)
        if (goalCar) {
            carBlock.setFill(Color.CADETBLUE);
        }
        //if the car is not the goal car we want to set it Black (obstacle??) idk looks like blackhole on board
        else
            carBlock.setFill(Color.BLACK);

        carBlock.setArcHeight(70);
        carBlock.setArcWidth(70);
        //add the car to stack pane object (node) added
        getChildren().add(carBlock);
        //this is the event handler for click (pressing down on mouse)
        //since the object's origin is at the top left corner, we want to avoid initial jumps from clicking off
        //origin, to do this we keep track off where the initial click is
        setOnMousePressed(event -> {
            //initial offset (where the mouse clicked pixels on scene) in the X direction
            initialClickOffsetInX = event.getSceneX();
            //initial offset (where the mouse clicked pixels on scene) in the Y direction
            initialClickOffsetInY = event.getSceneY();
            //will set the last valid row and column position as the objects initial origin (to avoid null errors and snapping errors)
            lastValidPosition(getLayoutX(), getLayoutY());
            //to show a vehicle is selected the outline of the rectangle becomes a nice red and becomes
            //thicker
            carBlock.setStroke(Color.INDIANRED);
            carBlock.setStrokeWidth(5);
        });
        //this is the event handler for dragging (pressed down continuously and move move in x/y direction)
        setOnMouseDragged(event -> {
            //relocates the vehicle based on cursor displacement from initial cursor position strictly
            //in x direction for horizontal vehicles, or y direction for vertical vehicles
            moveCar(event.getSceneX(), event.getSceneY());
            //to show a vehicle is selected the outline of the rectangle becomes a nice red and becomes
            //thicker
            carBlock.setStroke(Color.INDIANRED);
            carBlock.setStrokeWidth(5);
        });
        //this is the event handler for when the mouse click is released
        //it will snap the object back to the last valid row + col as calculated from when the car is being dragged
        //and the initial click
        setOnMouseReleased(event -> {
            //resets the obstruction flag in case the vehicle has collided (important in the drag function)
            //once vehicle is collided this stays false till release, to avoid displacement over into another block
            //avoids jumping over obstacle cars
            flag = true;
            //if the vehicle is horizontal we want to snap it HORIZONTALLY no vertical snapping
            if (vehicle.isHorizontal())
                //snaps the vehicle back to the last valid column and row (lastValidColumn and lastValidRow)
                //when horizontal we only want to snap in x coordinate based on last valid x only IGNORE Y
                relocate(lastValidColumn * GridVariables.tileSizeWidth, row * GridVariables.tileSizeHeight);
            else
                //snaps the vehicle back to the last valid column and row (lastValidColumn and lastValidRow)
                //when vertical we only want to snap in Y coordinate based on last valid Y only IGNORE X
                relocate(col * GridVariables.tileSizeWidth, lastValidRow * GridVariables.tileSizeHeight);
            //update the back end grid, will free the old coordinates blocked by vehicle
            //and then it will set the new coordinates blocked based on the column + row it snapped to and vehicle size
            updateGrid(lastValidColumn, lastValidRow);
            //increment the number of moves performed counter by 1
            GridVariables.numberOfMoves++;
            System.out.println(GridVariables.numberOfMoves);
            //thicker stroke so the blocks are visually more seperated, white stroke on black vehicles
            //when the vehicle is released it goes back to white stroke
            carBlock.setStrokeWidth(2);
            carBlock.setStroke(Color.WHITE);
        });
    }

    /**
     * this function will check and update the last valid row + column coordinate, it will update the coordinates
     * and return true if the move attempted is valid or false if there is an obstruction in the given x and y pixels.
     * This function expects a valid double representing cursor offset in both x and y, and will guarantee to update
     * the last valid row + column, and return true if the new updated coordinates were valid, or false if it was an invalid move.
     *
     * @param x the x coordinate based on cursor displacement in the x plane from initial click
     * @param y the y coordinate based on cursor displacement in the y plane from initial click
     * @return true if the attempted move has no obstruction, or false if the move is invalid (obstructed)
     */
    /*
     * this method will check if the pixel move (offset from original cursor click how much mouse moved accross)
     * is a valid movement, i.e the back end row + column index when released (snaps) into a empty tile
     */
    public boolean lastValidPosition(double x, double y) {
        //since we are doing integer divison, cast tilesizewidth to integer
        int tilewidth = (int) GridVariables.tileSizeWidth;
        //the updated column position is the offset + the pixel coordinate for the FIRST block in the vehicle
        //if we dont divide by vehicle size it will pick the end block for the vehicle
        int posI = (int) (x + GridVariables.tileSizeWidth / vehicle.getSize());
        //the resulting move column is the updated coordinate origin column (top left corner)  divided by the width of the column
        int resultCol = posI / tilewidth;
        //if this column is in a negative index FALSE INVALID MOVE, this will also set boundary at 0, so it cannot go
        //into negative board space
        if (posI < 0) {
            return false;
        }
        //since we are doing integer divison, cast tileSizeHeight to integer
        int tileHeight = (int) GridVariables.tileSizeHeight;
        //the updated row position is the offset + the pixel coordinate for the FIRST block in the vehicle
        //if we dont divide by vehicle size it will pick the end block for the vehicle
        int posI2 = (int) (y + GridVariables.tileSizeHeight / vehicle.getSize());
        //the resulting move row is the updated coordinate origin row (top left corner) divided by the height of the row
        int resultRow = posI2 / tileHeight;
        //if this row is in a negative index, like above. it sets the boundary at 0 for row, cannot go up into negative space
        if (posI2 < 0) {
            return false;
        }
        //now we are checking the back end board for valid move
        //if the resulting move col + row (the row and column the object will be snapped in) is valid
        if (isValidMove(resultCol, resultRow)) {
            //we want to update the last valid column
            lastValidColumn = resultCol;
            //update the last valid row
            lastValidRow = resultRow;
            //return true since it was a valid move
            return true;
        }
        //if the move was invalid, we want to set our flag as false so the object can no longer move
        if (!isValidMove(resultCol, resultRow))
            //set the flag as false
            flag = false;
        //also return false (invalid moves)
        return false;
    }

    /**
     * this method will check if there is an obstruction (object) in the updated column and row for the attempted move
     * this is the backend check for if there is an object obstruction. this method will guarantee to return true if there
     * are no obstructions, and false if otherwise, provided it receives valid integers for the row and column index to check
     *
     * @param col the column index for the attempted move
     * @param row the row index for the attempted move
     * @return true if the index is either null or the current object itself (since the vehicle spans), false if there is an object in that index
     */
    /*
     * there are 2 checks, one is if the vehicle is horizxontal, and the other is if the vehicle is vertical
     */
    public boolean isValidMove(int col, int row) {
        //if the object is horizontal
        if (vehicle.isHorizontal()) {
            //scan through matrix based on vehicle size
            for (int i = 0; i < vehicle.getSize(); i++) {
                //to avoid mull pointer issue and bound the car to the board (so it doesnt go over boardsize)
                //if the attempted move is outside board RETURN FALSE
                if (col + i >= GridVariables.grid.length) {
                    return false;
                }
                //this is the main check
                //If ANY block of the current object moved across collides with another. i.e.
                //the updated coordinates dont contain any object, or the current object itself (cant collide with itself)
                //if the object slided across does not have any objects in the way beside itself is the main check
                if (GridVariables.grid[col + i][row] != this && null != GridVariables.grid[col + i][row])
                    return false;
            }
        }
        //if the object is vertical
        else {
            //scan through matrix based on vehicle size
            for (int i = 0; i < vehicle.getSize(); i++) {
                //to avoid mull pointer issue and bound the car to the board (so it doesnt go over boardsize)
                //if the attempted move is outside board RETURN FALSE
                if (row + i >= GridVariables.grid.length) {
                    return false;
                }
                //similair to above
                //this is the main check
                //If ANY block of the current object moved across collides with another. i.e.
                //the updated coordinates dont contain any object, or the current object itself (cant collide with itself)
                //if the object slided across does not have any objects in the way beside itself is the main check
                if (GridVariables.grid[col][row + i] != this && null != GridVariables.grid[col][row + i])
                    return false;
            }
        }
        //default return true, that means the scan didn't fail for the shifted block
        return true;
    }

    /**
     * This method will update the back end on an attempted move, once the mouse is released and the vehicle
     * is snapped into the row and column, it will free the old row + column (backend) where the object was previously
     * and it will store the object in the new row and column provided.
     * This method guarantees to free the current coordinates occupied from the grid by nulling the row column pair index
     * and it will guarantee to place the object in the grid (backend) at the row and column given, provided it is supplied
     * with a valid non zero integer
     *
     * @param column the column where the object is located at after it is snapped
     * @param roww   the roww where the object is lovated at after it is snapped
     */
    /*
     * this function will update the back end grid based on where the row and column of the object. this is called
     * initially when the object is shifted from the top left origin, and when the cursor is released
     */
    public void updateGrid(int column, int roww) {
        //scan through the coordinates blocked by the current object
        for (Pair<Integer, Integer> coordinates : coordinatesBlocked) {
            //clear up on the back end grid teh column + row index in matrix where the object is located
            GridVariables.grid[coordinates.getKey()][coordinates.getValue()] = null;
        }
        //clear the list of coordinates blocked so we can properly updated
        coordinatesBlocked.clear();
        //if the vehicle is vertical/horizontal the update will be different so,
        //if the vehicle is hoirzontal
        if (vehicle.isHorizontal()) {
            //scan through the based on vehicle size
            for (int i = 0; i < vehicle.getSize(); i++) {
                //add the column + row index into the coordinate blocked pair
                coordinatesBlocked.add(new Pair<>(column + i, roww));
                //set the index at the column + row to be equal to the current object to update board
                //the row is always same since it doesnt change since the object is horizontal only horizontal movement
                GridVariables.grid[column + i][roww] = this;
            }
        }
        //if the vehicle is vertical
        else {
            //scan through the based on vehicle size
            for (int i = 0; i < vehicle.getSize(); i++) {
                //add the column + row index into the coordinate blocked pair
                coordinatesBlocked.add(new Pair<>(column, roww + i));
                //set the index at the column + row to be equal to the current object to update board
                //the column is always same since it doesnt change since vertical, only moves vertically
                GridVariables.grid[column][roww + i] = this;
            }
        }
    }

    /**
     * This method will relocate an object on the grid pane based on the objects orientation, and the displacement
     * from the mouse drag supplied. this method guarantees to move the object on the front end, and update the last valid
     * row and column whilst moving. it is a strict move in vertical and horizontal direction based on orientation.
     *
     * @param xNew the displaced x coordinate (pixels) aka how much it was dragged (mouse movement).
     * @param yNew the displaced y coordinate (pixels) aka how much it was dragged (mouse movement).
     */
    /*
     * this method will move the car in a given x position if it is horizontal.
     * this method will move the car in a given y position if it is vertical
     * this method will RELOCATE the object rather than translate it, (will move the objects origin properly)
     */
    public void moveCar(double xNew, double yNew) {
        //the flag respresents a collision has previously occured
        //if a collision has previously occured WE WANT TO IMMEDIATELY RETURN so the object doesn't relocate just returns to avoid
        //jumping over obstacles
        //if the vehicle is horizontal and the attempted move IS VALID
        //WE MAKE SURE WE DO NOT RELOCATE IT AT ALL VERTICALLY! ALL THE VERTICAL COORDINATES VARIABLES ETC STAY THE SAME
        if (flag && vehicle.isHorizontal() && lastValidPosition(xNew - initialClickOffsetInX + getLayoutX(), (row) * GridVariables.tileSizeHeight)) {
            //relocate the object based on offset
            //xNew - initalClickOffsetInx is the drag offset from where the block was and cursor position,
            //and we add that to the objects absolute position (getlayoutX) is absolute position
            //the reason we have xNew - initalClickOffsetInX is to stop the block from just jumping around
            //sporadically, and jumping based on click offset.
            //relocate changes the objects origin once relocated which is much better fix than translate
            relocate(xNew - initialClickOffsetInX + getLayoutX(), (row) * GridVariables.tileSizeHeight);
            //update the last position the object is at based on xNew (the click)
            initialClickOffsetInX = xNew;
        }
        //if the vehicle is vertical and the attempted move IS VALID
        //we make sure WE DO NOT MOVE THE OBJECT OR UPDATE ANY OF THE VARIABLES IN THE X (horizontal) positions
        else {
            if (flag && (!vehicle.isHorizontal()) && lastValidPosition((col) * GridVariables.tileSizeWidth, yNew - initialClickOffsetInY + getLayoutY())) {
                //relocate the object based on offset
                //YNew - initalClickOffsetInY is the drag offset from where the block was and cursor position,
                //and we add that to the objects absolute position (getlayoutY) is absolute position
                //the reason we have YNew - initalClickOffsetInY is to stop the block from just jumping around
                //sporadically, and jumping based on click offset.
                //relocate changes the objects origin once relocated which is much better fix than translate
                relocate((col) * GridVariables.tileSizeWidth, yNew - initialClickOffsetInY + getLayoutY());
                //update the last position the object is based on YNew (the click)
                initialClickOffsetInY = yNew;
            }
        }
    }

    /**
     * This method will initially shift the object once placed on the grid. since the inital placement is at the origin
     * of the group itself (top left corner). This method will guarantee to relocate the object based on the initial given
     * row and column. this method will also update back end on initiation, and place the objects on the back end grid.
     */
    public void initialShift() {
        //this initial shift links back end to front end
        //the initial shift in x is the column where it will be placed, multiplied by the pixel size of the tile width
        double offsetXtemp = (col) * GridVariables.tileSizeWidth;
        //the initial shift in the y is the row where it will be placed, multiplied by the pixel size of the tile hiehgt
        double offsetYtemp = (row) * GridVariables.tileSizeHeight;
        //relocate object to the initial shift coordinates
        relocate(offsetXtemp, offsetYtemp);
        //update the backend grid
        updateGrid(col, row);

    }
}