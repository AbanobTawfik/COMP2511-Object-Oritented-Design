import javafx.scene.*;
import javafx.application.*;
import javafx.scene.input.KeyCode;
import javafx.stage.*;

import static java.lang.Thread.sleep;


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
        Menu m = new Menu();
        Scene menu = new Scene(m.menu(), GridVariables.GRID_WIDTH, GridVariables.GRID_HEIGHT + 70);
        this.menu = menu;
        //this method will display the application
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    threadSleep(500);
                    Platform.runLater(() -> {
                        levelExists(m, g);
                    });
                    g.generateEasyGrid();

                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    threadSleep(150);
                    g.generateMediumGrid();


                }

            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    threadSleep(15);

                    g.generateHardGrid();

                }
            }
        }).start();


        game.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ESCAPE))
                g.toggleMenu();
            System.out.println("Easy - " + g.getEasyGrids().size());
            System.out.println("Medium - " + g.getMediumGrids().size());
            System.out.println("Hard - " + g.getHardGrids().size());
        });
        game.setOnMouseDragged(e -> {
            g.updateCounter();
        });

        game.setOnMouseReleased(e -> {
            g.updateCounter();
            if (g.getGrid().turnOverflow()) {
                try {
                    g.reGenerate();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                g.updateCounter();
                g.resetStreak();
                g.updateStreakGUI();
            }
            if (g.getGrid().isVictory()) {
                try {
                    g.reGenerate();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                g.updateCounter();
                g.updateStreak();
                g.updateStreakGUI();
            }
        });

        m.getEasyStandard().setOnAction(e -> {
            stage.setScene(game);
            g.setChallangerMode(false);
            g.getGrid().setDifficulty(Difficulty.EASY);
            if (g.menuOn())
                g.toggleMenu();
            try {
                g.reGenerate();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            g.updateCounter();
            g.updateStreakGUI();
            g.resetStreak();
            g.updateStreakGUI();
        });

        m.getMediumStandard().setOnAction(e -> {
            stage.setScene(game);
            g.setChallangerMode(false);
            g.getGrid().setDifficulty(Difficulty.MEDIUM);
            if (g.menuOn())
                g.toggleMenu();
            try {
                g.reGenerate();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            g.updateCounter();
            g.updateStreakGUI();
            g.resetStreak();
            g.updateStreakGUI();
        });

        m.getHardStandard().setOnAction(e -> {
            stage.setScene(game);
            g.setChallangerMode(false);
            g.getGrid().setDifficulty(Difficulty.HARD);
            if (g.menuOn())
                g.toggleMenu();
            try {
                g.reGenerate();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            g.updateCounter();
            g.updateStreakGUI();
            g.resetStreak();
            g.updateStreakGUI();
        });

        m.getEasyChallange().setOnAction(e -> {
            stage.setScene(game);
            g.setChallangerMode(true);
            g.getGrid().setDifficulty(Difficulty.EASY);
            if (g.menuOn())
                g.toggleMenu();
            try {
                g.reGenerate();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            g.updateCounter();
            g.updateStreakGUI();
            g.resetStreak();
            g.updateStreakGUI();

        });

        m.getMediumChallange().setOnAction(e -> {
            stage.setScene(game);
            g.setChallangerMode(true);
            g.getGrid().setDifficulty(Difficulty.MEDIUM);
            if (g.menuOn())
                g.toggleMenu();
            try {
                g.reGenerate();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            g.updateCounter();
            g.updateStreakGUI();
            g.resetStreak();
            g.updateStreakGUI();
        });

        m.getHardChallange().setOnAction(e -> {
            stage.setScene(game);
            g.setChallangerMode(true);
            g.getGrid().setDifficulty(Difficulty.HARD);
            if (g.menuOn())
                g.toggleMenu();
            try {
                g.reGenerate();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            g.updateCounter();
            g.updateStreakGUI();
            g.resetStreak();
            g.updateStreakGUI();
        });

        g.getMenuBoard().setOnAction(e -> {
            stage.setScene(menu);
            stage.show();
        });

        stage.setScene(menu);
        stage.setTitle("Gridlock");
        stage.show();
    }

    /**
     * Level exists.
     *
     * @param m the m
     * @param g the g
     */
    public void levelExists(Menu m, GridGenerator g) {
        g.updateMenuStatus();
        if (g.getEasyGrids().size() > 0) {
            m.getEasyChallange().setStyle("-fx-border-color: green; -fx-border-width: 2");
            m.getEasyStandard().setStyle("-fx-border-color: green; -fx-border-width: 2");
        } else {
            m.getEasyChallange().setStyle("-fx-borer-color: none;");
            m.getEasyStandard().setStyle("-fx-borer-color: none;");
        }

        if (g.getMediumGrids().size() > 0) {
            m.getMediumChallange().setStyle("-fx-border-color: green; -fx-border-width: 2");
            m.getMediumStandard().setStyle("-fx-border-color: green; -fx-border-width: 2");
        } else {
            m.getMediumChallange().setStyle("-fx-borer-color: none;");
            m.getMediumStandard().setStyle("-fx-borer-color: none;");
        }
        if (g.getHardGrids().size() > 0) {
            m.getHardChallange().setStyle("-fx-border-color: green; -fx-border-width: 2");
            m.getHardStandard().setStyle("-fx-border-color: green; -fx-border-width: 2");
        } else {
            m.getHardChallange().setStyle("-fx-borer-color: none;");
            m.getHardStandard().setStyle("-fx-borer-color: none;");
        }
    }

    public void threadSleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}