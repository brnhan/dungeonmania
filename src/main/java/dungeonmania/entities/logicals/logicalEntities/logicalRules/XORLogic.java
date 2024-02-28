package dungeonmania.entities.logicals.logicalEntities.logicalRules;

import dungeonmania.entities.logicals.logicalEntities.LogicalEntity;
import dungeonmania.map.GameMap;

public class XORLogic implements LogicalRule {

    @Override
    public boolean check(LogicalEntity logicalEntity, GameMap map) {
        int activeConnectedConductors = logicalEntity.getNumberActivatedConnectedConductors(map);
        return (activeConnectedConductors == 1);
    }

}
