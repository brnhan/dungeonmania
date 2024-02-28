package dungeonmania.entities;

import dungeonmania.map.GameMap;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.enemies.Spider;
import dungeonmania.util.Position;

public class Door extends Entity {
    private boolean open = false;
    private int number;

    public Door(Position position, int number) {
        super(position.asLayer(Entity.DOOR_LAYER));
        this.number = number;
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        if (open || entity instanceof Spider) {
            return true;
        }
        return (entity instanceof Player && ((Player) entity).hasValidDoorOpener(number));
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (!(entity instanceof Player))
            return;

        Player player = (Player) entity;

        if (player.findFirstItem(SunStone.class) == null) {
            player.removeKey();
        }
        open();
    }

    public boolean isOpen() {
        return open;
    }

    public void open() {
        open = true;
    }

    public int getNumber() {
        return number;
    }
}
