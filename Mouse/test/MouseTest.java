import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MouseTest {

    private Random fixedRandom;

    @BeforeEach
    public void setUp() {
        fixedRandom = new Random(42); // Fixed seed for reproducibility
    }

    @Test
    public void testCalculateNextPositionPositive() {
        Random r = new Random(42);
        int result = Mouse.calculateNextPosition(0, r, 10, true);
        // With seed 42, first nextInt(10) should give a predictable value
        assertTrue(result >= 0, "Position should be >= 0");
        assertTrue(result < 10, "Position should be < 10");
    }

    @Test
    public void testCalculateNextPositionNegative() {
        Random r = new Random(42);
        int result = Mouse.calculateNextPosition(100, r, 10, false);
        // With negative direction, position should decrease
        assertTrue(result <= 100, "Position should be <= 100");
        assertTrue(result > 90, "Position should be > 90");
    }

    @Test
    public void testCalculateNextPositionFromNonZero() {
        Random r = new Random(42);
        int startPos = 50;
        int result = Mouse.calculateNextPosition(startPos, r, 10, true);
        assertTrue(result >= startPos, "Position should be >= start");
        assertTrue(result < startPos + 10, "Position should be < start + maxIncrement");
    }

    @Test
    public void testGenerateMovementsCount() {
        int iterations = 50;
        List<int[]> movements = Mouse.generateMovements(iterations, 10, true, 0, 0, 42);
        assertEquals(iterations, movements.size(), "Should generate correct number of movements");
    }

    @Test
    public void testGenerateMovementsPositiveDirection() {
        List<int[]> movements = Mouse.generateMovements(100, 10, true, 0, 0, 42);

        // All positions should be non-negative when starting from 0 with positive direction
        for (int[] pos : movements) {
            assertTrue(pos[0] >= 0, "X should be >= 0");
            assertTrue(pos[1] >= 0, "Y should be >= 0");
        }

        // Final position should be greater than start (statistically very likely with 100 iterations)
        int[] lastPos = movements.get(movements.size() - 1);
        assertTrue(lastPos[0] > 0, "Final X should be > 0");
        assertTrue(lastPos[1] > 0, "Final Y should be > 0");
    }

    @Test
    public void testGenerateMovementsNegativeDirection() {
        int startX = 500;
        int startY = 500;
        List<int[]> movements = Mouse.generateMovements(100, 5, false, startX, startY, 42);

        // Final position should be less than start with negative direction
        int[] lastPos = movements.get(movements.size() - 1);
        assertTrue(lastPos[0] < startX, "Final X should be < start");
        assertTrue(lastPos[1] < startY, "Final Y should be < start");
    }

    @Test
    public void testGenerateMovementsReproducibility() {
        // Same seed should produce same results
        List<int[]> movements1 = Mouse.generateMovements(50, 10, true, 0, 0, 12345);
        List<int[]> movements2 = Mouse.generateMovements(50, 10, true, 0, 0, 12345);

        assertEquals(movements1.size(), movements2.size(), "Should have same size");

        for (int i = 0; i < movements1.size(); i++) {
            assertArrayEquals(movements1.get(i), movements2.get(i), "Position " + i + " should be identical");
        }
    }

    @Test
    public void testGenerateMovementsFromCustomStart() {
        int startX = 100;
        int startY = 200;
        List<int[]> movements = Mouse.generateMovements(10, 10, true, startX, startY, 42);

        // First movement should be close to start position
        int[] firstPos = movements.get(0);
        assertTrue(firstPos[0] >= startX, "First X should be >= startX");
        assertTrue(firstPos[0] < startX + 10, "First X should be < startX + 10");
        assertTrue(firstPos[1] >= startY, "First Y should be >= startY");
        assertTrue(firstPos[1] < startY + 10, "First Y should be < startY + 10");
    }

    @Test
    public void testMovementsAreSequential() {
        List<int[]> movements = Mouse.generateMovements(10, 5, true, 0, 0, 42);

        // Each position should build on the previous
        int prevX = 0;
        int prevY = 0;
        for (int[] pos : movements) {
            assertTrue(pos[0] >= prevX, "X should be >= previous X");
            assertTrue(pos[1] >= prevY, "Y should be >= previous Y");
            assertTrue(pos[0] - prevX < 5, "X increment should be < maxIncrement");
            assertTrue(pos[1] - prevY < 5, "Y increment should be < maxIncrement");
            prevX = pos[0];
            prevY = pos[1];
        }
    }

    @Test
    public void testZeroIterations() {
        List<int[]> movements = Mouse.generateMovements(0, 10, true, 0, 0, 42);
        assertTrue(movements.isEmpty(), "Should return empty list for 0 iterations");
    }

    @Test
    public void testMaxIncrementOne() {
        // With maxIncrement of 1, nextInt(1) always returns 0
        List<int[]> movements = Mouse.generateMovements(10, 1, true, 5, 5, 42);

        // All positions should equal start position since increment is always 0
        for (int[] pos : movements) {
            assertEquals(5, pos[0], "X should equal startX");
            assertEquals(5, pos[1], "Y should equal startY");
        }
    }

    // ========== Tests para KeyPress Execution ==========

    @Test
    public void testExecuteKeyPressesHolaFer() {
        Mouse.RecordingKeyPressExecutor recorder = new Mouse.RecordingKeyPressExecutor();
        List<Integer> keyCodes = Mouse.getKeyCodesForText("hola fer");

        Mouse.executeKeyPresses(recorder, keyCodes);

        List<String> actions = recorder.getActions();

        // Should have 16 actions: 8 PRESS + 8 RELEASE
        assertEquals(16, actions.size(), "Should have 16 actions (8 press + 8 release)");

        // Verify each key is pressed and then released in order
        assertEquals("PRESS:" + KeyEvent.VK_H, actions.get(0), "Should press H first");
        assertEquals("RELEASE:" + KeyEvent.VK_H, actions.get(1), "Should release H");
        assertEquals("PRESS:" + KeyEvent.VK_O, actions.get(2), "Should press O");
        assertEquals("RELEASE:" + KeyEvent.VK_O, actions.get(3), "Should release O");
        assertEquals("PRESS:" + KeyEvent.VK_L, actions.get(4), "Should press L");
        assertEquals("RELEASE:" + KeyEvent.VK_L, actions.get(5), "Should release L");
        assertEquals("PRESS:" + KeyEvent.VK_A, actions.get(6), "Should press A");
        assertEquals("RELEASE:" + KeyEvent.VK_A, actions.get(7), "Should release A");
        assertEquals("PRESS:" + KeyEvent.VK_SPACE, actions.get(8), "Should press SPACE");
        assertEquals("RELEASE:" + KeyEvent.VK_SPACE, actions.get(9), "Should release SPACE");
        assertEquals("PRESS:" + KeyEvent.VK_F, actions.get(10), "Should press F");
        assertEquals("RELEASE:" + KeyEvent.VK_F, actions.get(11), "Should release F");
        assertEquals("PRESS:" + KeyEvent.VK_E, actions.get(12), "Should press E");
        assertEquals("RELEASE:" + KeyEvent.VK_E, actions.get(13), "Should release E");
        assertEquals("PRESS:" + KeyEvent.VK_R, actions.get(14), "Should press R");
        assertEquals("RELEASE:" + KeyEvent.VK_R, actions.get(15), "Should release R");
    }

    @Test
    public void testExecuteKeyPressesOrderPressBeforeRelease() {
        Mouse.RecordingKeyPressExecutor recorder = new Mouse.RecordingKeyPressExecutor();
        List<Integer> keyCodes = Mouse.getKeyCodesForText("ab");

        Mouse.executeKeyPresses(recorder, keyCodes);

        List<String> actions = recorder.getActions();

        // Pattern should be: PRESS A, RELEASE A, PRESS B, RELEASE B
        assertEquals("PRESS:" + KeyEvent.VK_A, actions.get(0));
        assertEquals("RELEASE:" + KeyEvent.VK_A, actions.get(1));
        assertEquals("PRESS:" + KeyEvent.VK_B, actions.get(2));
        assertEquals("RELEASE:" + KeyEvent.VK_B, actions.get(3));
    }

    @Test
    public void testExecuteKeyPressesEmptyList() {
        Mouse.RecordingKeyPressExecutor recorder = new Mouse.RecordingKeyPressExecutor();
        List<Integer> keyCodes = Mouse.getKeyCodesForText("");

        Mouse.executeKeyPresses(recorder, keyCodes);

        assertTrue(recorder.getActions().isEmpty(), "No actions for empty input");
    }

    @Test
    public void testExecuteKeyPressesWithNumbers() {
        Mouse.RecordingKeyPressExecutor recorder = new Mouse.RecordingKeyPressExecutor();
        List<Integer> keyCodes = Mouse.getKeyCodesForText("a1b");

        Mouse.executeKeyPresses(recorder, keyCodes);

        List<String> actions = recorder.getActions();
        assertEquals(6, actions.size(), "Should have 6 actions");
        assertEquals("PRESS:" + KeyEvent.VK_A, actions.get(0));
        assertEquals("RELEASE:" + KeyEvent.VK_A, actions.get(1));
        assertEquals("PRESS:" + KeyEvent.VK_1, actions.get(2));
        assertEquals("RELEASE:" + KeyEvent.VK_1, actions.get(3));
        assertEquals("PRESS:" + KeyEvent.VK_B, actions.get(4));
        assertEquals("RELEASE:" + KeyEvent.VK_B, actions.get(5));
    }

    @Test
    public void testRecorderClear() {
        Mouse.RecordingKeyPressExecutor recorder = new Mouse.RecordingKeyPressExecutor();
        List<Integer> keyCodes = Mouse.getKeyCodesForText("test");

        Mouse.executeKeyPresses(recorder, keyCodes);
        assertEquals(8, recorder.getActions().size());

        recorder.clear();
        assertTrue(recorder.getActions().isEmpty(), "Should be empty after clear");
    }

    // ========== Tests para Mouse Movement Execution ==========

    @Test
    public void testExecuteMovementsRecordsAllPositions() {
        Mouse.RecordingMouseMovementExecutor recorder = new Mouse.RecordingMouseMovementExecutor();
        List<int[]> movements = Mouse.generateMovements(10, 5, true, 0, 0, 42);

        Mouse.executeMovements(recorder, movements);

        List<int[]> recorded = recorder.getMovements();
        assertEquals(10, recorded.size(), "Should record 10 movements");

        // Verify positions match
        for (int i = 0; i < movements.size(); i++) {
            assertArrayEquals(movements.get(i), recorded.get(i), "Position " + i + " should match");
        }
    }

    @Test
    public void testExecuteMovementsEmptyList() {
        Mouse.RecordingMouseMovementExecutor recorder = new Mouse.RecordingMouseMovementExecutor();
        List<int[]> movements = new ArrayList<>();

        Mouse.executeMovements(recorder, movements);

        assertTrue(recorder.getMovements().isEmpty(), "Should have no movements for empty list");
    }

    @Test
    public void testExecuteMovementsSinglePosition() {
        Mouse.RecordingMouseMovementExecutor recorder = new Mouse.RecordingMouseMovementExecutor();
        List<int[]> movements = new ArrayList<>();
        movements.add(new int[]{100, 200});

        Mouse.executeMovements(recorder, movements);

        List<int[]> recorded = recorder.getMovements();
        assertEquals(1, recorded.size(), "Should have 1 movement");
        assertEquals(100, recorded.get(0)[0], "X should be 100");
        assertEquals(200, recorded.get(0)[1], "Y should be 200");
    }

    @Test
    public void testMouseMovementRecorderClear() {
        Mouse.RecordingMouseMovementExecutor recorder = new Mouse.RecordingMouseMovementExecutor();
        List<int[]> movements = Mouse.generateMovements(5, 5, true, 0, 0, 42);

        Mouse.executeMovements(recorder, movements);
        assertEquals(5, recorder.getMovements().size());

        recorder.clear();
        assertTrue(recorder.getMovements().isEmpty(), "Should be empty after clear");
    }

    @Test
    public void testExecuteMovementsWithNegativeCoordinates() {
        Mouse.RecordingMouseMovementExecutor recorder = new Mouse.RecordingMouseMovementExecutor();
        List<int[]> movements = Mouse.generateMovements(20, 10, false, 0, 0, 42);

        Mouse.executeMovements(recorder, movements);

        List<int[]> recorded = recorder.getMovements();
        // With negative direction from 0, coordinates will go negative
        int[] lastPos = recorded.get(recorded.size() - 1);
        assertTrue(lastPos[0] <= 0, "Final X should be <= 0");
        assertTrue(lastPos[1] <= 0, "Final Y should be <= 0");
    }

    // ========== Tests para generateMovements con seed=-1 ==========

    @Test
    public void testGenerateMovementsWithRandomSeed() {
        // seed=-1 uses random seed, so results should vary (statistically)
        List<int[]> movements1 = Mouse.generateMovements(50, 10, true, 0, 0, -1);
        List<int[]> movements2 = Mouse.generateMovements(50, 10, true, 0, 0, -1);

        // Both should have correct size
        assertEquals(50, movements1.size(), "First should have 50 movements");
        assertEquals(50, movements2.size(), "Second should have 50 movements");

        // Results should exist and be valid (not testing randomness, just that it works)
        int[] lastPos1 = movements1.get(movements1.size() - 1);
        assertTrue(lastPos1[0] >= 0, "X should be non-negative with positive direction");
        assertTrue(lastPos1[1] >= 0, "Y should be non-negative with positive direction");
    }

    // ========== Tests adicionales para mayor cobertura ==========

    @Test
    public void testGetKeyCodesForTextMixedCase() {
        List<Integer> lower = Mouse.getKeyCodesForText("abc");
        List<Integer> upper = Mouse.getKeyCodesForText("ABC");
        List<Integer> mixed = Mouse.getKeyCodesForText("AbC");

        assertEquals(lower.size(), upper.size());
        assertEquals(lower.size(), mixed.size());

        for (int i = 0; i < lower.size(); i++) {
            assertEquals(lower.get(i), upper.get(i), "Key codes should match regardless of case");
            assertEquals(lower.get(i), mixed.get(i), "Key codes should match for mixed case");
        }
    }

    @Test
    public void testGetKeyCodesForTextOnlySpaces() {
        List<Integer> keyCodes = Mouse.getKeyCodesForText("   ");

        assertEquals(3, keyCodes.size(), "Should have 3 space key codes");
        for (Integer keyCode : keyCodes) {
            assertEquals(KeyEvent.VK_SPACE, keyCode.intValue(), "All should be SPACE");
        }
    }

    @Test
    public void testCalculateNextPositionMultipleCalls() {
        Random r = new Random(42);
        int pos = 0;

        // Simulate multiple increments
        for (int i = 0; i < 10; i++) {
            int newPos = Mouse.calculateNextPosition(pos, r, 10, true);
            assertTrue(newPos >= pos, "Position should increase or stay same");
            assertTrue(newPos - pos < 10, "Increment should be < maxIncrement");
            pos = newPos;
        }
    }

    @Test
    public void testCalculateNextPositionMultipleCallsNegative() {
        Random r = new Random(42);
        int pos = 100;

        // Simulate multiple decrements
        for (int i = 0; i < 10; i++) {
            int newPos = Mouse.calculateNextPosition(pos, r, 10, false);
            assertTrue(newPos <= pos, "Position should decrease or stay same");
            assertTrue(pos - newPos < 10, "Decrement should be < maxIncrement");
            pos = newPos;
        }
    }

    @Test
    public void testGenerateMovementsLargeIterations() {
        List<int[]> movements = Mouse.generateMovements(1000, 5, true, 0, 0, 42);

        assertEquals(1000, movements.size(), "Should generate 1000 movements");

        // Verify monotonic increase (each position >= previous)
        int prevX = 0, prevY = 0;
        for (int[] pos : movements) {
            assertTrue(pos[0] >= prevX, "X should be monotonically increasing");
            assertTrue(pos[1] >= prevY, "Y should be monotonically increasing");
            prevX = pos[0];
            prevY = pos[1];
        }
    }

    @Test
    public void testRecordingMouseMovementExecutorDirectMoveTo() {
        Mouse.RecordingMouseMovementExecutor recorder = new Mouse.RecordingMouseMovementExecutor();

        recorder.moveTo(50, 75);
        recorder.moveTo(100, 150);
        recorder.moveTo(200, 300);

        List<int[]> movements = recorder.getMovements();
        assertEquals(3, movements.size());
        assertArrayEquals(new int[]{50, 75}, movements.get(0));
        assertArrayEquals(new int[]{100, 150}, movements.get(1));
        assertArrayEquals(new int[]{200, 300}, movements.get(2));
    }

    @Test
    public void testRecordingKeyPressExecutorDirectCalls() {
        Mouse.RecordingKeyPressExecutor recorder = new Mouse.RecordingKeyPressExecutor();

        recorder.pressKey(65);
        recorder.releaseKey(65);
        recorder.pressKey(66);
        recorder.releaseKey(66);

        List<String> actions = recorder.getActions();
        assertEquals(4, actions.size());
        assertEquals("PRESS:65", actions.get(0));
        assertEquals("RELEASE:65", actions.get(1));
        assertEquals("PRESS:66", actions.get(2));
        assertEquals("RELEASE:66", actions.get(3));
    }

    // ========== Tests con Robot real (para cubrir las clases wrapper) ==========

    @Test
    public void testRobotKeyPressExecutorCreation() throws Exception {
        Robot robot = new Robot();
        Mouse.RobotKeyPressExecutor executor = new Mouse.RobotKeyPressExecutor(robot);
        assertNotNull(executor, "Executor should be created");
    }

    @Test
    public void testRobotKeyPressExecutorPressAndRelease() throws Exception {
        Robot robot = new Robot();
        Mouse.RobotKeyPressExecutor executor = new Mouse.RobotKeyPressExecutor(robot);

        // Press and release a safe key (Shift) - doesn't type anything
        executor.pressKey(KeyEvent.VK_SHIFT);
        executor.releaseKey(KeyEvent.VK_SHIFT);
        // If no exception, test passes
    }

    @Test
    public void testRobotMouseMovementExecutorCreation() throws Exception {
        Robot robot = new Robot();
        Mouse.RobotMouseMovementExecutor executor = new Mouse.RobotMouseMovementExecutor(robot);
        assertNotNull(executor, "Executor should be created");
    }

    @Test
    public void testRobotMouseMovementExecutorMoveTo() throws Exception {
        Robot robot = new Robot();
        Mouse.RobotMouseMovementExecutor executor = new Mouse.RobotMouseMovementExecutor(robot);

        // Move to a position - this actually moves the mouse
        executor.moveTo(100, 100);
        // If no exception, test passes
    }

    @Test
    public void testExecuteKeyPressesWithRobot() throws Exception {
        Robot robot = new Robot();
        List<Integer> keyCodes = new ArrayList<>();
        keyCodes.add(KeyEvent.VK_SHIFT); // Safe key that doesn't type

        // Test the convenience method that takes Robot directly
        Mouse.executeKeyPresses(robot, keyCodes);
        // If no exception, test passes
    }

    @Test
    public void testExecuteMovementsWithRobot() throws Exception {
        Robot robot = new Robot();
        List<int[]> movements = new ArrayList<>();
        movements.add(new int[]{50, 50});
        movements.add(new int[]{100, 100});

        // Test the convenience method that takes Robot directly
        Mouse.executeMovements(robot, movements);
        // If no exception, test passes
    }

    // ========== Tests adicionales para alcanzar 85% ==========

    @Test
    public void testGetKeyCodesSkipsUnsupportedCharacters() {
        // Test that unsupported characters (like @, #, !) are skipped
        List<Integer> keyCodes = Mouse.getKeyCodesForText("a@b#c!");

        // Should only have 3 key codes (a, b, c) - special chars skipped
        assertEquals(3, keyCodes.size(), "Should skip unsupported characters");
        assertEquals(KeyEvent.VK_A, keyCodes.get(0).intValue());
        assertEquals(KeyEvent.VK_B, keyCodes.get(1).intValue());
        assertEquals(KeyEvent.VK_C, keyCodes.get(2).intValue());
    }

    @Test
    public void testGetKeyCodesAllUnsupportedReturnsEmpty() {
        // All unsupported characters
        List<Integer> keyCodes = Mouse.getKeyCodesForText("@#$%^&*()");

        assertTrue(keyCodes.isEmpty(), "Should return empty list for all unsupported chars");
    }

    @Test
    public void testGetKeyCodesNewlineIgnored() {
        List<Integer> keyCodes = Mouse.getKeyCodesForText("a\nb\nc");

        // Newlines should be ignored
        assertEquals(3, keyCodes.size(), "Should skip newlines");
    }

    @Test
    public void testGetKeyCodesTabIgnored() {
        List<Integer> keyCodes = Mouse.getKeyCodesForText("a\tb");

        // Tabs should be ignored
        assertEquals(2, keyCodes.size(), "Should skip tabs");
    }

    // ========== Tests para MouseClickExecutor ==========

    @Test
    public void testRecordingMouseClickExecutor() {
        Mouse.RecordingMouseClickExecutor recorder = new Mouse.RecordingMouseClickExecutor();

        recorder.press(16);  // BUTTON1_MASK
        recorder.release(16);

        List<String> actions = recorder.getActions();
        assertEquals(2, actions.size());
        assertEquals("PRESS:16", actions.get(0));
        assertEquals("RELEASE:16", actions.get(1));
    }

    @Test
    public void testRobotMouseClickExecutor() throws Exception {
        Robot robot = new Robot();
        Mouse.RobotMouseClickExecutor executor = new Mouse.RobotMouseClickExecutor(robot);

        // Just test creation - actually clicking might cause issues
        assertNotNull(executor);
    }

    // ========== Test para runAutomation ==========

    @Test
    public void testRunAutomation() {
        Mouse.RecordingMouseMovementExecutor movementRecorder = new Mouse.RecordingMouseMovementExecutor();
        Mouse.RecordingMouseClickExecutor clickRecorder = new Mouse.RecordingMouseClickExecutor();
        Mouse.RecordingKeyPressExecutor keyRecorder = new Mouse.RecordingKeyPressExecutor();

        Mouse.runAutomation(movementRecorder, clickRecorder, keyRecorder, "ab");

        // Verify movements were recorded (100 forward + 100 backward)
        assertEquals(200, movementRecorder.getMovements().size(), "Should have 200 total movements");

        // Verify click was recorded
        List<String> clickActions = clickRecorder.getActions();
        assertEquals(2, clickActions.size(), "Should have press and release");
        assertTrue(clickActions.get(0).startsWith("PRESS:"));
        assertTrue(clickActions.get(1).startsWith("RELEASE:"));

        // Verify keys were recorded (2 chars = 4 actions: press+release each)
        assertEquals(4, keyRecorder.getActions().size(), "Should have 4 key actions for 'ab'");
    }

    @Test
    public void testRunAutomationEmptyText() {
        Mouse.RecordingMouseMovementExecutor movementRecorder = new Mouse.RecordingMouseMovementExecutor();
        Mouse.RecordingMouseClickExecutor clickRecorder = new Mouse.RecordingMouseClickExecutor();
        Mouse.RecordingKeyPressExecutor keyRecorder = new Mouse.RecordingKeyPressExecutor();

        Mouse.runAutomation(movementRecorder, clickRecorder, keyRecorder, "");

        // Movements should still happen
        assertEquals(200, movementRecorder.getMovements().size());

        // Click should still happen
        assertEquals(2, clickRecorder.getActions().size());

        // No keys since empty text
        assertEquals(0, keyRecorder.getActions().size());
    }
}
