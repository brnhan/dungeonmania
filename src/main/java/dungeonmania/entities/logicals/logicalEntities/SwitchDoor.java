package dungeonmania.entities.logicals.logicalEntities;

import dungeonmania.entities.Entity;
import dungeonmania.entities.enemies.Spider;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class SwitchDoor extends LogicalEntity {
    private boolean open = false;

    public SwitchDoor(Position position, String logic) {
        super(position, logic);
    }

    @Override
    public void logicalUpdate(GameMap gameMap) {
        if (logicalRuleCheck(gameMap)) {
            open = true;
        } else {
            open = false;
        }
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        if (entity instanceof Spider) {
            return true;
        } else {
            return open;
        }
    }

    public boolean isOpen() {
        return open;
    }

}
