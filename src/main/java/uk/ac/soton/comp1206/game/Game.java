package uk.ac.soton.comp1206.game;



import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.component.GameBlock;
import uk.ac.soton.comp1206.event.GameLoopHandler;
import uk.ac.soton.comp1206.event.GameOverHandler;
import uk.ac.soton.comp1206.utilities.Multimedia;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


/**
 * The Game class handles the main logic, state and properties of the TetrECS game. Methods to manipulate the game state
 * and to handle actions made by the player should take place inside this class.
 */
public class Game {

    /**
     * Declare the variables we are going to use
     */
    private static final Logger logger = LogManager.getLogger(Game.class);
    private GamePiece currentPiece;
    private GamePiece followingPiece;
    public GameLoopHandler gameLoopHandler;
    public GameOverHandler gameOverHandler;
    Random rand = new Random();
    private final Multimedia media = new Multimedia();
    protected final int rows;
    protected final int cols;
    protected final Grid grid;
    public static boolean isMusicOnGame = true;
    public static boolean isAudioOnGame = true;
    private boolean noRepeat=false;
    private boolean piecePlayed;
    private int time=12000;
    public static boolean isUpgradeOn = false;
    protected Timer timer = new Timer();
    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            Platform.runLater(() -> {
                if(getLives() > 0){
                    gameLoop();
                } else {
                    onGameOver();
                }
            });
        }
    };
    private final SimpleIntegerProperty gameScore = new SimpleIntegerProperty(0);
    private final SimpleIntegerProperty gameLevel = new SimpleIntegerProperty(0);
    private final SimpleIntegerProperty gameLives = new SimpleIntegerProperty(3);
    private final SimpleIntegerProperty multiplier = new SimpleIntegerProperty(1);

    /**
     * Create a new game with the specified rows and columns. Creates a corresponding grid model.
     * @param cols number of columns
     * @param rows number of rows
     */
    public Game(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;
        this.grid = new Grid(cols,rows);
        currentPiece = this.spawnPiece();
        followingPiece = this.spawnPiece();
        gameLoopHandler = null;
    }

    /**
     * Starting the game
     */
    public void start() {
        logger.info("Starting game");
        //Start the timer as soon as we start the game
        timer.scheduleAtFixedRate(timerTask, getTimerDelay(), getTimerDelay());

    }

    /**
     * Handle what should happen when a particular block is clicked
     * @param gameBlock the block that was clicked
     */
    public void blockClicked(GameBlock gameBlock) {
        int x = gameBlock.getX();
        int y = gameBlock.getY();

        //Updating the grid if a piece could be played or not
        if(grid.canPlayPiece(currentPiece,x,y)){
            grid.playPiece(currentPiece,x,y);
            //Spawning  a nextPiece and handling the afterPiece method to check if a line was cleared or not
            this.nextPiece();
            this.afterPiece();
            //A boolean variable to keep track whether a piece was played or not
            piecePlayed = true;
            logger.info("Piece was played successfully");
        } else {
            piecePlayed = false;
            logger.info("Cant play the piece");
        }
    }

    /**
     * Get the grid model inside this game
     */
    public Grid getGrid() {
        return grid;
    }

    /**
     * Generate a random piece
     * @return a random piece in a 14 range domain
     */
    public GamePiece spawnPiece(){
        Random random = new Random();
        logger.info("A new piece was created");
        return GamePiece.createPiece(random.nextInt(14));
    }

    /**
     * Generate another piece
     */
    public void nextPiece(){
        //Making the currentPiece the followingPiece and than making a new piece for the followingPiece
        currentPiece = followingPiece;
        followingPiece = this.spawnPiece();
    }

    /**
     * Handling what happens after a piece was played
     */
    public void afterPiece(){
        //Initially setting the variables to zero and declaring a two dimensional matrix a to keep track of the the values
        //and calculate easier if we need to clear a line
        int clearedBlocks = 0;
        int clearedLines = 0;
        int[][] a = new int[5][5];

        //Check if there is a vertical row that we need to clear
        for(int x = 0; x < grid.getCols(); x++) {
            int blocks = 0;
            for(int y = 0; y < grid.getRows(); y++) {
                if (grid.get(x,y) != 0){
                    blocks++;
                }
            }
            if (blocks == grid.getCols()){
                clearedLines = clearedLines + 1;
                for(int y = 0; y < grid.getRows(); y++) {
                    a[x][y] = 1;
                }
            }
        }

        //Checking if there is a horizontal row that we need to clear
        for(int y = 0; y < grid.getRows(); ++y) {
            int blocks = 0;
            for(int x = 0; x < grid.getCols(); ++x) {
                if (grid.get(x,y) != 0){
                    blocks++;
                }
            }
            if (blocks == grid.getRows()){
                clearedLines = clearedLines + 1;
                for(int x = 0; x < grid.getCols(); ++x) {
                    a[x][y] = 1;
                }
            }
        }

        //Setting all of the block values that was cleared to 0 so it enables us to play another piece on the tile
        for(int y = 0; y < grid.getRows(); ++y) {
            for (int x = 0; x < grid.getCols(); ++x) {
                if(a[x][y] == 1){
                    grid.set(x,y,0);
                    clearedBlocks = clearedBlocks + 1;
                }
            }
        }

        //If a line was cleared and the audio is turned on we play the clear sound
        if(clearedLines > 0){
            if(isAudioOnGame) media.playAudio("clear.wav");
        }

        //If we cleared a line we get the score and if upgrades are turned on we handle it
        if(clearedLines > 0) {
            //Has a random chance to give extra scores or extra time after a line has been cleared
            if(isUpgradeOn){
                logger.info("Went into upgrades");
                int i = rand.nextInt(20);
                if(i>15) setScore(getScore()+200);
                if(i==12 || i==13) time = time + 500;
            }
            this.score(clearedLines, clearedBlocks);
        }

        //Set the multiplier
        this.multiplier(clearedLines);
        //Set the level
        this.level();
        //Call the gameLoop and the createNewTimerTask
        onGameLoop();
        createNewTimerTask();
    }

    /**
     * Getting and setting level, score, multiplier and lives
     */
    public SimpleIntegerProperty levelProperty() {
        return gameLevel;
    }
    public SimpleIntegerProperty livesProperty() {
        return gameLives;
    }
    public SimpleIntegerProperty scoreProperty() {
        return gameScore;
    }
    public SimpleIntegerProperty multiplierProperty() {
        return multiplier;
    }
    public int getLevel(){
        return levelProperty().get();
    }
    public int getLives(){
        return livesProperty().get();
    }
    public int getScore(){
        return scoreProperty().get();
    }
    public int getMultiplier(){
        return multiplierProperty().get();
    }
    public void setLevel(int x){
        levelProperty().set(x);
    }
    public void setLives(int x){
        livesProperty().set(x);
    }
    public void setScore(int x){
        scoreProperty().set(x);
    }
    public void setMultiplier(int x){
        multiplierProperty().set(x);
    }

    /**
     * Calculating the score, multiplier and level
     */
    public void score(int numberOfLines, int numberOfBlocks){
        //Checking if we cleared a line and than setting the score
        if(numberOfLines > 0){
            this.setScore( this.getScore() + (numberOfLines * numberOfBlocks * 10 * this.getMultiplier()));
            logger.info("Calculated the current score");
        }
    }
    public void multiplier(int numberOfLines){
        //Setting the multiplier if we cleared a line
        if(numberOfLines > 0){
            this.setMultiplier(this.getMultiplier()+1);
        } else {
            this.setMultiplier(1);
        }
    }
    public void level(){
        //Setting the level of the game
        int increaseLevel = 0;
        int scoreToNextLevel = this.getScore() - (1000*this.getLevel());
        //scoreToNextLevel = scoreToNextLevel - 1000;
        while((scoreToNextLevel-1000) >= 0){
            increaseLevel = increaseLevel + 1;
            scoreToNextLevel = scoreToNextLevel - 1000;
        }
        this.setLevel(this.getLevel() + increaseLevel);
        logger.info("Calculated level " + this.getLevel());
    }

    /**
     * Return current and following piece and if it was played
     */
    public GamePiece getCurrentPiece(){
        return currentPiece;
    }
    public GamePiece getFollowingPiece() {
        return followingPiece;
    }
    public boolean getIfPlayed(){
        return piecePlayed;
    }

    /**
     * Rotating a piece
     */
    public void rotateCurrentPiece(int rotationNumber){
        logger.info("Rotated the piece");
        if(isAudioOnGame) media.playAudio("rotate.wav");
        currentPiece.rotate(rotationNumber);
    }

    /**
     * Swapping the current piece with the following piece
     */
    public void swapCurrentPiece(){
        //Adding a temporary piece to easily swap the current piece and next piece
        if(isAudioOnGame) media.playAudio("place.wav");
        GamePiece changePiece = currentPiece;
        currentPiece = followingPiece;
        followingPiece = changePiece;
    }


    /**
     * Playing the piece
     * @param x the x coordinate where we want to play the piece
     * @param y the y coordinate where we want to play the piece
     */
    public void playPiece(int x, int y){
        if(grid.canPlayPiece(currentPiece,x,y)){
            logger.info("Piece was played successfully");
            grid.playPiece(currentPiece,x,y);
            //Calling the nextPiece and afterPiece method to make a new piece and handle when we put down a piece
            this.nextPiece();
            this.afterPiece();
            piecePlayed = true;
        }
    }

    /**
     * Giving the gameLoop a value
     * @param handler the value we give
     */
    public void setOnGameLoop(GameLoopHandler handler){
        this.gameLoopHandler = handler;
    }

    /**
     * Check if the gameLoopHandler has any value and than activating the listener
     */
    private void onGameLoop() {
            if(gameLoopHandler != null) {
                gameLoopHandler.gameLoop();
            }
    }

    /**
     * Giving the gameOverHandler a value
     * @param handler the value we give it
     */
    public void setOnGameOver(GameOverHandler handler){
        this.gameOverHandler = handler;
    }

    /**
     * Activating the gameOverHandler if it has a value and if the noRepeat is false
     */
    private void onGameOver() {
        if(gameOverHandler != null) {
            if(!noRepeat){
                noRepeat = true;
                gameOverHandler.gameOver();
            }
        }
    }

    /**
     * Calculating the time we have for the game
     * @return the time we have
     */
    public int getTimerDelay(){
        return Math.max(time - 500 * this.getLevel(), 2500);
    }

    /**
     * Handling the gameLoop
     */
    public void gameLoop(){
        this.setMultiplier(1);
        this.decreaseLive();
        this.nextPiece();
        this.nextPiece();
        this.afterPiece();

    }

    /**
     * Decrease the live if it was called
     */
    public void decreaseLive(){
        this.setLives(Math.max(0,this.getLives()-1));
    }

    /**
     * Creating a new Timer Task to handle the time spent
     */
    private void createNewTimerTask() {
        timerTask.cancel();
        timer.cancel();
        timer.purge();
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    if (getLives() > 0) {
                        gameLoop();
                    } else {
                        onGameOver();
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask, getTimerDelay(), getTimerDelay());
    }

    /**
     * Handling if we need music
     * @param isMusicOn whether we want the music to be turned on or off
     */
    public static void musicOn(boolean isMusicOn){
        isMusicOnGame = isMusicOn;
    }

    /**
     * Handling if we need audio
     * @param isAudioOn whether we want the music to be turned on or off
     */
    public static void audioOn(boolean isAudioOn){
        isAudioOnGame = isAudioOn;
    }
}
