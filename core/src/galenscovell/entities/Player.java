
/**
 * PLAYER CLASS
 * Only one Player instance exists for persistence across levels.
 */

package galenscovell.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import galenscovell.graphics.SpriteSheet;

import galenscovell.logic.Point;


public class Player extends Creature {
    private boolean step, interacting;
    private Sprite[] upSprites, downSprites, leftSprites, rightSprites;
    private int atkX, atkY;


    public Player() {
        super();
        setSprites();
        setStats();
    }

    private void setStats() {
        sightRange = 5;
    }

    private void setSprites() {
        SpriteSheet sheet = SpriteSheet.charsheet;
        this.upSprites = new Sprite[3];
        this.downSprites = new Sprite[3];
        this.leftSprites = new Sprite[3];
        this.rightSprites = new Sprite[3];

        // Populate sprite animation sets
        for (int i = 0; i < 3; i++) {
            upSprites[i] = new Sprite(sheet.getSprite(i + 48));
            downSprites[i] = new Sprite(sheet.getSprite(i));
            leftSprites[i] = new Sprite(sheet.getSprite(i + 16));
            rightSprites[i] = new Sprite(sheet.getSprite(i + 32));
        }

        currentSet = downSprites;
        sprite = currentSet[0];
    }

    public Point getFacingPoint(int tileSize) {
        int tileX = x / tileSize;
        int tileY = y / tileSize;
        if (currentSet == upSprites) {
            return new Point(tileX, tileY - 1);
        } else if (currentSet == downSprites) {
            return new Point(tileX, tileY + 1);
        } else if (currentSet == leftSprites) {
            return new Point(tileX - 1, tileY);
        } else {
            return new Point(tileX + 1, tileY);
        }
    }

    @Override
    public void turn(int dx, int dy) {
        if (dy < 0 && currentSet != upSprites) {
            currentSet = upSprites;
        } else if (dy > 0 && currentSet != downSprites) {
            currentSet = downSprites;
        } else if (dx < 0 && currentSet != leftSprites) {
            currentSet = leftSprites;
        } else if (dx > 0 && currentSet != rightSprites) {
            currentSet = rightSprites;
        }
    }

    public void toggleInteracting() {
        if (interacting) {
            interacting = false;
        } else {
            interacting = true;
        }
    }

    public void interpolate(double interpolation) {
        // Disable movement animation during interactions
        if (!interacting) {
            animate(interpolation);
        } else if (interacting && interpolation > 0.1) {
            toggleInteracting();
        }
        currentX = (int) (prevX + ((x - prevX) * interpolation));
        currentY = (int) (prevY + ((y - prevY) * interpolation));

        if (currentX == x && currentY == y) {
            prevX = x;
            prevY = y;
        }
    }

    public void setAttackingCoords(int atkX, int atkY) {
        this.atkX = atkX;
        this.atkY = atkY;
    }

    @Override
    public void attack(double interpolation, Entity entity) {
        sprite = currentSet[2];
        int diffX = x - atkX;
        int diffY = y - atkY;
        currentX = (int) (prevX - (diffX * interpolation));
        currentY = (int) (prevY - (diffY * interpolation));

        // Attack animation only covers small portion of target's tile
        if (interpolation > 0.3) {
            toggleAttack();
        }
    }

    @Override
    public void animate(double interpolation) {
        if (interpolation > 0.1 && interpolation < 0.2) {
            if (step) {
                sprite = currentSet[2];
                step = false;
            } else {
                sprite = currentSet[1];
                step = true;
            }
        } else if (interpolation > 0.8) {
            sprite = currentSet[0];
            return;
        }
    }
}