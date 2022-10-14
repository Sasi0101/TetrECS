package uk.ac.soton.comp1206.component;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class TimeBar extends Rectangle
{
    /**
     * Creating a TimeBar and adding a height in it as well as a colour
     */
    public TimeBar() {
        //Set the height of the time countdown as well as the initial colour
        this.setHeight(25.0);
        this.setFill(Color.RED);
    }
}