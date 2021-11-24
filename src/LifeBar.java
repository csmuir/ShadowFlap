import bagel.Image;

/**
 * The type Life bar.
 */
public class LifeBar {
    private final Image HEART = new Image("res/level/fullLife.png");
    private final Image EMPTY_HEART = new Image("res/level/noLife.png");
    private final int LIVES_LVL0 = 3;
    private final int LIVES_LVL1 = 6;
    private final double FIRST_X = 100;
    private final double FIRST_Y = 15;
    private final double HEART_GAP = 50;
    private int numLives;
    private int numLivesLeft;

    /**
     * Instantiates a new Life bar.
     *
     * @param level The current level
     */
    public LifeBar(int level){
        if(level == 0){
            this.numLives = LIVES_LVL0;
        }
        else if(level == 1){
            this.numLives = LIVES_LVL1;
        }
        else this.numLives = 0;
        this.numLivesLeft = numLives;
    }

    /**
     * Renders life bar.
     */
    public void renderLifeBar(){
        double nextX = FIRST_X;
        for(int i = 0; i<numLivesLeft; i++){
            HEART.drawFromTopLeft(nextX, FIRST_Y);
            nextX += HEART_GAP;
        }
        for(int i = 0; i<(numLives-numLivesLeft); i++){
            EMPTY_HEART.drawFromTopLeft(nextX, FIRST_Y);
            nextX += HEART_GAP;
        }
    }

    /**
     * Reduces life.
     */
    public void reduceLife(){
        numLivesLeft--;
    }

    /**
     * Get number of lives left.
     *
     * @return The number of lives left.
     */
    public int getNumLivesLeft(){
        return this.numLivesLeft;
    }
}
