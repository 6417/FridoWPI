package ch.fridolins.fridowpi.joystick;

import ch.fridolins.fridowpi.Initializer;
import edu.wpi.first.math.Pair;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.button.Button;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import ch.fridolins.fridowpi.Initializer;

public class JoystickHandler implements IJoystickHandler {
    private static Supplier<IJoystickHandler> factory = JoystickHandler::new;
    private static IJoystickHandler instance;
    private final Map<Integer, IJoystick> joysticks = new HashMap<>();
    private Function<IJoystickId, IJoystick> joystickFactory = WPIJoystick::new;
    private List<Binding> toInitialize = new ArrayList<>();

    @Override
    public IJoystick getJoystick(IJoystickId id) {
        return joysticks.get(id.getPort());
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
        joystickIds.forEach((id) -> joysticks.put(id.getPort(), joystickFactory.apply(id)));
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
        toInitialize.forEach((binding) -> {
            binding.action.accept(getJoystick(binding.joystickId).getButton(binding.buttonId), binding.command);
        });

        toInitialize.clear();
    }

    @Override
    public boolean isInitialized() {
        return toInitialize.isEmpty();
    }

    @Override
    public void bindAll(List<Binding> bindings) {
        bindings.forEach(this::bind);
    }

    @Override
    public void bind(Binding binding) {
        toInitialize.add(binding);
    }

    @Override
    public void bindImediately(Binding binding, Command command) {
        binding.action.accept(getJoystick(binding.joystickId).getButton(binding.buttonId), command);
    }

    @Override
    public void bind(JoystickBindable bindable) {
        bindAll(bindable.getMappings());
    }

    private void checkIfBindingIsInToInitialzeAndDestroy(Binding binding) {
        if(this.toInitialize.contains(binding)) {
            this.toInitialize.remove(binding);
        }
    }
    
    @Override
    public void destroyAllBindings() {
        CommandScheduler.getInstance().clearButtons();
    }
}
