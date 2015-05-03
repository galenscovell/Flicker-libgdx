
/**
 * PLAYER CLASS
 * Displays player sprite and handles player coordinates.
 * Only one Player instance exists for persistence across levels.
 */

package galenscovell.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import galenscovell.graphics.SpriteSheet;
import galenscovell.graphics.Weapon;

import galenscovell.logic.Point;


public class Player {
    private int x, y, prevX, prevY, currentX, currentY;
    private int spriteFrame, waitFrames;

    public Sprite sprite;
    private Sprite[] currentSet;
    private Sprite[] upSprites, downSprites, leftSprites, rightSprites;

    private boolean attacking;
    private Weapon weapon;
    private boolean weaponPrepared;


    public Player() {
        SpriteSheet sheet = SpriteSheet.charsheet;
        this.upSprites = new Sprite[4];
        this.downSprites = new Sprite[4];
        this.leftSprites = new Sprite[4];
        this.rightSprites = new Sprite[4];

        // Populate sprite animation sets
        for (int i = 0; i < 4; i++) {
            upSprites[i] = new Sprite(sheet.getSprite(i + 48));
            downSprites[i] = new Sprite(sheet.getSprite(i));
            leftSprites[i] = new Sprite(sheet.getSprite(i + 16));
            rightSprites[i] = new Sprite(sheet.getSprite(i + 32));
        }

        this.currentSet = downSprites;
        this.sprite = currentSet[0];
        this.spriteFrame = 0;
        this.waitFrames = 15;

        this.weapon = new Weapon();
    }

    public void setPosition(int newX, int newY) {
        prevX = newX;
        prevY = newY;
        x = newX;
        y = newY;
        currentX = newX;
        currentY = newY;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getCurrentX() {
        return currentX;
    }

    public int getCurrentY() {
        return currentY;
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

    public boolean isAttacking() {
        return attacking;
    }

    public void toggleAttack() {
        if (attacking) {
            attacking = false;
        } else {
            attacking = true;
        }
    }

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

    public void move(int dx, int dy) {
        animate(currentSet);
        x += dx;
        y += dy;
    }

    public void interpolate(double interpolation) {
        animate(currentSet);
        currentX = (int) (prevX + ((x - prevX) * interpolation));
        currentY = (int) (prevY + ((y - prevY) * interpolation));

        if (currentX == x && currentY == y) {
            prevX = x;
            prevY = y;
        }
    }

    public void attack(SpriteBatch spriteBatch, double interpolation, int tileSize) {
        if (!weaponPrepared) {
            String dir = " ";
            if (currentSet == upSprites) {
                dir = "up";
            } else if (currentSet == downSprites) {
                dir = "down";
            } else if (currentSet == leftSprites) {
                dir = "left";
            } else if (currentSet == rightSprites) {
                dir = "right";
            }
            weapon.setDirection(dir);
            weapon.setPosition(currentX, currentY);
            weaponPrepared = true;
            weapon.resetFrame();
        }

        int weaponFrame = weapon.getFrame();
        if (weaponFrame <= 3) {
            weapon.draw(spriteBatch, tileSize, 0);
        } else if (weaponFrame > 3 && weaponFrame < 6) {
            weapon.draw(spriteBatch, tileSize, 1);
        } else if (weaponFrame > 5 && weaponFrame < 17) {
            weapon.draw(spriteBatch, tileSize, 2);
        } else if (weaponFrame == 18) {
            weaponPrepared = false;
            toggleAttack();
            return;
        }
        weapon.incrementFrame();
    }

    private void animate(Sprite[] currentSet) {
        if (waitFrames == 0) {
            spriteFrame++;
            waitFrames = 20;
            if (spriteFrame > 3) {
                spriteFrame = 0;
            }
        } else {
            waitFrames--;
        }
        sprite = currentSet[spriteFrame];
    }
}