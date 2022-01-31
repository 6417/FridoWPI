package ch.fridolins.fridowpi;

import ch.fridolins.fridowpi.base.Activatable;
import ch.fridolins.fridowpi.base.Initialisable;
import ch.fridolins.fridowpi.base.IInitializer;
import ch.fridolins.fridowpi.base.OptionalInitialisable;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Initializer implements IInitializer {
    private Initializer() {
        toInitialize = new HashSet<>();
    }

    private static IInitializer instance;
    private static Supplier<IInitializer> factory = Initializer::new;

    public static void reset() {
        instance = null;
    }

    public static void setFactory(Supplier<IInitializer> fact) {
        assert instance == null : "can't set factory, instance has already been initialized";
        factory = fact;
    }

    public static IInitializer getInstance() {
        if (instance == null)
            instance = factory.get();
        return instance;
    }

    private Set<Initialisable> toInitialize;

    @Override
    public void init() {
        toInitialize.stream()
                .filter((initialisable) -> !initialisable.isInitialized())
                .collect(Collectors.toList())
                .forEach(Initialisable::init);

        toInitialize.clear();
    }

    @Override
    public boolean isInitialized() {
        if (toInitialize.size() == 0)
            return true;
        return toInitialize.stream()
                .allMatch(Initialisable::isInitialized);
    }

    @Override
    public void addInitialisable(Initialisable... initialisables) {

        toInitialize.addAll(Arrays.asList(initialisables));
    }

    @Override
    public void removeInitialisable(Initialisable... initialisables) {
        for (var ini : initialisables)
            toInitialize.remove(ini);
    }
}
