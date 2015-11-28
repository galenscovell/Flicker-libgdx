package galenscovell.processing.actions.Skills;

import galenscovell.processing.*;
import galenscovell.processing.actions.*;
import galenscovell.things.entities.Entity;
import galenscovell.util.Constants;
import galenscovell.world.Tile;

import java.util.*;

public class Roll implements Action {
    private final Repository repo;
    private final Entity user;
    private List<Tile> range;
    private Tile targettedTile;
    private Entity targettedEntity;

    public Roll(Entity user, Repository repo) {
        this.user = user;
        this.repo = repo;
    }

    @Override
    public String[] getInfo() {
        return new String[]{"Roll", "Roll two spaces diagonally."};
    }

    @Override
    public boolean setTarget(Tile tile) {
        if (tile == null || !range.contains(tile) || tile.isOccupied()) {
            return false;
        } else {
            this.targettedTile = tile;
            return true;
        }
    }

    @Override
    public Entity getUser() {
        return user;
    }

    @Override
    public Tile getTarget() {
        return targettedTile;
    }

    @Override
    public boolean initialize() {
        setRange();
        enableRangeDisplay();
        return true;
    }

    @Override
    public boolean act() {
        disableRangeDisplay();
        return roll();
    }

    private void setRange() {
        List<Tile> pattern = new ArrayList<Tile>();
        int centerX = user.getX() / Constants.TILESIZE;
        int centerY = user.getY() / Constants.TILESIZE;
        Tile center = repo.findTile(centerX, centerY);

        // pattern: second tile out, diagonal
        for (int dx = -2; dx <= 2; dx += 4) {
            for (int dy = -2; dy <= 2; dy += 4) {
                if (Math.abs(dx) != Math.abs(dy)) {
                    continue;
                }
                Tile tile = repo.findTile(centerX + dx, centerY + dy);
                if (tile != null && tile != center && tile.isFloor()) {
                    pattern.add(tile);
                }
            }
        }
        this.range = repo.getRayCaster().instantiate(user, pattern, 5);
    }

    private void enableRangeDisplay() {
        for (Tile tile : range) {
            tile.enableHighlight();
        }
    }

    private void disableRangeDisplay() {
        for (Tile tile : range) {
            tile.disableHighlight();
        }
    }

    private boolean roll() {
        Move skillMovement = new Move(user, repo);
        Tile skillTarget = repo.findTile(targettedTile.x, targettedTile.y);
        skillMovement.setTarget(skillTarget);
        if (skillMovement.initialize()) {
            Point finalPoint = null;
            while (!user.pathStackEmpty()) {
                finalPoint = user.nextPathPoint();
            }
            user.pushToPathStack(finalPoint);
            return skillMovement.act();
        } else {
            return false;
        }
    }

    @Override
    public void resolve() {

    }

    @Override
    public void exit() {
        disableRangeDisplay();
    }
}

