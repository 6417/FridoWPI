package ch.fridolins.fridowpi.joystick;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.Button;

import java.util.function.BiConsumer;

public class Binding {
    public BiConsumer<Button, Command> action;
    public IJoystickButtonId buttonId;

    public Binding(BiConsumer<Button, Command> action, IJoystickButtonId buttonId) {
        this.action = action;
        this.buttonId = buttonId;
    }
}
