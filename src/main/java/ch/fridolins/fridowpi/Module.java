package ch.fridolins.fridowpi;

import java.util.*;
import java.util.stream.Collectors;

public class Module extends ModuleBase {
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
}
