
/**
 * SPRITESHEET CLASS
 * Manages spritesheet loading.
 */

package galenscovell.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class SpriteSheet {
    private String path;
    private int sheetX, sheetY, spriteSize;
    public Texture sheet;
    private TextureRegion[] subSprites;

    public static SpriteSheet charsheet = new SpriteSheet("textures/charsheet.png", 16);
    public static SpriteSheet tilesheet = new SpriteSheet("textures/tilesheet.png", 16);
    public static SpriteSheet fxsheet = new SpriteSheet("textures/fxsheet.png", 16);


    public SpriteSheet(String path, int spriteSize) {
        this.path = path;
        this.spriteSize = spriteSize;
        load();
        this.subSprites = getSubSprites();

        for (TextureRegion sprite : subSprites) {
            sprite.flip(false, true);
        }
    }

    public TextureRegion getSprite(int pos) {
        return subSprites[pos];
    }

    protected TextureRegion[] getSubSprites() {
        int partsX = sheetX / spriteSize;
        int partsY = sheetY / spriteSize;
        TextureRegion[] foundSprites = new TextureRegion[partsX * partsY];

        for (int y = 0; y < partsY; y++) {
            for (int x = 0; x < partsX; x++) {
                foundSprites[x + y * partsX] = getSubSprite(x * spriteSize, y * spriteSize);
            }
        }
        return foundSprites;
    }

    protected TextureRegion getSubSprite(int x, int y) {
        TextureRegion subSprite = new TextureRegion(sheet, x, y, spriteSize, spriteSize);
        return subSprite;
    }

    private void load() {
        this.sheet = new Texture(Gdx.files.internal(path));
        this.sheetX = sheet.getWidth();
        this.sheetY = sheet.getHeight();
    }

    public void dispose() {
        sheet.dispose();
    }
}