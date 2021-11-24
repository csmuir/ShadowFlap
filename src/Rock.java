import bagel.Image;

/**
 * The type Rock.
 */
public class Rock extends Weapon{
    private static final Image ROCK_IMAGE = new Image("res/level-1/rock.png");
    private static final double shootingRange = 25;

    /**
     * Instantiates a new Rock.
     */
    public Rock() {
        super(ROCK_IMAGE, shootingRange);
    }

    /**
     * Checks whether the weapon is able to destroy the given PipeSet type.
     *
     * @param pipeSet The PipeSet
     * @return Whether the weapon is able to destroy it.
     */
    @Override
    public boolean canDestroyPipeType(PipeSet pipeSet) {
        return pipeSet instanceof PlasticPipeSet;
    }
}
