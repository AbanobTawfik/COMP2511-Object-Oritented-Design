import javafx.scene.*;
import javafx.application.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.*;


/**
 * This is the main method which loads the game.
 * This class will launch the scene (it will get the Parent for the scene from GridGenerator)
 */
public class GridLock extends Application {

    private Stage stage;
    private Scene game;
    private Scene menu;

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
     *
     * @param stage a literal stage for the program which has the loaded scene, and title
     */
    @Override
    public void start(Stage stage) {
        this.stage = stage;

        //this will be used for generating a new grid everytime
        GridGenerator g = new GridGenerator();
        //screates new scene from the grid generator method (the parent)
        //and the resolution for the scene
        Scene game = new Scene(g.generateGrid(), GridVariables.GRID_WIDTH, GridVariables.GRID_HEIGHT + 70);
        this.game = game;
        //this method will display the application
        game.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ESCAPE))
                g.toggleMenu();
        });
        game.setOnMouseDragged(e -> {
            g.updateCounter();
        });

        game.setOnMouseReleased(e -> {
            g.updateCounter();
            if (g.getGrid().isVictory()) {
                g.reGenerate();
                g.updateCounter();
                g.updateStreak();
                g.updateStreakGUI();
            }
        });
        Menu m = new Menu();
        Scene menu = new Scene(m.menu(), GridVariables.GRID_WIDTH, GridVariables.GRID_HEIGHT + 70);
        this.menu = menu;
        stage.setScene(menu);
        stage.setTitle("Gridlock");
        stage.show();
    }
}