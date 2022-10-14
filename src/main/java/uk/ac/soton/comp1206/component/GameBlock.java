package uk.ac.soton.comp1206.component;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.*;

/**
 * The Visual User Interface component representing a single block in the grid.
 *
 * Extends Canvas and is responsible for drawing itself.
 *
 * Displays an empty square (when the value is 0) or a coloured square depending on value.
 *
 * The GameBlock value should be bound to a corresponding block in the Grid model.
 */
public class GameBlock extends Canvas {

    private final double width;
    private final double height;
    private boolean hovering;
    private final int x;
    private final int y;
    private final IntegerProperty value = new SimpleIntegerProperty(0);
    public static final Color[] COLOURS = {
            Color.TRANSPARENT,
            Color.DEEPPINK,
            Color.RED,
            Color.ORANGE,
            Color.YELLOW,
            Color.YELLOWGREEN,
            Color.LIME,
            Color.GREEN,
            Color.DARKGREEN,
            Color.DARKTURQUOISE,
            Color.DEEPSKYBLUE,
            Color.AQUA,
            Color.AQUAMARINE,
            Color.BLUE,
            Color.MEDIUMPURPLE,
            Color.PURPLE
    };

    /**
     * Create a new single Game Block
     * @param x the column the block exists in
     * @param y the row the block exists in
     * @param width the width of the canvas to render
     * @param height the height of the canvas to render
     */
    public GameBlock(int x, int y, double width, double height) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;

        //Giving the canvas a length and a width
        setWidth(width);
        setHeight(height);

        //Do an initial paint
        paint();

        //When the value property is updated, call the internal updateValue method
        value.addListener(this::updateValue);
    }

    /**
     * When the value of this block is updated,
     * @param observable what was updated
     * @param oldValue the old value
     * @param newValue the new value
     */
    private void updateValue(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        paint();
    }

    /**
     * Handle painting of the block canvas
     */
    public void paint() {

        //If the block is empty, paint as empty (to 0 value)
        if(value.get() == 0) {
            paintEmpty();
        } else {
            //If the block is not empty, paint with the colour represented by the value
            paintColor(COLOURS[value.get()]);
        }

        //If hovering is needed than it will be true
        if(this.hovering){
            this.paintHover();
        }
    }

    public void paintForKeyCommands(){
        var gc = getGraphicsContext2D();

        //Clear the rectangle
        gc.clearRect(0,0,width,height);

        //Fill the colour of the rectangle
        gc.setFill(Color.color(1, 1, 1, 0.5));
        gc.fillRect(0,0, width, height);

        //Set the border of the graphics context to black
        gc.setStroke(Color.BLACK);
        gc.strokeRect(0,0,width,height);
    }

    /**
     * Paint this canvas empty
     */
    private void paintEmpty() {
        var gc = getGraphicsContext2D();

        //Clear the rectangle
        gc.clearRect(0,0,width,height);

        //Fill the graphics context to a transparent colour
        gc.setFill(Color.TRANSPARENT);
        gc.fillRect(0,0, width, height);

        //Make a border and set it black
        gc.setStroke(Color.BLACK);
        gc.strokeRect(0,0,width,height);

    }

    /**
     * Paint this canvas with the given colour
     * @param colour the colour to paint
     */
    private void paintColor(Paint colour) {
        GraphicsContext gc = getGraphicsContext2D();
        //Clear th rectangle
        gc.clearRect(0,0,width,height);

        //Set the colour to the graphics context to the colour given as a parameter to the method
        gc.setFill(colour);
        gc.fillRect(0,0, width, height);

        //Adding a slightly lighter colour (give opacity to it) and make a triangle so it looks like it has shadows
        gc.setFill(Color.color(1.0, 1.0, 1.0, 0.2));
        gc.fillPolygon(new double[] { 0.0, 0.0, width }, new double[] { 0.0, height, height }, 3);


        //Adding a little darker colour to the right and bottom part to make a bit more real
        gc.setFill(Color.color(0,0,0,0.5));
        gc.fillRect(width - 3.0, 0.0, width, height);
        gc.setFill(Color.color(0,0,0,0.5));
        gc.fillRect(0.0,height-3.0, width, height);

        //Set a border to the gc
        gc.strokeRect(0,0,width,height);
    }

    /**
     * Get the column of this block
     * @return column number
     */
    public int getX() {
        return x;
    }

    /**
     * Get the row of this block
     * @return row number
     */
    public int getY() {
        return y;
    }

    /**
     * Get the current value held by this block, representing it's colour
     * @return value
     */
    public int getValue() {
        return this.value.get();
    }

    /**
     * Bind the value of this block to another property. Used to link the visual block to a corresponding block in the Grid.
     * @param input property to bind the value to
     */
    public void bind(ObservableValue<? extends Number> input) {
        value.bind(input);
    }

    /**
     * Set the color of the hovered tile and add an opacity to it
     */
    public void paintHover() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.setFill(Color.color(1.0, 1.0, 1.0, 0.5));
        gc.fillRect(0.0, 0.0, width, height);
    }

    /**
     * Set if we want to hover it
     * @param hovering true or false
     */
    public void setIfHovering(Boolean hovering){
        this.hovering = hovering;
        this.paint();
    }

    /**
     * Paint a small circle in the middle
     */
    public void paintMiddleCircle(){
        GraphicsContext gc = getGraphicsContext2D();
        gc.setFill(Color.color(1.0, 1.0, 1.0, 0.5));
        gc.fillOval(this.width / 4.0, this.height / 4.0, this.width / 2.0, this.height / 2.0);
    }





}
