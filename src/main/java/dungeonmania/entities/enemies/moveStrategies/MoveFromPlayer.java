package dungeonmania.entities.enemies.moveStrategies;

import dungeonmania.Game;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class MoveFromPlayer implements MoveStrategy {
    public Position getNextPos(Game game, Enemy enemy) {
        Position plrDiff = Position.calculatePositionBetween(game.getPlayerPosition(), enemy.getPosition());

        Position moveX = (plrDiff.getX() >= 0) ? Position.translateBy(enemy.getPosition(), Direction.RIGHT)
                : Position.translateBy(enemy.getPosition(), Direction.LEFT);
        Position moveY = (plrDiff.getY() >= 0) ? Position.translateBy(enemy.getPosition(), Direction.UP)
                : Position.translateBy(enemy.getPosition(), Direction.DOWN);
        Position offset = enemy.getPosition();
        if (plrDiff.getY() == 0 && game.canMoveTo(enemy, moveX))
            offset = moveX;
        else if (plrDiff.getX() == 0 && game.canMoveTo(enemy, moveY))
            offset = moveY;
        else if (Math.abs(plrDiff.getX()) >= Math.abs(plrDiff.getY())) {
            if (game.canMoveTo(enemy, moveX))
                offset = moveX;
            else if (game.canMoveTo(enemy, moveY))
                offset = moveY;
        } else {
            if (game.canMoveTo(enemy, moveY))
                offset = moveY;
            else if (game.canMoveTo(enemy, moveX))
                offset = moveX;
        }

        return offset;
    }
}
