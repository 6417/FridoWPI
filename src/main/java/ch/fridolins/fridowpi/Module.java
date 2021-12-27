package ch.fridolins.fridowpi;

import java.util.*;
import java.util.stream.Collectors;

public class Module extends ModuleBase {
    private Set<IModule> submodules = new HashSet<>();

    @Override
    protected void registerSubmodule(ModuleBase module) {
        submodules.add(module);
        assert getAllSubModules().stream().noneMatch((other) -> this == other) : "'this' can not be a submodule of its self";
    }

    @Override
    public Set<IModule> getSubModules() {
        return Collections.unmodifiableSet(submodules);
    }

    @Override
    public Set<IModule> getAllSubModules() {
        Set<IModule> result = submodules.stream()
                .filter((module) -> module.getSubModules().size() > 0)
                .filter((module) -> module != this)
                .map(IModule::getAllSubModules)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        result.addAll(submodules);
        return result;
    }
}
