package ch.fridolins.fridowpi.module;

import ch.fridolins.fridowpi.base.Activatable;
import ch.fridolins.fridowpi.base.Initializable;
import ch.fridolins.fridowpi.base.OptionalInitializable;

import java.util.*;
import java.util.stream.Collectors;

public class Module extends ModuleBase implements OptionalInitializable {
    private Set<IModule> submodules = new HashSet<>();

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


    @Override
    public void init() {

    }

    @Override
    public boolean isInitialized() {
        return false;
    }
}
