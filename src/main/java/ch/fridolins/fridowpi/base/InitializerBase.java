package ch.fridolins.fridowpi.base;

public interface InitializerBase extends Initialisable {
    void addInitialisable(Initialisable... initialisables);
}
