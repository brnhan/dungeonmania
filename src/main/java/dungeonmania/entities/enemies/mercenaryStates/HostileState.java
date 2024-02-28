package dungeonmania.entities.enemies.mercenaryStates;

import dungeonmania.entities.enemies.Mercenary;

public class HostileState extends MercenaryState {
    public HostileState(Mercenary mercenary) {
        super(mercenary);
        setTimeControlled(0);
    }

    public void verifyAlliedStatus() {
        return;
    }

    public String toString() {
        return "hostile";
    }
}
