package uk.ac.soton.comp1206.component;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.event.BlockClickedListener;
import uk.ac.soton.comp1206.event.RightClicked;
import uk.ac.soton.comp1206.game.Grid;

/**
 * A GameBoard is a visual component to represent the visual GameBoard.
 * It extends a GridPane to hold a grid of GameBlocks.
 *
 * The GameBoard can hold an internal grid of it's own, for example, for displaying an upcoming block. It also be
 * linked to an external grid, for the main game board.
 *
 * The GameBoard is only a visual representation and should not contain game logic or model logic in it, which should
 * take place in the Grid.
 */
public class GameBoard extends GridPane {

    //Declaring the variables we are going to use
    private RightClicked rightClickedListener;
    private BlockClickedListener blockClickedListener;
    private static final Logger logger = LogManager.getLogger(GameBoard.class);
    private final int cols;
    private final int rows;
    private final double width;
    private final double height;
    private final Grid grid;
    private GameBlock[][] blocks;


    /**
     * Create a new GameBoard, based off a given grid, with a visual width and height.
     * @param grid linked grid
     * @param width the visual width
     * @param height the visual height
     */
    public GameBoard(Grid grid, double width, double height) {
        this.cols = grid.getCols();
        this.rows = grid.getRows();
        this.width = width;
        this.height = height;
        this.grid = grid;

        //Build the GameBoard
        build();
    }

    /**
     * Create a new GameBoard with it's own internal grid, specifying the number of columns and rows, along with the
     * visual width and height.
     *
     * @param cols number of columns for internal grid
     * @param rows number of rows for internal grid
     * @param width the visual width
     * @param height the visual height
     */
    public GameBoard(int cols, int rows, double width, double height) {
        this.cols = cols;
        this.rows = rows;
        this.width = width;
        this.height = height;
        this.grid = new Grid(cols,rows);

        build();
    }

    /**
     * Get a specific block from the GameBoard, specified by it's row and column
     * @param x column
     * @param y row
     * @return game block at the given column and row
     */
    public GameBlock getBlock(int x, int y) {
        return blocks[x][y];
    }

    /**
     * Build the GameBoard by creating a block at every x and y column and row
     */
    protected void build() {
        logger.info("Building grid: {} x {}",cols,rows);
        setMaxWidth(width);
        setMaxHeight(height);
        setGridLinesVisible(true);
        blocks = new GameBlock[cols][rows];

        for(var y = 0; y < rows; y++) {
            for (var x = 0; x < cols; x++) {
                createBlock(x,y);
                GameBlock block = blocks[x][y];
                //If mouse entered on the block we call the hover method
                block.setOnMouseEntered(e -> this.hover(block));
                //If mouse exited we call the unHover method
                block.setOnMouseExited(e -> this.unHover(block));
            }
        }
        //Adding a method so when it it clicked we can observe it
        rightClick(this);


    }

    /**
     * Set the hovering to true
     * @param block the block where we need to hover
     */
    public void hover(GameBlock block){
        block.setIfHovering(true);
    }

    /**
     * Set the hovering to false
     * @param block the block where we no longer need to hover
     */
    public void unHover(GameBlock block){
        block.setIfHovering(false);
    }

    /**
     * Create a block at the given x and y position in the GameBoard
     * @param x column
     * @param y row
     */
    protected void createBlock(int x, int y) {
        var blockWidth = width / cols;
        var blockHeight = height / rows;

        //Create a new GameBlock UI component
        GameBlock block = new GameBlock(x, y, blockWidth, blockHeight);

        //Adding it to the Grid
        add(block,x,y);

        //Adding it to the block directory
        blocks[x][y] = block;

        //Link the GameBlock component to the corresponding value in the Grid
        block.bind(grid.getGridProperty(x,y));

        //Add a mouse click handler to the block to trigger GameBoard blockClicked method
        block.setOnMouseClicked((e) -> blockClicked(e, block));

    }

    /**
     * Set the block clocked listener to handle an event when a block is clicked
     * @param listener listener we need to add
     */
    public void setOnBlockClick(BlockClickedListener listener) {
        this.blockClickedListener = listener;
    }

    /**
     * Set the right clocked listener to handle when it was clicked with right click
     * @param listener listener we need to add
     */
    public void setOnRightClicked(RightClicked listener){this.rightClickedListener = listener; }

    /**
     * Triggered when a block is clicked. Call the attached listener.
     * @param event mouse event
     * @param block block clicked on
     */
    private void blockClicked(MouseEvent event, GameBlock block) {
        logger.info("Block clicked: {}", block);
        //If the button was clicked with left click and the blockClickedListener has value we activate it
        if(event.getButton() == MouseButton.PRIMARY) {
            if (blockClickedListener != null) {
                blockClickedListener.blockClicked(block);
            }
        }
    }

    /**
     * Return the grid
     * @return the grid
     */
    public Grid getGrid(){
        return grid;
    }

    /**
     *
     * @param event the mouseEvent that happened
     * @param board the gameBoard we are using
     */
    private void rightClicked(MouseEvent event, GameBoard board) {
        //If the button was clicked with left right  and the rightClickedListener has value we activate it
        if(event.getButton() == MouseButton.SECONDARY){
            if(rightClickedListener != null) {
                rightClickedListener.rightClicked(board);
            }
        }
    }


    public void rightClick(GameBoard board){
        board.setOnMouseClicked((e) -> rightClicked(e, board));
    }


}
