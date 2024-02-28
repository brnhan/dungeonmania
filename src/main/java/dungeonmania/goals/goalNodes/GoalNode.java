package dungeonmania.goals.goalNodes;

import dungeonmania.Game;

public interface GoalNode {
    public boolean achieved(Game game);

    public String toString(Game game);
}
