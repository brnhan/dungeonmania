package dungeonmania.entities.logicals.logicalEntities.logicalRules;

import dungeonmania.entities.logicals.logicalEntities.LogicalEntity;
import dungeonmania.map.GameMap;

public class ANDLogic implements LogicalRule {

    @Override
    public boolean check(LogicalEntity logicalEntity, GameMap map) {
        int connectedConductors = logicalEntity.getNumberConnectedConductors(map);
        int activeConnectedConductors = logicalEntity.getNumberActivatedConnectedConductors(map);
        return (connectedConductors == activeConnectedConductors) && (connectedConductors >= 2);
    }

}
