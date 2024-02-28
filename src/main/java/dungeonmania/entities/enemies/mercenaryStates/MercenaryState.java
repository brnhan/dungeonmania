package dungeonmania.entities.enemies.mercenaryStates;

import dungeonmania.entities.enemies.Mercenary;

public abstract class MercenaryState {
    private Mercenary mercenary;
    private int timeControlled = 0;

    public MercenaryState(Mercenary mercenary) {
        this.mercenary = mercenary;
    }

    public abstract void verifyAlliedStatus();

    public abstract String toString();

    public Mercenary getMercenary() {
        return mercenary;
    }

    public void setMercenary(Mercenary mercenary) {
        this.mercenary = mercenary;
    }

    public int getTimeControlled() {
        return timeControlled;
    }

    public void setTimeControlled(int timeControlled) {
        this.timeControlled = timeControlled;
    }

    public void setMercenaryState(MercenaryState state) {
        mercenary.setState(state);
    }
}
