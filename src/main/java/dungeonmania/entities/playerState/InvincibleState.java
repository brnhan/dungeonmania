package dungeonmania.entities.playerState;

import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.Player;

public class InvincibleState extends PlayerState {
    public static final BattleStatistics INVINCIBILITY_BUFF = new BattleStatistics(0, 0, 0,
            1, 1, true, true);

    public InvincibleState(Player player) {
        super(player);
    }

    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin, INVINCIBILITY_BUFF);
    }
}
