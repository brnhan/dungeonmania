package dungeonmania.entities;

import dungeonmania.Game;

/**
 * Item has buff in battles
 */
public interface BattleItem extends Buffing {
    public void use(Game game);

    public Integer getDurability();
}
