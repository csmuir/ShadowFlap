import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * The type Weapon.
 */
public abstract class Weapon {
    private final Image WEAPON_IMAGE;
    protected final double MAX_Y = 500;
    protected final double MIN_Y = 100;
    protected final double WEAPON_SPEED = 5;
    private final double WEAPON_SHOOTING_SPEED = 5;
    protected boolean destroyed = false;
    private boolean hasBird = false;
    private final double shootingRange;
    private boolean shooting = false;
    private double numFramesShot = 0;
    private double y;
    private double x;
    private final double TIMESCALE = 1.5;

    /**
     * Instantiates a new Weapon.
     *
     * @param WEAPON_IMAGE  the weapon image
     * @param shootingRange the shooting range
     */
    public Weapon(Image WEAPON_IMAGE, double shootingRange){
        this.WEAPON_IMAGE = WEAPON_IMAGE;
        this.x = Window.getWidth();
        this.shootingRange = shootingRange;
        this.y = (Math.random() * (MAX_Y-MIN_Y) + MIN_Y);
    }

    /**
     * Updates the weapon position, implementing a timescale if provided.
     *
     * @param timeScale The timeScale
     */
    public void update(int timeScale){
        renderWeapon();
        x-= WEAPON_SPEED*Math.pow(TIMESCALE,timeScale-1);
    }

    /**
     * Updates the weapon position when the weapon is equipped a bird.
     *
     * @param birdBox The bird Rectangle.
     */
    public void update(Rectangle birdBox){
        WEAPON_IMAGE.draw(birdBox.right(), (birdBox.bottom() + birdBox.top())/2.0);
    }

    /**
     * Render weapon.
     */
    public void renderWeapon(){
        WEAPON_IMAGE.draw(x,y);
    }

    /**
     * Get Rectangle around the weapon.
     *
     * @return The Rectangle.
     */
    public Rectangle getBox(){
        return WEAPON_IMAGE.getBoundingBoxAt(new Point(x,y));
    }

    /**
     * Sets whether the weapon belongs to a bird.
     *
     * @param hasBird Whether weapon has a bird.
     */
    public void setHasBird(boolean hasBird) {
        this.hasBird = hasBird;
    }

    /**
     * Returns whether the weapon belongs to a bird.
     *
     * @return Whether weapon has a bird.
     */
    public boolean getHasBird(){
        return this.hasBird;
    }

    /**
     * Shoot weapon.
     *
     * @param birdBox The bird Rectangle.
     */
    public void shootWeapon(Rectangle birdBox){
        shooting = true;
        hasBird = false;
        x = birdBox.right();
        y = (birdBox.bottom() + birdBox.top())/2.0;
    }

    /**
     * Render shooting.
     */
    public void renderShooting(){
        if(numFramesShot < shootingRange && !destroyed){
            WEAPON_IMAGE.draw(x,y);
            x+=WEAPON_SHOOTING_SPEED;
            numFramesShot++;
        }
        if(numFramesShot == shootingRange){
            destroyed = true;
            shooting = false;
        }
    }

    /**
     * Checks whether the weapon is able to destroy the given PipeSet type.
     *
     * @param pipeSet The PipeSet
     * @return Whether the weapon is able to destroy it.
     */
    public abstract boolean canDestroyPipeType(PipeSet pipeSet);

    /**
     * Get shooting boolean.
     *
     * @return Whether the weapon is currently being shot.
     */
    public boolean getShooting(){
        return this.shooting;
    }

    /**
     * Set shooting.
     *
     * @param shooting Whether the weapon is currently being shot.
     */
    public void setShooting(boolean shooting){
        this.shooting = shooting;
    }

    /**
     * Is destroyed boolean.
     *
     * @return Whether the weapon is destroyed.
     */
    public boolean isDestroyed(){
        return this.destroyed;
    }

    /**
     * Set destroyed.
     *
     * @param destroyed Whether the weapon is destroyed.
     */
    public void setDestroyed(boolean destroyed){
        this.destroyed = destroyed;
    }
}
