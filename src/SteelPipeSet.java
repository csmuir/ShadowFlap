import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * The type SteelPipeSet, an extension of PipeSet with flame.
 */
public class SteelPipeSet extends PipeSet{
    private final static Image STEEL_PIPE_IMAGE = new Image("res/level-1/steelPipe.png");
    private final Image FLAME_IMAGE = new Image("res/level-1/flame.png");
    private final double TOP_FLAME_Y ;
    private final double BOTTOM_FLAME_Y ;
    private final int FLAME_SPAWN = 20;
    private final int FLAME_LENGTH = 30;
    private int frameCount = 0;

    /**
     * Instantiates a new Steel pipe set.
     *
     * @param level The current game level
     */
    public SteelPipeSet(int level){
        super(level, STEEL_PIPE_IMAGE);
        TOP_FLAME_Y = super.TOP_PIPE_Y + FLAME_IMAGE.getHeight()/2.0 + STEEL_PIPE_IMAGE.getHeight()/2.0;
        BOTTOM_FLAME_Y = super.BOTTOM_PIPE_Y - FLAME_IMAGE.getHeight()/2.0 - STEEL_PIPE_IMAGE.getHeight()/2.0;
    }

    @Override
    public void renderPipeSet(){
        super.renderPipeSet();
        frameCount++;
        if(FLAME_SPAWN < frameCount && frameCount < FLAME_LENGTH + FLAME_SPAWN){
            FLAME_IMAGE.draw(super.pipeX,TOP_FLAME_Y);
            FLAME_IMAGE.draw(super.pipeX,BOTTOM_FLAME_Y, super.ROTATOR);
        }
        if(frameCount == FLAME_LENGTH + FLAME_SPAWN){
            frameCount =  0;
        }
    }

    /**
     * Gets a Rectangle for the top flame.
     *
     * @return The Rectangle.
     */
    public Rectangle getTopFlameBox(){
        return new Rectangle(FLAME_IMAGE.getBoundingBoxAt(new Point(super.pipeX, TOP_FLAME_Y)));
    }

    /**
     * Gets a Rectangle for the bottom flame.
     *
     * @return The Rectangle.
     */
    public Rectangle getBottomFlameBox(){
        return new Rectangle(FLAME_IMAGE.getBoundingBoxAt(new Point(super.pipeX, BOTTOM_FLAME_Y)));
    }

    @Override
    public <T> boolean detectCollision(Rectangle objectRec, T object){
        Rectangle bottomPipe = getBottomBox();
        Rectangle topPipe = getTopBox();
        if(object instanceof Weapon){
            return objectRec.intersects(topPipe) || objectRec.intersects(bottomPipe);
        }
        else{
            if (isFlameOn()){
                Rectangle topFlame = getTopFlameBox();
                Rectangle bottomFlame = getBottomFlameBox();
                return objectRec.intersects(topPipe) || objectRec.intersects(bottomPipe)
                        || objectRec.intersects(topFlame) || objectRec.intersects(bottomFlame);
            }
        }
        return objectRec.intersects(topPipe) || objectRec.intersects(bottomPipe);
    }


    /**
     * Is the flame on.
     *
     * @return A boolean
     */
    public boolean isFlameOn() {
        return FLAME_SPAWN < frameCount && frameCount < FLAME_LENGTH + FLAME_SPAWN;
    }

}
