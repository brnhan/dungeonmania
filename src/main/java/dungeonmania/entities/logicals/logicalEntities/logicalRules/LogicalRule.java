package dungeonmania.entities.logicals.logicalEntities.logicalRules;

import dungeonmania.entities.logicals.logicalEntities.LogicalEntity;
import dungeonmania.map.GameMap;

public interface LogicalRule {
    public boolean check(LogicalEntity logicalEntity, GameMap map);
}
