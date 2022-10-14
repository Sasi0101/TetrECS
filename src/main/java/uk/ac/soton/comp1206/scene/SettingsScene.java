package uk.ac.soton.comp1206.scene;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.game.Game;

import uk.ac.soton.comp1206.ui.GamePane;
import uk.ac.soton.comp1206.ui.GameWindow;


public class SettingsScene extends BaseScene{
    /**
     * Declaring the variables we are going to use
     */
    private static final Logger logger = LogManager.getLogger(SettingsScene.class);
    BorderPane mainPane = new BorderPane();
    CheckBox checkBox1, checkBox2, checkBox3, checkBox4, checkBox5, checkBox6, checkBox7, checkBox8, checkBox9;
    VBox vbox = new VBox();
    public static int backgroundNumber=1;

    /**
     * Create a new scene, passing in the GameWindow the scene will be displayed in
     *
     * @param gameWindow the game window
     */
    public SettingsScene(GameWindow gameWindow) {
        super(gameWindow);
    }

    /**
     * Initialising the SettingsScene and handling what happens when a checkBox was selected
     */
    @Override
    public void initialise() {
        getScene().setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                gameWindow.startMenu();
            }
        });
       checkBox1.setOnMouseClicked((e) -> {
            if(!checkBox1.isSelected()) isMusicOn(true);
            if(checkBox1.isSelected()) isMusicOn(false);
        });
       checkBox2.setOnMouseClicked((e) -> {
           if(!checkBox2.isSelected())  isAudioOn(true);
           if(checkBox2.isSelected()) isAudioOn(false);
       });
       checkBox3.setOnMouseClicked((e) -> Game.isUpgradeOn = checkBox3.isSelected());
       checkBox4.setOnMouseClicked((e) -> backgroundNumber = 1);
       checkBox5.setOnMouseClicked((e) -> backgroundNumber = 2);
       checkBox6.setOnMouseClicked((e) -> backgroundNumber = 3);
       checkBox7.setOnMouseClicked((e) -> backgroundNumber = 4);
       checkBox8.setOnMouseClicked((e) -> backgroundNumber = 5);
       checkBox9.setOnMouseClicked((e) -> backgroundNumber = 6);
    }

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

        Label title = new Label("Settings");
        title.getStyleClass().add("bigtitle");
        title.setPrefWidth(gameWindow.getWidth());
        title.setAlignment(Pos.CENTER);
        mainPane.setTop(title);

        //Choose if you want music or not
        HBox music = new HBox();
        music.setAlignment(Pos.CENTER);
        music.setPrefWidth(gameWindow.getWidth());
        music.setSpacing(20);
        Label musicWriting = new Label("Thick if you do not want music");
        music.getChildren().add(musicWriting);
        musicWriting.getStyleClass().add("title");
        checkBox1 = new CheckBox();
        music.getChildren().add(checkBox1);

        //Choose if you want audio or not
        HBox audio = new HBox();
        audio.setAlignment(Pos.CENTER);
        audio.setPrefWidth(gameWindow.getWidth());
        audio.setSpacing(20);
        Label audioWriting = new Label("Thick if you do not want sound effects");
        audio.getChildren().add(audioWriting);
        audioWriting.getStyleClass().add("title");
        checkBox2 = new CheckBox();
        audio.getChildren().add(checkBox2);

        //Choose if you want upgrades or not
        HBox upgrades = new HBox();
        upgrades.setAlignment(Pos.CENTER);
        upgrades.setPrefWidth(gameWindow.getWidth());
        upgrades.setSpacing(20);
        Label upgrade = new Label("Thick if you want upgrades");
        upgrades.getChildren().add(upgrade);
        upgrade.getStyleClass().add("title");
        checkBox3 = new CheckBox();
        upgrades.getChildren().add(checkBox3);

        //Choose the background you want for the ChallengeScene
        vbox.setAlignment(Pos.CENTER);
        vbox.setPrefWidth(gameWindow.getWidth());
        vbox.getChildren().addAll(music, audio, upgrades);
        Label chooseOne = new Label("Choose your background for challengeScene (only one):");
        chooseOne.getStyleClass().add("smallertitle");
        vbox.getChildren().add(chooseOne);
        chooseBackground();
        checkBoxesForBackground();
        mainPane.setCenter(vbox);
    }

    /**
     * Adding images of the backgrounds we can choose
     */
    public void chooseBackground(){
        HBox backgroundImages = new HBox();
        backgroundImages.setPrefWidth(gameWindow.getWidth());
        backgroundImages.setSpacing(10);
        backgroundImages.setPadding(new Insets(0,0,0,15));

        //First backgroundImage
        var firstImage = new ImageView(new Image(getClass().getResource("/images/1.jpg").toExternalForm()));
        firstImage.setFitWidth((float)gameWindow.getHeight()/5);
        firstImage.setPreserveRatio(true);
        backgroundImages.getChildren().add(firstImage);

        //Second backgroundImage
        var secondImage = new ImageView(new Image(getClass().getResource("/images/2.jpg").toExternalForm()));
        secondImage.setFitWidth((float)gameWindow.getHeight()/5);
        secondImage.setPreserveRatio(true);
        backgroundImages.getChildren().add(secondImage);

        //Third backgroundImage
        var thirdImage = new ImageView(new Image(getClass().getResource("/images/3.jpg").toExternalForm()));
        thirdImage.setFitWidth((float)gameWindow.getHeight()/5);
        thirdImage.setPreserveRatio(true);
        backgroundImages.getChildren().add(thirdImage);

        //Fourth backgroundImage
        var fourthImage = new ImageView(new Image(getClass().getResource("/images/4.jpg").toExternalForm()));
        fourthImage.setFitWidth((float)gameWindow.getHeight()/5);
        fourthImage.setPreserveRatio(true);
        backgroundImages.getChildren().add(fourthImage);

        //Fifth backgroundImage
        var fifthImage = new ImageView(new Image(getClass().getResource("/images/5.jpg").toExternalForm()));
        fifthImage.setFitWidth((float)gameWindow.getHeight()/5);
        fifthImage.setPreserveRatio(true);
        backgroundImages.getChildren().add(fifthImage);

        //Sixth backgroundImage
        var sixthImage = new ImageView(new Image(getClass().getResource("/images/6.jpg").toExternalForm()));
        sixthImage.setFitWidth((float)gameWindow.getHeight()/5);
        sixthImage.setPreserveRatio(true);
        backgroundImages.getChildren().add(sixthImage);
        vbox.getChildren().add(backgroundImages);
    }

    /**
     * Adding the checkBoxes to the vbox
     */
    public void checkBoxesForBackground(){
        HBox checkBoxes = new HBox();
        checkBoxes.setPrefWidth(gameWindow.getWidth());
        checkBoxes.setSpacing(113);
        checkBoxes.setPadding(new Insets(0,0,0,70));

        checkBox4 = new CheckBox();
        checkBox5 = new CheckBox();
        checkBox6 = new CheckBox();
        checkBox7 = new CheckBox();
        checkBox8 = new CheckBox();
        checkBox9 = new CheckBox();

        checkBoxes.getChildren().addAll(checkBox4, checkBox5, checkBox6, checkBox7, checkBox8, checkBox9);
        vbox.getChildren().add(checkBoxes);
    }

    /**
     * Handling if we need music or not
     * @param isMusicOn boolean value that decides whether we need music or not
     */
    public void isMusicOn(boolean isMusicOn){
        ChallengeScene.musicOn(isMusicOn);
        Game.musicOn(isMusicOn);
        MenuScene.musicOn(isMusicOn);
        ScoreScene.musicOn(isMusicOn);
    }

    /**
     * Handling if we need audio or not
     * @param isAudioOn boolean value that decides whether we need audio or not
     */
    public void isAudioOn(boolean isAudioOn){
        ChallengeScene.audioOn(isAudioOn);
        Game.audioOn(isAudioOn);
        MenuScene.audioOn(isAudioOn);
        ScoreScene.audioOn(isAudioOn);
    }
}
