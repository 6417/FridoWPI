package ch.fridolins.fridowpi.initializer;

public interface IInitializer extends Initialisable {
    InitialisableComposer addInitialisable(Initialisable initialisables);

    void removeInitialisable(Initialisable... initialisables);

    InitialisableComposer after(Initialisable initialisable, Initialisable after);

    boolean willBeInitialized(Initialisable ini);

    void addComposer(InitialisableComposer initialisableComposer);
}
