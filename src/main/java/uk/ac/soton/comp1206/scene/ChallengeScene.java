package uk.ac.soton.comp1206.scene;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.component.GameBlock;
import uk.ac.soton.comp1206.component.GameBoard;
import uk.ac.soton.comp1206.component.PieceBoard;
import uk.ac.soton.comp1206.component.TimeBar;
import uk.ac.soton.comp1206.game.Game;
import uk.ac.soton.comp1206.ui.GamePane;
import uk.ac.soton.comp1206.ui.GameWindow;
import uk.ac.soton.comp1206.utilities.Multimedia;

/**
 * The Single Player challenge scene. Holds the UI for the single player challenge mode in the game.
 */
public class ChallengeScene extends BaseScene {

    /**
     * Declaring the variables we are going to use
     */
    GameBoard board;
    private static final Logger logger = LogManager.getLogger(ChallengeScene.class);
    protected Game game;
    private final Multimedia media = new Multimedia();
    VBox sideBar;
    PieceBoard pieceBoard1, pieceBoard2;
    BorderPane mainPane = new BorderPane();
    int colourValue;
    private boolean escWasPressed = false;
    int x,y;
    TimeBar timer;
    static boolean isMusicOnChallengeScene = true;
    static boolean isAudioOnChallengeScene = true;
    StackPane challengePane = new StackPane();


    /**
     * Create a new Single Player challenge scene
     * @param gameWindow the Game Window
     */
    public ChallengeScene(GameWindow gameWindow) {
        super(gameWindow);
        logger.info("Creating Menu Scene");
    }

    /**
     * Build the Challenge window
     */
    @Override
    public void build() {
        logger.info("Building " + this.getClass().getName());
        setupGame();
        //Adding the root a value and a background style depending on the choice in the settings scene and than adding
        //the root to the challengePane
        root = new GamePane(gameWindow.getWidth(),gameWindow.getHeight());
        challengePane.setMaxWidth(gameWindow.getWidth());
        challengePane.setMaxHeight(gameWindow.getHeight());
        if(SettingsScene.backgroundNumber == 1) challengePane.getStyleClass().add("menu-background");
        if(SettingsScene.backgroundNumber == 2) challengePane.getStyleClass().add("menu-background2");
        if(SettingsScene.backgroundNumber == 3) challengePane.getStyleClass().add("menu-background3");
        if(SettingsScene.backgroundNumber == 4) challengePane.getStyleClass().add("menu-background4");
        if(SettingsScene.backgroundNumber == 5) challengePane.getStyleClass().add("menu-background5");
        if(SettingsScene.backgroundNumber == 6) challengePane.getStyleClass().add("menu-background6");
        root.getChildren().add(challengePane);

        //Adding the mainPane to the challengePane
        challengePane.getChildren().add(mainPane);

        //Making a new gameBoard
        board = new GameBoard(game.getGrid(),(float)gameWindow.getWidth()/2,(float)gameWindow.getWidth()/2);
        mainPane.setCenter(board);

        //Display information and title and the piece as well as the nextPiece and followingPiece
        verticalBox();
        horizontalBox();

        //Giving the listeners values and handling them
        board.setOnBlockClick(this::blockClicked);
        board.setOnRightClicked(this::rightClicked);
        game.setOnGameLoop(this::onGameLoop);
        game.setOnGameOver(this::onGameOver);
        pieceBoard1.setOnLeftClicked(this::leftClicked);

        //Creating a new TimeBar and adding it to the challengePane
        timer = new TimeBar();
        BorderPane.setMargin(challengePane, new Insets(5.0, 5.0, 5.0, 5.0));
        challengePane.getChildren().add(timer);
        StackPane.setAlignment(timer, Pos.BOTTOM_LEFT);
    }


    /**
     * Handling if there was a left click on the pieceBoard
     */
    private void leftClicked() {
        game.rotateCurrentPiece(1);
        this.blockRotated();
    }

    /**
     * Handling if the game is over
     */
    private void onGameOver() {
        if(!escWasPressed) {
            gameWindow.startScoreScene(game);
            this.media.getMusicPlayer().pause();
            if (isAudioOnChallengeScene) media.playAudio("transition.wav");
        }

    }

    /**
     * Handling the game loop if it was called
     */
    private void onGameLoop() {
        pieceBoard1.setPiece(game);
        pieceBoard2.setFollowingPiece(game);
        timeLine();

    }

    /**
     * Handling if there was a rightClick on the gameBoard
     * @param gameBoard the gameBoard we are using
     */
    private void rightClicked(GameBoard gameBoard) {
        game.rotateCurrentPiece(1);
        this.blockRotated();
    }


    /**
     * Handle when a block is clicked
     * @param gameBlock the Game Block that was clocked
     */
    private void blockClicked(GameBlock gameBlock) {
        game.blockClicked(gameBlock);
        if(game.getIfPlayed()){
            if(isAudioOnChallengeScene) media.playAudio("place.wav");
        }
    }

