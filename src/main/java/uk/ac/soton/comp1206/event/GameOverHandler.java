package uk.ac.soton.comp1206.event;

/**
 * The Game Over Handler is used to handle the event when the game is over, when all 3 life was lost
 *
 */
public interface GameOverHandler {
     /**
      * Handle a game over event
      */
     void gameOver();
}
