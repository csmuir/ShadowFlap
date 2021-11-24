import bagel.DrawOptions;
import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;


/**
 * The type PipeSet.
 * @author Catherine Muir, adapted from the Project 1 solution.
 */
public abstract class PipeSet {
    private final Image PIPE_IMAGE;
    protected final DrawOptions ROTATOR = new DrawOptions().setRotation(Math.PI);
    private final int PIPE_GAP = 168;
    private final double[] LVL0_GAPS = {100, 300, 500};
    private final double MAX_GAP_HEIGHT = 500;
    private final double MIN_GAP_HEIGHT = 100;
    private final int PIPE_SPEED = 5;
    protected double TOP_PIPE_Y;
    protected double BOTTOM_PIPE_Y;
    protected double pipeX = Window.getWidth();
    private boolean birdPassed = false;
    private final double TIMESCALE = 1.5;
    private boolean destroyed = false;

    /**
     * Instantiates a new Pipe set.
     *
     * @param level          the level
     * @param NEW_PIPE_IMAGE the new pipe image
     */
    public PipeSet(int level, Image NEW_PIPE_IMAGE) {
        this.PIPE_IMAGE = NEW_PIPE_IMAGE;
        TOP_PIPE_Y = (-PIPE_IMAGE.getHeight()/2.0) + generateGapHeight(level);
        BOTTOM_PIPE_Y = TOP_PIPE_Y + PIPE_GAP + PIPE_IMAGE.getHeight();
    }

    /**
     * Renders a pipe set.
     */
    public void renderPipeSet() {
        if(!destroyed){
            PIPE_IMAGE.draw(pipeX, TOP_PIPE_Y);
            PIPE_IMAGE.draw(pipeX, BOTTOM_PIPE_Y, ROTATOR);
        }
    }

    /**
     * Updates the PipeSet position, implementing a timescale if provided.
     *
     * @param timeScale The timescale
     */
    public void update(int timeScale) {
        renderPipeSet();
        pipeX -= PIPE_SPEED*Math.pow(TIMESCALE,timeScale-1);
    }

    /**
     * Generates a random gap height for the PipeSet based on it's level.
     *
     * @param level The level
     * @return The gap height
     */
    public double generateGapHeight(int level){
        if(level == 1){ //assuming future levels would have a different gap height requirement
            return (Math.random() * (MAX_GAP_HEIGHT-MIN_GAP_HEIGHT) + MIN_GAP_HEIGHT);
        }
        return LVL0_GAPS[(int)(Math.random() * LVL0_GAPS.length)];
    }

    /**
     * Detect collision boolean.
     *
     * @param <T>       The type parameter
     * @param objectRec The object Rectangle
     * @param object    The object
     * @return Whether there is a collision.
     */
    public <T> boolean detectCollision(Rectangle objectRec, T object){
        return objectRec.intersects(getTopBox()) || objectRec.intersects(getBottomBox());
    }

    /**
     * Gets top box.
     *
     * @return The top box
     */
    public Rectangle getTopBox() {
        return PIPE_IMAGE.getBoundingBoxAt(new Point(pipeX, TOP_PIPE_Y));
    }

    /**
     * Gets bottom box.
     *
     * @return the bottom box
     */
    public Rectangle getBottomBox() {
        return PIPE_IMAGE.getBoundingBoxAt(new Point(pipeX, BOTTOM_PIPE_Y));
    }

    /**
     * Get bird passed boolean.
     *
     * @return Whether the bird has passed this PipeSet.
     */
    public boolean getBirdPassed(){
        return this.birdPassed;
    }

    /**
     * Set bird passed.
     *
     * @param birdPassed Whether the bird has passed this PipeSet.
     */
    public void setBirdPassed(boolean birdPassed){
        this.birdPassed = birdPassed;
    }

    /**
     * Is destroyed boolean.
     *
     * @return Whether this PipeSet is destroyed.
     */
    public boolean isDestroyed() {
        return destroyed;
    }

    /**
     * Sets destroyed.
     *
     * @param destroyed Whether this PipeSet is destroyed.
     */
    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

}
