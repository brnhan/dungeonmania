package dungeonmania.entities.enemies.moveStrategies;

import dungeonmania.Game;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.util.Position;

public class MovePlayerPreviousPos implements MoveStrategy {
    public Position getNextPos(Game game, Enemy enemy) {
        return game.getPlayerPreviousDistinctPosition();
    }
}
