import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * The type Menu.
 */
public class Menu {
    private Button easyStandard = new Button("Standard Mode - Easy");
    private Button mediumStandard = new Button("Standard Mode - Medium");
    private Button hardStandard = new Button("Standard Mode - Hard");
    private Button easyChallange = new Button("Challange Mode - Easy");
    private Button mediumChallange = new Button("Challange Mode - Medium");
    private Button hardChallange = new Button("Challange Mode - Hard");
    private Button exit = new Button("Exit");

    /**
     * Menu parent.
     *
     * @return the parent
     */
    public Parent menu() {
        VBox mainMenu = new VBox(GridVariables.GRID_HEIGHT / 10);
        mainMenu.setMinHeight(GridVariables.GRID_HEIGHT);
        mainMenu.setMinWidth(GridVariables.GRID_WIDTH);
        VBox menubackground = new VBox();
        HBox row0 = new HBox(0);
        row0.setAlignment(Pos.BASELINE_CENTER);
        HBox row1 = new HBox(30);
        row1.setAlignment(Pos.CENTER);
        HBox row2 = new HBox(30);
        row2.setAlignment(Pos.CENTER);
        HBox row3 = new HBox(30);
        row3.setAlignment(Pos.CENTER);
        HBox row4 = new HBox(30);
        row4.setAlignment(Pos.CENTER);
        Label tutorial = new Label();
        tutorial.getStylesheets().add("Buttons.css");
        Label tutorial_ext = new Label();
        tutorial_ext.getStylesheets().add("Buttons.css");
        String s0 = "Welcome to gridlock! The aim of the game is simple.";
        String s1 = "Move the blue-green car into the right most end.";
        String s2 = "Challange mode has a turn restriction.\nOne turn over limit will reset streak!";
        String s3 = "Good luck and have fun!!";
        tutorial.setMinHeight(GridVariables.GRID_HEIGHT / 6.8);
        tutorial.setText(s0 + System.lineSeparator() + s1 + System.lineSeparator() + s2 + System.lineSeparator() + s3);
        tutorial.setFont(new Font("Impact", GridVariables.GRID_HEIGHT / 40));
        tutorial.setTextFill(Color.MIDNIGHTBLUE);


        row0.getChildren().addAll(tutorial);
        row1.getChildren().addAll(easyStandard, easyChallange);
        row2.getChildren().addAll(mediumStandard, mediumChallange);
        row3.getChildren().addAll(hardStandard, hardChallange);
        row4.getChildren().add(exit);
        row0.setTranslateX(GridVariables.GRID_WIDTH / 30);
        easyStandard.getStylesheets().add("Buttons.css");
        mediumStandard.getStylesheets().add("Buttons.css");
        hardStandard.getStylesheets().add("Buttons.css");
        easyChallange.getStylesheets().add("Buttons.css");
        mediumChallange.getStylesheets().add("Buttons.css");
        hardChallange.getStylesheets().add("Buttons.css");
        exit.getStylesheets().add("Buttons.css");
        exit.setOnAction(e -> {
            System.exit(0);
        });
        mainMenu.getChildren().addAll(row0, row1, row2, row3, row4);
        mainMenu.setStyle("-fx-background-color: rgba(50, 50, 50, 0.5); -fx-background-radius: 10;opacity: 0.5;");
        row4.toBack();
        row3.toBack();
        row2.toBack();
        row1.toBack();
        row0.toBack();

        //the css styling for the buttons will be used from a standard css style
        return mainMenu;
    }

    /**
     * Gets easy standard.
     *
     * @return the easy standard
     */
    public Button getEasyStandard() {
        return easyStandard;
    }

    /**
     * Gets medium standard.
     *
     * @return the medium standard
     */
    public Button getMediumStandard() {
        return mediumStandard;
    }

    /**
     * Gets hard standard.
     *
     * @return the hard standard
     */
    public Button getHardStandard() {
        return hardStandard;
    }

    /**
     * Gets easy challange.
     *
     * @return the easy challange
     */
    public Button getEasyChallange() {
        return easyChallange;
    }

    /**
     * Gets medium challange.
     *
     * @return the medium challange
     */
    public Button getMediumChallange() {
        return mediumChallange;
    }

    /**
     * Gets hard challange.
     *
     * @return the hard challange
     */
    public Button getHardChallange() {
        return hardChallange;
    }
}
