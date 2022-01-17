package ch.fridolins.fridowpi;

import ch.fridolins.fridowpi.joystick.*;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.Button;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class JoystickBindableTest {
    public class TestJoystick implements IJoystick {
        Map<Integer, Boolean> buttonsPressed = new HashMap<>();
        Map<Integer, Button> buttons = new HashMap<>();

        public void pressButton(int id) {

        }

        public void releaseButton(int id) {

        }

        public TestJoystick(IJoystickId id) {

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

    IJoystickButtonId mkButtonId(int joystick, int button) {
        return new IJoystickButtonId() {
            @Override
            public int getButtonId() {
                return button;
            }

            @Override
            public IJoystickId getJoystickId() {
                return () -> joystick;
            }
        };
    }

    List<IJoystickId> mkJoystickIds(Integer... ids) {
        return Arrays.stream(ids)
                .map((id) -> (IJoystickId) () -> id)
                .collect(Collectors.toList());
    }

    enum Joysticks implements IJoystickId {
        Drive(0);

        private final int port;

        private Joysticks(int port) {
            this.port = port;
        }

        @Override
        public int getPort() {
            return 0;
        }

        public enum Demo implements IJoystickButtonId {
            X(0);

            private final int buttonId;

            private Demo(int id) {
                buttonId = id;
            }

            @Override
            public int getButtonId() {
                return 0;
            }

            @Override
            public IJoystickId getJoystickId() {
                return Joysticks.this.getPort();
            }
        }
    }

    @Test
    void bindTrivialWillBeTriggered() {
        JoystickHandler.getInstance().setupJoysticks(mkJoystickIds(1));
        JoystickHandler.getInstance().init();


        final AtomicReference<Boolean> executed = new AtomicReference<>(false);

        JoystickHandler.getInstance().bind(
                new Binding(Button::whenPressed, Joysticks.Drive.Demo.X),
                new InstantCommand(() -> executed.set(true)));
    }
}
