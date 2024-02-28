package dungeonmania.task2;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.mvp.TestUtils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class EnemyGoalsTest {
    @Test
    @DisplayName("Test goal - no enemies required no spawners")
    public void noEnemies() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse response = dmc.newGame("d_enemyGoalsTest_noEnemies",
                "c_basicGoalsTest_exit");
        List<EntityResponse> entities = response.getEntities();
        assertEquals(1, TestUtils.countEntityOfType(entities, "player"));

        // Goal evaluted on first tick - assert goal not met
        assertEquals(":enemies", TestUtils.getGoals(response));

        // Goal evaluted on first tick - assert goal met
        response = dmc.tick(Direction.RIGHT);
        assertEquals("", TestUtils.getGoals(response));
    }

    @Test
    @DisplayName("Test achieving a basic enemy goal - 1 zombie no spawners")
    public void basicZombieGoal() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse response = dmc.newGame("d_enemyGoalsTest_basicZombieGoal",
                "c_enemyGoalsTest_basicZombieGoal");
        List<EntityResponse> entities = response.getEntities();
        assertEquals(1, TestUtils.countEntityOfType(entities, "player"));
        assertEquals(1, TestUtils.countEntityOfType(entities, "zombie_toast"));
        assertTrue(TestUtils.getGoals(response).contains(":enemies"));

        for (int i = 0; i < 3; i++) {
            response = dmc.tick(Direction.RIGHT);
            // Check if there is a battle - if there is one of the player or zombie is dead
            int battlesHeld = response.getBattles().size();
            if (battlesHeld != 0) {
                break;
            }
            // Assert goal not met
            assertTrue(TestUtils.getGoals(response).contains(":enemy"));
        }

        // Assert goal met
        assertEquals("", TestUtils.getGoals(response));
    }

    @Test
    @DisplayName("Test achieving a basic enemy goal - 1 mercenary no spawners")
    public void basicMercenaryGoal() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse response = dmc.newGame("d_enemyGoalsTest_basicMercenaryGoal",
                "c_enemyGoalsTest_basicMercenaryGoal");
        List<EntityResponse> entities = response.getEntities();
        assertEquals(1, TestUtils.countEntityOfType(entities, "player"));
        assertEquals(1, TestUtils.countEntityOfType(entities, "mercenary"));

        // Assert goal not met
        assertTrue(TestUtils.getGoals(response).contains(":enemies"));

        for (int i = 0; i < 3; i++) {
            response = dmc.tick(Direction.RIGHT);
            // Check if there is a battle - if there is one of the player or zombie is dead
            int battlesHeld = response.getBattles().size();
            if (battlesHeld != 0) {
                break;
            }
            // Assert goal not met
            assertTrue(TestUtils.getGoals(response).contains(":enemies"));
        }

        // Assert goal met
        assertEquals("", TestUtils.getGoals(response));
    }

    @Test
    @DisplayName("Test achieving a basic enemy goal - 1 spider no spawners")
    public void basicSpiderGoal() {
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse response = dmc.newGame("d_enemyGoalsTest_basicSpiderGoal",
                "c_enemyGoalsTest_basicSpiderGoal");
        List<EntityResponse> entities = response.getEntities();
        assertEquals(1, TestUtils.countEntityOfType(entities, "player"));
        assertEquals(1, TestUtils.countEntityOfType(entities, "spider"));

        // Assert goal not met
        assertTrue(TestUtils.getGoals(response).contains(":enemies"));

        // Player kills spider
        response = dmc.tick(Direction.RIGHT);
        assertTrue(TestUtils.countEntityOfType(response.getEntities(), "spider") == 0);

        // Assert goal met
        assertEquals("", TestUtils.getGoals(response));
    }

    @Test
    @DisplayName("Test basic goal - 1 spawner no enemies")
    public void basicSpawnerGoal() {
        //  PLA  ZTS
        //  SWO
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse response = dmc.newGame("d_enemyGoalsTest_basicSpawnerGoal",
                "c_zombieTest_toastDestruction");
        assertEquals(1, TestUtils.getEntities(response, "zombie_toast_spawner").size());
        String spawnerId = TestUtils.getEntities(response, "zombie_toast_spawner").get(0).getId();

        // Assert goal not met
        assertTrue(TestUtils.getGoals(response).contains(":enemies"));

        // cardinally adjacent: true, has sword: false
        assertThrows(InvalidActionException.class, () -> dmc.interact(spawnerId));
        assertEquals(1, TestUtils.getEntities(response, "zombie_toast_spawner").size());
        // Assert goal not met
        assertTrue(TestUtils.getGoals(response).contains(":enemies"));

        // pick up sword
        response = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(response, "sword").size());

        // cardinally adjacent: false, has sword: true
        assertThrows(InvalidActionException.class, () -> dmc.interact(spawnerId));
        assertEquals(1, TestUtils.getEntities(response, "zombie_toast_spawner").size());
        // Assert goal not met
        assertTrue(TestUtils.getGoals(response).contains(":enemies"));

        // move right
        response = dmc.tick(Direction.RIGHT);

        // cardinally adjacent: true, has sword: true, but invalid_id
        assertThrows(IllegalArgumentException.class, () -> dmc.interact("random_invalid_id"));
        // Assert goal not met
        assertTrue(TestUtils.getGoals(response).contains(":enemies"));

        // cardinally adjacent: true, has sword: true
        response = assertDoesNotThrow(() -> dmc.interact(spawnerId));
        assertEquals(0, TestUtils.getEntities(response, "zombie_toast_spawner").size());
        // Assert goal met
        assertEquals("", TestUtils.getGoals(response));
    }

    @Test
    @DisplayName("Destroying spawner doesn't count towards enemy goal - 1 spawner no enemies, 1 enemy to be destroyed")
    public void destroySpawnerNotEnemy() {
        //  PLA  ZTS
        //  SWO
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse response = dmc.newGame("d_enemyGoalsTest_destroySpawnerNotEnemy",
                "c_enemyGoalsTest_destroySpawnerNotEnemy");
        assertEquals(1, TestUtils.getEntities(response, "zombie_toast_spawner").size());
        String spawnerId = TestUtils.getEntities(response, "zombie_toast_spawner").get(0).getId();

        // Assert goal not met
        assertTrue(TestUtils.getGoals(response).contains(":enemies"));

        // cardinally adjacent: true, has sword: false
        assertThrows(InvalidActionException.class, () -> dmc.interact(spawnerId));
        assertEquals(1, TestUtils.getEntities(response, "zombie_toast_spawner").size());
        // Assert goal not met
        assertTrue(TestUtils.getGoals(response).contains(":enemies"));

        // pick up sword
        response = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(response, "sword").size());

        // cardinally adjacent: false, has sword: true
        assertThrows(InvalidActionException.class, () -> dmc.interact(spawnerId));
        assertEquals(1, TestUtils.getEntities(response, "zombie_toast_spawner").size());
        // Assert goal not met
        assertTrue(TestUtils.getGoals(response).contains(":enemies"));

        // move right
        response = dmc.tick(Direction.RIGHT);

        // cardinally adjacent: true, has sword: true, but invalid_id
        assertThrows(IllegalArgumentException.class, () -> dmc.interact("random_invalid_id"));
        // Assert goal not met
        assertTrue(TestUtils.getGoals(response).contains(":enemies"));

        // cardinally adjacent: true, has sword: true
        response = assertDoesNotThrow(() -> dmc.interact(spawnerId));
        assertEquals(0, TestUtils.getEntities(response, "zombie_toast_spawner").size());
        // Assert goal not met
        assertTrue(TestUtils.getGoals(response).contains(":enemies"));
    }

    @Test
    @DisplayName("Test enemy goal of 200")
    public void enemyGoal200() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_enemyGoalsTest_enemyGoal200", "c_enemyGoalsTest_enemyGoal200");
        assertEquals(0, TestUtils.countEntityOfType(res.getEntities(), "zombie_toast"));
        String spawnerId = TestUtils.getEntities(res, "zombie_toast_spawner").get(0).getId();

        // Insufficient materials to build -> throw exception
        assertThrows(InvalidActionException.class, () -> dmc.build("midnight_armour"));

        assertEquals(0, TestUtils.getInventory(res, "sword").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());

        // Pick up Sword
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());

        // Pick up Sunstone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // Build Midnight Armour successfully
        assertEquals(0, TestUtils.countEntityOfType(res.getEntities(), "zombie_toast"));
        assertEquals(0, TestUtils.getInventory(res, "midnight_armour").size());
        res = assertDoesNotThrow(() -> dmc.build("midnight_armour"));
        assertEquals(1, TestUtils.getInventory(res, "midnight_armour").size());

        // 202 enemies defeated, spawner still exists
        while (res.getBattles().size() < 202) {
            res = dmc.tick(Direction.RIGHT);
        }
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        // Pick up Sword
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());

        // Destroy spawner
        res = assertDoesNotThrow(() -> dmc.interact(spawnerId));
        assertEquals(0, TestUtils.getEntities(res, "zombie_toast_spawner").size());

        // Goal achieved
        assertEquals("", TestUtils.getGoals(res));
    }
}
