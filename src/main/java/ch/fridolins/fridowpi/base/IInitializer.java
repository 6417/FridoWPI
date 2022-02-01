package ch.fridolins.fridowpi.base;

public interface IInitializer extends Initialisable {
    void addInitialisable(Initialisable... initialisables);

    void addInitialisableToFront(Initialisable... initialisables);

    void removeInitialisable(Initialisable... initialisables);
    void before(Initialisable initialisable, Initialisable before);
    void after(Initialisable initialisable, Initialisable after);

    void removeBefore(Initialisable initialisable, Initialisable before);

    void removeAfter(Initialisable initialisable, Initialisable after);
}
