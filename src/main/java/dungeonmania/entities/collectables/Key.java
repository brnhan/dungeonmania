package dungeonmania.entities.collectables;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Key extends Collectable implements InventoryItem {
    private int number;

    public Key(Position position, int number) {
        super(position);
        this.number = number;
    }

    public int getnumber() {
        return number;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (entity instanceof Player && ((Player) entity).findFirstItem(Key.class) == null) {
            pickUpDestroy(map, (Player) entity);
        }

        return;
    }
}
