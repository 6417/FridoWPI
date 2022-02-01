package ch.fridolins.fridowpi;

import ch.fridolins.fridowpi.base.Initialisable;
import ch.fridolins.fridowpi.base.IInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.function.Supplier;

public class Initializer implements IInitializer {
    private Initializer() {
        toInitialize = new LinkedList<>();
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

    private List<Initialisable> toInitialize;
    private Map<Initialisable, List<Initialisable>> beforeMap = new HashMap<>();
    private Map<Initialisable, List<Initialisable>> afterMap = new HashMap<>();

    private final Logger logger = LogManager.getLogger(Initializer.class);

    private List<Initialisable> getOrInitialize(Map<Initialisable, List<Initialisable>> map, Initialisable key) {
        if (!map.containsKey(key))
            map.put(key, new LinkedList<>());
        return map.get(key);
    }

    private void initialize(Initialisable ini) {
        getOrInitialize(beforeMap, ini).forEach(this::initialize);
        ini.init();
        getOrInitialize(afterMap, ini).forEach(this::initialize);
    }

    @Override
    public void init() {
        toInitialize.stream()
                .filter((initialisable) -> !initialisable.isInitialized())
                .forEach(this::initialize);
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

    @Override
    public void before(Initialisable initialisable, Initialisable before) {
        getOrInitialize(beforeMap, initialisable).add(before);
    }

    @Override
    public void after(Initialisable initialisable, Initialisable after) {
        getOrInitialize(afterMap, initialisable).add(after);
    }

    @Override
    public void removeBefore(Initialisable initialisable, Initialisable before) {
        if (!beforeMap.containsKey(initialisable))
            return;
        beforeMap.get(initialisable).remove(before);
    }

    @Override
    public void removeAfter(Initialisable initialisable, Initialisable after) {
        if (!afterMap.containsKey(initialisable))
            return;
        afterMap.get(initialisable).remove(after);
    }
}
