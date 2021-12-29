package ch.fridolins.fridowpi;

import ch.fridolins.fridowpi.base.InitializerBase;
import ch.fridolins.fridowpi.base.OptionalInitializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class Initializer implements InitializerBase {
    private Initializer() {
        toInitialize = new ArrayList<>();
    }

    private static InitializerBase instance;
    private static Supplier<InitializerBase> factory = Initializer::new;

    public static void setFactory(Supplier<InitializerBase> fact) {
        assert instance == null : "instance has already been initialized";
        factory = fact;
    }

    public static InitializerBase getInstance() {
        if (instance == null)
            instance = factory.get();
        return instance;
    }

    private List<OptionalInitializable> toInitialize;

    @Override
    public void init() {
        toInitialize.stream()
                .filter((initializable) -> !initializable.isInitialized())
                .filter(OptionalInitializable::isActivated)
                .forEach(OptionalInitializable::init);
    }

    @Override
    public boolean isInitialized() {
        return toInitialize.stream()
                .filter(OptionalInitializable::isActivated)
                .allMatch(OptionalInitializable::isInitialized);
    }

    @Override
    public void addInitializable(OptionalInitializable... initializable) {
        Collections.addAll(toInitialize, initializable);
    }
}
