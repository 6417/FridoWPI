package ch.fridolins.fridowpi.joystick;

import ch.fridolins.fridowpi.base.Initialisable;
import edu.wpi.first.wpilibj2.command.Command;

import java.util.Map;

public interface IJoystickHandler extends Initialisable {
    void bindAll(Map<IJoystickButton, Command> bindings);
    void bind(IJoystickButton button, Command command);
}
