package dungeonmania.goals.goalNodes;

import java.util.List;

import dungeonmania.Game;
import dungeonmania.entities.*;
import dungeonmania.util.Position;

public class ExitGoal implements GoalNode {
    public boolean achieved(Game game) {
        Position pos = game.getPlayerPosition();
        List<Exit> es = game.getAllEntityType(Exit.class);
        if (es == null || es.size() == 0)
            return false;
        return es.stream().map(Entity::getPosition).anyMatch(pos::equals);
    }

    public String toString(Game game) {
        if (this.achieved(game) && game.getTick() != 0)
            return "";

        return ":exit";
    }
}
