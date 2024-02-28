package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.entities.collectables.potions.InvincibilityPotion;
import dungeonmania.entities.enemies.moveStrategies.*;
import dungeonmania.util.Position;

public class ZombieToast extends Enemy {
    public static final double DEFAULT_HEALTH = 5.0;
    public static final double DEFAULT_ATTACK = 6.0;

    public ZombieToast(Position position, double health, double attack) {
        super(position, health, attack);
    }

    @Override
    public void move(Game game) {
        MoveStrategy strategy = determineMoveStrategy(game);
        Position nextPos = strategy.getNextPos(game, this);
        game.moveTo(this, nextPos);
    }

    private MoveStrategy determineMoveStrategy(Game game) {
        if (game.getPlayerEffectivePotion() instanceof InvincibilityPotion) {
            return new MoveFromPlayer();
        } else {
            return new MoveRandom();
        }
    }

}
