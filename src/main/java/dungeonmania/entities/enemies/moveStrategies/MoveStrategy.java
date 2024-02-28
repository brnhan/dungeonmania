package dungeonmania.entities.enemies.moveStrategies;

import dungeonmania.Game;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.util.Position;

public interface MoveStrategy {
    public Position getNextPos(Game game, Enemy enemy);
}
