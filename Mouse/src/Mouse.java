import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Mouse {

    private static final Map<Character, Integer> KEY_MAP = new HashMap<>();

    static {
        // Letras
        KEY_MAP.put('a', KeyEvent.VK_A);
        KEY_MAP.put('b', KeyEvent.VK_B);
        KEY_MAP.put('c', KeyEvent.VK_C);
        KEY_MAP.put('d', KeyEvent.VK_D);
        KEY_MAP.put('e', KeyEvent.VK_E);
        KEY_MAP.put('f', KeyEvent.VK_F);
        KEY_MAP.put('g', KeyEvent.VK_G);
        KEY_MAP.put('h', KeyEvent.VK_H);
        KEY_MAP.put('i', KeyEvent.VK_I);
        KEY_MAP.put('j', KeyEvent.VK_J);
        KEY_MAP.put('k', KeyEvent.VK_K);
        KEY_MAP.put('l', KeyEvent.VK_L);
        KEY_MAP.put('m', KeyEvent.VK_M);
        KEY_MAP.put('n', KeyEvent.VK_N);
        KEY_MAP.put('o', KeyEvent.VK_O);
        KEY_MAP.put('p', KeyEvent.VK_P);
        KEY_MAP.put('q', KeyEvent.VK_Q);
        KEY_MAP.put('r', KeyEvent.VK_R);
        KEY_MAP.put('s', KeyEvent.VK_S);
        KEY_MAP.put('t', KeyEvent.VK_T);
        KEY_MAP.put('u', KeyEvent.VK_U);
        KEY_MAP.put('v', KeyEvent.VK_V);
        KEY_MAP.put('w', KeyEvent.VK_W);
        KEY_MAP.put('x', KeyEvent.VK_X);
        KEY_MAP.put('y', KeyEvent.VK_Y);
        KEY_MAP.put('z', KeyEvent.VK_Z);
        // Espacio
        KEY_MAP.put(' ', KeyEvent.VK_SPACE);
        // Números
        KEY_MAP.put('0', KeyEvent.VK_0);
        KEY_MAP.put('1', KeyEvent.VK_1);
        KEY_MAP.put('2', KeyEvent.VK_2);
        KEY_MAP.put('3', KeyEvent.VK_3);
        KEY_MAP.put('4', KeyEvent.VK_4);
        KEY_MAP.put('5', KeyEvent.VK_5);
        KEY_MAP.put('6', KeyEvent.VK_6);
        KEY_MAP.put('7', KeyEvent.VK_7);
        KEY_MAP.put('8', KeyEvent.VK_8);
        KEY_MAP.put('9', KeyEvent.VK_9);
    }

    /**
     * Converts a string to a list of KeyEvent codes.
     * @param text The text to convert
     * @return List of key codes, or empty list if character not supported
     */
    public static List<Integer> getKeyCodesForText(String text) {
        List<Integer> keyCodes = new ArrayList<>();
        for (char c : text.toLowerCase().toCharArray()) {
            Integer keyCode = KEY_MAP.get(c);
            if (keyCode != null) {
                keyCodes.add(keyCode);
            }
        }
        return keyCodes;
    }

    /**
     * Interface for key press actions (allows testing without real Robot).
     */
    public interface KeyPressExecutor {
        void pressKey(int keyCode);
        void releaseKey(int keyCode);
    }

    /**
     * Real implementation using Robot.
     */
    public static class RobotKeyPressExecutor implements KeyPressExecutor {
        private final Robot robot;

        public RobotKeyPressExecutor(Robot robot) {
            this.robot = robot;
        }

        @Override
        public void pressKey(int keyCode) {
            robot.keyPress(keyCode);
        }

        @Override
        public void releaseKey(int keyCode) {
            robot.keyRelease(keyCode);
        }
    }

    /**
     * Recording implementation for testing - captures all key actions.
     */
    public static class RecordingKeyPressExecutor implements KeyPressExecutor {
        private final List<String> actions = new ArrayList<>();

        @Override
        public void pressKey(int keyCode) {
            actions.add("PRESS:" + keyCode);
        }

        @Override
        public void releaseKey(int keyCode) {
            actions.add("RELEASE:" + keyCode);
        }

        public List<String> getActions() {
            return new ArrayList<>(actions);
        }

        public void clear() {
            actions.clear();
        }
    }

    /**
     * Executes key presses using a KeyPressExecutor.
     * @param executor The executor to use
     * @param keyCodes List of key codes to press
     */
    public static void executeKeyPresses(KeyPressExecutor executor, List<Integer> keyCodes) {
        for (int keyCode : keyCodes) {
            executor.pressKey(keyCode);
            executor.releaseKey(keyCode);
        }
    }

    /**
     * Executes key presses using a Robot (convenience method).
     * @param robot The Robot instance to use
     * @param keyCodes List of key codes to press
     */
    public static void executeKeyPresses(Robot robot, List<Integer> keyCodes) {
        executeKeyPresses(new RobotKeyPressExecutor(robot), keyCodes);
    }

    /**
     * Calculates the next position by adding a random increment to the current position.
     * @param current The current position
     * @param r Random number generator
     * @param maxIncrement Maximum increment value (exclusive)
     * @param positive If true, adds increment; if false, subtracts increment
     * @return The new position
     */
    public static int calculateNextPosition(int current, Random r, int maxIncrement, boolean positive) {
        int increment = r.nextInt(maxIncrement);
        return positive ? current + increment : current - increment;
    }

    /**
     * Generates a list of movement coordinates.
     * @param iterations Number of movements to generate
     * @param maxIncrement Maximum increment per movement
     * @param positive Direction of movement (true = positive, false = negative)
     * @param startX Starting X position
     * @param startY Starting Y position
     * @param seed Random seed for reproducibility (use -1 for random seed)
     * @return List of int arrays [x, y] representing positions
     */
    public static List<int[]> generateMovements(int iterations, int maxIncrement, boolean positive,
                                                 int startX, int startY, long seed) {
        List<int[]> movements = new ArrayList<>();
        Random r = (seed == -1) ? new Random() : new Random(seed);

        int x = startX;
        int y = startY;

        for (int i = 0; i < iterations; i++) {
            x = calculateNextPosition(x, r, maxIncrement, positive);
            y = calculateNextPosition(y, r, maxIncrement, positive);
            movements.add(new int[]{x, y});
        }

        return movements;
    }

    /**
     * Interface for mouse movement actions (allows testing without real Robot).
     */
    public interface MouseMovementExecutor {
        void moveTo(int x, int y);
    }

    /**
     * Real implementation using Robot.
     */
    public static class RobotMouseMovementExecutor implements MouseMovementExecutor {
        private final Robot robot;

        public RobotMouseMovementExecutor(Robot robot) {
            this.robot = robot;
        }

        @Override
        public void moveTo(int x, int y) {
            robot.mouseMove(x, y);
        }
    }

    /**
     * Recording implementation for testing - captures all mouse movements.
     */
    public static class RecordingMouseMovementExecutor implements MouseMovementExecutor {
        private final List<int[]> movements = new ArrayList<>();

        @Override
        public void moveTo(int x, int y) {
            movements.add(new int[]{x, y});
        }

        public List<int[]> getMovements() {
            return new ArrayList<>(movements);
        }

        public void clear() {
            movements.clear();
        }
    }

    /**
     * Executes mouse movements using a MouseMovementExecutor.
     * @param executor The executor to use
     * @param movements List of [x, y] positions
     */
    public static void executeMovements(MouseMovementExecutor executor, List<int[]> movements) {
        for (int[] pos : movements) {
            executor.moveTo(pos[0], pos[1]);
        }
    }

    /**
     * Executes mouse movements using a Robot (convenience method).
     * @param robot The Robot instance to use
     * @param movements List of [x, y] positions
     */
    public static void executeMovements(Robot robot, List<int[]> movements) {
        executeMovements(new RobotMouseMovementExecutor(robot), movements);
    }

    /**
     * Interface for mouse click actions (allows testing without real Robot).
     */
    public interface MouseClickExecutor {
        void press(int button);
        void release(int button);
    }

    /**
     * Real implementation using Robot.
     */
    public static class RobotMouseClickExecutor implements MouseClickExecutor {
        private final Robot robot;

        public RobotMouseClickExecutor(Robot robot) {
            this.robot = robot;
        }

        @Override
        public void press(int button) {
            robot.mousePress(button);
        }

        @Override
        public void release(int button) {
            robot.mouseRelease(button);
        }
    }

    /**
     * Recording implementation for testing.
     */
    public static class RecordingMouseClickExecutor implements MouseClickExecutor {
        private final List<String> actions = new ArrayList<>();

        @Override
        public void press(int button) {
            actions.add("PRESS:" + button);
        }

        @Override
        public void release(int button) {
            actions.add("RELEASE:" + button);
        }

        public List<String> getActions() {
            return new ArrayList<>(actions);
        }
    }

    /**
     * Runs the mouse automation sequence.
     * @param movementExecutor Executor for mouse movements
     * @param clickExecutor Executor for mouse clicks
     * @param keyExecutor Executor for key presses
     * @param textToType Text to type
     */
    public static void runAutomation(MouseMovementExecutor movementExecutor,
                                     MouseClickExecutor clickExecutor,
                                     KeyPressExecutor keyExecutor,
                                     String textToType) {
        // Generate and execute forward movements
        List<int[]> forwardMovements = generateMovements(100, 10, true, 0, 0, -1);
        executeMovements(movementExecutor, forwardMovements);

        // Get final position from forward movements
        int[] lastPos = forwardMovements.get(forwardMovements.size() - 1);

        // Click
        clickExecutor.press(InputEvent.BUTTON1_MASK);
        clickExecutor.release(InputEvent.BUTTON1_MASK);

        // Type text
        List<Integer> keyCodes = getKeyCodesForText(textToType);
        executeKeyPresses(keyExecutor, keyCodes);

        // Generate and execute backward movements
        List<int[]> backwardMovements = generateMovements(100, 5, false, lastPos[0], lastPos[1], -1);
        executeMovements(movementExecutor, backwardMovements);
    }

    public static void main(String... args) throws Exception {
        Robot robot = new Robot();
        robot.setAutoDelay(5);

        runAutomation(
            new RobotMouseMovementExecutor(robot),
            new RobotMouseClickExecutor(robot),
            new RobotKeyPressExecutor(robot),
            "hola fer"
        );
    }
}