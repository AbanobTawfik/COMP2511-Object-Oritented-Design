import javafx.scene.*;
import javafx.application.*;
import javafx.scene.input.KeyCode;
import javafx.stage.*;

/**
 * This is the main method which loads the game.
 * This class will launch the scene (it will get the Parent for the scene from GridGenerator and menu)
 * All the linking buttons will be managed on here, and multiple threads will be created in order to improve
 * gameplay experience for the user. due to multi threadding this allows for boards to constantly be created while
 * the user is playing. This will allow boards to be loaded instantly provided there is one on the queue.
 */
public class GridLock extends Application {
    //this will be the main stage for the application
    private Stage stage;
    //this will be the game scene with the board
    private Scene game;
    //this will be the menu scene with the main menu
    private Scene menu;

    /**
     * The main class which will launch the project program
     *
     * @param args command line arguments
     */
    public static void main(String args[]) {
        //bootstrapping for main
        GridLock gl = new GridLock();
        //launch the project
        gl.launch(args);
    }

    /**
     * This method will stage a program to be launched. will load the scene with a title
     * and display.
     *
     * @param stage a literal stage for the program which has the loaded scene, and title
     */
    @Override
    public void start(Stage stage) {
        //set the stage for the class
        this.stage = stage;
        //this will be used for generating new grids
        GridGenerator g = new GridGenerator();
        //screates new game scene from the grid generator
        //set the resolution of the scren to be the global width, however add 70 pixels for the height to
        //allow room for the dashboard
        Scene game = new Scene(g.generateGrid(), GridVariables.GRID_WIDTH, GridVariables.GRID_HEIGHT + 70);
        //set the game for the class (allows for scene switches)
        this.game = game;
        //this will be the main menu to be added to the main stage
        Menu m = new Menu();
        //create the menu with the same resolution as the game state, in a scene
        Scene menu = new Scene(m.menu(), GridVariables.GRID_WIDTH, GridVariables.GRID_HEIGHT + 70);
        //set the menu for the class allowing for scene switching
        this.menu = menu;
        //--------------------------------------MULTI THREADING---------------------------------------------------------
        //multi threading will be used to improve overall game quality. by having boards generated on the side, this allows
        //the ui thread to focus on the rendering and moving blocks and updating game state etc, and doesn't require the ui
        //thread to perform the very costly board generation algorithm. these will constantly be generated in the background
        //and added to a queue while the user is playing, even if the user is loading a level they will always be generating boards.
        //once the queue for the states on the thread is full, the thread will sleep

        //the first side thread created will be used to generate easy boards constantly
        new Thread(new Runnable() {
            @Override
            public void run() {
                //infinitely generating boards with a delay
                while (true) {
                    //500ms delay to save ur cpu
                    threadSleep(500);
                    //this is to avoid attempting to write while ui thread is still reading.
                    Platform.runLater(() -> {
                        //update the menu
                        levelExists(m, g);
                    });
                    //generate an easy board
                    g.generateEasyGrid();
                }
            }
            //start the thread
        }).start();

        //this thread will be used for generating medium difficulty boards
        new Thread(new Runnable() {
            @Override
            public void run() {
                //generate boards infinitely
                while (true) {
                    //create a smaller delay as this generation will be harder than the easy generation
                    //and it requires more searches to make a medium board
                    threadSleep(150);
                    //keep generating medium grids
                    g.generateMediumGrid();
                }

            }
            //start the thread
        }).start();

        //this thread will be used for generating hard difficulty boards
        new Thread(new Runnable() {
            @Override
            public void run() {
                //generate hard boards constantly
                while (true) {
                    //reducing the sleep to be able to generate boards of this difficulty, otherwise
                    //it will just use cpu for nothing
                    threadSleep(15);
                    //constantly generate harder difficulty boards
                    g.generateHardGrid();

                }
            }
            //start the thread
        }).start();

        //--------------------------------------------------------------------------------------------------------------
        //this will be the toggle for the game menu
        game.setOnKeyPressed(e -> {
            //if the user presses esc whilst in the game
            if (e.getCode().equals(KeyCode.ESCAPE))
                //we want to toggle the game menu
                g.toggleMenu();
            System.out.println("Easy - " + g.getEasyGrids().size());
            System.out.println("Medium - " + g.getMediumGrids().size());
            System.out.println("Hard - " + g.getHardGrids().size());
        });
        //whilst the user is draggint the vehicle
        game.setOnMouseDragged(e -> {
            //we want to update the gui counter constantly
            g.updateCounter();
        });
        //whhen the user releases the mouse we want to do the following
        game.setOnMouseReleased(e -> {
            //update the counter
            g.updateCounter();
            //if the turn overflow condition is ever met (challange mode)
            if (g.getGrid().turnOverflow()) {
                //generate a new board
                g.reGenerate();
                //update the gui counter
                g.updateCounter();
                //we want to reset the streak since user cheated and used the button
                g.resetStreak();
                //we also want to update the gui for the streak
                g.updateStreakGUI();
            }
            //if the user is victorious!!!
            if (g.getGrid().isVictory()) {
                //generate a new board for the user
                g.reGenerate();
                //update the counter for the user on the bottom of the board
                g.updateCounter();
                //update the streak for the user by increasing by 1 since victorious!
                g.updateStreak();
                //we also want to update the gui for the streak aswell
                g.updateStreakGUI();
            }
        });
        //--------------------------------------------------------------------------------------------------------------
        //main menu buttons
        //if the user picks easy mode standard by pressing the button
        m.getEasyStandard().setOnAction(e -> {
            //switch the scene to the game
            stage.setScene(game);
            //disable challange mode since user chose standard
            g.setChallangerMode(false);
            //want to set the difficulty for the board to be set as easy
            g.getGrid().setDifficulty(Difficulty.EASY);
            //if the menu is still on we want to turn it off incase user went from menu->mainmenu->game need to turn off menu
            if (g.menuOn())
                g.toggleMenu();
            //now we want to load a board for the user, the difficulty is currently set as easy
            g.reGenerate();
            //now we want to update the counter display
            g.updateCounter();
            //current streak is reset to 0 since button was use dto load level
            g.resetStreak();
            //update the gui for the streak display currenty streak
            g.updateStreakGUI();
        });
        //if the user clicks medium mode standard by pressing the button
        m.getMediumStandard().setOnAction(e -> {
            //switch the scene to the game
            stage.setScene(game);
            //disable challange mode since standard was chosen
            g.setChallangerMode(false);
            //we want to now set the difficulty of the grid to be intermediate
            g.getGrid().setDifficulty(Difficulty.MEDIUM);
            //if the menu is on, we want to disable the menu
            if (g.menuOn())
                g.toggleMenu();
            //load a new board with the medium difficulty
            g.reGenerate();
            //update the counter for the gui
            g.updateCounter();
            //reset streak since button was used to load level
            g.resetStreak();
            //update the streak counter on the dashboard
            g.updateStreakGUI();
        });
        //if the user chooses hard and standard from the menu button
        m.getHardStandard().setOnAction(e -> {
            //switch the scene to be the game scene
            stage.setScene(game);
            //disable challange mode for the user since standard was chosen
            g.setChallangerMode(false);
            //we want to set the grid difficulty to hard
            g.getGrid().setDifficulty(Difficulty.HARD);
            //if the menu is still on we want to toggle it off
            if (g.menuOn())
                g.toggleMenu();
            //generate a board of hard difficulty as from above
            g.reGenerate();
            //update the counter graphically on the dashboard
            g.updateCounter();
            //reset streak since button was used
            g.resetStreak();
            //update the streak counter ont he dashboard ui
            g.updateStreakGUI();
        });
        //if the user chooses challange mode on easy
        m.getEasyChallange().setOnAction(e -> {
            //we want to switch the scene to the game scene
            stage.setScene(game);
            //challange mode is on!!
            g.setChallangerMode(true);
            //set difficulty of board generated as easy
            g.getGrid().setDifficulty(Difficulty.EASY);
            //if the menu is still enabled, toggle the menu off
            if (g.menuOn())
                g.toggleMenu();
            //generate the board of easy difficulty
            g.reGenerate();
            //update the movve counter on dashboard since challange mode it will be the estimated turns to solve
            g.updateCounter();
            //reset streak whenever a button is used to load a level
            g.resetStreak();
            //now we want to update the streak on the dashboard
            g.updateStreakGUI();
        });
        //if the user chooses challange mode on medium difficulty
        m.getMediumChallange().setOnAction(e -> {
            //we want to switch the current display scene to the game scene
            stage.setScene(game);
            //enable challange mode
            g.setChallangerMode(true);
            //set the difficulty of the board to be medium
            g.getGrid().setDifficulty(Difficulty.MEDIUM);
            //if the menu is still displaying, retoggle it
            if (g.menuOn())
                g.toggleMenu();
            //load a new board in from the grid generator class
            g.reGenerate();
            //update the dashboard counter on the ui
            g.updateCounter();
            //reset the streak since button was used to load a level
            g.resetStreak();
            //now we want to display the streak on the gui
            g.updateStreakGUI();
        });
        //if the user chooses challange mode on hard difficulty
        m.getHardChallange().setOnAction(e -> {
            //we want to switch the current display scene to the game
            stage.setScene(game);
            //enabling challange mode!
            g.setChallangerMode(true);
            //setting the difficulty of this board to be hard
            g.getGrid().setDifficulty(Difficulty.HARD);
            //if the menu is still left on, toggle it off
            if (g.menuOn())
                g.toggleMenu();
            //load a new board in from the grid generator
            g.reGenerate();
            //update the dashboard gui with the counter
            g.updateCounter();
            //reset streak since button used to load level
            g.resetStreak();
            //display the streak on the gui
            g.updateStreakGUI();
        });
        //this will be the event handler for the main menu button in the menu
        g.getMenuBoard().setOnAction(e -> {
            //switch the scene back to the menu
            stage.setScene(menu);
            //and display the scene
            stage.show();
        });
        //initially we want the game to load in the main menu
        stage.setScene(menu);
        //set the title of the application to gridlock
        stage.setTitle("Gridlock");
        //display the main menu and run!
        stage.show();
    }

