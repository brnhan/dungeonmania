package dungeonmania.entities;

import dungeonmania.battles.BattleStatistics;

public interface Buffing {
    public BattleStatistics applyBuff(BattleStatistics origin);
}
