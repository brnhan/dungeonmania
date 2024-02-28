package dungeonmania.entities.buildables;

import dungeonmania.battles.BattleStatistics;

public class Shield extends BattleBuildable {
    private double defence;

    public static final double DEFAULT_ATTACK_MAGNIFIER = 1;
    public static final double DEFAULT_DAMAGE_REDUCER = 1;
    public static final double DEFAULT_ATTACK = 0;

    public static final int WOOD_NEEDED = 2;
    public static final double TREASURE_NEEDED = 1;
    public static final double KEYS_NEEDED = 1;

    public Shield(int durability, double defence) {
        super(durability);
        this.defence = defence;
    }

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin, new BattleStatistics(0, DEFAULT_ATTACK,
                defence, DEFAULT_ATTACK_MAGNIFIER, DEFAULT_DAMAGE_REDUCER));
    }

    public static boolean canBuild(int wood, int treasure, int keys) {
        return wood >= WOOD_NEEDED && (treasure >= TREASURE_NEEDED || keys >= KEYS_NEEDED);
    }
}
