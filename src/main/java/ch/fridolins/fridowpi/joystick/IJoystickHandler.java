package ch.fridolins.fridowpi.joystick;

import ch.fridolins.fridowpi.base.Initialisable;
import edu.wpi.first.math.Pair;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.Button;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public interface IJoystickHandler extends Initialisable {
    void bindAll(List<Pair<Binding, Command>> bindings);

    void bind(Binding binding, Command command);

    void bind(JoystickBindable bindable);

    void setupJoysticks(List<IJoystickId> joystickIds);

    public IJoystick getJoystick(IJoystickId id);

    void setJoystickFactory(Function<IJoystickId, IJoystick> factory);
}
