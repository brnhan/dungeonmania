package dungeonmania.goals.goalNodes;

import dungeonmania.Game;
import dungeonmania.entities.enemies.ZombieToastSpawner;

public class EnemyGoal implements GoalNode {
    private int target;

    public EnemyGoal(int target) {
        this.target = target;
    }

    public boolean achieved(Game game) {
        return game.getEnemiesDestroyed() >= target && game.getAllEntityType(ZombieToastSpawner.class).size() == 0;
    }

    public String toString(Game game) {
        if (this.achieved(game) && game.getTick() != 0) {
            return "";
        }

        return ":enemies";
    }
}
