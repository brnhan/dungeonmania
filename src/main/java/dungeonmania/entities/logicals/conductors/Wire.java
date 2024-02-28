package dungeonmania.entities.logicals.conductors;

import java.util.HashSet;
import java.util.Set;

import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Wire extends Conductor {
    private Set<Switch> connectedActivatedSwitches = new HashSet<Switch>();

    public Wire(Position position) {
        super(position);
    }

    public void activate(GameMap map, Switch switchObj) {
        connectedActivatedSwitches.add(switchObj);
        if (!isActivated()) {
            super.activate(map);
        }
    }

    public boolean isActivated() {
        return super.isActivated();
    }

    public void deactivate(Switch switchObj) {
        connectedActivatedSwitches.remove(switchObj);
        if (connectedActivatedSwitches.isEmpty()) {
            super.setActivated(false);
        }
    }
}
