package ch.fridolins.fridowpi;

import ch.fridolins.fridowpi.base.Initialisable;
import ch.fridolins.fridowpi.base.InitializerBase;
import ch.fridolins.fridowpi.base.OptionalInitialisable;

import java.util.*;
import java.util.function.Supplier;

public class Initializer implements InitializerBase {
    private Initializer() {
        toInitialize = new HashSet<>();
    }

    private static InitializerBase instance;
    private static Supplier<InitializerBase> factory = Initializer::new;

    public static void reset() {
        instance = null;
    }

    public static void setFactory(Supplier<InitializerBase> fact) {
        assert instance == null : "instance has already been initialized";
        factory = fact;
    }

    public static InitializerBase getInstance() {
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
    public void addOptionalInitialisable(OptionalInitialisable... initialisable) {
        Collections.addAll(toInitialize, initialisable);
    }

    @Override
    public void addInitialisable(Initialisable... initialisables) {
        Arrays.stream(initialisables)
                .map(this::initialisableToOptionalInitialize)
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
