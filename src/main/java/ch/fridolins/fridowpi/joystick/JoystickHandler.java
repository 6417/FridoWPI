package ch.fridolins.fridowpi.joystick;

import ch.fridolins.fridowpi.Initializer;
import edu.wpi.first.math.Pair;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.button.Button;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class JoystickHandler implements IJoystickHandler {
    private static Supplier<IJoystickHandler> factory = JoystickHandler::new;
    private static IJoystickHandler instance;
    private Map<IJoystickId, IJoystick> joysticks;
    private Function<IJoystickId, IJoystick> joystickFactory;
    private List<Pair<Binding, Command>> toInitialize;

    public IJoystick getJoystick(IJoystickId id) {
        return joysticks.get(id);
    }

    public static void setFactory(Supplier<IJoystickHandler> fact) {
        assert instance == null : "can't set factory, instance has already been initialized";
        factory = fact;
    }

    @Override
    public void setJoystickFactory(Function<IJoystickId, IJoystick> factory) {
        joystickFactory = factory;
    }

    @Override
    public void setupJoysticks(List<IJoystickId> joystickIds) {
        joystickIds.forEach((id) -> joysticks.put(id, joystickFactory.apply(id)));
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
        toInitialize.forEach((pair) -> {
            Binding binding = pair.getFirst();
            Command command = pair.getSecond();

            binding.action.accept(getJoystick(binding.buttonId.getJoystickId()).getButton(binding.buttonId), command);
        });

        toInitialize.clear();
    }

    @Override
    public boolean isInitialized() {
        return toInitialize.isEmpty();
    }

    @Override
    public void bindAll(List<Pair<Binding, Command>> bindings) {
        bindings.forEach((pair) -> this.bind(pair.getFirst(), pair.getSecond()));
    }

    @Override
    public void bind(Binding binding, Command command) {
        toInitialize.add(new Pair<>(binding, command));
    }

    @Override
    public void bind(JoystickBindable bindable) {
        bindAll(bindable.getMappings());
    }
}
