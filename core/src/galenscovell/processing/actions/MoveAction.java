package galenscovell.processing.actions;

import galenscovell.processing.*;
import galenscovell.things.entities.Entity;
import galenscovell.world.Tile;

public class MoveAction implements Action {
    private Pathfinder pathfinder;

    public MoveAction() {
        this.pathfinder = new Pathfinder();
    }

    public void act() {

    }

//    public void act(int[] destination) {
//        if (!findPath(hero, destination[0], destination[1]) || hero.getPathStack() == null || hero.getPathStack().isEmpty()) {
//            return false;
//        } else {
//            Point nextMove = hero.getPathStack().pop();
//            if (move(hero, nextMove.x, nextMove.y)) {
//                // TODO: Movement power usage and regeneration
//            } else {
//                hero.setPathStack(null);
//                return false;
//            }
//            npcTurn();
//        }
//        return true;
//    }
//
//    private void npcTurn() {
//        for (Entity entity : entities) {
//            if (entity.movementTimer()) {
//                if (entity.isAggressive()) {
//                    findPath(entity, hero.getX(), hero.getY());
//                } else {
//                    findPath(entity, hero.getX(), hero.getY());
//                    // TODO: Passive behavior, destination depends on entity
//                }
//                if (entity.getPathStack() == null || entity.getPathStack().isEmpty()) {
//                    continue;
//                } else {
//                    Point nextMove = entity.getPathStack().pop();
//                    if (!move(entity, nextMove.x, nextMove.y)) {
//                        entity.setPathStack(null);
//                    }
//                }
//            }
//        }
//    }
//
//    private boolean findPath(Entity entity, int destX, int destY) {
//        Tile startTile = updater.getTile(entity.getX(), entity.getY());
//        Tile endTile = updater.getTile(destX, destY);
//        if (endTile == null || startTile == endTile) {
//            return false;
//        } else {
//            entity.setPathStack(pathfinder.findPath(tiles, startTile, endTile));
//            return true;
//        }
//    }
//
//    public boolean move(Entity entity, int x, int y) {
//        int entityX = (entity.getX() / tileSize);
//        int entityY = (entity.getY() / tileSize);
//        int diffX = x - entityX;
//        int diffY = y - entityY;
//        Tile nextTile = updater.findTile(x, y);
//        if (nextTile.isFloor() && !nextTile.isOccupied()) {
//            findTile(entityX, entityY).toggleOccupied();
//            entity.move(diffX * tileSize, diffY * tileSize, true);
//            nextTile.toggleOccupied();
//            return true;
//        } else {
//            entity.move(diffX, diffY, false);
//            return false;
//        }
//    }

}
