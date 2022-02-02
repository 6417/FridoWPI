package ch.fridolins.fridowpi.joystick;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.Button;

import java.util.function.BiConsumer;

public interface IJoystickButtonId {
    /**
     *
     * @return the id of the button
     */
    int getButtonId();
}
