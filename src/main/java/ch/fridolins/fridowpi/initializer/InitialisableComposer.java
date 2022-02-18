package ch.fridolins.fridowpi.initializer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Closeable;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * With a {@link InitialisableComposer} you can define in which order
 * the {@code Initialisable}s should be initialized.
 *
 * Example:
 *    - @code Initializer.getInstance().compose(i1).then(i2).close();
 * means that {@code i1} will be initialized before {@code i2}
 * the {@link #close} is used to add everything to the {@link Initializer}
 * <br>
 * <strong>You have to call {@link #close} or the initialisables won't be added</strong>
 */
public class InitialisableComposer {

    protected static class Node {
        public Initialisable initialisable;

        Node(Initialisable initialisable) {
            if (Initializer.getInstance().willBeInitialized(initialisable))
                Initializer.getInstance().removeInitialisable(initialisable);
            this.initialisable = initialisable;
        }
    }

    List<Node> queue = new LinkedList<>();

    protected InitialisableComposer(Initialisable initialisable) {
        queue.add(new Node(initialisable));
    }

    protected InitialisableComposer() {
    }

    protected static InitialisableComposer makeAfter(Initialisable after, Initialisable initialisable) {
        InitialisableComposer instance = new InitialisableComposer(initialisable);
        instance.queue.add(0, new Node(after));
        return instance;
    }

    private final Logger logger = LogManager.getLogger(InitialisableComposer.class);

    /**
     * Adds {@code ini} to the queue
     * @param ini
     * @return a composer on which you can call "{@code then}" again
     */
    public InitialisableComposer then(Initialisable ini) {
        if (queue.stream().anyMatch((n) -> n.initialisable == ini)) {
            logger.warn("Initialisable was added twice with the 'then', the last call will be used");

            queue.removeIf((n) -> n.initialisable == ini);
        }
        queue.add(new Node(ini));
        return this;
    }

    /**
     * @return All initialisables that are in the composer, this list is <strong>Not ordered</strong>
     */
    protected List<Node> getQueue() {
        return queue;
    }

    /**
     * Add the queue to the {@link Initializer}
     */
    public void close() {
        Initializer.getInstance().addComposer(this);
    }
}
