package ch.fridolins.fridowpi.base;

public interface IInitializer extends Initialisable {
    void addInitialisable(Initialisable... initialisables);
    void removeInitialisable(Initialisable... initialisables);
}
