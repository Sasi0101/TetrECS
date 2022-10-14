package uk.ac.soton.comp1206.game;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The Grid is a model which holds the state of a game board. It is made up of a set of Integer values arranged in a 2D
 * arrow, with rows and columns.
 *
 * Each value inside the Grid is an IntegerProperty can be bound to enable modification and display of the contents of
 * the grid.
 *
 * The Grid contains functions related to modifying the model, for example, placing a piece inside the grid.
 *
 * The Grid should be linked to a GameBoard for it's display.
 */
public class Grid {

    private static final Logger logger = LogManager.getLogger(Grid.class);
    private final int cols;
    private final int rows;

    /**
     * The grid is a 2D matrix with rows and columns.
     */
    private final SimpleIntegerProperty[][] grid;

    /**
     * Create a new Grid with the specified number of columns and rows
     * @param cols number of columns
     * @param rows number of rows
     */
    public Grid(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;
        grid = new SimpleIntegerProperty[cols][rows];
        for(var y = 0; y < rows; y++) {
            for(var x = 0; x < cols; x++) {
                grid[x][y] = new SimpleIntegerProperty(0);
            }
        }
    }

    /**
     * Get the Integer property contained inside the grid at a given row and column index. Can be used for binding.
     * @param x column
     * @param y row
     * @return the IntegerProperty at the given x and y in this grid
     */
    public IntegerProperty getGridProperty(int x, int y) {
        return grid[x][y];
    }

    /**
     * Update the value at the given x and y index within the grid
     * @param x column
     * @param y row
     * @param value the new value
     */
    public void set(int x, int y, int value) {
        grid[x][y].set(value);
    }

    /**
     * Get the value represented at the given x and y index within the grid
     * @param x column
     * @param y row
     * @return the value in that place
     */
    public int get(int x, int y) {
        try {
            return grid[x][y].get();
        } catch (ArrayIndexOutOfBoundsException e) {
            return -1;
        }
    }

    /**
     * Get the number of columns in this game
     * @return number of columns
     */
    public int getCols() {
        return cols;
    }

    /**
     * Get the number of rows in this game
     * @return number of rows
     */
    public int getRows() {
        return rows;
    }

    /**
     * Check if you can play the piece
     * @param piece the piece chose
     * @param placeX where to put it the X coordinate
     * @param placeY where to put it the Y coordinate
     * @return true or false if we can play the piece or not
     */
    public boolean canPlayPiece(GamePiece piece, int placeX, int placeY){
        int[][] block = piece.getBlocks();
        for(int x = 0; x < block.length; x++) {
            for(int y = 0; y < block[x].length; y++) {
                int value = block[x][y];
                if (value != 0) {
                    int gridValue = this.get(x + placeX -1, y + placeY-1);
                    if (gridValue != 0) {
                        logger.info("Piece could not be played.");
                        return false;
                    }
                }
            }
        }
        logger.info("Piece could be played.");
        return true;
    }

    /**
     * Try playing the piece
     * @param piece the piece we want to play
     * @param placeX the place where to put
     * @param placeY the place where to put
     */
    public void playPiece(GamePiece piece, int placeX, int placeY){
        int[][] blocks = piece.getBlocks();
        //Checking if we can play a piece
        if (this.canPlayPiece(piece, placeX, placeY)) {
            for(int x = 0; x < blocks.length; x++) {
                for(int y = 0; y < blocks[x].length; y++) {
                    int value = blocks[x][y];
                    if (value != 0) {
                        this.set(x + placeX-1, y + placeY-1, value);
                    }
                }
            }
        } else {
            logger.info("Piece was not played.");
        }
    }


}
