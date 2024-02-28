package dungeonmania.entities.logicals.logicalEntities;

import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class LightBulb extends LogicalEntity {
    private boolean on = false;

    public LightBulb(Position position, String logic) {
        super(position, logic);
    }

    @Override
    public void logicalUpdate(GameMap gameMap) {
        if (logicalRuleCheck(gameMap)) {
            on = true;
        } else {
            on = false;
        }
    }

    public boolean isOn() {
        return on;
    }

}
