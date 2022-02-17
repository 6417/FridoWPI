package ch.fridolins.fridowpi.initializer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public interface Initialisable {
    /**
     * In this function all the objects that depend on some other {@link Initialisable} are instantiated
     */
    void init();

    /**
     * @return if everything initialized
     */
    boolean isInitialized();

    /**
     * Add a dependency, this means {@code initialisable} will be initialized before the current object
     * @param initialisable will be initialized before {@code this}, may not be {@code this}
     */
    default void requires(Initialisable initialisable) {
        Initializer.getInstance().after(initialisable, this);
    }
}
