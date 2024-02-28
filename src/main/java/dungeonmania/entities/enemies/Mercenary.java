package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Interactable;
import dungeonmania.entities.Player;
import dungeonmania.entities.buildables.Sceptre;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.potions.InvincibilityPotion;
import dungeonmania.entities.collectables.potions.InvisibilityPotion;
import dungeonmania.entities.enemies.mercenaryStates.AlliedState;
import dungeonmania.entities.enemies.mercenaryStates.HostileState;
import dungeonmania.entities.enemies.mercenaryStates.MercenaryState;
import dungeonmania.entities.enemies.moveStrategies.*;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Mercenary extends Enemy implements Interactable {
    public static final int DEFAULT_BRIBE_AMOUNT = 1;
    public static final int DEFAULT_BRIBE_RADIUS = 1;
    public static final double DEFAULT_ATTACK = 5.0;
    public static final double DEFAULT_HEALTH = 10.0;

    private int bribeAmount = Mercenary.DEFAULT_BRIBE_AMOUNT;
    private int bribeRadius = Mercenary.DEFAULT_BRIBE_RADIUS;

    private double allyAttack;
    private double allyDefence;
    private boolean isAdjacentToPlayer = false;
    private MercenaryState state = new HostileState(this);

    public Mercenary(Position position, double health, double attack, int bribeAmount, int bribeRadius,
            double allyAttack, double allyDefence) {
        super(position, health, attack);
        this.bribeAmount = bribeAmount;
        this.bribeRadius = bribeRadius;
        this.allyAttack = allyAttack;
        this.allyDefence = allyDefence;
    }

    public boolean isAllied() {
        return state.toString().equals("allied");
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (isAllied())
            return;
        super.onOverlap(map, entity);
    }

    /**
     * check whether the current merc can be bribed
     * @param player
     * @return
     */
    private boolean canBeBribed(Player player) {
        return bribeRadius >= 0 && player.countEntityOfType(Treasure.class) >= bribeAmount;
    }

    /**
     * bribe the merc
     */
    private void bribe(Player player) {
        for (int i = 0; i < bribeAmount; i++) {
            player.use(Treasure.class);
        }
    }

    @Override
    public void interact(Player player, Game game) {
        this.state = new AlliedState(this, player.getMindControlDuration());
        if (!canBeControlled(player)) {
            bribe(player);
            this.state = new AlliedState(this, null);
        }

        if (!isAdjacentToPlayer && Position.isAdjacent(player.getPosition(), getPosition()))
            isAdjacentToPlayer = true;
    }

    @Override
    public void move(Game game) {
        state.verifyAlliedStatus();
        MoveStrategy strategy = determineMoveStrategy(game);
        Position nextPos = strategy.getNextPos(game, this);

        if (isAllied() && !isAdjacentToPlayer
                && Position.isAdjacent(game.getPlayerPosition(), nextPos))
            isAdjacentToPlayer = true;

        game.moveTo(this, nextPos);
    }

    private MoveStrategy determineMoveStrategy(Game game) {
        if (isAllied()) {
            if (isAdjacentToPlayer) {
                return new MovePlayerPreviousPos();
            } else {
                return new MoveShortestPath();
            }
        } else if (game.getPlayerEffectivePotion() instanceof InvisibilityPotion) {
            // Move random
            return new MoveRandom();
        } else if (game.getPlayerEffectivePotion() instanceof InvincibilityPotion) {
            return new MoveFromPlayer();
        } else {
            // Follow hostile
            return new MoveShortestPath();
        }
    }

    @Override
    public boolean isInteractable(Player player) {
        return !isAllied() && (canBeBribed(player) || canBeControlled(player));
    }

    @Override
    public BattleStatistics getBattleStatistics() {
        if (!isAllied())
            return super.getBattleStatistics();
        return new BattleStatistics(0, allyAttack, allyDefence, 1, 1);
    }

    private boolean canBeControlled(Player player) {
        return player.countEntityOfType(Sceptre.class) > 0;
    }

    public void setState(MercenaryState state) {
        this.state = state;
    }
}
