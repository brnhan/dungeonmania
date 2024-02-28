package dungeonmania.task2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;

import static org.junit.jupiter.api.Assertions.*;

public class MidnightArmourTest {
    @Test
    @DisplayName("Test InvalidActionException is raised when the player "
            + "does not have sufficient items to build midnight armour")
    public void buildInvalidActionExceptionNoItems() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        dmc.newGame("d_BuildablesTest_BuildInvalidArgumentException", "c_BuildablesTest_BuildInvalidArgumentException");
        assertThrows(InvalidActionException.class, () -> dmc.build("midnight_armour"));
    }

    @Test
    @DisplayName("Test InvalidActionException is raised when the player "
            + "attempts to build midnight armour when there are zombies in the map")
    public void buildInvalidActionExceptionZombiesPresent() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_midnightArmourTest_zombiesPresent",
                "c_BuildablesTest_BuildInvalidArgumentException");
        assertEquals(1, TestUtils.countEntityOfType(res.getEntities(), "zombie_toast"));

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

        // Build Midnight Armour
        assertEquals(0, TestUtils.getInventory(res, "midnight_armour").size());
        assertThrows(InvalidActionException.class, () -> dmc.build("midnight_armour"));
    }

    @Test
    @DisplayName("Test building midnight armour")
    public void build() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_midnightArmourTest_build", "c_BuildablesTest_BuildShieldWithKey");
        assertEquals(0, TestUtils.countEntityOfType(res.getEntities(), "zombie_toast"));

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

        // Materials used in construction disappear from inventory
        assertEquals(0, TestUtils.getInventory(res, "sword").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @DisplayName("Test using sword then building")
    public void usedSwordBuild() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_midnightArmourTest_usedSwordBuild", "c_BuildablesTest_BuildShieldWithKey");
        assertEquals(0, TestUtils.countEntityOfType(res.getEntities(), "zombie_toast"));
        assertEquals(1, TestUtils.countEntityOfType(res.getEntities(), "mercenary"));

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

        // Fight Mercenary
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, res.getBattles().size());
        assertEquals(0, TestUtils.countEntityOfType(res.getEntities(), "mercenary"));

        // Build Midnight Armour successfully
        assertEquals(0, TestUtils.countEntityOfType(res.getEntities(), "zombie_toast"));
        assertEquals(0, TestUtils.getInventory(res, "midnight_armour").size());
        res = assertDoesNotThrow(() -> dmc.build("midnight_armour"));
        assertEquals(1, TestUtils.getInventory(res, "midnight_armour").size());

        // Materials used in construction disappear from inventory
        assertEquals(0, TestUtils.getInventory(res, "sword").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @DisplayName("Test midnight armour doesn't break")
    public void durability() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_midnightArmourTest_durability", "c_midnightArmourTest_durability");
        assertEquals(0, TestUtils.countEntityOfType(res.getEntities(), "zombie_toast"));

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

        // Materials used in construction disappear from inventory
        assertEquals(0, TestUtils.getInventory(res, "sword").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());

        while (res.getBattles().size() < 200) {
            res = dmc.tick(Direction.RIGHT);
            assertEquals(1, TestUtils.countEntityOfType(res.getEntities(), "player"));
        }
        assertEquals(1, TestUtils.getInventory(res, "midnight_armour").size());
    }
}
