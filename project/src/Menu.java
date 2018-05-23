import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * This class will be the node for the main menu. the main menu will contain buttons to access
 * every possible game mode, including challange mode, and an exit button. The challange mode
 * will be a game mode, that contains a turn restriction given by the estimated number of moves
 * to solve the path.
 */
public class Menu {
    //the easy button in standard mode
    private Button easyStandard = new Button("Standard Mode - Easy");
    //the medium button in standard mode
    private Button mediumStandard = new Button("Standard Mode - Medium");
    //the hard button in standard mode
    private Button hardStandard = new Button("Standard Mode - Hard");
    //the easy button in challange mode
    private Button easyChallange = new Button("Challange Mode - Easy");
    //the medium button in challange mode
    private Button mediumChallange = new Button("Challange Mode - Medium");
    //the hard button in challange mode
    private Button hardChallange = new Button("Challange Mode - Hard");
    //the exit button
    private Button exit = new Button("Exit");

    /**
     * This method will create a menu display for the user in a parent node for a stage.
     * The menu will contain a total of 7 buttons, with a mini tutorial at the top giving
     * basic rundowns. This method guarantees to create a working menu for the scene
     *
     * @return the menu scene for the stage
     */
    public Parent menu() {
        //create a vertical spaced box scaled based on grid height
        VBox mainMenu = new VBox(GridVariables.GRID_HEIGHT / 10);
        //set the height of the box to the grid height
        mainMenu.setMinHeight(GridVariables.GRID_HEIGHT);
        //set the width of the box to be the grid width
        mainMenu.setMinWidth(GridVariables.GRID_WIDTH);
        //now we want to evenly space our rows with pairing the two buttons
        //example                   EASY STANDARD | EASY DIFFICULT

        //row0 will be the mini tutuorial row
        HBox row0 = new HBox(0);
        //center the row around the center
        row0.setAlignment(Pos.BASELINE_CENTER);

        //row1 be the row for holding the easy buttons spaced at 30 pixels
        HBox row1 = new HBox(30);
        //row1 will be centered
        row1.setAlignment(Pos.CENTER);

        //row2 be the row for holding the medium buttons spaced at 30 pixels
        HBox row2 = new HBox(30);
        //row2 will be centered
        row2.setAlignment(Pos.CENTER);

        //row3 be the row for holding the hard buttons spaced at 30 pixels
        HBox row3 = new HBox(30);
        //row3 will be centered
        row3.setAlignment(Pos.CENTER);

        //row4 be the row for holding the easy buttons spaced at 30 pixels
        HBox row4 = new HBox(30);
        //row4 will be centered
        row4.setAlignment(Pos.CENTER);

        //creating a label for the tutorial, screen text
        Label tutorial = new Label();
        //setup tutorial strings
        String s0 = "Welcome to gridlock! The aim of the game is simple.\n";
        String s1 = "Move the blue-green car into the right most end.\n";
        String s2 = "Challange mode has a turn restriction.\n";
        String s3 = "One turn over limit will reset streak! Good luck and have fun!!\n";
        //set the height for the tutorial block based on the grid height
        tutorial.setMinHeight(GridVariables.GRID_HEIGHT / 6.8);
        //set the text for the tutorial to be the strings lined up together
        tutorial.setText(s0 + s1 + s2 + s3);
        //set the font style for the tutorial
        tutorial.setFont(new Font("Impact", GridVariables.GRID_HEIGHT / 40));
        //use a blueish colour for the text constrant
        tutorial.setTextFill(Color.MIDNIGHTBLUE);

        //row0 will be used for holding the tutorial
        row0.getChildren().addAll(tutorial);
        //row1 holds the easy buttons
        row1.getChildren().addAll(easyStandard, easyChallange);
        //row2 holds the intermediate buttons
        row2.getChildren().addAll(mediumStandard, mediumChallange);
        //row3 holds the harder level buttons
        row3.getChildren().addAll(hardStandard, hardChallange);
        //row4 will hold the exit
        row4.getChildren().add(exit);
        //center row0 around the buttons
        row0.setTranslateX(GridVariables.GRID_WIDTH / 30);
        //style the buttons using the css sheet
        easyStandard.getStylesheets().add("files/style/Buttons.css");
        mediumStandard.getStylesheets().add("files/style/Buttons.css");
        hardStandard.getStylesheets().add("files/style/Buttons.css");
        easyChallange.getStylesheets().add("files/style/Buttons.css");
        mediumChallange.getStylesheets().add("files/style/Buttons.css");
        hardChallange.getStylesheets().add("files/style/Buttons.css");
        exit.getStylesheets().add("files/style/Buttons.css");
        //if exit button is pressed we want to exit the program
        exit.setOnAction(e -> {
            System.exit(0);
        });
        //now we want to add all rows to the menu VBOX
        mainMenu.getChildren().addAll(row0, row1, row2, row3, row4);
        //set the background for menu as opaque darker background
        mainMenu.setStyle("-fx-background-color: rgba(50, 50, 50, 0.5); -fx-background-radius: 10;opacity: 0.5;");
        //return the main menu
        return mainMenu;
    }

    /**
     * This method will be used for retrieving the standard easy button
     * to allow access to this button within the main class
     *
     * @return the easy standard button
     */
    public Button getEasyStandard() {
        return easyStandard;
    }

    /**
     * This method will be used for retrieving the standard intermediate button
     * to allow access to this button within the main class
     *
     * @return the intermediate standard button
     */
    public Button getMediumStandard() {
        return mediumStandard;
    }

    /**
     * This method will be used for retrieving the standard hard button
     * to allow access to this button within the main class
     *
     * @return the hard standard button
     */
    public Button getHardStandard() {
        return hardStandard;
    }

    /**
     * This method will be used for retrieving the challange mode easy button
     * to allow access to this button within the main class
     *
     * @return the easy challange button
     */
    public Button getEasyChallange() {
        return easyChallange;
    }

    /**
     * This method will be used for retrieving the medium challange button
     * to allow access to this button within the main class
     *
     * @return the medium challange button
     */
    public Button getMediumChallange() {
        return mediumChallange;
    }

    /**
     * This method will be used for retrieving the hard challange button
     * to allow access to this button within the main class
     *
     * @return the hard challange button
     */
    public Button getHardChallange() {
        return hardChallange;
    }
}
