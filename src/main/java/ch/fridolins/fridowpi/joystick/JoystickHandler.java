package ch.fridolins.fridowpi.joystick;

import ch.fridolins.fridowpi.Initializer;
import edu.wpi.first.wpilibj2.command.Command;

import java.util.Map;
import java.util.function.Supplier;

public class JoystickHandler implements IJoystickHandler {
    private static Supplier<IJoystickHandler> factory = JoystickHandler::new;
    private static IJoystickHandler instance;

    public static void setFactory(Supplier<IJoystickHandler> fact) {
        assert instance == null : "can't set factory, instance has already been initialized";
        factory = fact;
    }

    protected JoystickHandler() {
        Initializer.getInstance().addInitialisable(this);
    }

    public static IJoystickHandler getInstance() {
        if (instance == null)
            instance = factory.get();
        return instance;
    }

    public static void reset() {
        Initializer.getInstance().removeInitialisable(getInstance());
        instance = null;
    }


    @Override
    public void init() {

    }

    @Override
    public boolean isInitialized() {
        return false;
    }

    @Override
    public void bindAll(Map<IJoystickButton, Command> bindings) {

    }

    @Override
    public void bind(IJoystickButton button, Command command) {

    }

    @Override
    public void bind(JoystickBindable bindable) {
        bindAll(bindable.getMappings());
    }
}
