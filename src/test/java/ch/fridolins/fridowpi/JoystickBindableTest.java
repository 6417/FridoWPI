package ch.fridolins.fridowpi;

import ch.fridolins.fridowpi.initializer.Initializer;
import ch.fridolins.fridowpi.joystick.*;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.Button;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static ch.fridolins.fridowpi.Utils.withRobotEnabled;
import static org.junit.jupiter.api.Assertions.*;

public class JoystickBindableTest {
    public class TestJoystick implements IJoystick {
        Map<Integer, Boolean> buttonsPressed = new HashMap<>();
        Map<Integer, Button> buttons = new HashMap<>();

        public void pressButton(int id) {
            if (buttons.get(id) == null) {
                buttonsPressed.put(id, false);
                buttons.put(id, new Button(() -> buttonsPressed.get(id)));
            }

            buttonsPressed.put(id, true);
        }

        public void releaseButton(int id) {
            if (buttons.get(id) == null) {
                buttonsPressed.put(id, false);
                buttons.put(id, new Button(() -> buttonsPressed.get(id)));
            }

            buttonsPressed.put(id, false);
        }

        public TestJoystick(IJoystickId id) {
            for (int i = 0; i < 10; i++)
                buttonsPressed.put(i, false);
        }

        @Override
        public Button getButton(IJoystickButtonId id) {
            if (buttons.get(id.getButtonId()) == null) {
                buttonsPressed.put(id.getButtonId(), false);
                buttons.put(id.getButtonId(), new Button(() -> buttonsPressed.get(id.getButtonId())));
            }
            return buttons.get(id.getButtonId());
        }

        @Override
        public double getX() {
            return 0;
        }

        @Override
        public double getY() {
            return 0;
        }

        @Override
        public double getZ() {
            return 0;
        }

        @Override
        public double getTwist() {
            return 0;
        }

        @Override
        public double getThrottle() {
            return 0;
        }

        @Override
        public boolean getTrigger() {
            return false;
        }

        @Override
        public boolean getTriggerPressed() {
            return false;
        }

        @Override
        public boolean getTriggerReleased() {
            return false;
        }

        @Override
        public boolean getTop() {
            return false;
        }

        @Override
        public boolean getTopPressed() {
            return false;
        }

        @Override
        public boolean getTopReleased() {
            return false;
        }

        @Override
        public double getMagnitude() {
            return 0;
        }

        @Override
        public double getDirectionRadians() {
            return 0;
        }

        @Override
        public double getDirectionDegrees() {
            return 0;
        }
    }

    @BeforeEach
    void setup() {
        JoystickHandler.getInstance().setJoystickFactory(TestJoystick::new);
    }

    @AfterEach
    void teardown() {
        Initializer.reset();
        JoystickHandler.reset();
    }


    List<IJoystickId> mkJoystickIds(Integer... ids) {
        return Arrays.stream(ids).map((id) -> (IJoystickId) () -> id).collect(Collectors.toList());
    }

    @Test
    void willPressAndReleaseButtonWork() {
        JoystickHandler.getInstance().setupJoysticks(mkJoystickIds(1));

        ((TestJoystick) JoystickHandler.getInstance().getJoystick(() -> 1)).pressButton(1);
        assertTrue(JoystickHandler.getInstance().getJoystick(() -> 1).getButton(() -> 1).get());

        ((TestJoystick) JoystickHandler.getInstance().getJoystick(() -> 1)).releaseButton(1);
        assertFalse(JoystickHandler.getInstance().getJoystick(() -> 1).getButton(() -> 1).get());
    }

    @Test
    void bindTrivialWillBeTriggered() {
        JoystickHandler.getInstance().setupJoysticks(mkJoystickIds(1));

        final AtomicReference<Boolean> executed = new AtomicReference<>(false);

        JoystickHandler.getInstance().bind(new Binding(() -> 1, () -> 1, Button::whenPressed, new InstantCommand(() -> executed.set(true))));

        ((TestJoystick) JoystickHandler.getInstance().getJoystick(() -> 1)).releaseButton(1);
        JoystickHandler.getInstance().init();


        withRobotEnabled(() -> {
            assertFalse(executed.get());
            ((TestJoystick) JoystickHandler.getInstance().getJoystick(() -> 1)).pressButton(1);
            CommandScheduler.getInstance().run();
            assertTrue(executed.get());
        });
    }

    enum Joysticks implements IJoystickId {
        Drive(0), Control(1);

        private final int port;

        private Joysticks(int port) {
            this.port = port;
        }

        @Override
        public int getPort() {
            return port;
        }
    }

