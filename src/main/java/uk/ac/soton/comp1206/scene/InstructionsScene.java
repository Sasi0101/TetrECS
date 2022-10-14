package uk.ac.soton.comp1206.scene;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.component.GameBoard;
import uk.ac.soton.comp1206.game.GamePiece;
import uk.ac.soton.comp1206.ui.GamePane;
import uk.ac.soton.comp1206.ui.GameWindow;
import uk.ac.soton.comp1206.utilities.Multimedia;

public class InstructionsScene extends BaseScene{

    /**
     * Declaring the variables we are going to use
     */
    private static final Logger logger = LogManager.getLogger(InstructionsScene.class);
    BorderPane mainPane = new BorderPane();
    private final Multimedia media = new Multimedia();
    public GridPane gridPane = new GridPane();
    private int x,y;


    public InstructionsScene(GameWindow gameWindow){
        super(gameWindow);
        logger.info("Creating instruction scene");
    }

    @Override
    public void initialise() {
        logger.info("Went into initialise");
        getScene().setOnKeyPressed((e) -> {
                    if (e.getCode() == KeyCode.ESCAPE) {
                        gameWindow.startMenu();
                        media.playAudio("transition.wav");
                    }
                });
    }

    /**
     * Building the instructionScene in 3 parts: description, picture and pieces.
     */
    @Override
    public void build() {
        //Giving value to the root
        logger.info("Building instructionScene");
        root = new GamePane(gameWindow.getWidth(),gameWindow.getHeight());
        var instructionPane = new StackPane();
        instructionPane.setMaxWidth(gameWindow.getWidth());
        instructionPane.setMaxHeight(gameWindow.getHeight());
        instructionPane.getStyleClass().add("menu-background");
        root.getChildren().add(instructionPane);
        instructionPane.getChildren().add(mainPane);

        //Adding description about the title and all the options
        titleDescription();

        //Adding the instruction picture
        insertPicture();

        //Adding dynamically generated images of the 15 pieces
        dynamicPictures();

    }

    /**
     * Inserting the png picture
     */
    private void insertPicture(){
        var instructionsImage = new ImageView(new Image(getClass().getResource("/images/Instructions.png").toExternalForm()));
        instructionsImage.setFitWidth(gameWindow.getHeight()-60);
        instructionsImage.setPreserveRatio(true);
        BorderPane.setMargin(instructionsImage, new Insets(0,0,0,0));
        BorderPane.setAlignment(instructionsImage, Pos.CENTER);
        mainPane.setCenter(instructionsImage);
    }

    /**
     * Setting the title and the brief description of the game
     */

    private void titleDescription(){
        VBox vb1 = new VBox();
        vb1.setSpacing(0);
        vb1.prefWidthProperty().bind(mainPane.widthProperty().multiply(1));

        Label title = new Label("Instructions");
        title.getStyleClass().add("heading");
        title.prefWidthProperty().bind(mainPane.widthProperty().multiply(1));
        title.setPadding(new Insets(0,0,0,350));

        Label description1 = new Label("TetrECS is a fast-paced gravity-free block placement game, where you must survive by cleaning rows through careful placement of the ");
        description1.setPadding(new Insets(0,0,0,20));
        description1.getStyleClass().add("instructions");

        Label description2 = new Label("upcoming blocks before the time runs out. Lose 3 lives and you're destroyed.");
        description2.getStyleClass().add("instructions");
        description2.setPadding(new Insets(0,0,0,180));

        vb1.getChildren().addAll(title, description1, description2);
        mainPane.setTop(vb1);
    }

    /**
     * Adding the dynamic pictures to instructions page
     */
    private void dynamicPictures(){
        VBox vb = new VBox();
        vb.setSpacing(0);
        vb.prefWidthProperty().bind(mainPane.widthProperty().multiply(1));

        Label gamePieces = new Label("Game Pieces");
        gamePieces.getStyleClass().add("heading");
        gamePieces.setPadding(new Insets(0,0,0,325));
        gamePieces.prefWidthProperty().bind(mainPane.widthProperty().multiply(1));

        //Setting the Hgap and Vgap of the gridPane where we will store the pieces
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.TOP_CENTER);

        //Adding the 15 different pieces and a counter x so when it reaches 5 we go to the next row
        for(int i = 0; i < 15; i++) {
            GamePiece piece = GamePiece.createPiece(i);
            GameBoard gameBoard = new GameBoard(3, 3, gameWindow.getWidth() / 14.0, gameWindow.getWidth() / 14.0);
            gameBoard.getGrid().playPiece(piece,1,1);
            gridPane.add(gameBoard, x, y);
            x++;
            if (x == 5) {
                x = 0;
                y++;
            }
        }

        vb.getChildren().addAll(gamePieces, gridPane);
        mainPane.setBottom(vb);
    }

}
