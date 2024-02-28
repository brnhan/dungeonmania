package dungeonmania.goals;

import dungeonmania.Game;
import dungeonmania.goals.goalNodes.GoalNode;

public class Goal {
    private GoalNode goalNode;

    public Goal(GoalNode goalNode) {
        this.goalNode = goalNode;
    }

    /**
     * @return true if the goal has been achieved, false otherwise
     */
    public boolean achieved(Game game) {
        if (game.getPlayer() == null || game.getTick() == 0)
            return false;

        return goalNode.achieved(game);
    }

    public String toString(Game game) {
        return goalNode.toString(game);
    }

}