    /**
     * Setup the game object and model
     */
    public void setupGame() {
        logger.info("Starting a new challenge");
        game = new Game(5, 5);
    }

    /**
     * VerticalBox for displaying information
     */
    private void verticalBox(){
        sideBar = new VBox();
        sideBar.setPrefWidth(150);
        sideBar.setPadding(new Insets(0, 10, 0, 20));
        sideBar.setSpacing(10);
        sideBar.setAlignment(Pos.BASELINE_RIGHT);
        sideBar.getStylesheets().add(getClass().getResource("/style/game.css").toExternalForm());

        Label highScoreText = new Label("High Score");
        highScoreText.getStyleClass().add("heading");
        sideBar.getChildren().add(highScoreText);

        Label highScore = new Label();
        highScore.textProperty().bind(ScoreScene.returnHighScore.asString());
        highScore.getStyleClass().add("level");
        sideBar.getChildren().add(highScore);

        Label levelText = new Label("Level");
        levelText.getStyleClass().add("heading");
        sideBar.getChildren().add(levelText);

        Label level = new Label();
        level.textProperty().bind(game.levelProperty().asString());
        level.getStyleClass().add("level");
        sideBar.getChildren().add(level);

        Label incomingText = new Label("Incoming");
        incomingText.getStyleClass().add("heading");
        sideBar.getChildren().add(incomingText);

        pieceBoard1 = new PieceBoard(3,3,gameWindow.getWidth() / 6.0, gameWindow.getWidth() / 6.0);
        pieceBoard1.setPiece(game);
        sideBar.getChildren().add(pieceBoard1);
        pieceBoard2 = new PieceBoard(3,3,gameWindow.getWidth() / 10.0, gameWindow.getWidth() / 10.0);
        pieceBoard2.setFollowingPiece(game);
        sideBar.getChildren().add(pieceBoard2);

        mainPane.setRight(sideBar);
    }

    /**
     * Horizontal box for displaying information
     */
    private void horizontalBox(){
        HBox topBar = new HBox();
        topBar.setPrefHeight(25);
        topBar.setPadding(new Insets(0, 100, 0, 100));
        topBar.setSpacing(6);

        VBox vb1 = new VBox();
        vb1.setPadding(new Insets(0, 10, 0, 10));
        vb1.setSpacing(6);
        vb1.setAlignment(Pos.BASELINE_LEFT);
        Label scoreText = new Label("Score");
        scoreText.getStyleClass().add("heading");
        vb1.getChildren().add(scoreText);
        Label score = new Label();
        score.textProperty().bind(game.scoreProperty().asString());
        score.getStyleClass().add("score");
        vb1.getChildren().add(score);
        topBar.getChildren().add(vb1);

        Region blank1 = new Region();
        HBox.setHgrow(blank1, Priority.ALWAYS);
        topBar.getChildren().add(blank1);

        Label challengeModeText = new Label("Challenge Mode");
        challengeModeText.getStyleClass().add("title");
        topBar.getChildren().add(challengeModeText);

        Region blank2 = new Region();
        HBox.setHgrow(blank2, Priority.ALWAYS);
        topBar.getChildren().add(blank2);

        VBox vb2 = new VBox();
        vb2.setPadding(new Insets(0, 10, 0, 10));
        vb2.setSpacing(6);
        vb2.setAlignment(Pos.BASELINE_RIGHT);
        Label livesText = new Label("Lives");
        livesText.getStyleClass().add("heading");
        vb2.getChildren().add(livesText);
        Label lives = new Label();
        lives.textProperty().bind(game.livesProperty().asString());
        lives.getStyleClass().add("lives");
        vb2.getChildren().add(lives);
        topBar.getChildren().add(vb2);
        mainPane.setTop(topBar);
    }

