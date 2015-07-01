package galenscovell.screens;

import galenscovell.entities.Player;
import galenscovell.screens.components.*;
import galenscovell.util.ResourceManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.StringBuilder;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * HUD STAGE
 * Creates HUD and handles HUD updates.
 * HUD camera uses exact pixel dimensions (800, 480).
 *
 * @author Galen Scovell
 */

public class HudStage extends Stage {
    private GameScreen game;
    private int eventLines = 1;
    private Label eventLog;
    private ProgressBar chassis, power, matter;
    private Table inventoryMenu, optionsMenu, examineMenu;
    private Button examineButton, inventoryButton, optionsButton;

    public HudStage(GameScreen game, Player player, SpriteBatch spriteBatch) {
        super(new FitViewport(800, 480), spriteBatch);
        this.game = game;
        create(player);
    }

    public void create(Player player) {
        this.examineMenu = new HudExamineMenu(this);
        this.inventoryMenu = new HudInventoryMenu(this);
        this.optionsMenu = new HudOptionsMenu(this);

        Table mainTable = new Table();
        mainTable.setFillParent(true);

        /**********************************
         * TOP TABLE                      *
         **********************************/
        Table topTable = new Table();


        Table topLeft = new Table();
        topLeft.pad(8, 8, 8, 8);
        this.eventLog = new Label("Events displayed here.", ResourceManager.detailStyle);
        eventLog.setAlignment(Align.topLeft, Align.topLeft);
        eventLog.setWrap(true);
        topLeft.add(eventLog).expand().fill().top().left();
        topTable.add(topLeft).height(120).width(400).expand().top().left();


        Table topRight = new Table();
        topTable.add(topRight).height(120).width(400).expand().top().right();

        mainTable.add(topTable).expand().fill().top();
        mainTable.row();

        /**********************************
         * BOTTOM TABLE                   *
         **********************************/
        Table bottomTable = new Table();

        // Bottom left
        Table bottomLeft = new Table();
        Table barTable = new Table();
        this.chassis = createBar("chassisFill");
        this.power = createBar("powerFill");
        this.matter = createBar("matterFill");
        barTable.add(chassis).width(260).height(20);
        barTable.row();
        barTable.add(power).width(260).height(20);
        barTable.row();
        barTable.add(matter).width(260).height(20);
        bottomLeft.add(barTable).width(260).height(60).expand().fill().bottom().left();
        bottomTable.add(bottomLeft).expand().fill().left();

        // Bottom right
        Table bottomRight = new Table();
        this.examineButton = new Button(ResourceManager.buttonStyle);
        setIcon(examineButton, "sensor");
        examineButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                examine();
                game.toggleExamineMode();
            }
        });
        this.inventoryButton = new Button(ResourceManager.buttonStyle);
        setIcon(inventoryButton, "microchip");
        inventoryButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                menuOperation(inventoryMenu);
            }
        });
        this.optionsButton = new Button(ResourceManager.buttonStyle);
        setIcon(optionsButton, "network");
        optionsButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                menuOperation(optionsMenu);
            }
        });
        bottomRight.add(examineButton).height(80).width(80);
        bottomRight.add(inventoryButton).height(80).width(80);
        bottomRight.add(optionsButton).height(80).width(80);
        bottomTable.add(bottomRight).expand().bottom().right();

        mainTable.add(bottomTable).bottom().fill();

        mainTable.pack();
        this.addActor(mainTable);
    }

    public void dispose() {
        this.addActor(examineButton);
        this.addActor(inventoryMenu);
        this.addActor(optionsMenu);
    }

    public void returnToMainMenu() {
        optionsMenu.remove();
        game.toMainMenu();
    }

    public void addToLog(String text) {
        if (eventLines == 5) {
            eventLog.setText(text);
            eventLines = 1;
        } else {
            StringBuilder currentText = eventLog.getText();
            String newText = currentText + "\n" + text;
            eventLog.setText(newText);
            eventLines++;
        }
    }

    public void updateChassis(int val) {
        chassis.setValue(chassis.getValue() + val);
    }

    public void updatePower(int val) {

    }

    public void updateMatter(int val) {

    }

    public void examine() {
        if (examineMenu.hasParent()) {
            examineMenu.remove();
            inventoryButton.setTouchable(Touchable.enabled);
            optionsButton.setTouchable(Touchable.enabled);
        } else {
            this.addActor(examineMenu);
            inventoryButton.setTouchable(Touchable.disabled);
            optionsButton.setTouchable(Touchable.disabled);
        }
    }

    private void setIcon(Table table, String name) {
        Image icon = new Image(new TextureAtlas.AtlasRegion(ResourceManager.uiAtlas.findRegion(name)));
        icon.setScaling(Scaling.fit);
        table.add(icon).expand().fill().center();
    }

    private ProgressBar createBar(String path) {
        TextureRegionDrawable fill = new TextureRegionDrawable(ResourceManager.uiAtlas.findRegion(path));
        TextureRegionDrawable empty = new TextureRegionDrawable(ResourceManager.uiAtlas.findRegion("barEmpty"));
        ProgressBar.ProgressBarStyle barStyle = new ProgressBar.ProgressBarStyle(empty, fill);
        ProgressBar bar = new ProgressBar(0, 260, 1, false, barStyle);
        barStyle.knobBefore = fill;
        bar.setValue(260);
        bar.setAnimateDuration(0.1f);
        bar.validate();
        return bar;
    }

    private void menuOperation(Table menu) {
        if (menu.hasParent()) {
            menu.remove();
            game.enableWorldInput();
        } else {
            if (optionsMenu != menu && optionsMenu.hasParent()) {
                optionsMenu.remove();
            } else if (inventoryMenu != menu && inventoryMenu.hasParent()) {
                inventoryMenu.remove();
            }
            this.addActor(menu);
            game.disableWorldInput();
        }
    }
}