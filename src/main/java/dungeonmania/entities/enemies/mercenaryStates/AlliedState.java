package dungeonmania.entities.enemies.mercenaryStates;

import dungeonmania.entities.enemies.Mercenary;

public class AlliedState extends MercenaryState {
    private Integer mindControlDuration;

    public AlliedState(Mercenary mercenary, Integer mindControlDuration) {
        super(mercenary);
        this.mindControlDuration = mindControlDuration;
    }

    public void verifyAlliedStatus() {
        if (mindControlDuration == null) {
            return;
        }

        setTimeControlled(getTimeControlled() + 1);
        if (getTimeControlled() >= mindControlDuration) {
            setMercenaryState(new HostileState(getMercenary()));
        }
    }

    public String toString() {
        return "allied";
    }
}
