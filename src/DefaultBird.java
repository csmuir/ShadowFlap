import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.lang.Math;

/**
 * The type Default bird.
 * @author Catherine Muir, adapted from the Project 1 solution.
 */
public class DefaultBird {
    /**
     * The Wing down image.
     */
    protected Image WING_DOWN_IMAGE = new Image("res/level-0/birdWingDown.png");
    /**
     * The Wing up image.
     */
    protected Image WING_UP_IMAGE = new Image("res/level-0/birdWingUp.png");
    private LifeBar lifeBar;
    private final double X = 200;
    private final double FLY_SIZE = 6;
    private final double FALL_SIZE = 0.4;
    private final double INITIAL_Y = 350;
    private final double Y_TERMINAL_VELOCITY = 10;
    private final double SWITCH_FRAME = 10;
    private int frameCount = 0;
    private double y;
    private double yVelocity;
    private Rectangle boundingBox;
    private int level = 0;

    /**
     * Instantiates a new Default bird.
     */
    public DefaultBird() {
        y = INITIAL_Y;
        yVelocity = 0;
        boundingBox = WING_DOWN_IMAGE.getBoundingBoxAt(new Point(X, y));
        lifeBar = new LifeBar(level);
    }

    /**
     * Updates the bird position.
     *
     * @param input The user input
     */
    public void update(Input input) {
        frameCount++;
        if (input.wasPressed(Keys.SPACE)) {
            yVelocity = -FLY_SIZE;
            WING_DOWN_IMAGE.draw(X, y);
        }
        else {
            yVelocity = Math.min(yVelocity + FALL_SIZE, Y_TERMINAL_VELOCITY);
            if (frameCount % SWITCH_FRAME == 0) {
                WING_UP_IMAGE.draw(X, y);
                boundingBox = WING_UP_IMAGE.getBoundingBoxAt(new Point(X, y));
            }
            else {
                WING_DOWN_IMAGE.draw(X, y);
                boundingBox = WING_DOWN_IMAGE.getBoundingBoxAt(new Point(X, y));
            }
        }
        y += yVelocity;

        lifeBar.renderLifeBar();
    }

    /**
     * Reduces life by 1
     */
    public void reduceLife(){
        lifeBar.reduceLife();
    }

    /**
     * Checks whether the bird has no lives.
     *
     * @return Whether the bird has no lives.
     */
    public boolean noLives(){
        return lifeBar.getNumLivesLeft() == 0;
    }

    /**
     * Reset life bar.
     *
     * @param level the level
     */
    public void resetLifeBar(int level){
        this.lifeBar = new LifeBar(level);
    }

    /**
     * Respawns the bird.
     */
    public void respawn(){
        y = INITIAL_Y;
    }

    /**
     * Gets y.
     *
     * @return the y
     */
    public double getY() {
        return y;
    }

    /**
     * Gets x.
     *
     * @return the x
     */
    public double getX() {
        return X;
    }

    /**
     * Gets a Rectangle around the bird.
     *
     * @return the box
     */
    public Rectangle getBox() {
        return boundingBox;
    }
}