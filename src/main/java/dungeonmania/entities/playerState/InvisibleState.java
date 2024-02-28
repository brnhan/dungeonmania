package dungeonmania.entities.playerState;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.Player;

public class InvisibleState extends PlayerState {
    public static final BattleStatistics INVISIBILITY_BUFF = new BattleStatistics(0, 0, 0,
            1, 1, false, false);

    public InvisibleState(Player player) {
        super(player);
    }

    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin, INVISIBILITY_BUFF);
    }
}
