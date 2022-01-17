package ch.fridolins.fridowpi.joystick;

import edu.wpi.first.wpilibj2.command.button.Button;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

public class Joystick extends edu.wpi.first.wpilibj.Joystick implements IJoystick{
    public Joystick(int port) {
        super(port);
    }

    @Override
    public Button getButton(IJoystickButtonId id) {
        return new JoystickButton(this, id.getButtonId());
    }
}
