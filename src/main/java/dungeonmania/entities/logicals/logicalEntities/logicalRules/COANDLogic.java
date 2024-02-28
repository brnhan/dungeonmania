package dungeonmania.entities.logicals.logicalEntities.logicalRules;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import dungeonmania.entities.logicals.conductors.Conductor;
import dungeonmania.entities.logicals.logicalEntities.LogicalEntity;
import dungeonmania.map.GameMap;

public class COANDLogic extends ANDLogic {

    @Override
    public boolean check(LogicalEntity logicalEntity, GameMap map) {
        if (!(super.check(logicalEntity, map))) {
            return false;
        }
        List<Conductor> conductors = logicalEntity.getConnectedConductors(map);
        Set<Integer> activationTimes = conductors.stream()
                .map(conductor -> conductor.getTickActivated())
                .collect(Collectors.toSet());
        return activationTimes.size() == 1;

    }

}
