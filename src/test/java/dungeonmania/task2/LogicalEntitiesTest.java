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

public class LogicalEntitiesTest {
    private boolean lightBulbOnAt(DungeonResponse res, int x, int y) {
        Position pos = new Position(x, y);
        return TestUtils.getEntitiesStream(res, "light_bulb_on").anyMatch(it -> it.getPosition().equals(pos));
    }

    private boolean lightBulbOffAt(DungeonResponse res, int x, int y) {
        Position pos = new Position(x, y);
        return TestUtils.getEntitiesStream(res, "light_bulb_off").anyMatch(it -> it.getPosition().equals(pos));
    }

    @Test
    @Tag("16-1")
    @DisplayName("Test walk over wire")
    public void walkOverWire() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_LogicalEntitiesTest_walkOverWire", "c_LogicalEntitiesTest_noMonstersCase");

        // start
        Position pos = TestUtils.getEntities(res, "player").get(0).getPosition();

        // walk over wire
        res = dmc.tick(Direction.RIGHT);
        assertNotEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());

    }

    private void checkLightTurnsOnOff(String testName, int lightBulbX, int lightBulbY) {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_" + testName, "c_LogicalEntitiesTest_noMonstersCase");

        // start
        Position pos = TestUtils.getEntities(res, "player").get(0).getPosition();
        assertTrue(lightBulbOffAt(res, lightBulbX, lightBulbY));
        assertFalse(lightBulbOnAt(res, lightBulbX, lightBulbY));

        // push boulder
        res = dmc.tick(Direction.RIGHT);
        assertNotEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());

        // light bulb on
        assertTrue(lightBulbOnAt(res, lightBulbX, lightBulbY));
        assertFalse(lightBulbOffAt(res, lightBulbX, lightBulbY));

        // push boulder away
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);

        // light bulb off
        assertTrue(lightBulbOffAt(res, lightBulbX, lightBulbY));
        assertFalse(lightBulbOnAt(res, lightBulbX, lightBulbY));
    }

    // OR Light
    @Test
    @Tag("16-2-1")
    @DisplayName("Test turning on/off OR Light with a cardinally adjacent switch")
    public void orLightSwitch() {
        checkLightTurnsOnOff("LogicalEntitiesTest_orLightSwitch", 4, 1);
    }

    @Test
    @Tag("16-2-2")
    @DisplayName("Test turning on/off OR Light with a cardinally adjacent wire")
    public void orLightWire() {
        checkLightTurnsOnOff("LogicalEntitiesTest_orLightWire", 5, 1);
    }

    @Test
    @Tag("16-2-3")
    @DisplayName("Test turning on/off OR Light with a cardinally adjacent wire far from switch")
    public void orLightWireLong() {
        checkLightTurnsOnOff("LogicalEntitiesTest_orLightWireLong", 10, 1);
    }

    @Test
    @Tag("16-2-4")
    @DisplayName("Test turning on/off OR Light with 2 cardinally adjacent wire")
    public void orLightWireMultiple2() {
        checkLightTurnsOnOff("LogicalEntitiesTest_orLightWireMultiple2", 5, 1);
    }

    @Test
    @Tag("16-2-5")
    @DisplayName("Test turning on/off OR Light with 3 cardinally adjacent wire")
    public void orLightWireMultiple3() {
        checkLightTurnsOnOff("LogicalEntitiesTest_orLightWireMultiple3", 5, 1);
    }

    @Test
    @Tag("16-2-6")
    @DisplayName("Test turning on/off OR Light with 4 cardinally adjacent wire")
    public void orLightWireMultiple4() {
        checkLightTurnsOnOff("LogicalEntitiesTest_orLightWireMultiple4", 5, 1);
    }

    private void checkLightStayOff(String testName, int lightBulbX, int lightBulbY) {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_" + testName, "c_LogicalEntitiesTest_noMonstersCase");

        // start
        Position pos = TestUtils.getEntities(res, "player").get(0).getPosition();
        assertTrue(lightBulbOffAt(res, lightBulbX, lightBulbY));
        assertFalse(lightBulbOnAt(res, lightBulbX, lightBulbY));

        // push boulder
        res = dmc.tick(Direction.RIGHT);
        assertNotEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());

        // light bulb doesn't turn on
        assertTrue(lightBulbOffAt(res, lightBulbX, lightBulbY));
        assertFalse(lightBulbOnAt(res, lightBulbX, lightBulbY));
    }

    @Test
    @Tag("16-2-7")
    @DisplayName("Test turning on/off OR Light with a diagonally adjacent switch")
    public void orLightSwitchDiagonal() {
        checkLightStayOff("LogicalEntitiesTest_orLightSwitchDiagonal", 4, 2);
    }

    @Test
    @Tag("16-2-8")
    @DisplayName("Test turning on/off OR Light with a diagonally adjacent wire")
    public void orLightWireDiagonal() {
        checkLightStayOff("LogicalEntitiesTest_orLightWireDiagonal", 5, 2);
    }

    // XOR Light
    @Test
    @Tag("16-3-1")
    @DisplayName("Test turning on/off XOR Light with a cardinally adjacent switch")
    public void xorLightSwitch() {
        checkLightTurnsOnOff("LogicalEntitiesTest_xorLightSwitch", 4, 1);
    }

    @Test
    @Tag("16-3-2")
    @DisplayName("Test turning on/off XOR Light with a cardinally adjacent wire")
    public void xorLightWire() {
        checkLightTurnsOnOff("LogicalEntitiesTest_xorLightWire", 5, 1);
    }

    @Test
    @Tag("16-3-3")
    @DisplayName("Test turning on/off XOR Light with a cardinally adjacent wire far from switch")
    public void xorLightWireLong() {
        checkLightTurnsOnOff("LogicalEntitiesTest_xorLightWireLong", 10, 1);
    }

    @Test
    @Tag("16-3-4")
    @DisplayName("Test turning on/off XOR Light with 2 cardinally adjacent wire")
    public void xorLightWireMultiple2() {
        checkLightStayOff("LogicalEntitiesTest_xorLightWireMultiple2", 5, 1);
    }

    @Test
    @Tag("16-3-5")
    @DisplayName("Test turning on/off XOR Light with 3 cardinally adjacent wire")
    public void xorLightWireMultiple3() {
        checkLightStayOff("LogicalEntitiesTest_xorLightWireMultiple3", 5, 1);
    }

    @Test
    @Tag("16-3-6")
    @DisplayName("Test turning on/off XOR Light with 4 cardinally adjacent wire")
    public void xorLightWireMultiple4() {
        checkLightStayOff("LogicalEntitiesTest_xorLightWireMultiple4", 5, 1);
    }

    @Test
    @Tag("16-3-7")
    @DisplayName("Test turning on/off XOR Light with a diagonally adjacent switch")
    public void xorLightSwitchDiagonal() {
        checkLightStayOff("LogicalEntitiesTest_xorLightSwitchDiagonal", 4, 2);
    }

    @Test
    @Tag("16-3-8")
    @DisplayName("Test turning on/off XOR Light with a diagonally adjacent wire")
    public void xorLightWireDiagonal() {
        checkLightStayOff("LogicalEntitiesTest_xorLightWireDiagonal", 5, 2);
    }

    // AND Light
    @Test
    @Tag("16-4-1")
    @DisplayName("Test turning on/off AND Light with a cardinally adjacent switch")
    public void andLightSwitch() {
        checkLightStayOff("LogicalEntitiesTest_andLightSwitch", 4, 1);
    }

    @Test
    @Tag("16-4-2")
    @DisplayName("Test turning on/off AND Light with a cardinally adjacent wire")
    public void andLightWire() {
        checkLightStayOff("LogicalEntitiesTest_andLightWire", 5, 1);
    }

    @Test
    @Tag("16-4-3")
    @DisplayName("Test turning on/off AND Light with a cardinally adjacent wire far from switch")
    public void andLightWireLong() {
        checkLightStayOff("LogicalEntitiesTest_andLightWireLong", 10, 1);
    }

    @Test
    @Tag("16-4-4")
    @DisplayName("Test turning on/off AND Light with 2 cardinally adjacent wire")
    public void andLightWireMultiple2() {
        checkLightTurnsOnOff("LogicalEntitiesTest_andLightWireMultiple2", 5, 1);
    }

    @Test
    @Tag("16-4-5")
    @DisplayName("Test turning on/off AND Light with 3 cardinally adjacent wire")
    public void andLightWireMultiple3() {
        checkLightTurnsOnOff("LogicalEntitiesTest_andLightWireMultiple3", 5, 1);
    }

    @Test
    @Tag("16-4-6")
    @DisplayName("Test turning on/off AND Light with 4 cardinally adjacent wire")
    public void andLightWireMultiple4() {
        checkLightTurnsOnOff("LogicalEntitiesTest_andLightWireMultiple4", 5, 1);
    }

    @Test
    @Tag("16-4-7")
    @DisplayName("Test turning on/off AND Light with a diagonally adjacent switch")
    public void andLightSwitchDiagonal() {
        checkLightStayOff("LogicalEntitiesTest_andLightSwitchDiagonal", 4, 2);
    }

    @Test
    @Tag("16-4-8")
    @DisplayName("Test turning on/off AND Light with a diagonally adjacent wire")
    public void andLightWireDiagonal() {
        checkLightStayOff("LogicalEntitiesTest_andLightWireDiagonal", 5, 2);
    }

    private void checkANDLightTurnsOnOffSeparateTick(String testName, int lightBulbX, int lightBulbY) {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_" + testName, "c_LogicalEntitiesTest_noMonstersCase");

        // start
        Position pos = TestUtils.getEntities(res, "player").get(0).getPosition();
        assertTrue(lightBulbOffAt(res, lightBulbX, lightBulbY));
        assertFalse(lightBulbOnAt(res, lightBulbX, lightBulbY));

        // push boulder
        res = dmc.tick(Direction.RIGHT);
        assertNotEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());

        // light still off
        assertTrue(lightBulbOffAt(res, lightBulbX, lightBulbY));
        assertFalse(lightBulbOnAt(res, lightBulbX, lightBulbY));

        // push boulder 2
        res = dmc.tick(Direction.UP);
        assertTrue(lightBulbOnAt(res, lightBulbX, lightBulbY));
        assertFalse(lightBulbOffAt(res, lightBulbX, lightBulbY));

        // push boulder 1 off switch
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.DOWN);
        assertTrue(lightBulbOffAt(res, lightBulbX, lightBulbY));
        assertFalse(lightBulbOnAt(res, lightBulbX, lightBulbY));
    }

    @Test
    @Tag("16-4-9")
    @DisplayName("Test turning on/off AND Light with 2 cardinally adjacent wire and 1 cardinally adjacent switch")
    public void andLight2Wire1SwitchSeparateTick() {
        checkANDLightTurnsOnOffSeparateTick("LogicalEntitiesTest_andLight2Wire1SwitchSeparateTick", 4, 2);
    }

    @Test
    @Tag("16-4-9")
    @DisplayName("Test turning on/off AND Light with 2 cardinally adjacent wire and 1 separate wire")
    public void andLight2Wire1WireSeparateTick() {
        checkANDLightTurnsOnOffSeparateTick("LogicalEntitiesTest_andLight2Wire1WireSeparateTick", 4, 2);
    }

    // CO_AND Light
    @Test
    @Tag("16-4-1")
    @DisplayName("Test turning on/off CO_AND Light with a cardinally adjacent switch")
    public void coAndLightSwitch() {
        checkLightStayOff("LogicalEntitiesTest_coAndLightSwitch", 4, 1);
    }

    @Test
    @Tag("16-4-2")
    @DisplayName("Test turning on/off CO_AND Light with a cardinally adjacent wire")
    public void coAndLightWire() {
        checkLightStayOff("LogicalEntitiesTest_coAndLightWire", 5, 1);
    }

    @Test
    @Tag("16-4-3")
    @DisplayName("Test turning on/off CO_AND Light with a cardinally adjacent wire far from switch")
    public void coAndLightWireLong() {
        checkLightStayOff("LogicalEntitiesTest_coAndLightWireLong", 10, 1);
    }

    @Test
    @Tag("16-4-4")
    @DisplayName("Test turning on/off CO_AND Light with 2 cardinally adjacent wire")
    public void coAndLightWireMultiple2() {
        checkLightTurnsOnOff("LogicalEntitiesTest_coAndLightWireMultiple2", 5, 1);
    }

    @Test
    @Tag("16-4-5")
    @DisplayName("Test turning on/off CO_AND Light with 3 cardinally adjacent wire")
    public void coAndLightWireMultiple3() {
        checkLightTurnsOnOff("LogicalEntitiesTest_coAndLightWireMultiple3", 5, 1);
    }

    @Test
    @Tag("16-4-6")
    @DisplayName("Test turning on/off CO_AND Light with 4 cardinally adjacent wire")
    public void coAndLightWireMultiple4() {
        checkLightTurnsOnOff("LogicalEntitiesTest_coAndLightWireMultiple4", 5, 1);
    }

    @Test
    @Tag("16-4-7")
    @DisplayName("Test turning on/off CO_AND Light with a diagonally adjacent switch")
    public void coAndLightSwitchDiagonal() {
        checkLightStayOff("LogicalEntitiesTest_coAndLightSwitchDiagonal", 4, 2);
    }

    @Test
    @Tag("16-4-8")
    @DisplayName("Test turning on/off CO_AND Light with a diagonally adjacent wire")
    public void coAndLightWireDiagonal() {
        checkLightStayOff("LogicalEntitiesTest_coAndLightWireDiagonal", 5, 2);
    }

    private void checkCOANDLightStaysOffSeparateTick(String testName, int lightBulbX, int lightBulbY) {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_" + testName, "c_LogicalEntitiesTest_noMonstersCase");

        // start
        Position pos = TestUtils.getEntities(res, "player").get(0).getPosition();
        assertTrue(lightBulbOffAt(res, lightBulbX, lightBulbY));
        assertFalse(lightBulbOnAt(res, lightBulbX, lightBulbY));

        // push boulder
        res = dmc.tick(Direction.RIGHT);
        assertNotEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());

        // light still off
        assertTrue(lightBulbOffAt(res, lightBulbX, lightBulbY));
        assertFalse(lightBulbOnAt(res, lightBulbX, lightBulbY));

        // push boulder 2
        res = dmc.tick(Direction.UP);
        assertTrue(lightBulbOffAt(res, lightBulbX, lightBulbY));
        assertFalse(lightBulbOnAt(res, lightBulbX, lightBulbY));
    }

    @Test
    @Tag("16-4-9")
    @DisplayName("Test turning on/off CO_AND Light with 2 cardinally adjacent wire and 1 cardinally adjacent switch")
    public void coAndLight2Wire1SwitchSeparateTick() {
        checkCOANDLightStaysOffSeparateTick("LogicalEntitiesTest_coAndLight2Wire1SwitchSeparateTick", 4, 2);
    }

    @Test
    @Tag("16-4-10")
    @DisplayName("Test turning on/off CO_AND Light with 2 cardinally adjacent wire and 1 separate wire")
    public void coAndLight2Wire1WireSeparateTick() {
        checkCOANDLightStaysOffSeparateTick("LogicalEntitiesTest_coAndLight2Wire1WireSeparateTick", 4, 2);
    }

}
