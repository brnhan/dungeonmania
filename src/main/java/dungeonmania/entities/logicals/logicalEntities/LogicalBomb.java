package dungeonmania.entities.logicals.logicalEntities;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.collectables.Bomb;
import dungeonmania.entities.collectables.Bomb.State;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class LogicalBomb extends LogicalEntity implements InventoryItem {
    private Bomb bomb;

    public LogicalBomb(Position position, int radius, String logic) {
        super(position, logic);
        bomb = new Bomb(position, radius);
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (bomb.getState() != State.SPAWNED)
            return;
        if (entity instanceof Player) {
            ((Player) entity).pickUp(this);
            map.destroyEntity(this);
            bomb.setState(State.INVENTORY);
        }
    }

    public void onPutDown(GameMap map, Position p) {
        setPosition(p);
        bomb.setPosition(p);
        map.addEntity(this);
        bomb.setState(State.PLACED);
        map.triggerLogicalEvents();
    }

    @Override
    public void logicalUpdate(GameMap map) {
        if (logicalRuleCheck(map)) {
            bomb.explode(map);
        }
    }
}
