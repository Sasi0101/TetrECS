package uk.ac.soton.comp1206.event;
import uk.ac.soton.comp1206.component.GameBoard;

/**
 * The Right Clicked Handler is used to handle the event when a block was right clicked
 *
 */
public interface RightClicked {
    /**
     * Handle the event when it was right clicked
     * @param gameBoard passing on the gameBoard
     */
    void rightClicked(GameBoard gameBoard);

}
