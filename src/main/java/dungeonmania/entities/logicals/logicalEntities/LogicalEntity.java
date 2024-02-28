package dungeonmania.entities.logicals.logicalEntities;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.entities.Entity;
import dungeonmania.entities.logicals.conductors.Conductor;
import dungeonmania.entities.logicals.logicalEntities.logicalRules.ANDLogic;
import dungeonmania.entities.logicals.logicalEntities.logicalRules.COANDLogic;
import dungeonmania.entities.logicals.logicalEntities.logicalRules.LogicalRule;
import dungeonmania.entities.logicals.logicalEntities.logicalRules.ORLogic;
import dungeonmania.entities.logicals.logicalEntities.logicalRules.XORLogic;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public abstract class LogicalEntity extends Entity {
    private LogicalRule logicalRule;

    public LogicalEntity(Position position, String logic) {
        super(position);
        switch (logic) {
            case "and":
                logicalRule = new ANDLogic();
                break;
            case "or":
                logicalRule = new ORLogic();
                break;
            case "xor":
                logicalRule = new XORLogic();
                break;
            case "co_and":
                logicalRule = new COANDLogic();
                break;
            default:
                throw new IllegalArgumentException("Invalid logic: " + logic);
        }
    }

    public abstract void logicalUpdate(GameMap map);

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    public boolean logicalRuleCheck(GameMap map) {
        return logicalRule.check(this, map);
    }

    public List<Conductor> getConnectedConductors(GameMap map) {
        List<Conductor> conductors = new ArrayList<>();
        getPosition().getCardinallyAdjacentPositions().forEach(position -> {
            Conductor conductor = map.getEntity(position, Conductor.class);
            if (conductor != null) {
                conductors.add(conductor);
            }
        });
        return conductors;
    }

    public int getNumberConnectedConductors(GameMap map) {
        return getConnectedConductors(map).size();
    }

    public int getNumberActivatedConnectedConductors(GameMap map) {
        int active = 0;
        for (Conductor conductor : getConnectedConductors(map)) {
            if (conductor.isActivated()) {
                active++;
            }
        }
        return active;
    }

}
