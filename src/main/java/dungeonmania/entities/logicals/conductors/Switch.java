package dungeonmania.entities.logicals.conductors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dungeonmania.entities.Boulder;
import dungeonmania.entities.Entity;
import dungeonmania.entities.collectables.Bomb;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Switch extends Conductor {
    private List<Bomb> bombs = new ArrayList<>();

    public Switch(Position position) {
        super(position.asLayer(Entity.ITEM_LAYER));
    }

    public void subscribe(Bomb b) {
        bombs.add(b);
    }

    public void subscribe(Bomb bomb, GameMap map) {
        bombs.add(bomb);
        if (isActivated()) {
            bombs.stream().forEach(b -> b.notify(map));
        }
    }

    public void unsubscribe(Bomb b) {
        bombs.remove(b);
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (entity instanceof Boulder) {
            // Activate and store tick activated if not activated
            super.activate(map);
            // Explode non-logical linked bombs
            bombs.stream().forEach(b -> b.notify(map));
            // Turn on all connected wires
            turnOnConnectedWires(map, getPosition(), new HashSet<>());
        }
    }

    private void turnOnConnectedWires(GameMap map, Position position, Set<Position> visited) {
        visited.add(position);
        for (Position cardPosition : position.getCardinallyAdjacentPositions()) {
            turnOnAdjacentWire(map, cardPosition, visited);
        }
    }

    private void turnOnAdjacentWire(GameMap map, Position position, Set<Position> visited) {
        Wire wire = map.getEntity(position, Wire.class);
        if (wire != null && !visited.contains(position)) {
            wire.activate(map, this);
            turnOnConnectedWires(map, position, visited);
        }
    }

    @Override
    public void onMovedAway(GameMap map, Entity entity) {
        if (entity instanceof Boulder) {
            super.setActivated(false);
            turnOffConnectedWires(map, getPosition(), new HashSet<>());
        }
    }

    private void turnOffConnectedWires(GameMap map, Position position, Set<Position> visited) {
        visited.add(position);
        for (Position cardPosition : position.getCardinallyAdjacentPositions()) {
            turnOffAdjacentWire(map, cardPosition, visited);
        }
    }

    private void turnOffAdjacentWire(GameMap map, Position position, Set<Position> visited) {
        Wire wire = map.getEntity(position, Wire.class);
        if (wire != null && !visited.contains(position)) {
            wire.deactivate(this);
            turnOffConnectedWires(map, position, visited);
        }
    }

}
