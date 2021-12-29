package ch.fridolins.fridowpi.joystick;

import edu.wpi.first.wpilibj2.command.Command;

import java.util.Map;

public interface JoystickBindable {
    Map<JoystickButton, Command> getMappings();
}
