package dungeonmania.entities.buildables;

import dungeonmania.battles.BattleStatistics;

public class Bow extends BattleBuildable {
    public static final BattleStatistics BOW_BUFF_STATS = new BattleStatistics(0, 0, 0, 2, 1);

    public static final int WOOD_NEEDED = 1;
    public static final int ARROWS_NEEDED = 3;

    public Bow(Integer durability) {
        super(durability);
    }

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin, BOW_BUFF_STATS);
    }

    public static boolean canBuild(Integer wood, Integer arrows) {
        return wood >= WOOD_NEEDED && arrows >= ARROWS_NEEDED;
    }
}
