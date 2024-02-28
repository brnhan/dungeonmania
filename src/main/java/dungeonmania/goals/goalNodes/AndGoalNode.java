package dungeonmania.goals.goalNodes;

import dungeonmania.Game;

public class AndGoalNode implements GoalNode {
    private GoalNode goal1;
    private GoalNode goal2;

    public AndGoalNode(GoalNode goal1, GoalNode goal2) {
        this.goal1 = goal1;
        this.goal2 = goal2;
    }

    public boolean achieved(Game game) {
        return goal1.achieved(game) && goal2.achieved(game);
    }

    public String toString(Game game) {
        if (this.achieved(game) && game.getTick() != 0)
            return "";

        return "(" + goal1.toString(game) + " AND " + goal2.toString(game) + ")";
    }
}
