package uk.ac.soton.comp1206.scene;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.ui.GamePane;
import uk.ac.soton.comp1206.ui.GameWindow;


public class HighScoreScene extends BaseScene{

    private static final Logger logger = LogManager.getLogger(HighScoreScene.class);
    BorderPane mainPane = new BorderPane();
    private final VBox vBox;
    private final VBox onlineScoreVBox;
    private final VBox scorePrint;
    private final String name;
    String[] check;

    /**
     * Create a new scene, passing in the GameWindow the scene will be displayed in
     *
     * @param gameWindow the game window
     */
    public HighScoreScene(GameWindow gameWindow, String s, VBox vbox1, VBox vbox2, VBox vbox3) {
        super(gameWindow);
        this.name = s;
        this.vBox = vbox1;
        this.onlineScoreVBox= vbox2;
        this.scorePrint = vbox3;
    }

    @Override
    public void initialise() {

    }

    @Override
    public void build() {
        logger.info("Building " + this.getClass().getName());
        root = new GamePane(gameWindow.getWidth(),gameWindow.getHeight());

        //Creating a new StackPane
        var menuPane = new StackPane();
        menuPane.setMaxWidth(gameWindow.getWidth());
        menuPane.setMaxHeight(gameWindow.getHeight());
        menuPane.getStyleClass().add("menu-background");
        root.getChildren().add(menuPane);
        menuPane.getChildren().add(mainPane);
        System.out.println(name);

        //Asking for the onlineScores
        gameWindow.getCommunicator().addListener(this::loadOnlineScores);
        gameWindow.getCommunicator().send("HISCORES");

        //Let the thread sleep to receive the scores
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Managing what is shown on the mainPane
        mainPane.setTop(vBox);
        mainPane.setLeft(scorePrint);
        mainPane.setRight(onlineScoreVBox);

    }

    /**
     * Loading the online scores and managing them
     * @param s the scores as a string
     */
    public void loadOnlineScores(String s) {

        //We split the string by "\n" so we get 10 different strings with names and values
        String[] onlineScores = s.split("\n");

        //Replace the HISCORE writing in the first string so all of the will be the same
        onlineScores[0] = onlineScores[0].replaceAll("HISCORES " , "");

        Label onlineTitle = new Label("Online Scores");
        onlineTitle.getStyleClass().add("title");
        onlineScoreVBox.getChildren().add(onlineTitle);

        onlineScoreVBox.setSpacing(1);
        onlineScoreVBox.setPrefWidth((double)this.gameWindow.getWidth()/2);
        onlineScoreVBox.setPadding(new Insets(0,100,0,0));
        onlineScoreVBox.setAlignment(Pos.CENTER);

        //Handling the 10 HISCORES and adding the different colours to it
        for(int i=0; i<10; i++){
            check = onlineScores[i].split(":");
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
        }

        //Setting the onlineScoreVBox to the mainPane
        mainPane.setRight(onlineScoreVBox);

    }
}
