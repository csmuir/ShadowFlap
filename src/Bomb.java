import bagel.Image;

/**
 * The type Bomb.
 */
public class Bomb extends Weapon {
    private static final Image BOMB_IMAGE = new Image("res/level-1/bomb.png");
    private static final double shootingRange = 50;

    /**
     * Instantiates a new Bomb.
     */
    public Bomb() {
        super(BOMB_IMAGE, shootingRange);
    }

    /**
     * Checks whether the weapon is able to destroy the given PipeSet type.
     *
     * @param pipeSet The PipeSet
     * @return Whether the weapon is able to destroy it.
     */
    @Override
    public boolean canDestroyPipeType(PipeSet pipeSet) {
        return pipeSet instanceof PlasticPipeSet || pipeSet instanceof SteelPipeSet;
    }
}
