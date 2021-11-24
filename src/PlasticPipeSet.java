import bagel.Image;

/**
 * The type PlasticPipeSet, an extension of PipeSet.
 */
public class PlasticPipeSet extends PipeSet{
    private static final Image PLASTIC_PIPE_IMAGE = new Image("res/level/plasticPipe.png");

    /**
     * Instantiates a new Plastic pipe set.
     *
     * @param level The level
     */
    public PlasticPipeSet(int level){
        super(level, PLASTIC_PIPE_IMAGE);
    }
}
