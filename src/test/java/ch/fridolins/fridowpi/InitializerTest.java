package ch.fridolins.fridowpi;

import ch.fridolins.fridowpi.base.Initialisable;
import ch.fridolins.fridowpi.base.IInitializer;
import ch.fridolins.fridowpi.base.OptionalInitialisable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ch.fridolins.fridowpi.module.Module;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class InitializerTest {
    Initialisable initialisable;

    @BeforeEach
    protected void setup() {
        initialisable = new Initialisable() {
            private boolean initialized = false;

            @Override
            public void init() {
                initialized = true;
            }

            @Override
            public boolean isInitialized() {
                return initialized;
            }
        };
    }

    @AfterEach
    protected void teardown() {
        Initializer.reset();
    }

    @Test
    void resetWillReturnNewInstance() {
        IInitializer beforeReset1 = Initializer.getInstance();
        IInitializer beforeReset2 = Initializer.getInstance();
        assertSame(beforeReset1, beforeReset2);
        Initializer.reset();
        IInitializer afterReset1 = Initializer.getInstance();
        IInitializer afterReset2 = Initializer.getInstance();
        assertSame(afterReset1, afterReset2);
        assertNotSame(beforeReset1, afterReset1);
    }

    @Test
    void willInitialisableBeInitialized() {
        Initializer.getInstance().addInitialisable(initialisable);
        assertFalse(initialisable.isInitialized());

        Initializer.getInstance().init();

        assertTrue(initialisable.isInitialized());
    }

    @Test
    void willIsInitializedReturnTrueAfterInit() {
        Initializer.getInstance().addInitialisable(initialisable);
        assertFalse(Initializer.getInstance().isInitialized());
        Initializer.getInstance().init();
        assertTrue(Initializer.getInstance().isInitialized());
    }

    @Test
    void addingSameMultipleTimesWillBeInitializedOnce() {
        AtomicInteger initCounter = new AtomicInteger(0);
        Initialisable initialisable = new Initialisable() {
            @Override
            public void init() {
                initCounter.incrementAndGet();
            }

            @Override
            public boolean isInitialized() {
                return initCounter.get() > 0;
            }
        };

        Initializer.getInstance().addInitialisable(initialisable, initialisable);
        assertEquals(0, initCounter.get());
        Initializer.getInstance().init();
        assertEquals(1, initCounter.get());
        Initializer.getInstance().init();
        assertEquals(1, initCounter.get());
    }

    @Test
    void willModuleBeAddedAutomatically() {
        class TestModule extends Module {
            public TestModule() {
                super();
            }
        }

        TestModule mod = new TestModule();

        Initializer.getInstance().init();
        assertTrue(mod.isInitialized());
    }
}