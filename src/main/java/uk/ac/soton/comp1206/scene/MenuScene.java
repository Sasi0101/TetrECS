package uk.ac.soton.comp1206.scene;
import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.App;
import uk.ac.soton.comp1206.ui.GamePane;
import uk.ac.soton.comp1206.ui.GameWindow;
import uk.ac.soton.comp1206.utilities.Multimedia;



/**
 * The main menu of the game. Provides a gateway to the rest of the game.
 */
public class MenuScene extends BaseScene{

    private static final Logger logger = LogManager.getLogger(MenuScene.class);
    private final Multimedia media = new Multimedia();
    BorderPane mainPane = new BorderPane();
    public static boolean isMusicOnMenuScene= true;
    public static boolean isAudioOnMenuScene = true;


    /**
     * Create a new menu scene
     * @param gameWindow the Game Window this will be displayed in
     */
    public MenuScene(GameWindow gameWindow) {
        super(gameWindow);
        logger.info("Creating Menu Scene");
    }

    /**
     * Build the menu layout
     */

    @Override
    public void build() {
        logger.info("Building " + this.getClass().getName());
        root = new GamePane(gameWindow.getWidth(),gameWindow.getHeight());

        var menuPane = new StackPane();
        menuPane.setMaxWidth(gameWindow.getWidth());
        menuPane.setMaxHeight(gameWindow.getHeight());
        menuPane.getStyleClass().add("menu-background");
        root.getChildren().add(menuPane);
        menuPane.getChildren().add(mainPane);

        //The tetrECS title with animation
        animateTitle();

        //Write the texts
        texts();

        //Playing music if isMusicOnMenuScene true
        if(isMusicOnMenuScene) media.playMusic("menu.mp3");

    }

    /**
     * Initialise the menu
     */
    @Override
    public void initialise() {
        getScene().setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                App.getInstance().shutdown();
            }
        });
    }

    /**
     * Create the title animation and position it at the top part of the borderpane
     */
    private void animateTitle(){
        //Loading the tetrECS image and adding it to the mainPane
        var tetrECS = new ImageView(new Image(getClass().getResource("/images/TetrECS.png").toExternalForm()));
        tetrECS.setFitWidth(gameWindow.getHeight());
        tetrECS.setPreserveRatio(true);
        BorderPane.setAlignment(tetrECS, Pos.CENTER);
        BorderPane.setMargin(tetrECS, new Insets(120,0,0,0));
        mainPane.setTop(tetrECS);

        //Making the picture to rotate
        RotateTransition rotate = new RotateTransition(new Duration(2500), tetrECS);
        rotate.setCycleCount(-1);
        rotate.setFromAngle(-5);
        rotate.setToAngle(5);
        rotate.setAutoReverse(true);
        rotate.play();
    }


    /**
     * Displays what options to choose from the single player or the multi player or to see how to play it
     * or open the settings ot exit the game
     */
    private void texts(){
        VBox texts = new VBox();
        texts.setPrefWidth(150);
        texts.setPadding(new Insets(0, 0,0, 0));
        texts.setSpacing(20);
        texts.setAlignment(Pos.CENTER);
        texts.getStylesheets().add(getClass().getResource("/style/game.css").toExternalForm());

        Label singlePlayer = new Label("Single Player");
        singlePlayer.getStyleClass().add("menuItem");
        texts.getChildren().add(singlePlayer);


        Label multiPLayer = new Label("Multi Player");
        multiPLayer.getStyleClass().add("menuItem");
        texts.getChildren().add(multiPLayer);

        Label howToPlay = new Label("How To Play");
        howToPlay.getStyleClass().add("menuItem");
        texts.getChildren().add(howToPlay);

        Label settings = new Label("Settings");
        settings.getStyleClass().add("menuItem");
        texts.getChildren().add(settings);

        Label exit = new Label("Exit");
        exit.getStyleClass().add("menuItem");
        texts.getChildren().add(exit);

        mainPane.setCenter(texts);

        //If a label was clicked we decide what to do with it
        singlePlayer.setOnMouseClicked((e) -> this.startGame());
        howToPlay.setOnMouseClicked((e) -> this.startInstructions());
        settings.setOnMouseClicked((e) -> this.startSettingsScene());
        exit.setOnMouseClicked((e)->App.getInstance().shutdown());
    }

    /**
     * Handle when the single player label is pressed
     */
    public void startGame() {
        gameWindow.startChallenge();
        this.media.getMusicPlayer().pause();
        if(isAudioOnMenuScene) media.playAudio("transition.wav");
    }

    /**
     * Handle when instructions label is pressed
     */
    private void startInstructions(){
        gameWindow.startInstructionScene();
        this.media.getMusicPlayer().pause();
        if(isAudioOnMenuScene) media.playAudio("transition.wav");
    }

    /**
     * Handle when the settings scene label is pressed
     */
    private void startSettingsScene(){
        gameWindow.startSettingsScene();
        this.media.getMusicPlayer().pause();
        if(isAudioOnMenuScene) media.playAudio("transition.wav");
    }

    /**
     * Handling if the music is on or off
     * @param isMusicOn the boolean value deciding whether the music is on or off
     */
    public static void musicOn(boolean isMusicOn){
        isMusicOnMenuScene = isMusicOn;
    }

    /**
     * Handling if audio is on or off
     * @param isAudioOn the boolean value deciding whether the audio is on or off
     */
    public static void audioOn(boolean isAudioOn){
        isAudioOnMenuScene = isAudioOn;
    }


}
