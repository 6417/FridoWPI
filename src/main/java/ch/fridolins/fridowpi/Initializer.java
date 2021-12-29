package ch.fridolins.fridowpi;

import ch.fridolins.fridowpi.base.Activatable;
import ch.fridolins.fridowpi.base.Initialisable;
import ch.fridolins.fridowpi.base.IInitializer;
import ch.fridolins.fridowpi.base.OptionalInitialisable;

import java.util.*;
import java.util.function.Supplier;

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

    private Set<OptionalInitialisable> toInitialize;

    @Override
    public void init() {
        toInitialize.stream()
                .filter((initialisable) -> !initialisable.isInitialized())
                .filter(OptionalInitialisable::isActivated)
                .forEach(OptionalInitialisable::init);
    }

    @Override
    public boolean isInitialized() {
        if (toInitialize.size() == 0)
            return true;
        return toInitialize.stream()
                .filter(OptionalInitialisable::isActivated)
                .allMatch(OptionalInitialisable::isInitialized);
    }

    @Override
    public void addInitialisable(Initialisable... initialisables) {
        Arrays.stream(initialisables)
                .filter((ini) -> !(ini instanceof Activatable))
                .map(this::initialisableToOptionalInitialize)
                .forEach(toInitialize::add);

        Arrays.stream(initialisables)
                .filter((ini) -> (ini instanceof Activatable))
                .map((ini) -> (OptionalInitialisable) ini)
                .forEach(toInitialize::add);
    }

    private OptionalInitialisable initialisableToOptionalInitialize(Initialisable initialisable) {
        return new OptionalInitialisable() {
            private final Initialisable initialisableProxy = initialisable;

            @Override
            public boolean isActivated() {
                return true;
            }

            @Override
            public void init() {
                initialisableProxy.init();
            }

            @Override
            public boolean isInitialized() {
                return initialisableProxy.isInitialized();
            }
        };
    }
}
