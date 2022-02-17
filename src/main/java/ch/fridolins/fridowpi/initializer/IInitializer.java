package ch.fridolins.fridowpi.initializer;

public interface IInitializer extends Initialisable {
    /**
     * Start a new composer
     *
     * @param initialisable the initialisable that should be initialized first
     * @return a new {@link InitialisableComposer}
     */
    InitialisableComposer compose(Initialisable initialisable);

    /**
     * add initialisable that will be initialized when Initializer.init() is called
     *
     * @param initialisable
     */
    void addInitialisable(Initialisable initialisable);

    /**
     * remove all {@code initialisables}, this means it will not be initialized when {@link #init} is called
     *
     * @param initialisables
     */
    void removeInitialisable(Initialisable... initialisables);

    /**
     * "{@code after}" will be initialized after "{@code initialisable}"
     *
     * @param initialisable
     * @param after
     * @return new {@link InitialisableComposer}
     */
    InitialisableComposer after(Initialisable initialisable, Initialisable after);

    /**
     * @param ini
     * @return whether {@code ini} is going to be initialized on the next {@link #init} call
     */
    boolean willBeInitialized(Initialisable ini);

    /**
     * add a composer, this will initialize all Initialisables according
     * to the order specified by the {@link InitialisableComposer}
     *
     * @param initialisableComposer
     */
    void addComposer(InitialisableComposer initialisableComposer);

    /**
     * initialises all {@link Initialisable}s that were added.
     */
    void init();

    /**
     * @return whether all {@link Initialisable}s are initialized
     */
    boolean isInitialized();
}
