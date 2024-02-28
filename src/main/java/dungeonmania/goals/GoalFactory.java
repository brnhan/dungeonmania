package dungeonmania.goals;

import dungeonmania.goals.goalNodes.*;

import org.json.JSONArray;
import org.json.JSONObject;

public class GoalFactory {
    public static Goal createGoal(JSONObject jsonGoal, JSONObject config) {
        GoalNode goalNode = createGoalNode(jsonGoal, config);
        if (goalNode == null) {
            return null;
        }

        return new Goal(goalNode);
    }

    private static GoalNode createGoalNode(JSONObject jsonGoal, JSONObject config) {
        JSONArray subgoals;
        switch (jsonGoal.getString("goal")) {
        case "AND":
            subgoals = jsonGoal.getJSONArray("subgoals");
            return new AndGoalNode(createGoalNode(subgoals.getJSONObject(0), config),
                    createGoalNode(subgoals.getJSONObject(1), config));
        case "OR":
            subgoals = jsonGoal.getJSONArray("subgoals");
            return new OrGoalNode(createGoalNode(subgoals.getJSONObject(0), config),
                    createGoalNode(subgoals.getJSONObject(1), config));
        case "exit":
            return new ExitGoal();
        case "boulders":
            return new BouldersGoal();
        case "treasure":
            int treasureGoal = config.optInt("treasure_goal", 1);
            return new TreasureGoal(treasureGoal);
        case "enemies":
            int enemyGoal = config.optInt("enemy_goal", 0);
            return new EnemyGoal(enemyGoal);
        default:
            return null;
        }
    }
}
