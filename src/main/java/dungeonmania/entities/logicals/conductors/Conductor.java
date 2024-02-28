package dungeonmania.entities.logicals.conductors;

import dungeonmania.entities.Entity;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Conductor extends Entity {
    private boolean activated = false;
    private int tickActivated;

    public Conductor(Position position) {
        super(position);
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public void activate(GameMap map) {
        setActivated(true);
        tickActivated = map.getTick();

    }

    public int getTickActivated() {
        return tickActivated;
    }

}
