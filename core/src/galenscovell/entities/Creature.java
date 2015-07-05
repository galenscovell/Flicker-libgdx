package galenscovell.entities;

import galenscovell.logic.Point;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * CREATURE ENTITY
 * Superclass of all mobile entities.
 *
 * @author Galen Scovell
 */

public class Creature implements Entity {
    protected int x, y, prevX, prevY, currentX, currentY;
    protected boolean aggressive, moving, attacking, beingAttacked;

    protected Sprite[] currentSet;
    protected Sprite[] leftSprites, rightSprites;

    protected String title, description;
    protected Map<String, Integer> stats = new HashMap<String, Integer>();
    private Stack<Point> pathStack;
    private int moveTimer, frame;

    public void setPosition(int newX, int newY) {
        prevX = newX;
        prevY = newY;
        x = newX;
        y = newY;
        currentX = newX;
        currentY = newY;
    }

    public String examine() {
        return description;
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

    public int getStat(String key) {
        return stats.get(key);
    }

    public void toggleAggressive() {
        aggressive = !aggressive;
    }

    public boolean isAggressive() {
        return aggressive;
    }

    public boolean movementTimer() {
        if (moveTimer == stats.get("speed")) {
            moveTimer = 0;
            return true;
        }
        moveTimer++;
        return false;
    }

    public void setBeingAttacked() {
        beingAttacked = true;
    }

    public void setAttacking() {
        attacking = true;
    }

    public void setPathStack(Stack<Point> path) {
        this.pathStack = path;
    }

    public Stack<Point> getPathStack() {
        return pathStack;
    }

    public void move(int dx, int dy, boolean possible) {
        turn(dx, dy);
        if (possible) {
            moving = true;
            x += dx;
            y += dy;
        }
    }

    protected void turn(int dx, int dy) {
        if (dx < 0 && currentSet != leftSprites) {
            currentSet = leftSprites;
        } else if (dx > 0 && currentSet != rightSprites) {
            currentSet = rightSprites;
        }
    }

    public void attack(double interpolation, Entity entity) {
        int diffX = entity.getCurrentX() - x;
        int diffY = entity.getCurrentY() - y;
        if (diffX > 0) {
            currentSet = rightSprites;
        } else if (diffX < 0) {
            currentSet = leftSprites;
        }
        currentX = (int) (prevX + (diffX * interpolation));
        currentY = (int) (prevY + (diffY * interpolation));

        // Attack animation only covers small portion of target's tile
        if (interpolation > 0.3) {
            attacking = false;
        }
    }

    public void interpolate(double interpolation) {
        currentX = (int) (prevX + ((x - prevX) * interpolation));
        currentY = (int) (prevY + ((y - prevY) * interpolation));
        if (currentX == x && currentY == y) {
            prevX = x;
            prevY = y;
            moving = false;
            frame = 0;
        }
        if (interpolation >= 0.6) {
            beingAttacked = false;
        }
    }

    public void draw(SpriteBatch batch, int tileSize, double interpolation, Entity entity) {
        interpolate(interpolation);
        if (moving) {
            if (interpolation == 0.2) {
                frame = 1;
            } else if (interpolation == 0.4) {
                frame = 2;
            } else if (interpolation == 0.6) {
                frame = 3;
            } else if (interpolation == 0.8) {
                frame = 4;
            } else if (interpolation == 1.0) {
                frame = 5;
            }
            if (frame >= currentSet.length) {
                frame = 0;
            }
        }
        if (attacking) {
            attack(interpolation, entity);
        }
        if (beingAttacked) {
            batch.setColor(1, 0, 0, 1.0f - (float) interpolation);
        } else {
            batch.setColor(1, 1, 1, 1);
        }
        batch.draw(currentSet[frame], currentX, currentY, tileSize, tileSize);
    }
}