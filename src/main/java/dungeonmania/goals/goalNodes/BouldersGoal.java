package dungeonmania.goals.goalNodes;

import dungeonmania.Game;
import dungeonmania.entities.logicals.conductors.Switch;

public class BouldersGoal implements GoalNode {
    public boolean achieved(Game game) {
        return game.getAllEntityType(Switch.class).stream().allMatch(s -> s.isActivated());
    }

    public String toString(Game game) {
        if (this.achieved(game) && game.getTick() != 0)
            return "";

        return ":boulders";
    }
}
