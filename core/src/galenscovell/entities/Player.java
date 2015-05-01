
/**
 * PLAYER CLASS
 * Displays player sprite and handles player coordinates.
 * Only one Player instance exists for persistence across levels.
 */

package galenscovell.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import galenscovell.graphics.Pickaxe;
import galenscovell.graphics.SpriteSheet;

import galenscovell.logic.Point;

import java.lang.invoke.VolatileCallSite;


public class Player {
    private int x, y, prevX, prevY, currentX, currentY;
    private int spriteFrame, waitFrames;

    public Sprite sprite;
    private Sprite[] currentSet;
    private Sprite[] upSprites, downSprites, leftSprites, rightSprites;

    private boolean attacking;
    private Pickaxe pickaxe;
    private boolean pickaxePrepared;


    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        this.prevX = x;
        this.prevY = y;
        this.currentX = x;
        this.currentY = y;

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
        this.waitFrames = 20;

        this.pickaxe = new Pickaxe();
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

    public boolean turn(int dx, int dy) {
        if (dy < 0) {
            if (currentSet != upSprites) {
                currentSet = upSprites;
                return true;
            }
        } else if (dy > 0) {
            if (currentSet != downSprites) {
                currentSet = downSprites;
                return true;
            }
        } else if (dx < 0) {
            if (currentSet != leftSprites) {
                currentSet = leftSprites;
                return true;
            }
        } else if (dx > 0) {
            if (currentSet != rightSprites) {
                currentSet = rightSprites;
                return true;
            }
        }
        return false;
    }

    public void move(int dx, int dy) {
        animate(currentSet);
        x += dx;
        y += dy;
    }

    public void occupyTile(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }

    public void interpolate(double interpolation) {
        animate(currentSet);
        // When interpolation is 1, movement animation is complete
        if (interpolation == 1.0) {
            prevX = x;
            prevY = y;
            return;
        }
        currentX = (int) (prevX + ((x - prevX) * interpolation));
        currentY = (int) (prevY + ((y - prevY) * interpolation));
    }

    public void attack(SpriteBatch spriteBatch, double interpolation, int tileSize) {
        if (!pickaxePrepared) {
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
            pickaxe.setDirection(dir);
            pickaxe.setPosition(currentX, currentY);
            pickaxePrepared = true;
            pickaxe.resetFrame();
        }

        int pickaxeFrame = pickaxe.getFrame();
        if (pickaxeFrame <= 3) {
            pickaxe.draw(spriteBatch, tileSize, 0);
        } else if (pickaxeFrame > 3 && pickaxeFrame < 6) {
            pickaxe.draw(spriteBatch, tileSize, 1);
        } else if (pickaxeFrame > 5 && pickaxeFrame < 17) {
            pickaxe.draw(spriteBatch, tileSize, 2);
        } else if (pickaxeFrame == 18) {
            pickaxePrepared = false;
            toggleAttack();
            return;
        }
        pickaxe.incrementFrame();
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