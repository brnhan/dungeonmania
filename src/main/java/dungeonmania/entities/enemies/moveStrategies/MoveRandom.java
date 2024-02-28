package dungeonmania.entities.enemies.moveStrategies;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import dungeonmania.Game;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.util.Position;

public class MoveRandom implements MoveStrategy {
    public Position getNextPos(Game game, Enemy enemy) {
        Random randGen = new Random();

        List<Position> pos = enemy.getCardinallyAdjacentPositions();
        pos = pos.stream()
                .filter(p -> game.canMoveTo(enemy, p))
                .collect(Collectors.toList());
        if (pos.size() == 0) {
            return enemy.getPosition();
        } else {
            return pos.get(randGen.nextInt(pos.size()));
        }
    }
}
