import bagel.*;
import bagel.util.Rectangle;
import java.util.ArrayList;

/**
 * SWEN20003 Project 1, Semester 2, 2021
 *
 * @author Catherine Muir, adapted from the Project 1 solution.
 */
public class ShadowFlap extends AbstractGame {
    private final Image BACKGROUND_IMAGE_LVL0 = new Image("res/level-0/background.png");
    private final Image BACKGROUND_IMAGE_LVL1 = new Image("res/level-1/background.png");

    private final String INSTRUCTION_MSG = "PRESS SPACE TO START";
    private final String SHOOTING_MSG = "PRESS 'S' TO SHOOT";
    private final String LEVEL_UP_MSG = "LEVEL-UP!";
    private final String GAME_OVER_MSG = "GAME OVER!";
    private final String CONGRATS_MSG = "CONGRATULATIONS!";
    private final String SCORE_MSG = "SCORE: ";
    private final String FINAL_SCORE_MSG = "FINAL SCORE: ";
    private final int FONT_SIZE = 48;
    private final Font FONT = new Font("res/font/slkscr.ttf", FONT_SIZE);
    private final int SCORE_MSG_OFFSET = 75;

    private final double SCORE_X_COORD = 100;
    private final double SCORE_Y_COORD = 100;
    private final int MAX_LVL0_SCORE = 10;
    private final int MAX_LVL1_SCORE = 30;
    private final int LEVEL_UP_SCREEN_FRAMES = 20;
    private final double TIMESCALE = 1.5;
    private final int PIPE_SPAWN = 100;

    private DefaultBird bird;
    private ArrayList<PipeSet> pipes;
    private ArrayList<Weapon> weapons;

    private int numPipes;
    private int numWeapons;
    private int frameCount;
    private int score;
    private boolean levelingUp;
    private boolean gameOn;
    private boolean collision;
    private boolean win;
    private int level = 0;
    private int timeScale;

    /**
     * Instantiates a new Shadow flap.
     */
    public ShadowFlap() {
        super(1024, 768, "ShadowFlap");
        bird = new DefaultBird();
        pipes = new ArrayList<>();
        numPipes = 0;
        weapons = new ArrayList<>();
        numWeapons = 0;
        frameCount = 0;
        score = 0;
        levelingUp = false;
        gameOn = false;
        collision = false;
        win = false;
        timeScale = 1;
        level = 0;
    }

    /**
     * The entry point for the program.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        ShadowFlap game = new ShadowFlap();
        game.run();
    }

    /**
     * Performs a state update.
     * allows the game to exit when the escape key is pressed.
     * @param input Input from the user
     */
    @Override
    public void update(Input input) {
        // draw background
        if(level == 0){
            BACKGROUND_IMAGE_LVL0.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
        }
        else if(level == 1){
            BACKGROUND_IMAGE_LVL1.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
        }

        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }

        // change timescale
        if (input.wasPressed(Keys.L)) {
            if(timeScale < 5){
                timeScale++;
            }
        }
        if (input.wasPressed(Keys.K)) {
            if(timeScale > 1){
                timeScale--;
            }
        }

        // game has not started
        if (!gameOn) {
            renderInstructionScreen(input);
        }

        // game is on level-up screen
        if(levelingUp){
            frameCount++;
            renderLevelUpScreen();
            if(frameCount == LEVEL_UP_SCREEN_FRAMES){
                levelingUp = false;
                level++; //finalise the level up
                gameOn = false;
            }
        }

        // game over
        if (bird.noLives()) {
            renderGameOverScreen();
        }

        // game won
        if (win) {
            renderWinScreen();
        }

