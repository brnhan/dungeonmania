package dungeonmania.entities.buildables;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.enemies.ZombieToast;
import dungeonmania.map.GameMap;

public class MidnightArmour extends BattleBuildable {
    private double attack;
    private double defence;

    public static final double DEFAULT_ATTACK_MAGNIFIER = 1;
    public static final double DEFAULT_DAMAGE_REDUCER = 1;
    public static final double SWORDS_NEEDED = 1;
    public static final double SUNSTONES_NEEDED = 1;

    public MidnightArmour(double attack, double defence) {
        super(null);
        this.attack = attack;
        this.defence = defence;
    }

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin, new BattleStatistics(0, attack, defence,
                DEFAULT_ATTACK_MAGNIFIER, DEFAULT_DAMAGE_REDUCER));
    }

    public static boolean canBuild(GameMap map, int swords, int sunstones) {
        return map.getEntities(ZombieToast.class).size() == 0 && swords >= SWORDS_NEEDED
                && sunstones >= SUNSTONES_NEEDED;
    }
}
