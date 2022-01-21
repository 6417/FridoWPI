package ch.fridolins.fridowpi.module;

import ch.fridolins.fridowpi.Initializer;
import ch.fridolins.fridowpi.base.OptionalInitialisable;
import ch.fridolins.fridowpi.joystick.Binding;
import ch.fridolins.fridowpi.joystick.JoystickBindable;
import edu.wpi.first.math.Pair;
import edu.wpi.first.wpilibj2.command.Command;

import java.util.*;
import java.util.stream.Collectors;

public class Module extends ModuleBase implements OptionalInitialisable, JoystickBindable {
    private Set<IModule> submodules = new HashSet<>();

    protected Module() {
        Initializer.getInstance().addInitialisable(this);
    }

    @Override
    protected void registerSubmodule(IModule module) {
        assert getAllSubModules().stream().noneMatch((other) -> this == other) && module != this : "'this' can not be a submodule of its self";
        submodules.add(module);
    }

    @Override
    public Collection<IModule> getSubModules() {
        return Collections.unmodifiableSet(submodules);
    }

    @Override
    public Collection<IModule> getAllSubModules() {
        Set<IModule> result = submodules.stream()
                .filter((module) -> {
                    assert module != this : "'this' can not be a submodule of its self";
                    return module.getSubModules().size() > 0;
                })
                .map(IModule::getAllSubModules)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        result.addAll(submodules);
        return result;
    }

    @Override
    public boolean isActivated() {
        return true;
    }


    private boolean initialized = false;

    @Override
    public void init() {
        initialized = true;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public List<Pair<Binding, Command>> getMappings() {
        return new ArrayList<>();
    }
}
