package dungeonmania.entities;

import dungeonmania.Game;
import dungeonmania.entities.inventory.InventoryItem;

public class BattleItemBehaviour {
    private Integer durability;

    public BattleItemBehaviour(Integer durability) {
        this.durability = durability;
    }

    public void use(Game game, InventoryItem battleItem) {
        if (durability == null) {
            return;
        }

        durability--;
        if (durability <= 0) {
            game.removeFromPlayer(battleItem);
        }
    }

    public void setDurability(Integer durability) {
        this.durability = durability;
    }

    public Integer getDurability() {
        return durability;
    }

}
