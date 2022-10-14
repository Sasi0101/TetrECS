package uk.ac.soton.comp1206.component;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import uk.ac.soton.comp1206.event.LeftClickHandler;
import uk.ac.soton.comp1206.game.Game;

public class PieceBoard extends GameBoard{
    double width2;
    double height2;
    private LeftClickHandler leftClickListener;

    /**
     *
     * @param cols the columns of the pieceBoard
     * @param rows the rows of the pieceBoard
     * @param width the width we need
     * @param height the height we need
     */
    public PieceBoard(int cols, int rows, double width, double height) {
        super(cols, rows, width, height);
        this.width2 = width;
        this.height2 = height;
        //If it was clicked on it the listener would get a value
        setOnMouseClicked(this::onLeftClicked);
    }

    /**
     * Set the value of a piece
     * @param game the game where we need to set it
     */
    public void setPiece(Game game){
        for(int y = 0; y < this.getGrid().getRows(); ++y) {
            for (int x = 0; x < this.getGrid().getCols(); ++x) {
                this.getGrid().set(x,y,0);
            }
        }
        //Playing the piece on the pieceBoard
        this.getGrid().playPiece(game.getCurrentPiece(), 1,1);
        //The circle(oval) to the middle of it
        this.getBlock(1,1).paintMiddleCircle();
    }

    /**
     * Setting the following piece
     * @param game the game where we need to set them
     */
    public void setFollowingPiece(Game game){
        for(int y = 0; y < this.getGrid().getRows(); ++y) {
            for (int x = 0; x < this.getGrid().getCols(); ++x) {
                this.getGrid().set(x,y,0);
            }
        }
        //Playing the piece on the pieceBoard
        this.getGrid().playPiece(game.getFollowingPiece(), 1,1);
    }

    /**
     * Handling the leftClickListener by giving it a value
     * @param listener the value we add to it
     */
    public void setOnLeftClicked(LeftClickHandler listener) {
        this.leftClickListener = listener;
    }

    /**
     * Handling the leftClicked method if it was called
     * @param event the mouseEvent we got
     */
    public void onLeftClicked(MouseEvent event){
        //If there was a leftClick and the listener is not null we activate the listener
        if(event.getButton() == MouseButton.PRIMARY){
            if(leftClickListener != null) {
                leftClickListener.leftClicked();
            }
        }
    }
}
