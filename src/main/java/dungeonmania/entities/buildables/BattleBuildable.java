package dungeonmania.entities.buildables;

import dungeonmania.Game;
import dungeonmania.entities.BattleItem;
import dungeonmania.entities.BattleItemBehaviour;

public abstract class BattleBuildable extends Buildable implements BattleItem {
    private BattleItemBehaviour battleItemBehaviour;

    public BattleBuildable(Integer durability) {
        super(null);
        this.battleItemBehaviour = new BattleItemBehaviour(durability);
    }

    @Override
    public void use(Game game) {
        battleItemBehaviour.use(game, this);
    }

    @Override
    public Integer getDurability() {
        return battleItemBehaviour.getDurability();
    }
}
