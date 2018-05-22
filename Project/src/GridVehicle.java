import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;

import java.io.File;
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
    //This is a flag which will tell us if the car which is selected is the goal car which reaches goal state
    private boolean goalCar;
    //the back-end grid, this is so updating board state and checking board state is done and handled by back end grid class
    private Grid grid;
    //this will keep track of the most recent row where a crash has occured (allows for movement after crash provided valid)
    private int crashRow;
    //this will keep track of the most recent column where a crash has occured (allows for movement after crash provided valid)
    private int crashCol;

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
     * @param grid    the grid which contains the board state (allows us to access methods to update board state inside the grid class)
     */
    public GridVehicle(boolean goalCar, Vehicle vehicle, int row, int col, Grid grid) {
        //sets the vehicle for the GridVehicle
        this.vehicle = vehicle;
        //sets the initial row for the GridVehicle
        this.row = row;
        //sets the initial column for the GridVehicle
        this.col = col;
        //sets the boolean if the car is goal car
        this.goalCar = goalCar;
        //sets the grid board state for the car, (allows grid to update state rather than this class
        this.grid = grid;
        //initiallising the width of the rectangle (pixels in x direction)
        double vehicleSizeX = GridVariables.TILE_SIZE_WIDTH;
        //initiallising the height of the rectangle (pixels in y direction)
        double vehicleSizeY = GridVariables.TILE_SIZE_HEIGHT;
        //if the vehicle is horizontal, we set it's pixel size in x direction to
        //vehicle size * tile size in x
        if (vehicle.isHorizontal()) {
            vehicleSizeX = (GridVariables.TILE_SIZE_WIDTH) * vehicle.getSize();
        }
        //otherwise it's vertical and do the converse, set it's pixel size in y direction to
        //vehicle size * tile size in y direction
        else
            vehicleSizeY = (GridVariables.TILE_SIZE_HEIGHT) * vehicle.getSize();
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

        //rounding the vehicle for a nicer rounded vehicle
        carBlock.setArcHeight(60);
        carBlock.setArcWidth(60);

        carBlock.setOpacity(0.94);
        //add the car to stack pane object (node) added
        getChildren().add(carBlock);
        //shift the vehicle to the correct index upon being added
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
            grid.setVictory(victoryCondition());
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
                relocate(lastValidColumn * GridVariables.TILE_SIZE_WIDTH, row * GridVariables.TILE_SIZE_HEIGHT);
            else
                //snaps the vehicle back to the last valid column and row (lastValidColumn and lastValidRow)
                //when vertical we only want to snap in Y coordinate based on last valid Y only IGNORE X
                relocate(col * GridVariables.TILE_SIZE_WIDTH, lastValidRow * GridVariables.TILE_SIZE_HEIGHT);
            //update the back end grid, will free the old coordinates blocked by vehicle
            //and then it will set the new coordinates blocked based on the column + row it snapped to and vehicle size
            grid.updateGrid(lastValidColumn, lastValidRow, coordinatesBlocked, vehicle, this);
            //increment the number of moves performed counter by 1
            grid.incrementMoveCounter();
            //thicker stroke so the blocks are visually more seperated, white stroke on black vehicles
            //when the vehicle is released it goes back to white stroke
            carBlock.setStrokeWidth(2);
            carBlock.setStroke(Color.WHITE);
            //if victory we want sound also if the sound option is toggled
            if(victoryCondition() && grid.isSound()){
                //file name for the audio file
                String musicFile = "hit.mp3";
                //converting the media file into readable code
                Media sound = new Media(new File(musicFile).toURI().toString());
                //
                MediaPlayer mediaPlayer = new MediaPlayer(sound);
                mediaPlayer.play();
            }
            grid.setVictory(victoryCondition());
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
        int tilewidth = (int) GridVariables.TILE_SIZE_WIDTH;
        //the updated column position is the offset + the pixel coordinate for the FIRST block in the vehicle
        //if we dont divide by vehicle size it will pick the end block for the vehicle
        int posI = (int) (x + GridVariables.TILE_SIZE_WIDTH / vehicle.getSize());
        //the resulting move column is the updated coordinate origin column (top left corner)  divided by the width of the column
        int resultCol = posI / tilewidth;
        //if this column is in a negative index FALSE INVALID MOVE, this will also set boundary at 0, so it cannot go
        //into negative board space
        if (posI < 0) {
            return false;
        }
        //since we are doing integer divison, cast TILE_SIZE_HEIGHT to integer
        int tileHeight = (int) GridVariables.TILE_SIZE_HEIGHT;
        //the updated row position is the offset + the pixel coordinate for the FIRST block in the vehicle
        //if we dont divide by vehicle size it will pick the end block for the vehicle
        int posI2 = (int) (y + GridVariables.TILE_SIZE_HEIGHT / vehicle.getSize());
        //the resulting move row is the updated coordinate origin row (top left corner) divided by the height of the row
        int resultRow = posI2 / tileHeight;
        //if this row is in a negative index, like above. it sets the boundary at 0 for row, cannot go up into negative space
        if (posI2 < 0) {
            return false;
        }
        //now we are checking the back end board for valid move
        //if the resulting move col + row (the row and column the object will be snapped in) is valid
        if (grid.isValidMove(resultCol, resultRow, vehicle, this)) {
            //we want to update the last valid column
            lastValidColumn = resultCol;
            //update the last valid row
            lastValidRow = resultRow;
            //return true since it was a valid move
            return true;
        }
        //if the move was invalid, we want to set our flag as false so the object can no longer move
        if (!grid.isValidMove(resultCol, resultRow, vehicle, this)) {
            //if it is the FIRST TIME the vehicle has crashed (so the sound only plays once each crash)
            if(flag == true && grid.isSound() && !victoryCondition()){
                //retrieve the string for the sound file
                String thump = "bad.mp3";
                //create a media file from the string with the sound file name
                Media Thump = new Media(new File(thump).toURI().toString());
                //create a media playable object with the media file
                MediaPlayer hornMediaPlayer = new MediaPlayer(Thump);
                //play the small thump sound
                hornMediaPlayer.play();
            }
            //set the flag as false
            flag = false;
            //keep track of the row the crash occured to allow for opposite movement
            crashCol = resultCol;
            //keep track of the column the crash occured to allow for opposite movement
            crashRow = resultRow;

        }
        //also return false (invalid moves)
        return false;
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
        if (flag && vehicle.isHorizontal() && lastValidPosition(xNew - initialClickOffsetInX + getLayoutX(), (row) * GridVariables.TILE_SIZE_HEIGHT)) {
            //relocate the object based on offset
            //xNew - initalClickOffsetInx is the drag offset from where the block was and cursor position,
            //and we add that to the objects absolute position (getlayoutX) is absolute position
            //the reason we have xNew - initalClickOffsetInX is to stop the block from just jumping around
            //sporadically, and jumping based on click offset.
            //relocate changes the objects origin once relocated which is much better fix than translate
            relocate(xNew - initialClickOffsetInX + getLayoutX(), (row) * GridVariables.TILE_SIZE_HEIGHT);
            //update the last position the object is at based on xNew (the click)
            initialClickOffsetInX = xNew;
        }
        //if the vehicle is vertical and the attempted move IS VALID
        //we make sure WE DO NOT MOVE THE OBJECT OR UPDATE ANY OF THE VARIABLES IN THE X (horizontal) positions
        else {
            if (flag && (!vehicle.isHorizontal()) && lastValidPosition((col) * GridVariables.TILE_SIZE_WIDTH, yNew - initialClickOffsetInY + getLayoutY())) {
                //relocate the object based on offset
                //YNew - initalClickOffsetInY is the drag offset from where the block was and cursor position,
                //and we add that to the objects absolute position (getlayoutY) is absolute position
                //the reason we have YNew - initalClickOffsetInY is to stop the block from just jumping around
                //sporadically, and jumping based on click offset.
                //relocate changes the objects origin once relocated which is much better fix than translate
                relocate((col) * GridVariables.TILE_SIZE_WIDTH, yNew - initialClickOffsetInY + getLayoutY());
                //update the last position the object is based on YNew (the click)
                initialClickOffsetInY = yNew;
            }
        }
        //note for this FORWARD -> TO THE RIGHT and BACKWARDS <- TO THE LEFT
        //if a collision has occured and the vehicle wants to move AWAY from the collision provided the move away is a legal move
        //we will allow the vehicle to move away from the collision on the following conditions
        //if the vehicle
        //1. crashed (flag == false)
        //2. and the vehicle is horizontal
        //3. and the car has crashed moving BACK
        //4. and the car is trying to move FORWARD
        //5. and the move is LEGAL (valid)
        //we allow the car to freely and smoothly move backward updating its details AND making the flag true now
        if (!flag && vehicle.isHorizontal() && lastValidColumn < crashCol && (xNew - initialClickOffsetInX + getLayoutX() < initialClickOffsetInX)
                && lastValidPosition(xNew - initialClickOffsetInX + getLayoutX(), (row) * GridVariables.TILE_SIZE_HEIGHT)) {
            //relocate the object based on offset
            //xNew - initalClickOffsetInx is the drag offset from where the block was and cursor position,
            //and we add that to the objects absolute position (getlayoutX) is absolute position
            //the reason we have xNew - initalClickOffsetInX is to stop the block from just jumping around
            //sporadically, and jumping based on click offset.
            //relocate changes the objects origin once relocated which is much better fix than translate
            flag = true;
            relocate(xNew - initialClickOffsetInX + getLayoutX(), (row) * GridVariables.TILE_SIZE_HEIGHT);
            //update the last position the object is at based on xNew (the click)
            initialClickOffsetInX = xNew;
        }

        //if the vehicle
        //1. crashed (flag == false)
        //2. and the vehicle is horizontal
        //3. and the car has crashed moving FORWARD
        //4. and the car is trying to move BACKWARDS
        //5. and the move is LEGAL (valid)
        //we allow the car to freely and smoothly move FORWARD updating its details AND making the flag true now
        if (!flag && vehicle.isHorizontal() && lastValidColumn > crashCol && (xNew + initialClickOffsetInX - getLayoutX() > initialClickOffsetInX)
                && lastValidPosition(xNew - initialClickOffsetInX + getLayoutX(), (row) * GridVariables.TILE_SIZE_HEIGHT)) {
            //relocate the object based on offset
            //xNew - initalClickOffsetInx is the drag offset from where the block was and cursor position,
            //and we add that to the objects absolute position (getlayoutX) is absolute position
            //the reason we have xNew - initalClickOffsetInX is to stop the block from just jumping around
            //sporadically, and jumping based on click offset.
            //relocate changes the objects origin once relocated which is much better fix than translate
            flag = true;
            relocate(xNew - initialClickOffsetInX + getLayoutX(), (row) * GridVariables.TILE_SIZE_HEIGHT);
            //update the last position the object is at based on xNew (the click)
            initialClickOffsetInX = xNew;
        }
        //this is the case for Vertical movements after collisions
        //note up  refers to moving towards the ceiling and down refers moving towards the floor
        else {
            //if the vehicle
            //1. crashed (flag == false)
            //2. and the vehicle is Vertical
            //3. and the car has crashed moving DOWN
            //4. and the car is trying to move UP
            //5. and the move is LEGAL (valid)
            //we allow the car to freely and smoothly move UP updating its details AND making the flag true now
            if (!flag && !vehicle.isHorizontal() && lastValidRow > crashRow && (yNew + initialClickOffsetInY - getLayoutY() > initialClickOffsetInY)
                    && lastValidPosition((col) * GridVariables.TILE_SIZE_WIDTH, yNew - initialClickOffsetInY + getLayoutY())) {
                //relocate the object based on offset
                //YNew - initalClickOffsetInY is the drag offset from where the block was and cursor position,
                //and we add that to the objects absolute position (getlayoutY) is absolute position
                //the reason we have YNew - initalClickOffsetInY is to stop the block from just jumping around
                //sporadically, and jumping based on click offset.
                //relocate changes the objects origin once relocated which is much better fix than translate
                flag = true;
                relocate((col) * GridVariables.TILE_SIZE_WIDTH, yNew - initialClickOffsetInY + getLayoutY());
                //update the last position the object is based on YNew (the click)
                initialClickOffsetInY = yNew;
            }
            //if the vehicle
            //1. crashed (flag == false)
            //2. and the vehicle is Vertical
            //3. and the car has crashed moving UP
            //4. and the car is trying to move DOWN
            //5. and the move is LEGAL (valid)
            //we allow the car to freely and smoothly move DOWN updating its details AND making the flag true now
            if (!flag && !vehicle.isHorizontal() && lastValidRow < crashRow && (yNew - initialClickOffsetInY + getLayoutY() < initialClickOffsetInY)
                    && lastValidPosition((col) * GridVariables.TILE_SIZE_WIDTH, yNew - initialClickOffsetInY + getLayoutY())) {
                //relocate the object based on offset
                //YNew - initalClickOffsetInY is the drag offset from where the block was and cursor position,
                //and we add that to the objects absolute position (getlayoutY) is absolute position
                //the reason we have YNew - initalClickOffsetInY is to stop the block from just jumping around
                //sporadically, and jumping based on click offset.
                //relocate changes the objects origin once relocated which is much better fix than translate
                flag = true;
                relocate((col) * GridVariables.TILE_SIZE_WIDTH, yNew - initialClickOffsetInY + getLayoutY());
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
        double offsetXtemp = (col) * GridVariables.TILE_SIZE_WIDTH;
        //the initial shift in the y is the row where it will be placed, multiplied by the pixel size of the tile hiehgt
        double offsetYtemp = (row) * GridVariables.TILE_SIZE_HEIGHT;
        //relocate object to the initial shift coordinates
        relocate(offsetXtemp, offsetYtemp);
        //update the backend grid
        grid.updateGrid(col, row, coordinatesBlocked, vehicle, this);
        grid.getGridVehicles().add(this);
    }

    /**
     * This method will be used to check if the goal car has gone through the goal tile,
     * This method guarantees to return a true/false expression representing if the car has met goal state
     *
     * @return true if the goal car has gone through the goal row, which will always be on the last column, false if the goal state has not been met
     */
    public boolean victoryCondition() {
        //return the following
        //1. the last valid row (where the car will snap on release) is the goal row
        //2. the column (always the case) is the size of the board minus the size of the vehicle
        //   this is so that the FRONT of the vehicle passing through is goal state, rather than the back
        //3. the vehicle is the goal car we need to move through
        //the goal car will always start on row 2 and is always horizontal so the vertical case is never considered
        return lastValidRow == grid.getGoalRow() && lastValidColumn == GridVariables.BOARD_SIZE - vehicle.getSize() && goalCar;
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //THESE METHODS BELOW ARE ALL FOR CREATING BACKEND VEHICLES FOR BOARD GENERATION

    /**
     * This method will be used to return the initial row allocated to the grid vehicle upon creation.
     * It will be used for backend use during board generation. This method will guarantee to return a positive
     * positive non zero integer.
     *
     * @return the row which was initially assigned to the GridVehicle
     */
    public int getRow() {
        return row;
    }

    /**
     * This method will be used to return the initial column allocated to the block upon creation. It's
     * use is for board generation, and will guarantee to return a positive non zero integer.
     *
     * @return the column which was initially assigned to the gridVehicle
     */
    public int getCol() {
        return col;
    }

    /**
     * This method will be used to return the vehicle which was assigned to the gridvehicle that contains
     * information on SIZE and ORIENTATION. this will be mainly used when creating backend vehicles
     * This method guarantees to return a non null vehicle object.
     *
     * @return the vehicle which represents the orientation and size of the GridVehicle
     */
    public Vehicle getVehicle() {
        return vehicle;
    }

    /**
     * This method will be used to return if the GridVehicle is the goal car. this method will guarantee
     * to return a boolean true/false representing if the gridvehicle is the goal car
     *
     * @return true if the gridVehicle is the goal car, otherwise false
     */
    public boolean isGoalCar() {
        return goalCar;
    }
}
