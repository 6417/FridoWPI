package ch.fridolins.fridowpi.joystick;

import ch.fridolins.fridowpi.initializer.Initialisable;

import java.util.List;
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