        // game is active
        if (gameOn && !win && !levelingUp && !bird.noLives()) {
            frameCount++;

            bird.update(input);
            Rectangle birdBox = bird.getBox();

            // pipes
            if(frameCount % (int)((PIPE_SPAWN) / Math.pow(TIMESCALE,timeScale-1)) == 0){
                addPipeSet();
            }
            for(int i = 0; i<numPipes; i++){
                if(!pipes.get(i).isDestroyed() ){
                    pipes.get(i).update(timeScale);
                    collision = pipes.get(i).detectCollision(birdBox, bird);
                    if(collision){
                        bird.reduceLife();
                        pipes.get(i).setDestroyed(true);
                        break;
                    }
                }
            }
            // weapons
            // spawn a weapon for every 2nd set of pipes in level 1 in between 2 sets of pipes
            if((frameCount) % (int)((PIPE_SPAWN*2)/ Math.pow(TIMESCALE,timeScale-1)) == PIPE_SPAWN + PIPE_SPAWN/2.0
                    && level != 0){ //assuming levels beyond 1 would also have weapons
                addWeapon();
            }
            for(int i = 0; i<numWeapons; i++){
                if(weapons.get(i).getHasBird()){ //if the weapon is currently picked up by a bird
                    weapons.get(i).update(birdBox);
                }
                else if(weapons.get(i).getShooting()){ //if the weapon is currently being shot
                    weapons.get(i).renderShooting();
                    Rectangle weaponBox = weapons.get(i).getBox();
                    for(int j = 0; j<numPipes; j++){
                        if(!pipes.get(j).isDestroyed() && weapons.get(i).canDestroyPipeType(pipes.get(j))){
                            collision = pipes.get(j).detectCollision(weaponBox, weapons.get(i));
                            if(collision){
                                pipes.get(j).setDestroyed(true);
                                weapons.get(i).setDestroyed(true);
                                weapons.get(i).setShooting(false);
                            }
                        }

                    }
                }
                else if(!weapons.get(i).isDestroyed()){ //if the weapon is not destroyed
                    Rectangle weaponBox = weapons.get(i).getBox();
                    weapons.get(i).update(timeScale);
                    collision = detectCollision(birdBox, weaponBox);
                    //if bird collides with weapon, check that it doesn't already have weapon,
                    // check it doesn't re-pick up weapon after shooting
                    if(collision && !((BirdLvl1) bird).getHasWeapon() &&
                            i!=((BirdLvl1) bird).getEquippedWeaponIndex()){
                        ((BirdLvl1) bird).setHasWeapon(true);
                        ((BirdLvl1) bird).setEquippedWeaponIndex(i);
                        weapons.get(i).setHasBird(true);
                    }
                }

            }
            // user shoots equipped weapon
            if(input.wasPressed(Keys.S) && ((BirdLvl1) bird).getHasWeapon()){
                int equippedWeaponIndex = ((BirdLvl1) bird).getEquippedWeaponIndex();
                weapons.get(equippedWeaponIndex).shootWeapon(birdBox);
                ((BirdLvl1) bird).setHasWeapon(false);
            }

            if(birdOutOfBound()){
                bird.reduceLife();
                bird.respawn();
            }
            updateScore();
        }
    }

    /**
     * Adds a pipe set to an ArrayList of pipes.
     */
    public void addPipeSet(){
        if(level != 0){ //assuming any level after 1 would also have steel pipes
            if(Math.random() < 0.5){
                pipes.add(new SteelPipeSet(level));
            }
            else{
                pipes.add(new PlasticPipeSet(level));
            }
        }
        else{
            pipes.add(new PlasticPipeSet(level));
        }
        numPipes++;
    }

    /**
     * Adds a random weapon to an ArrayList of weapons.
     */
    public void addWeapon(){
        // picks a random number between 0 and 1, half of which will trigger bomb weapon, half will trigger rock weapon
        if(Math.random() < 0.5){
            weapons.add(new Bomb());
        }
        else{
            weapons.add(new Rock());
        }
        numWeapons++;
    }

    /**
     * Checks if the bird is out of bounds of the window.
     *
     * @return Whether the bird is out of bounds.
     */
    public boolean birdOutOfBound() {
        return (bird.getY() > Window.getHeight()) || (bird.getY() < 0);
    }

    /**
     * Renders instruction screen.
     *
     * @param input the input
     */
    public void renderInstructionScreen(Input input) {
        // paint the instruction on screen
        FONT.drawString(INSTRUCTION_MSG, (Window.getWidth()/2.0-(FONT.getWidth(INSTRUCTION_MSG)/2.0)), (Window.getHeight()/2.0+(FONT_SIZE/2.0)));
        if(level == 1){
            FONT.drawString(SHOOTING_MSG, (Window.getWidth()/2.0-(FONT.getWidth(SHOOTING_MSG)/2.0)), (Window.getHeight()/2.0+(FONT_SIZE/2.0))+SCORE_MSG_OFFSET);
        }
        if (input.wasPressed(Keys.SPACE)) {
            gameOn = true;
            frameCount = 0;
        }
    }

    /**
     * Renders level up screen.
     */
    public void renderLevelUpScreen(){
        FONT.drawString(LEVEL_UP_MSG, (Window.getWidth()/2.0-(FONT.getWidth(LEVEL_UP_MSG)/2.0)), (Window.getHeight()/2.0+(FONT_SIZE/2.0)));
    }

    /**
     * Renders game over screen.
     */
    public void renderGameOverScreen() {
        FONT.drawString(GAME_OVER_MSG, (Window.getWidth()/2.0-(FONT.getWidth(GAME_OVER_MSG)/2.0)), (Window.getHeight()/2.0+(FONT_SIZE/2.0)));
        String finalScoreMsg = FINAL_SCORE_MSG + score;
        FONT.drawString(finalScoreMsg, (Window.getWidth()/2.0-(FONT.getWidth(finalScoreMsg)/2.0)), (Window.getHeight()/2.0+(FONT_SIZE/2.0))+SCORE_MSG_OFFSET);
    }

    /**
     * Renders win screen.
     */
    public void renderWinScreen() {
        FONT.drawString(CONGRATS_MSG, (Window.getWidth()/2.0-(FONT.getWidth(CONGRATS_MSG)/2.0)), (Window.getHeight()/2.0+(FONT_SIZE/2.0)));
        String finalScoreMsg = FINAL_SCORE_MSG + score;
        FONT.drawString(finalScoreMsg, (Window.getWidth()/2.0-(FONT.getWidth(finalScoreMsg)/2.0)), (Window.getHeight()/2.0+(FONT_SIZE/2.0))+SCORE_MSG_OFFSET);
    }

    /**
     * Detects collision between the bird and a weapon.
     *
     * @param birdBox The bird Rectangle
     * @param weaponBox The weapon Rectangle
     * @return Whether the bird and weapon collide.
     */
    public boolean detectCollision(Rectangle birdBox,Rectangle weaponBox){
        return birdBox.intersects(weaponBox);
    }

    /**
     * Updates score.
     */
    public void updateScore() {

        for(int i = 0; i<numPipes; i++){
            if(bird.getX() > pipes.get(i).getTopBox().right() && !pipes.get(i).getBirdPassed() &&
                    !pipes.get(i).isDestroyed()){
                score++;
                pipes.get(i).setBirdPassed(true);
            }
        }
        String scoreMsg = SCORE_MSG + score;
        FONT.drawString(scoreMsg, SCORE_X_COORD, SCORE_Y_COORD);

        //detect level up
        if(level == 0 && score == MAX_LVL0_SCORE){
            levelUp();
        }

        //detect win
        if(level == 1 && score == MAX_LVL1_SCORE){
            win = true;
        }
    }

    /**
     * Levels up the game.
     */
    public void levelUp(){
        pipes.clear();
        bird = new BirdLvl1();
        bird.resetLifeBar(level+1);
        levelingUp = true;
        frameCount = 0;
        numPipes = 0;
        score = 0; // reset the score
        timeScale = 1; // reset the timescale
    }

}
