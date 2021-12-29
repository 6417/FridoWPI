package ch.fridolins.fridowpi.base;

public interface InitializerBase extends Initialisable {
    void addOptionalInitialisable(OptionalInitialisable... initialisables);
    void addInitialisable(Initialisable... initialisables);
}