    /**
     * This method will be used to update the border around the buttons in the main menu which load in levels.
     * The method will check every one of its difficulty queues that it has a level inside. if there is a level stored
     * in, we put a green border over the button indicating no lag when click. otherwise it will remove the border which
     * will indicate there will be ui lag when generating board since one isnt loaded in from queue. This method
     * will guarantee to update the button style on each call.
     *
     * @param m the menu with all of its buttons
     * @param g the grid generator with all the queues for different difficulties
     */
    public void levelExists(Menu m, GridGenerator g) {
        //we want to call in update menu status since this method is called in thread in infinite loop
        //it will allow for non stop updating (some sleep could cause some issues)
        g.updateMenuStatus();
        //if the easy state queue has atleast one state
        if (g.getEasyGrids().size() > 0) {
            //set a green border for BOTH easy mode buttons to green
            m.getEasyChallange().setStyle("-fx-border-color: green; -fx-border-width: 2");
            m.getEasyStandard().setStyle("-fx-border-color: green; -fx-border-width: 2");
        } else {
            //remove the border from the button if otherwise.
            m.getEasyChallange().setStyle("-fx-border-color: none;");
            m.getEasyStandard().setStyle("-fx-border-color: none;");
        }
        //if the medium state queue has atleast one state
        if (g.getMediumGrids().size() > 0) {
            //set a green border for BOTH medium mode buttons to green
            m.getMediumChallange().setStyle("-fx-border-color: green; -fx-border-width: 2");
            m.getMediumStandard().setStyle("-fx-border-color: green; -fx-border-width: 2");
        } else {
            //remove the border from the button if otherwise.
            m.getMediumChallange().setStyle("-fx-border-color: none;");
            m.getMediumStandard().setStyle("-fx-border-color: none;");
        }
        //if the hard state queue has atleast one state
        if (g.getHardGrids().size() > 0) {
            //set a green border for BOTH hard mode buttons to green
            m.getHardChallange().setStyle("-fx-border-color: green; -fx-border-width: 2");
            m.getHardStandard().setStyle("-fx-border-color: green; -fx-border-width: 2");
        } else {
            //remove the border from the button if otherwise.
            m.getHardChallange().setStyle("-fx-border-color: none;");
            m.getHardStandard().setStyle("-fx-border-color: none;");
        }
    }

    /**
     * This method will be used to put the current thread to sleep. this will greatly reduce cpu usage and allow for the game
     * to not be a slideshow haha but no seriously its really choppy without the sleeps.
     *
     * @param time the amount of time the thread will be asleep for
     */
    public void threadSleep(long time) {
        try {
            //put the thread to sleep for the given time
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}