package dungeonmania.goals.goalNodes;

import dungeonmania.Game;

public class TreasureGoal implements GoalNode {
    private int target;

    public TreasureGoal(int target) {
        this.target = target;
    }

    public boolean achieved(Game game) {
        return game.getCollectedTreasureCount() >= target;
    }

    public String toString(Game game) {
        if (this.achieved(game) && game.getTick() != 0)
            return "";

        return ":treasure";
    }
}
