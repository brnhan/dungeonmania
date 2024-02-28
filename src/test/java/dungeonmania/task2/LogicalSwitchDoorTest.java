package dungeonmania.task2;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class LogicalSwitchDoorTest {
    private boolean switchDoorOpenAt(DungeonResponse res, int x, int y) {
        Position pos = new Position(x, y);
        return TestUtils.getEntitiesStream(res, "switch_door_open").anyMatch(it -> it.getPosition().equals(pos));
    }

    private boolean switchDoorClosedAt(DungeonResponse res, int x, int y) {
        Position pos = new Position(x, y);
        return TestUtils.getEntitiesStream(res, "switch_door").anyMatch(it -> it.getPosition().equals(pos));
    }

    @Test
    @Tag("17-1")
    @DisplayName("Test switch door collision")
    public void collision() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_LogicalSwitchDoorTest_collision", "c_LogicalEntitiesTest_noMonstersCase");
        // Door begins closed
        assertTrue(switchDoorClosedAt(res, 4, 1));
        assertFalse(switchDoorOpenAt(res, 4, 1));

        // try walk through door
        Position pos = TestUtils.getEntities(res, "player").get(0).getPosition();
        res = dmc.tick(Direction.LEFT);
        assertEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());

        // activate switch
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        assertTrue(switchDoorOpenAt(res, 4, 1));

        // Move to door
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // Move through door
        pos = TestUtils.getEntities(res, "player").get(0).getPosition();
        res = dmc.tick(Direction.UP);
        assertNotEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());

        // deactivate switch
        res = dmc.tick(Direction.LEFT);
        assertTrue(switchDoorClosedAt(res, 4, 1));

        // try walk through door
        pos = TestUtils.getEntities(res, "player").get(0).getPosition();
        res = dmc.tick(Direction.RIGHT);
        assertEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());

    }

    private void checkDoorOpensCloses(String testName, int switchDoorX, int switchDoorY) {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_" + testName, "c_LogicalEntitiesTest_noMonstersCase");

        // start
        Position pos = TestUtils.getEntities(res, "player").get(0).getPosition();
        assertTrue(switchDoorClosedAt(res, switchDoorX, switchDoorY));
        assertFalse(switchDoorOpenAt(res, switchDoorX, switchDoorY));

        // push boulder
        res = dmc.tick(Direction.RIGHT);
        assertNotEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());

        // door opens
        assertTrue(switchDoorOpenAt(res, switchDoorX, switchDoorY));

        // push boulder away
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);

        // door closes
        assertTrue(switchDoorClosedAt(res, switchDoorX, switchDoorY));
        assertFalse(switchDoorOpenAt(res, switchDoorX, switchDoorY));
    }

    // OR Switch Door
    @Test
    @Tag("17-2-1")
    @DisplayName("Test turning on/off OR Switch Door with a cardinally adjacent switch")
    public void orSwitchDoorSwitch() {
        checkDoorOpensCloses("LogicalSwitchDoorTest_orSwitch", 4, 1);
    }

    @Test
    @Tag("17-2-2")
    @DisplayName("Test turning on/off OR Switch Door with a cardinally adjacent wire")
    public void orSwitchDoorWire() {
        checkDoorOpensCloses("LogicalSwitchDoorTest_orWire", 5, 1);
    }

    @Test
    @Tag("17-2-3")
    @DisplayName("Test turning on/off OR Switch Door with a cardinally adjacent wire far from switch")
    public void orSwitchDoorWireLong() {
        checkDoorOpensCloses("LogicalSwitchDoorTest_orWireLong", 10, 1);
    }

    @Test
    @Tag("17-2-4")
    @DisplayName("Test turning on/off OR Switch Door with 2 cardinally adjacent wire")
    public void orSwitchDoorWireMultiple2() {
        checkDoorOpensCloses("LogicalSwitchDoorTest_orWireMultiple2", 5, 1);
    }

    @Test
    @Tag("17-2-5")
    @DisplayName("Test turning on/off OR Switch Door with 3 cardinally adjacent wire")
    public void orSwitchDoorWireMultiple3() {
        checkDoorOpensCloses("LogicalSwitchDoorTest_orWireMultiple3", 5, 1);
    }

    @Test
    @Tag("17-2-6")
    @DisplayName("Test turning on/off OR Switch Door with 4 cardinally adjacent wire")
    public void orSwitchDoorWireMultiple4() {
        checkDoorOpensCloses("LogicalSwitchDoorTest_orWireMultiple4", 5, 1);
    }

    private void checkSwitchDoorStayOff(String testName, int switchDoorX, int switchDoorY) {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_" + testName, "c_LogicalEntitiesTest_noMonstersCase");

        // start
        Position pos = TestUtils.getEntities(res, "player").get(0).getPosition();
        assertTrue(switchDoorClosedAt(res, switchDoorX, switchDoorY));
        assertFalse(switchDoorOpenAt(res, switchDoorX, switchDoorY));

        // push boulder
        res = dmc.tick(Direction.RIGHT);
        assertNotEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());

        // light bulb doesn't turn on
        assertTrue(switchDoorClosedAt(res, switchDoorX, switchDoorY));
        assertFalse(switchDoorOpenAt(res, switchDoorX, switchDoorY));
    }

    @Test
    @Tag("17-2-7")
    @DisplayName("Test turning on/off OR Switch Door with a diagonally adjacent switch")
    public void orSwitchDoorSwitchDiagonal() {
        checkSwitchDoorStayOff("LogicalSwitchDoorTest_orSwitchDiagonal", 4, 2);
    }

    @Test
    @Tag("17-2-8")
    @DisplayName("Test turning on/off OR Switch Door with a diagonally adjacent wire")
    public void orSwitchDoorWireDiagonal() {
        checkSwitchDoorStayOff("LogicalSwitchDoorTest_orWireDiagonal", 5, 2);
    }

    // XOR Switch Door
    @Test
    @Tag("17-3-1")
    @DisplayName("Test turning on/off XOR Switch Door with a cardinally adjacent switch")
    public void xorSwitchDoorSwitch() {
        checkDoorOpensCloses("LogicalSwitchDoorTest_xorSwitch", 4, 1);
    }

    @Test
    @Tag("17-3-2")
    @DisplayName("Test turning on/off XOR Switch Door with a cardinally adjacent wire")
    public void xorSwitchDoorWire() {
        checkDoorOpensCloses("LogicalSwitchDoorTest_xorWire", 5, 1);
    }

    @Test
    @Tag("17-3-3")
    @DisplayName("Test turning on/off XOR Switch Door with a cardinally adjacent wire far from switch")
    public void xorSwitchDoorWireLong() {
        checkDoorOpensCloses("LogicalSwitchDoorTest_xorWireLong", 10, 1);
    }

    @Test
    @Tag("17-3-4")
    @DisplayName("Test turning on/off XOR Switch Door with 2 cardinally adjacent wire")
    public void xorSwitchDoorWireMultiple2() {
        checkSwitchDoorStayOff("LogicalSwitchDoorTest_xorWireMultiple2", 5, 1);
    }

    @Test
    @Tag("17-3-5")
    @DisplayName("Test turning on/off XOR Switch Door with 3 cardinally adjacent wire")
    public void xorSwitchDoorWireMultiple3() {
        checkSwitchDoorStayOff("LogicalSwitchDoorTest_xorWireMultiple3", 5, 1);
    }

    @Test
    @Tag("17-3-6")
    @DisplayName("Test turning on/off XOR Switch Door with 4 cardinally adjacent wire")
    public void xorSwitchDoorWireMultiple4() {
        checkSwitchDoorStayOff("LogicalSwitchDoorTest_xorWireMultiple4", 5, 1);
    }

    @Test
    @Tag("17-3-7")
    @DisplayName("Test turning on/off XOR Switch Door with a diagonally adjacent switch")
    public void xorSwitchDoorSwitchDiagonal() {
        checkSwitchDoorStayOff("LogicalSwitchDoorTest_xorSwitchDiagonal", 4, 2);
    }

    @Test
    @Tag("17-3-8")
    @DisplayName("Test turning on/off XOR Switch Door with a diagonally adjacent wire")
    public void xorSwitchDoorWireDiagonal() {
        checkSwitchDoorStayOff("LogicalSwitchDoorTest_xorWireDiagonal", 5, 2);
    }

    // AND Switch Door
    @Test
    @Tag("17-4-1")
    @DisplayName("Test turning on/off AND Switch Door with a cardinally adjacent switch")
    public void andSwitchDoorSwitch() {
        checkSwitchDoorStayOff("LogicalSwitchDoorTest_andSwitch", 4, 1);
    }

    @Test
    @Tag("17-4-2")
    @DisplayName("Test turning on/off AND Switch Door with a cardinally adjacent wire")
    public void andSwitchDoorWire() {
        checkSwitchDoorStayOff("LogicalSwitchDoorTest_andWire", 5, 1);
    }

    @Test
    @Tag("17-4-3")
    @DisplayName("Test turning on/off AND Switch Door with a cardinally adjacent wire far from switch")
    public void andSwitchDoorWireLong() {
        checkSwitchDoorStayOff("LogicalSwitchDoorTest_andWireLong", 10, 1);
    }

    @Test
    @Tag("17-4-4")
    @DisplayName("Test turning on/off AND Switch Door with 2 cardinally adjacent wire")
    public void andSwitchDoorWireMultiple2() {
        checkDoorOpensCloses("LogicalSwitchDoorTest_andWireMultiple2", 5, 1);
    }

    @Test
    @Tag("17-4-5")
    @DisplayName("Test turning on/off AND Switch Door with 3 cardinally adjacent wire")
    public void andSwitchDoorWireMultiple3() {
        checkDoorOpensCloses("LogicalSwitchDoorTest_andWireMultiple3", 5, 1);
    }

    @Test
    @Tag("17-4-6")
    @DisplayName("Test turning on/off AND Switch Door with 4 cardinally adjacent wire")
    public void andSwitchDoorWireMultiple4() {
        checkDoorOpensCloses("LogicalSwitchDoorTest_andWireMultiple4", 5, 1);
    }

    @Test
    @Tag("17-4-7")
    @DisplayName("Test turning on/off AND Switch Door with a diagonally adjacent switch")
    public void andSwitchDoorSwitchDiagonal() {
        checkSwitchDoorStayOff("LogicalSwitchDoorTest_andSwitchDiagonal", 4, 2);
    }

    @Test
    @Tag("17-4-8")
    @DisplayName("Test turning on/off AND Switch Door with a diagonally adjacent wire")
    public void andSwitchDoorWireDiagonal() {
        checkSwitchDoorStayOff("LogicalSwitchDoorTest_andWireDiagonal", 5, 2);
    }

    private void checkANDDoorOpensClosesSeparateTick(String testName, int switchDoorX, int switchDoorY) {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_" + testName, "c_LogicalEntitiesTest_noMonstersCase");

        // start
        Position pos = TestUtils.getEntities(res, "player").get(0).getPosition();
        assertTrue(switchDoorClosedAt(res, switchDoorX, switchDoorY));
        assertFalse(switchDoorOpenAt(res, switchDoorX, switchDoorY));

        // push boulder
        res = dmc.tick(Direction.RIGHT);
        assertNotEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());

        // door still off
        assertTrue(switchDoorClosedAt(res, switchDoorX, switchDoorY));
        assertFalse(switchDoorOpenAt(res, switchDoorX, switchDoorY));

        // push boulder 2
        res = dmc.tick(Direction.UP);
        assertTrue(switchDoorOpenAt(res, switchDoorX, switchDoorY));

        // push boulder 1 off switch
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        assertTrue(switchDoorClosedAt(res, switchDoorX, switchDoorY));
        assertFalse(switchDoorOpenAt(res, switchDoorX, switchDoorY));
    }

    @Test
    @Tag("17-4-9")
    @DisplayName("Test turning on/off AND Switch Door with 2 cardinally adjacent wire and 1 cardinally adjacent switch")
    public void andSwitchDoor2Wire1SwitchSeparateTick() {
        checkANDDoorOpensClosesSeparateTick("LogicalSwitchDoorTest_and2Wire1SwitchSeparateTick", 4, 2);
    }

    @Test
    @Tag("17-4-9")
    @DisplayName("Test turning on/off AND Switch Door with 2 cardinally adjacent wire and 1 separate wire")
    public void andSwitchDoor2Wire1WireSeparateTick() {
        checkANDDoorOpensClosesSeparateTick("LogicalSwitchDoorTest_and2Wire1WireSeparateTick", 4, 2);
    }

    // CO_AND Switch Door
    @Test
    @Tag("17-4-1")
    @DisplayName("Test turning on/off CO_AND Switch Door with a cardinally adjacent switch")
    public void coAndSwitchDoorSwitch() {
        checkSwitchDoorStayOff("LogicalSwitchDoorTest_coAndSwitch", 4, 1);
    }

    @Test
    @Tag("17-4-2")
    @DisplayName("Test turning on/off CO_AND Switch Door with a cardinally adjacent wire")
    public void coAndSwitchDoorWire() {
        checkSwitchDoorStayOff("LogicalSwitchDoorTest_coAndWire", 5, 1);
    }

    @Test
    @Tag("17-4-3")
    @DisplayName("Test turning on/off CO_AND Switch Door with a cardinally adjacent wire far from switch")
    public void coAndSwitchDoorWireLong() {
        checkSwitchDoorStayOff("LogicalSwitchDoorTest_coAndWireLong", 10, 1);
    }

    @Test
    @Tag("17-4-4")
    @DisplayName("Test turning on/off CO_AND Switch Door with 2 cardinally adjacent wire")
    public void coAndSwitchDoorWireMultiple2() {
        checkDoorOpensCloses("LogicalSwitchDoorTest_coAndWireMultiple2", 5, 1);
    }

    @Test
    @Tag("17-4-5")
    @DisplayName("Test turning on/off CO_AND Switch Door with 3 cardinally adjacent wire")
    public void coAndSwitchDoorWireMultiple3() {
        checkDoorOpensCloses("LogicalSwitchDoorTest_coAndWireMultiple3", 5, 1);
    }

    @Test
    @Tag("17-4-6")
    @DisplayName("Test turning on/off CO_AND Switch Door with 4 cardinally adjacent wire")
    public void coAndSwitchDoorWireMultiple4() {
        checkDoorOpensCloses("LogicalSwitchDoorTest_coAndWireMultiple4", 5, 1);
    }

    @Test
    @Tag("17-4-7")
    @DisplayName("Test turning on/off CO_AND Switch Door with a diagonally adjacent switch")
    public void coAndSwitchDoorSwitchDiagonal() {
        checkSwitchDoorStayOff("LogicalSwitchDoorTest_coAndSwitchDiagonal", 4, 2);
    }

    @Test
    @Tag("17-4-8")
    @DisplayName("Test turning on/off CO_AND Switch Door with a diagonally adjacent wire")
    public void coAndSwitchDoorWireDiagonal() {
        checkSwitchDoorStayOff("LogicalSwitchDoorTest_coAndWireDiagonal", 5, 2);
    }

    private void checkCOANDSwitchDoorStaysOffSeparateTick(String testName, int switchDoorX, int switchDoorY) {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_" + testName, "c_LogicalEntitiesTest_noMonstersCase");

        // start
        Position pos = TestUtils.getEntities(res, "player").get(0).getPosition();
        assertTrue(switchDoorClosedAt(res, switchDoorX, switchDoorY));
        assertFalse(switchDoorOpenAt(res, switchDoorX, switchDoorY));

        // push boulder
        res = dmc.tick(Direction.RIGHT);
        assertNotEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());

        // light still off
        assertTrue(switchDoorClosedAt(res, switchDoorX, switchDoorY));
        assertFalse(switchDoorOpenAt(res, switchDoorX, switchDoorY));

        // push boulder 2
        res = dmc.tick(Direction.UP);
        assertTrue(switchDoorClosedAt(res, switchDoorX, switchDoorY));
        assertFalse(switchDoorOpenAt(res, switchDoorX, switchDoorY));
    }

    @Test
    @Tag("17-4-9")
    @DisplayName("Test turning on/off COAND Switch Door with cardinally adjacent wires (2) and switch")
    public void coAndSwitchDoor2Wire1SwitchSeparateTick() {
        checkCOANDSwitchDoorStaysOffSeparateTick("LogicalSwitchDoorTest_coAnd2Wire1SwitchSeparateTick", 4, 2);
    }

    @Test
    @Tag("17-4-10")
    @DisplayName("Test turning on/off CO_AND Switch Door with 2 cardinally adjacent wire and 1 separate wire")
    public void coAndSwitchDoor2Wire1WireSeparateTick() {
        checkCOANDSwitchDoorStaysOffSeparateTick("LogicalSwitchDoorTest_coAnd2Wire1WireSeparateTick", 4, 2);
    }
}
