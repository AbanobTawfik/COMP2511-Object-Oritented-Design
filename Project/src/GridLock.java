import javafx.scene.*;
import javafx.application.*;
import javafx.scene.input.KeyCode;
import javafx.stage.*;


/**
 * This is the main method which loads the game.
 * This class will launch the scene (it will get the Parent for the scene from GridGenerator)
 */
public class GridLock extends Application {
    /**
     * The main class which will launch the project program
     *
     * @param args command line arguments
     */
    public static void main(String args[]) {
        //launch the project
        launch(args);
    }

    /**
     * This method will stage a program to be launched. will load the scene with a title
     * and display.
     * @param stage a literal stage for the program which has the loaded scene, and title
     */
    @Override
    public void start(Stage stage) {
        //this will be used for generating a new grid everytime
        GridGenerator g = new GridGenerator();
        //screates new scene from the grid generator method (the parent)
        //and the resolution for the scene
        Scene scene = new Scene(g.generateGrid(), GridVariables.gridWidth, GridVariables.gridHeight);
        //this method will display the application
        scene.setOnKeyPressed(e->{
            if(e.getCode().equals(KeyCode.ESCAPE))
                toggleMenu();
        });
        stage.setScene(scene);
        stage.setTitle("MeMeS");
        stage.show();
    }

    public void toggleMenu(){
        if(GridVariables.root.getChildren().contains(GridVariables.menu))
            GridVariables.root.getChildren().remove(GridVariables.menu);
        else if(!GridVariables.root.getChildren().contains(GridVariables.menu))
            GridVariables.root.getChildren().add(GridVariables.menu);
        else
            return;
    }
}