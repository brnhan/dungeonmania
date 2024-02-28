package dungeonmania.task2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class LogicalBombTest {
    private boolean bombAt(DungeonResponse res, int x, int y) {
        Position pos = new Position(x, y);
        return TestUtils.getEntitiesStream(res, "bomb").anyMatch(it -> it.getPosition().equals(pos));
    }

    @Test
    @Tag("18-1-1")
    @DisplayName("Test Logical Bomb Collision")
    public void collision() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_LogicalBombTest_collisions", "c_LogicalEntitiesTest_noMonstersCase");
        assertTrue(bombAt(res, 2, 1));

        // try walk over bomb (spawned)
        Position pos = TestUtils.getEntities(res, "player").get(0).getPosition();
        res = dmc.tick(Direction.RIGHT);
        assertNotEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());
        assertFalse(bombAt(res, 2, 1));
        assertEquals(1, TestUtils.countEntityOfTypeInInventory(res, "bomb"));

        // walk away from bomb
        pos = TestUtils.getEntities(res, "player").get(0).getPosition();
        res = dmc.tick(Direction.LEFT);
        assertNotEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());
        assertFalse(bombAt(res, 2, 1));

        // place bomb
        String bombId = TestUtils.getFirstItemId(res, "bomb");
        res = dmc.tick(bombId);
        assertTrue(bombAt(res, 1, 1));

        // try walk over bomb (placed)
        pos = TestUtils.getEntities(res, "player").get(0).getPosition();
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.LEFT);
        assertEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());
        assertTrue(bombAt(res, 1, 1));
        assertEquals(0, TestUtils.countEntityOfTypeInInventory(res, "bomb"));
    }

    @Test
    @Tag("18-1-2")
    @DisplayName("Test removal of surrounding entities")
    public void removal() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_LogicalBombTest_removal", "c_LogicalEntitiesTest_noMonstersCase");
        assertTrue(bombAt(res, 4, 4));

        // Activate Switch
        res = dmc.tick(Direction.RIGHT);

        // Check Bomb exploded
        assertEquals(0, TestUtils.getEntities(res, "bomb").size());
        assertEquals(0, TestUtils.getEntities(res, "boulder").size());
        assertEquals(0, TestUtils.getEntities(res, "switch").size());
        assertEquals(0, TestUtils.getEntities(res, "wall").size());
        assertEquals(0, TestUtils.getEntities(res, "treasure").size());
        assertEquals(0, TestUtils.getEntities(res, "exit").size());
        assertEquals(1, TestUtils.getEntities(res, "player").size());
        assertFalse(bombAt(res, 4, 4));
    }

    @Test
    @Tag("18-1-3")
    @DisplayName("Test removal of surrounding entities - radius 10")
    public void removal10() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_LogicalBombTest_removal10", "c_LogicalBombTest_radius10");
        assertTrue(bombAt(res, 4, 4));

        // Activate Switch
        res = dmc.tick(Direction.RIGHT);

        // Check Bomb exploded
        assertEquals(0, TestUtils.getEntities(res, "bomb").size());
        assertEquals(0, TestUtils.getEntities(res, "boulder").size());
        assertEquals(0, TestUtils.getEntities(res, "switch").size());
        assertEquals(0, TestUtils.getEntities(res, "wall").size());
        assertEquals(0, TestUtils.getEntities(res, "treasure").size());
        assertEquals(1, TestUtils.getEntities(res, "exit").size());
        assertEquals(1, TestUtils.getEntities(res, "player").size());
        assertFalse(bombAt(res, 4, 4));
    }

    @Test
    @Tag("18-1-4")
    @DisplayName("Test place bomb then activate")
    public void takePutActivate() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_LogicalBombTest_takePutActivate", "c_LogicalEntitiesTest_noMonstersCase");
        assertTrue(bombAt(res, 4, 4));

        // Pickup bomb
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.UP);
        assertFalse(bombAt(res, 4, 4));
        assertEquals(1, TestUtils.countEntityOfTypeInInventory(res, "bomb"));

        // Move and Place bomb
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.UP);
        String bombId = TestUtils.getFirstItemId(res, "bomb");
        res = dmc.tick(bombId);
        assertEquals(0, TestUtils.countEntityOfTypeInInventory(res, "bomb"));
        assertTrue(bombAt(res, 3, 3));

        // Activate Switch
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);

        // Check Bomb exploded
        assertEquals(0, TestUtils.getEntities(res, "bomb").size());
        assertEquals(0, TestUtils.getEntities(res, "boulder").size());
        assertEquals(0, TestUtils.getEntities(res, "switch").size());
        assertEquals(0, TestUtils.getEntities(res, "wall").size());
        assertEquals(1, TestUtils.getEntities(res, "treasure").size());
        assertEquals(1, TestUtils.getEntities(res, "exit").size());
        assertEquals(1, TestUtils.getEntities(res, "player").size());
        assertFalse(bombAt(res, 3, 3));
    }

    @Test
    @Tag("18-1-4")
    @DisplayName("Test place bomb on Active Switch")
    public void takeActivatePut() throws IllegalArgumentException, InvalidActionException {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_LogicalBombTest_takePutActivate", "c_LogicalEntitiesTest_noMonstersCase");
        assertTrue(bombAt(res, 4, 4));

        // Pickup bomb
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.UP);
        assertFalse(bombAt(res, 4, 4));
        assertEquals(1, TestUtils.countEntityOfTypeInInventory(res, "bomb"));

        // Move and Activate Switch
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);

        // Place bomb
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        String bombId = TestUtils.getFirstItemId(res, "bomb");
        res = dmc.tick(bombId);
        assertEquals(0, TestUtils.countEntityOfTypeInInventory(res, "bomb"));

        // Check Bomb exploded
        assertEquals(0, TestUtils.getEntities(res, "bomb").size());
        assertEquals(0, TestUtils.getEntities(res, "boulder").size());
        assertEquals(0, TestUtils.getEntities(res, "switch").size());
        assertEquals(0, TestUtils.getEntities(res, "wall").size());
        assertEquals(1, TestUtils.getEntities(res, "treasure").size());
        assertEquals(1, TestUtils.getEntities(res, "exit").size());
        assertEquals(1, TestUtils.getEntities(res, "player").size());
        assertFalse(bombAt(res, 3, 3));
    }

    private void checkBombDetonates(String testName, int bombX, int bombY) {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_" + testName, "c_LogicalEntitiesTest_noMonstersCase");

        // start
        Position pos = TestUtils.getEntities(res, "player").get(0).getPosition();
        assertTrue(bombAt(res, bombX, bombY));

        // push boulder
        res = dmc.tick(Direction.RIGHT);
        assertNotEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());

        // door opens
        assertFalse(bombAt(res, bombX, bombY));
    }

    // OR Bomb
    @Test
    @Tag("18-2-1")
    @DisplayName("Test turning on/off OR Bomb with a cardinally adjacent switch")
    public void orBombSwitch() {
        checkBombDetonates("LogicalBombTest_orSwitch", 4, 1);
    }

    @Test
    @Tag("18-2-2")
    @DisplayName("Test turning on/off OR Bomb with a cardinally adjacent wire")
    public void orBombWire() {
        checkBombDetonates("LogicalBombTest_orWire", 5, 1);
    }

    @Test
    @Tag("18-2-3")
    @DisplayName("Test turning on/off OR Bomb with a cardinally adjacent wire far from switch")
    public void orBombWireLong() {
        checkBombDetonates("LogicalBombTest_orWireLong", 10, 1);
    }

    @Test
    @Tag("18-2-4")
    @DisplayName("Test turning on/off OR Bomb with 2 cardinally adjacent wire")
    public void orBombWireMultiple2() {
        checkBombDetonates("LogicalBombTest_orWireMultiple2", 5, 1);
    }

    @Test
    @Tag("18-2-5")
    @DisplayName("Test turning on/off OR Bomb with 3 cardinally adjacent wire")
    public void orBombWireMultiple3() {
        checkBombDetonates("LogicalBombTest_orWireMultiple3", 5, 1);
    }

    @Test
    @Tag("18-2-6")
    @DisplayName("Test turning on/off OR Bomb with 4 cardinally adjacent wire")
    public void orBombWireMultiple4() {
        checkBombDetonates("LogicalBombTest_orWireMultiple4", 5, 1);
    }

    private void checkBombStayOff(String testName, int bombX, int bombY) {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_" + testName, "c_LogicalEntitiesTest_noMonstersCase");

        // start
        Position pos = TestUtils.getEntities(res, "player").get(0).getPosition();
        assertTrue(bombAt(res, bombX, bombY));

        // push boulder
        res = dmc.tick(Direction.RIGHT);
        assertNotEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());

        // bomb doesn't explode
        assertTrue(bombAt(res, bombX, bombY));
    }

    @Test
    @Tag("18-2-7")
    @DisplayName("Test turning on/off OR Bomb with a diagonally adjacent switch")
    public void orBombSwitchDiagonal() {
        checkBombStayOff("LogicalBombTest_orSwitchDiagonal", 4, 2);
    }

    @Test
    @Tag("18-2-8")
    @DisplayName("Test turning on/off OR Bomb with a diagonally adjacent wire")
    public void orBombWireDiagonal() {
        checkBombStayOff("LogicalBombTest_orWireDiagonal", 5, 2);
    }

    // XOR Bomb
    @Test
    @Tag("18-3-1")
    @DisplayName("Test turning on/off XOR Bomb with a cardinally adjacent switch")
    public void xorBombSwitch() {
        checkBombDetonates("LogicalBombTest_xorSwitch", 4, 1);
    }

    @Test
    @Tag("18-3-2")
    @DisplayName("Test turning on/off XOR Bomb with a cardinally adjacent wire")
    public void xorBombWire() {
        checkBombDetonates("LogicalBombTest_xorWire", 5, 1);
    }

    @Test
    @Tag("18-3-3")
    @DisplayName("Test turning on/off XOR Bomb with a cardinally adjacent wire far from switch")
    public void xorBombWireLong() {
        checkBombDetonates("LogicalBombTest_xorWireLong", 10, 1);
    }

    @Test
    @Tag("18-3-4")
    @DisplayName("Test turning on/off XOR Bomb with 2 cardinally adjacent wire")
    public void xorBombWireMultiple2() {
        checkBombStayOff("LogicalBombTest_xorWireMultiple2", 5, 1);
    }

    @Test
    @Tag("18-3-5")
    @DisplayName("Test turning on/off XOR Bomb with 3 cardinally adjacent wire")
    public void xorBombWireMultiple3() {
        checkBombStayOff("LogicalBombTest_xorWireMultiple3", 5, 1);
    }

    @Test
    @Tag("18-3-6")
    @DisplayName("Test turning on/off XOR Bomb with 4 cardinally adjacent wire")
    public void xorBombWireMultiple4() {
        checkBombStayOff("LogicalBombTest_xorWireMultiple4", 5, 1);
    }

    @Test
    @Tag("18-3-7")
    @DisplayName("Test turning on/off XOR Bomb with a diagonally adjacent switch")
    public void xorBombSwitchDiagonal() {
        checkBombStayOff("LogicalBombTest_xorSwitchDiagonal", 4, 2);
    }

    @Test
    @Tag("18-3-8")
    @DisplayName("Test turning on/off XOR Bomb with a diagonally adjacent wire")
    public void xorBombWireDiagonal() {
        checkBombStayOff("LogicalBombTest_xorWireDiagonal", 5, 2);
    }

    // AND Bomb
    @Test
    @Tag("18-4-1")
    @DisplayName("Test turning on/off AND Bomb with a cardinally adjacent switch")
    public void andBombSwitch() {
        checkBombStayOff("LogicalBombTest_andSwitch", 4, 1);
    }

    @Test
    @Tag("18-4-2")
    @DisplayName("Test turning on/off AND Bomb with a cardinally adjacent wire")
    public void andBombWire() {
        checkBombStayOff("LogicalBombTest_andWire", 5, 1);
    }

    @Test
    @Tag("18-4-3")
    @DisplayName("Test turning on/off AND Bomb with a cardinally adjacent wire far from switch")
    public void andBombWireLong() {
        checkBombStayOff("LogicalBombTest_andWireLong", 10, 1);
    }

    @Test
    @Tag("18-4-4")
    @DisplayName("Test turning on/off AND Bomb with 2 cardinally adjacent wire")
    public void andBombWireMultiple2() {
        checkBombDetonates("LogicalBombTest_andWireMultiple2", 5, 1);
    }

    @Test
    @Tag("18-4-5")
    @DisplayName("Test turning on/off AND Bomb with 3 cardinally adjacent wire")
    public void andBombWireMultiple3() {
        checkBombDetonates("LogicalBombTest_andWireMultiple3", 5, 1);
    }

    @Test
    @Tag("18-4-6")
    @DisplayName("Test turning on/off AND Bomb with 4 cardinally adjacent wire")
    public void andBombWireMultiple4() {
        checkBombDetonates("LogicalBombTest_andWireMultiple4", 5, 1);
    }

    @Test
    @Tag("18-4-7")
    @DisplayName("Test turning on/off AND Bomb with a diagonally adjacent switch")
    public void andBombSwitchDiagonal() {
        checkBombStayOff("LogicalBombTest_andSwitchDiagonal", 4, 2);
    }

    @Test
    @Tag("18-4-8")
    @DisplayName("Test turning on/off AND Bomb with a diagonally adjacent wire")
    public void andBombWireDiagonal() {
        checkBombStayOff("LogicalBombTest_andWireDiagonal", 5, 2);
    }

    private void checkANDBombExplodesSeparateTick(String testName, int bombX, int bombY) {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_" + testName, "c_LogicalEntitiesTest_noMonstersCase");

        // start
        Position pos = TestUtils.getEntities(res, "player").get(0).getPosition();
        assertTrue(bombAt(res, bombX, bombY));

        // push boulder
        res = dmc.tick(Direction.RIGHT);
        assertNotEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());

        // bomb still off
        assertTrue(bombAt(res, bombX, bombY));

        // push boulder 2
        res = dmc.tick(Direction.UP);
        assertFalse(bombAt(res, bombX, bombY));
    }

    @Test
    @Tag("18-4-9")
    @DisplayName("Test turning on/off AND Bomb with 2 cardinally adjacent wire and 1 cardinally adjacent switch")
    public void andBomb2Wire1SwitchSeparateTick() {
        checkANDBombExplodesSeparateTick("LogicalBombTest_and2Wire1SwitchSeparateTick", 4, 2);
    }

    @Test
    @Tag("18-4-9")
    @DisplayName("Test turning on/off AND Bomb with 2 cardinally adjacent wire and 1 separate wire")
    public void andBomb2Wire1WireSeparateTick() {
        checkANDBombExplodesSeparateTick("LogicalBombTest_and2Wire1WireSeparateTick", 4, 2);
    }

    // CO_AND Bomb
    @Test
    @Tag("18-4-1")
    @DisplayName("Test turning on/off CO_AND Bomb with a cardinally adjacent switch")
    public void coAndBombSwitch() {
        checkBombStayOff("LogicalBombTest_coAndSwitch", 4, 1);
    }

    @Test
    @Tag("18-4-2")
    @DisplayName("Test turning on/off CO_AND Bomb with a cardinally adjacent wire")
    public void coAndBombWire() {
        checkBombStayOff("LogicalBombTest_coAndWire", 5, 1);
    }

    @Test
    @Tag("18-4-3")
    @DisplayName("Test turning on/off CO_AND Bomb with a cardinally adjacent wire far from switch")
    public void coAndBombWireLong() {
        checkBombStayOff("LogicalBombTest_coAndWireLong", 10, 1);
    }

    @Test
    @Tag("18-4-4")
    @DisplayName("Test turning on/off CO_AND Bomb with 2 cardinally adjacent wire")
    public void coAndBombWireMultiple2() {
        checkBombDetonates("LogicalBombTest_coAndWireMultiple2", 5, 1);
    }

    @Test
    @Tag("18-4-5")
    @DisplayName("Test turning on/off CO_AND Bomb with 3 cardinally adjacent wire")
    public void coAndBombWireMultiple3() {
        checkBombDetonates("LogicalBombTest_coAndWireMultiple3", 5, 1);
    }

    @Test
    @Tag("18-4-6")
    @DisplayName("Test turning on/off CO_AND Bomb with 4 cardinally adjacent wire")
    public void coAndBombWireMultiple4() {
        checkBombDetonates("LogicalBombTest_coAndWireMultiple4", 5, 1);
    }

    @Test
    @Tag("18-4-7")
    @DisplayName("Test turning on/off CO_AND Bomb with a diagonally adjacent switch")
    public void coAndBombSwitchDiagonal() {
        checkBombStayOff("LogicalBombTest_coAndSwitchDiagonal", 4, 2);
    }

    @Test
    @Tag("18-4-8")
    @DisplayName("Test turning on/off CO_AND Bomb with a diagonally adjacent wire")
    public void coAndBombWireDiagonal() {
        checkBombStayOff("LogicalBombTest_coAndWireDiagonal", 5, 2);
    }

    private void checkCOANDBombStaysOffSeparateTick(String testName, int bombX, int bombY) {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_" + testName, "c_LogicalEntitiesTest_noMonstersCase");

        // start
        Position pos = TestUtils.getEntities(res, "player").get(0).getPosition();
        assertTrue(bombAt(res, bombX, bombY));

        // push boulder
        res = dmc.tick(Direction.RIGHT);
        assertNotEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());

        // light still off
        assertTrue(bombAt(res, bombX, bombY));

        // push boulder 2
        res = dmc.tick(Direction.UP);
        assertTrue(bombAt(res, bombX, bombY));
    }

    @Test
    @Tag("18-4-9")
    @DisplayName("Test turning on/off COAND Bomb with cardinally adjacent wires (2) and switch")
    public void coAndBomb2Wire1SwitchSeparateTick() {
        checkCOANDBombStaysOffSeparateTick("LogicalBombTest_coAnd2Wire1SwitchSeparateTick", 4, 2);
    }

    @Test
    @Tag("18-4-10")
    @DisplayName("Test turning on/off CO_AND Bomb with 2 cardinally adjacent wire and 1 separate wire")
    public void coAndBomb2Wire1WireSeparateTick() {
        checkCOANDBombStaysOffSeparateTick("LogicalBombTest_coAnd2Wire1WireSeparateTick", 4, 2);
    }
}
