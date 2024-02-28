package dungeonmania.entities.collectables.potions;

import dungeonmania.entities.collectables.Collectable;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.util.Position;

public abstract class Potion extends Collectable implements InventoryItem {
    private int duration;

    public Potion(Position position, int duration) {
        super(position);
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }
}
