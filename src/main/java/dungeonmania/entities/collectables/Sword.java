package dungeonmania.entities.collectables;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.BattleItem;
import dungeonmania.entities.BattleItemBehaviour;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.util.Position;

public class Sword extends Collectable implements InventoryItem, BattleItem {
    public static final double DEFAULT_ATTACK = 1;
    public static final double DEFAULT_ATTACK_SCALE_FACTOR = 1;
    public static final int DEFAULT_DURABILITY = 5;
    public static final double DEFAULT_DEFENCE = 0;
    public static final double DEFAULT_DEFENCE_SCALE_FACTOR = 1;

    private BattleItemBehaviour battleItemBehaviour;
    private double attack;

    public Sword(Position position, double attack, int durability) {
        super(position);
        this.attack = attack;
        battleItemBehaviour = new BattleItemBehaviour(durability);
    }

    @Override
    public void use(Game game) {
        battleItemBehaviour.use(game, this);
    }

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin, new BattleStatistics(0, attack, DEFAULT_DEFENCE,
                DEFAULT_ATTACK_SCALE_FACTOR, DEFAULT_DEFENCE_SCALE_FACTOR));
    }

    @Override
    public Integer getDurability() {
        return battleItemBehaviour.getDurability();
    }
}
