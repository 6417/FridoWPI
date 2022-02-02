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
    /**
     * @param bindings List of bindings to add
     */
    void bindAll(List<Binding> bindings);

    /**
     * @param binding single binding to add
     */
    void bind(Binding binding);

    /**
     * @param bindable
     */
    void bind(JoystickBindable bindable);

    void setupJoysticks(List<IJoystickId> joystickIds);

    public IJoystick getJoystick(IJoystickId id);

    void setJoystickFactory(Function<IJoystickId, IJoystick> factory);
}
