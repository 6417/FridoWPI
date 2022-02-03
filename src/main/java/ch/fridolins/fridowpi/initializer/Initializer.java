package ch.fridolins.fridowpi.initializer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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

    private final Logger logger = LogManager.getLogger(Initializer.class);

    private List<Initialisable> getOrInitialize(Map<Initialisable, List<Initialisable>> map, Initialisable key) {
        if (!map.containsKey(key))
            map.put(key, new LinkedList<>());
        return map.get(key);
    }

    private void initialize(Initialisable ini) {
        if (!ini.isInitialized())
            ini.init();
    }

    @Override
    public void init() {
        toInitialize.forEach(this::initialize);
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
    public InitialisableComposer addInitialisable(Initialisable initialisable) {
        if (!toInitialize.contains(initialisable))
            toInitialize.add(initialisable);
        return new InitialisableComposer(initialisable);
    }

    @Override
    public void removeInitialisable(Initialisable... initialisables) {
        for (var ini : initialisables)
            toInitialize.remove(ini);
    }

    @Override
    public InitialisableComposer after(Initialisable initialisable, Initialisable after) {
        if (!toInitialize.contains(initialisable))
            toInitialize.add(initialisable);
        return new InitialisableComposer(initialisable).then(after);
    }

    @Override
    public boolean willBeInitialized(Initialisable ini) {
        return toInitialize.contains(ini);
    }

    private void addComposerQueue(List<InitialisableComposer.Node> queue) {
    }

    private List<Initialisable> nodesToInitialisables(Collection<InitialisableComposer.Node> nodes) {
        return nodes.stream().map((n) -> n.initialisable).collect(Collectors.toList());
    }

    private List<List<InitialisableComposer.Node>> splitQueue(List<InitialisableComposer.Node> queue) {
        List<List<InitialisableComposer.Node>> result = new ArrayList<>();

        if (queue.size() == 0)
            return result;

        result.add(new ArrayList<>());
        int i = 0;
        for (int j = 0; j < queue.size() - 1; j++) {
            result.get(i).add(queue.get(j));
            if (queue.get(j + 1).external) {
                i++;
                result.add(new ArrayList<>());
            }
        }
        result.get(i).add(queue.get(queue.size() - 1));
        return result;
    }

    @Override
    public void addComposer(InitialisableComposer initialisableComposer) {
        List<InitialisableComposer.Node> queue = initialisableComposer.getQueue();
        List<List<InitialisableComposer.Node>> subQueues = splitQueue(queue);

        if (subQueues.stream().allMatch((q) -> q.size() == 0))
            return;

        if (subQueues.size() == 1) {
            if (!subQueues.get(0).get(0).external)
                toInitialize.addAll(
                        subQueues.get(0).stream().map((n) -> n.initialisable).collect(Collectors.toList())
                );
            else {
                int index = getIndex(subQueues.get(0));
                toInitialize.addAll(index + 1, nodesToInitialisables(subQueues.get(0)));
            }
        } else {
            if (!subQueues.get(0).get(0).external) {
                subQueues = List.of(subQueues.stream().flatMap(Collection::stream).collect(Collectors.toList()));
                for (var subQueue : subQueues) {
                    toInitialize.removeIf((ini) -> subQueue.stream().anyMatch((n) -> n.initialisable == ini));
                    toInitialize.addAll(nodesToInitialisables(subQueue));
                }
            } else {
                for (var subQueue : subQueues) {
                    int index = getIndex(subQueue);
                    toInitialize.addAll(index + 1, nodesToInitialisables(subQueue));
                }
            }
        }
    }

    private int getIndex(List<InitialisableComposer.Node> subQueues) {
        int index = toInitialize.indexOf(subQueues.get(0).initialisable);
        subQueues.remove(0);
        return index;
    }
}