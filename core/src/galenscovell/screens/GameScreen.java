
/**
 * GAMESCREEN CLASS
 * Handles game loop calls to updater/renderer and player input.
 */

package galenscovell.screens;

import galenscovell.entities.Player;
import galenscovell.logic.Renderer;
import galenscovell.logic.Updater;
import galenscovell.logic.World;
import galenscovell.util.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;


public class GameScreen implements Screen {
    private Player playerInstance;
    private World world;
    private Renderer renderer;
    private Updater updater;

    private double interpolation;
    private int updateAccumulator;

    private boolean movePressed;


    public GameScreen() {
        this.playerInstance = new Player(48, 48);

        createNewLevel();
        this.updateAccumulator = 0;
    }

    @Override
    public void render(float delta) {
        // Player movement and entity logic
        if (updateAccumulator > Constants.TIMESTEP) {
            updater.update(checkMovement(), movePressed, Gdx.input.isKeyPressed(Input.Keys.SPACE), Gdx.input.isKeyPressed(Input.Keys.E), renderer.getEntityList(), renderer.getDeadList(), renderer.getInanimateList());
            updateAccumulator = 0;

            if (updater.playerDescends()) {
                renderer.deconstruct();
                updater.deconstruct();
                world.deconstruct();
                System.gc(); // Suggest garbage collection on null references
                createNewLevel();
            }
        }

        // Graphics rendering
        interpolation = (double) updateAccumulator / Constants.TIMESTEP;
        renderer.render(interpolation);
        updateAccumulator++;
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    private void createNewLevel() {
        this.world = new World();
        this.renderer = new Renderer(world.getTiles());
        this.updater = new Updater(world.getTiles());

        int smoothTicks = Constants.WORLD_SMOOTHING_PASSES;
        while (smoothTicks > 0) {
            world.update();
            smoothTicks--;
        }

        world.optimizeLayout();
        renderer.assembleLevel(playerInstance);
        updater.setPlayer(playerInstance);
        updater.setStairs(renderer.getInanimateList());
    }

    private int[] checkMovement() {
        int[] direction = new int[2];

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            direction[1]--;
            movePressed = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            direction[1]++;
            movePressed = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            direction[0]--;
            movePressed = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            direction[0]++;
            movePressed = true;
        } else {
            movePressed = false;
        }
        return direction;
    }

}
