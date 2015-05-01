
/**
 * INANIMATE INTERFACE
 * All inanimates have getX(), getY(), interact() and getSprite().
 */

package galenscovell.inanimates;

import com.badlogic.gdx.graphics.g2d.Sprite;

import galenscovell.logic.Tile;


public interface Inanimate {
    public int getX();
    public int getY();
    public void interact(Tile tile);
    public boolean isBlocking();
    public String getType();
    public Sprite getSprite();
}