    /**
     * Initialise the scene and start the game and here I handle all the key pressed by the player
     */
    @Override
    public void initialise() {
        logger.info("Initialising Challenge");

        //Playing music if isMusicOnChallengeScene is on
        if(isMusicOnChallengeScene) media.playMusic("game_start.wav");
        timeLine();

        //Handling the different keys pressed
        getScene().setOnKeyPressed((e)->{
            logger.info("Scene " + getScene());
            if(e.getCode() == KeyCode.ESCAPE) {
                escWasPressed = true;
                gameWindow.startMenu();
                this.media.getMusicPlayer().pause();
                if(isAudioOnChallengeScene) media.playAudio("transition.wav");
            }
            if(e.getCode() == KeyCode.E){
                game.rotateCurrentPiece(45);
                pieceBoard1.setPiece(game);
            }
            if(e.getCode() == KeyCode.C){
                game.rotateCurrentPiece(45);
                pieceBoard1.setPiece(game);
            }
            if(e.getCode() == KeyCode.CLOSE_BRACKET){
                game.rotateCurrentPiece(45);
                pieceBoard1.setPiece(game);
            }
            if(e.getCode() == KeyCode.Q){
                game.rotateCurrentPiece(135);
                pieceBoard1.setPiece(game);
            }
            if(e.getCode() == KeyCode.Z){
                game.rotateCurrentPiece(135);
                pieceBoard1.setPiece(game);
            }
            if(e.getCode() == KeyCode.OPEN_BRACKET){
                game.rotateCurrentPiece(135);
                pieceBoard1.setPiece(game);
            }
            if(e.getCode() == KeyCode.SPACE){
                game.swapCurrentPiece();
                pieceBoard1.setPiece(game);
                pieceBoard2.setFollowingPiece(game);
            }
            if(e.getCode() == KeyCode.R){
                game.swapCurrentPiece();
                pieceBoard1.setPiece(game);
                pieceBoard2.setFollowingPiece(game);
            }
            if(e.getCode() == KeyCode.LEFT){
                board.getBlock(x,y).paint();
                if(x>0) x--;
                paintBlock(x,y);
            }
            if(e.getCode() == KeyCode.RIGHT){
                board.getBlock(x,y).paint();
                if(x<4) x++;
                paintBlock(x,y);
            }
            if(e.getCode() == KeyCode.UP){
                board.getBlock(x,y).paint();
                if(y>0) y--;
                paintBlock(x,y);
            }
            if(e.getCode() == KeyCode.DOWN){
                board.getBlock(x,y).paint();
                if(y<4) y++;
                paintBlock(x,y);
            }
            if(e.getCode() == KeyCode.A){
                board.getBlock(x,y).paint();
                if(x>0) x--;
                paintBlock(x,y);
            }
            if(e.getCode() == KeyCode.D){
                board.getBlock(x,y).paint();
                if(x<4) x++;
                paintBlock(x,y);
            }
            if(e.getCode() == KeyCode.W){
                board.getBlock(x,y).paint();
                if(y>0) y--;
                paintBlock(x,y);
            }
            if(e.getCode() == KeyCode.S){
                board.getBlock(x,y).paint();
                if(y<4) y++;
                paintBlock(x,y);
            }
            if(e.getCode() == KeyCode.ENTER){
                if(isAudioOnChallengeScene) media.playAudio("place.wav");
                game.playPiece(x,y);
                pieceBoard1.setPiece(game);
                pieceBoard2.setFollowingPiece(game);
            }
            if(e.getCode() == KeyCode.X){
                if (isMusicOnChallengeScene)media.playAudio("place.wav");
                game.playPiece(x,y);
                pieceBoard1.setPiece(game);
                pieceBoard2.setFollowingPiece(game);
            }
        });
        game.start();
    }

    /**
     * Handling a block rotation and placing again
     */
    public void blockRotated(){
        pieceBoard1.setPiece(game);
        if(isAudioOnChallengeScene) media.playAudio("rotate.wav");
    }

    /**
     * Painting a block
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public void paintBlock(int x, int y){
        colourValue = game.getGrid().get(x,y);
        board.getBlock(x,y).paintForKeyCommands();

    }

    /**
     * Handle the timeline
     */
    public void timeLine(){
        final Timeline timeline = new Timeline();

        //The beginning
        KeyFrame beginning = new KeyFrame(Duration.ZERO, new KeyValue(timer.widthProperty(), challengePane.getWidth()));
        timeline.getKeyFrames().add(beginning);

        //The green part
        KeyFrame greenPart = new KeyFrame(Duration.ZERO, new KeyValue(timer.fillProperty(), Color.GREEN));
        timeline.getKeyFrames().add(greenPart);

        //After a delay change the colour
        KeyFrame yellowPart = new KeyFrame(new Duration(game.getTimerDelay() * 0.5), new KeyValue(timer.fillProperty(), Color.YELLOW));
        timeline.getKeyFrames().add(yellowPart);

        //At the end change it to red
        KeyFrame redPart = new KeyFrame(new Duration(game.getTimerDelay() * 0.75), new KeyValue(timer.fillProperty(), Color.RED));
        timeline.getKeyFrames().add(redPart);

        //End when the time is over
        KeyFrame end = new KeyFrame(new Duration(game.getTimerDelay()), new KeyValue(timer.widthProperty(), 0));
        timeline.getKeyFrames().add(end);

        //Play the timeline
        timeline.play();
    }

    /**
     * Handling if we need music or not
     * @param isMusicOn true or false if we need music or not
     */
    static void musicOn(boolean isMusicOn){
       isMusicOnChallengeScene = isMusicOn;
    }

    /**
     * Handling if we need audio or not
     * @param isAudioOn true or false depending on whether we need audio or not
     */
    static void audioOn(boolean isAudioOn){
        isAudioOnChallengeScene = isAudioOn;
    }
}