    @Test
    void bindMultipleButtonsWillBeTriggered() {
        JoystickHandler.getInstance().setupJoysticks(mkJoystickIds(1));

        final AtomicReference<Boolean> executed1 = new AtomicReference<>(false);
        final AtomicReference<Boolean> executed2 = new AtomicReference<>(false);

        JoystickHandler.getInstance().bind(new Binding(() -> 1, () -> 1, Button::whenPressed, new InstantCommand(() -> executed1.set(true))));
        JoystickHandler.getInstance().bind(new Binding(() -> 1, () -> 2, Button::whenPressed, new InstantCommand(() -> executed2.set(true))));

        ((TestJoystick) JoystickHandler.getInstance().getJoystick(() -> 1)).releaseButton(1);
        ((TestJoystick) JoystickHandler.getInstance().getJoystick(() -> 1)).releaseButton(2);

        JoystickHandler.getInstance().init();


        withRobotEnabled(() -> {
            assertFalse(executed1.get());
            assertFalse(executed2.get());

            ((TestJoystick) JoystickHandler.getInstance().getJoystick(() -> 1)).pressButton(1);
            CommandScheduler.getInstance().run();
            assertTrue(executed1.get());
            assertFalse(executed2.get());

            ((TestJoystick) JoystickHandler.getInstance().getJoystick(() -> 1)).pressButton(2);
            CommandScheduler.getInstance().run();
            assertTrue(executed1.get());
            assertTrue(executed2.get());
        });
    }

    @Test
    void bindMultipleButtonsWithDifferentJoysticksWillBeTriggered() {
        JoystickHandler.getInstance().setupJoysticks(mkJoystickIds(1, 2));

        final AtomicReference<Boolean> executed1 = new AtomicReference<>(false);
        final AtomicReference<Boolean> executed2 = new AtomicReference<>(false);

        JoystickHandler.getInstance().bind(new Binding(() -> 1, () -> 1, Button::whenPressed, new InstantCommand(() -> executed1.set(true))));
        JoystickHandler.getInstance().bind(new Binding(() -> 2, () -> 1, Button::whenPressed, new InstantCommand(() -> executed2.set(true))));

        ((TestJoystick) JoystickHandler.getInstance().getJoystick(() -> 1)).releaseButton(1);
        ((TestJoystick) JoystickHandler.getInstance().getJoystick(() -> 2)).releaseButton(1);

        JoystickHandler.getInstance().init();


        withRobotEnabled(() -> {
            assertFalse(executed1.get());
            assertFalse(executed2.get());

            ((TestJoystick) JoystickHandler.getInstance().getJoystick(() -> 1)).pressButton(1);
            CommandScheduler.getInstance().run();
            assertTrue(executed1.get());
            assertFalse(executed2.get());

            ((TestJoystick) JoystickHandler.getInstance().getJoystick(() -> 2)).pressButton(1);
            CommandScheduler.getInstance().run();
            assertTrue(executed1.get());
            assertTrue(executed2.get());
        });
    }

    @Test
    void joystickBindables() {
        JoystickHandler.getInstance().setupJoysticks(mkJoystickIds(1, 2));

        class TestBindable implements JoystickBindable {
            public boolean cmd11Executed = false;
            public boolean cmd12Executed = false;
            public boolean cmd21Executed = false;

            @Override
            public List<Binding> getMappings() {
                return List.of(
                        new Binding(() -> 1, () -> 1, Button::whenPressed, new InstantCommand(() -> {
                            this.cmd11Executed = true;
                        })),
                        new Binding(() -> 1, () -> 2, Button::whenPressed, new InstantCommand(() -> {
                            this.cmd12Executed = true;
                        })),
                        new Binding(() -> 2, () -> 1, Button::whenPressed, new InstantCommand(() -> {
                            this.cmd21Executed = true;
                        })));
            }
        }


        TestBindable test = new TestBindable();
        JoystickHandler.getInstance().bind(test);

        JoystickHandler.getInstance().init();

        withRobotEnabled(() -> {
            ((TestJoystick) JoystickHandler.getInstance().getJoystick(() -> 1)).pressButton(1);
            CommandScheduler.getInstance().run();

            assertTrue(test.cmd11Executed);
            assertFalse(test.cmd12Executed);
            assertFalse(test.cmd21Executed);


            ((TestJoystick) JoystickHandler.getInstance().getJoystick(() -> 1)).pressButton(2);
            CommandScheduler.getInstance().run();

            assertTrue(test.cmd11Executed);
            assertTrue(test.cmd12Executed);
            assertFalse(test.cmd21Executed);


            ((TestJoystick) JoystickHandler.getInstance().getJoystick(() -> 2)).pressButton(1);
            CommandScheduler.getInstance().run();

            assertTrue(test.cmd11Executed);
            assertTrue(test.cmd12Executed);
            assertTrue(test.cmd21Executed);
        });
    }
}
