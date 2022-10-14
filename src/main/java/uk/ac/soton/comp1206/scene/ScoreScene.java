package uk.ac.soton.comp1206.scene;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.game.Game;
import uk.ac.soton.comp1206.ui.GamePane;
import uk.ac.soton.comp1206.ui.GameWindow;
import uk.ac.soton.comp1206.utilities.Multimedia;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ScoreScene extends BaseScene{
    /**
     * Declaring the variables we are going to use
     */
    private static final Logger logger = LogManager.getLogger(ScoreScene.class);
    static Multimedia media = new Multimedia();
    BorderPane mainPane = new BorderPane();
    private final int currentScore;
    private final VBox vBox = new VBox();
    private final VBox onlineScoreVBox = new VBox();
    private final VBox scorePrint = new VBox();
    private TextField name;
    static File scoreFile;
    static String[] scores;
    static List<String> lines;
    public static boolean isMusicOnScoreScene = true;
    public static boolean isAudioOnScoreScene = true;
    private boolean ifHighScore= false;
    String[] check;
    private String finalName;
    //Make the returnHighScore static so we can access it from the ChallengeScene
    static SimpleIntegerProperty returnHighScore = new SimpleIntegerProperty(makeFile());


    /**
     * Create a new scene, passing in the GameWindow the scene will be displayed in
     *
     * @param gameWindow the game window
     */
    public ScoreScene(GameWindow gameWindow, Game game) {
        super(gameWindow);
        logger.info("Loading ScoreScene");
        currentScore  = game.getScore();
    }

    @Override
    public void initialise() {

        //Handling what happens when Escape or Enter was pressed
        getScene().setOnKeyPressed((e) -> {
            if(e.getCode() == KeyCode.ESCAPE) {
                gameWindow.startMenu();
                media.getMusicPlayer().pause();
                if(isAudioOnScoreScene) media.playAudio("transition.wav");
            }
            if((e.getCode() == KeyCode.ENTER) && (ifHighScore)){
                afterNameTyped();
                printOutScores();
                mainPane.setLeft(scorePrint);
            }
        });
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
        title();


        //Checking whether or not we have to add a high score
        try {

            //Open the scores.txt file and add it to a List<String>
            lines = Files.readAllLines(Paths.get("scores.txt"));
            scores = lines.get(9).split(": ");

            //Call the ifHighScore method if the currentScore is bigger than the last one in the local scores
            //and making the ifHighScore boolean true
            if(Integer.parseInt(scores[1]) < currentScore){
                ifHighScore = true;
                ifHighScore();

            }else{
                //Asking for the HISCORES from the server
                gameWindow.getCommunicator().addListener(this::loadOnlineScores);
                gameWindow.getCommunicator().send("HISCORES");
                try {
                    Thread.sleep(200);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }

                //Printing out the scores
                printOutScores();

                Label highScores = new Label("High Scores");
                highScores.getStyleClass().add("title");
                vBox.getChildren().add(highScores);
                mainPane.setLeft(scorePrint);

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        mainPane.setTop(vBox);

    }

    /**
     * Loading the online scores and
     * @param s the online high scores in form of a string
     */
    public void loadOnlineScores(String s) {

        //Separating the string by "\n"
        String[] onlineScores = s.split("\n");
        //In the first string we replace the HISCORE with "" so all of the string values will look alike
        onlineScores[0] = onlineScores[0].replaceAll("HISCORES " , "");

        Label onlineTitle = new Label("Online Scores");
        onlineTitle.getStyleClass().add("title");
        onlineScoreVBox.getChildren().add(onlineTitle);

        onlineScoreVBox.setSpacing(1);
        onlineScoreVBox.setPrefWidth((double)this.gameWindow.getWidth()/2);
        onlineScoreVBox.setPadding(new Insets(0,100,0,0));
        onlineScoreVBox.setAlignment(Pos.CENTER);

        //Loading the online scores and printing the out with different colours
        for(int i=0; i<10; i++){
            check = onlineScores[i].split(":");
            if(Integer.parseInt(check[1]) > currentScore){
                Label label = new Label(check[0] + ":" + check[1]);
                if(i == 0) label.getStyleClass().add("pinkscoreitem");
                if(i == 1) label.getStyleClass().add("redscoreitem");
                if(i == 2) label.getStyleClass().add("orangescoreitem");
                if(i == 3) label.getStyleClass().add("yellowscoreitem");
                if(i == 4) label.getStyleClass().add("lawngreenscoreitem");
                if(i == 5) label.getStyleClass().add("greenscoreitem");
                if(i == 6) label.getStyleClass().add("forestgreenscoreitem");
                if(i == 7) label.getStyleClass().add("darkgreenscoreitem");
                if(i == 8) label.getStyleClass().add("cyanscoreitem");
                if(i == 9) label.getStyleClass().add("bluescoreitem");
                onlineScoreVBox.getChildren().add(label);
            } else{
                //Adding our score to the online scores list
                Label label = new Label(name + ":" + currentScore);
                if(i == 0) label.getStyleClass().add("pinkscoreitem");
                if(i == 1) label.getStyleClass().add("redscoreitem");
                if(i == 2) label.getStyleClass().add("orangescoreitem");
                if(i == 3) label.getStyleClass().add("yellowscoreitem");
                if(i == 4) label.getStyleClass().add("lawngreenscoreitem");
                if(i == 5) label.getStyleClass().add("greenscoreitem");
                if(i == 6) label.getStyleClass().add("forestgreenscoreitem");
                if(i == 7) label.getStyleClass().add("darkgreenscoreitem");
                if(i == 8) label.getStyleClass().add("cyanscoreitem");
                if(i == 9) label.getStyleClass().add("bluescoreitem");
                onlineScoreVBox.getChildren().add(label);
                for(int j=i; j<9; j++){
                    //Adding the rest of the scores to the online scores list
                    check = onlineScores[j].split(":");
                    Label label2 = new Label(check[0] + ":" + check[1]);
                    if(i == 0) label2.getStyleClass().add("pinkscoreitem");
                    if(i == 1) label2.getStyleClass().add("redscoreitem");
                    if(i == 2) label2.getStyleClass().add("orangescoreitem");
                    if(i == 3) label2.getStyleClass().add("yellowscoreitem");
                    if(i == 4) label2.getStyleClass().add("lawngreenscoreitem");
                    if(i == 5) label2.getStyleClass().add("greenscoreitem");
                    if(i == 6) label2.getStyleClass().add("forestgreenscoreitem");
                    if(i == 7) label2.getStyleClass().add("darkgreenscoreitem");
                    if(i == 8) label2.getStyleClass().add("cyanscoreitem");
                    onlineScoreVBox.getChildren().add(label2);
                }
                i=10;
            }
        }

        mainPane.setRight(onlineScoreVBox);

    }


    /**
     * Displaying the title and the picture.
     */
    private void title(){

        vBox.setPrefWidth(150);
        vBox.setSpacing(10);
        vBox.setAlignment(Pos.TOP_CENTER);

        var tetrECS = new ImageView(new Image(getClass().getResource("/images/TetrECS.png").toExternalForm()));
        tetrECS.setFitWidth(gameWindow.getHeight());
        tetrECS.setPreserveRatio(true);
        BorderPane.setAlignment(tetrECS, Pos.TOP_CENTER);
        vBox.getChildren().add(0,tetrECS);

        Label gameOver = new Label("Game Over");
        gameOver.getStyleClass().add("bigtitle");

        vBox.getChildren().add(1,gameOver);
    }

    /**
     * Handle what happens if a high score was achieved.
     */
    private void ifHighScore(){
        name = new TextField();
        name.setPromptText("Enter your name");
        name.setPrefWidth((float)(this.gameWindow.getWidth() / 2));
        name.requestFocus();
        vBox.getChildren().add(2,name);

        Button button = new Button("Submit");
        button.setDefaultButton(true);
        vBox.getChildren().add(3, button);

        Label youGotAHighScore = new Label("You got a High Score!");
        youGotAHighScore.getStyleClass().add("title");
        vBox.getChildren().add(4,youGotAHighScore);

        //If the button was clicked we call the afterNameTyped and printOutScores methods
        button.setOnMouseClicked(e -> {
            if(e.getButton() == MouseButton.PRIMARY) {
                afterNameTyped();
                printOutScores();
                gameWindow.startHighScoreScene(finalName, vBox, onlineScoreVBox, scorePrint);
            }
        });
    }

    /**
     * What happens if the name was typed remove the last two elements of the vBox and than save the name and than
     * adding the scores to the text
     */
    private void afterNameTyped(){
        try {
            lines = Files.readAllLines(Paths.get("scores.txt"));
            scores = lines.get(9).split(": ");
            if(Integer.parseInt(scores[1]) < currentScore){

                //Opening a fileWriter so we can append to the scores.txt
                FileWriter writeScore = new FileWriter("scores.txt");
                for(int i=0; i<10; i++){
                    scores = lines.get(i).split(": ");

                    //If our score do not exceeds the scores in the file we just write the regular score to the list
                    if(Integer.parseInt(scores[1]) > currentScore){
                        writeScore.append(lines.get(i)).append("\n");
                    } else {

                        //We add our score to the file and than the rest of it
                        finalName = name.getText().replace(":", "");
                        writeScore.append(name.getText().replace(":", "")).append(": ").append(String.valueOf(currentScore)).append("\n");
                        for(int j=i; j<9; j++){
                            writeScore.append(lines.get(j)).append("\n");
                        }
                        i=10;
                    }

                }

                //Closing the fileWriter
                writeScore.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        vBox.getChildren().remove(2);
        vBox.getChildren().remove(2);
        vBox.getChildren().remove(2);
        Label youGotAHighScore = new Label("You got a High Score!");
        youGotAHighScore.getStyleClass().add("title");
        vBox.getChildren().add(youGotAHighScore);
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Sending our score to the server
        gameWindow.getCommunicator().send("HISCORE " + finalName + ":" + currentScore);
        try {
            Thread.sleep(200);
        } catch (InterruptedException c) {
            c.printStackTrace();
        }
    }

    /**
     * Check if the file exist if not than make a new one and returning the high score.
     */
    static int makeFile(){
       try{
           scoreFile = new File("scores.txt");
           if(scoreFile.createNewFile()){
               logger.info("File was created at " + scoreFile.getAbsolutePath());
               FileWriter writeScore = new FileWriter("scores.txt");
               for(int i=10; i>0; i--){
                   writeScore.write("Oli: " + i*1000 + "\n");
               }
               writeScore.close();
           } else {
               logger.info("File already exists");
           }
       } catch (IOException e){
            logger.info("Exception caught " + e);
       }
        try {
            //We split the first line to the name and score
            lines = Files.readAllLines(Paths.get("scores.txt"));
            scores = lines.get(0).split(": ");

        } catch (IOException e) {
            e.printStackTrace();
        }

        //Return the highest score in the list
        return Integer.parseInt(scores[1]);
    }

    /**
     * This method will write out the local scores.
     */
    private void printOutScores(){
        scorePrint.setSpacing(1);
        scorePrint.setPrefWidth((double)this.gameWindow.getWidth()/2);
        scorePrint.setPadding(new Insets(0,0,0,100));
        scorePrint.setAlignment(Pos.CENTER);
        Label localScore = new Label("Local Scores");
        localScore.getStyleClass().add("title");
        scorePrint.getChildren().add(localScore);
        //We get the lines in the score.txt file and adding it to the scorePrint
        try {
            lines = Files.readAllLines(Paths.get("scores.txt"));
            for(int i=0; i<10; i++){
                Label writeOut = new Label(lines.get(i));
                if(i == 0) writeOut.getStyleClass().add("pinkscoreitem");
                if(i == 1) writeOut.getStyleClass().add("redscoreitem");
                if(i == 2) writeOut.getStyleClass().add("orangescoreitem");
                if(i == 3) writeOut.getStyleClass().add("yellowscoreitem");
                if(i == 4) writeOut.getStyleClass().add("lawngreenscoreitem");
                if(i == 5) writeOut.getStyleClass().add("greenscoreitem");
                if(i == 6) writeOut.getStyleClass().add("forestgreenscoreitem");
                if(i == 7) writeOut.getStyleClass().add("darkgreenscoreitem");
                if(i == 8) writeOut.getStyleClass().add("cyanscoreitem");
                if(i == 9) writeOut.getStyleClass().add("bluescoreitem");
                scorePrint.getChildren().add(writeOut);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Deciding if we need music or not
     * @param isMusicOn boolean value that decides whether we need music or not
     */
    public static void musicOn(boolean isMusicOn){
        isMusicOnScoreScene = isMusicOn;
    }

    /**
     * Deciding if we need audio or not
     * @param isAudioOn boolean value that decides whether we need audio or not
     */
    public static void audioOn(boolean isAudioOn){
        isAudioOnScoreScene = isAudioOn;
    }

}
