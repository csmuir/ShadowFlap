import bagel.Image;

/**
 * The type Bird Level 1. Has all the functions of Default Bird, with a new image and weapon ability.
 */
public class BirdLvl1 extends DefaultBird{
    private boolean hasWeapon = false;
    private int equippedWeaponIndex = -1;

    /**
     * Instantiates a new Bird lvl 1.
     */
    public BirdLvl1(){
        super();
        super.WING_DOWN_IMAGE = new Image("res/level-1/birdWingDown.png");
        super.WING_UP_IMAGE = new Image("res/level-1/birdWingUp.png");
    }

    /**
     * Get has weapon boolean.
     *
     * @return Whether the bird currently has a weapon.
     */
    public boolean getHasWeapon(){
        return this.hasWeapon;
    }

    /**
     * Sets has weapon.
     *
     * @param hasWeapon Whether the bird currently has a weapon.
     */
    public void setHasWeapon(boolean hasWeapon) {
        this.hasWeapon = hasWeapon;
    }

    /**
     * Sets equipped weapon index.
     *
     * @param equippedWeaponIndex The equipped weapon index.
     */
    public void setEquippedWeaponIndex(int equippedWeaponIndex) {
        this.equippedWeaponIndex = equippedWeaponIndex;
    }

    /**
     * Gets equipped weapon index.
     *
     * @return The equipped weapon index
     */
    public int getEquippedWeaponIndex() {
        return this.equippedWeaponIndex;
    }

}
