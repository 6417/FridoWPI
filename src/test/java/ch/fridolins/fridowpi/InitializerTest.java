package ch.fridolins.fridowpi;

import ch.fridolins.fridowpi.base.Initialisable;
import ch.fridolins.fridowpi.base.IInitializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ch.fridolins.fridowpi.module.Module;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

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

    class DieWhenCalled implements Initialisable {
        @Override
        public void init() {
            fail();
        }

        @Override
        public boolean isInitialized() {
            return false;
        }
    }

    @Test
    void willBeforeBeCalledFirst() {
        AtomicReference<Initialisable> test = new AtomicReference<Initialisable>();
        test.set(new DieWhenCalled());

        Initialisable testBefore = new Initialisable() {
            private boolean initialized = false;

            @Override
            public void init() {
                initialized = true;
                assertFalse(test.get().isInitialized());
            }

            @Override
            public boolean isInitialized() {
                return initialized;
            }
        };

        test.set(new Initialisable() {
            private boolean initialized = false;

            @Override
            public void init() {
                initialized = true;
                assertTrue(testBefore.isInitialized());
            }

            @Override
            public boolean isInitialized() {
                return initialized;
            }
        });


        Initializer.getInstance().before(test.get(), testBefore);

        Initializer.getInstance().addInitialisable(test.get());
    }

    @Test
    void willAfterBeCalledSecond() {
        AtomicReference<Initialisable> test = new AtomicReference<Initialisable>();
        test.set(new DieWhenCalled());

        Initialisable testAfter = new Initialisable() {
            private boolean initialized = false;

            @Override
            public void init() {
                initialized = true;
                assertTrue(test.get().isInitialized());
            }

            @Override
            public boolean isInitialized() {
                return initialized;
            }
        };

        test.set(new Initialisable() {
            private boolean initialized = false;

            @Override
            public void init() {
                initialized = true;
                assertFalse(testAfter.isInitialized());
            }

            @Override
            public boolean isInitialized() {
                return initialized;
            }
        });


        Initializer.getInstance().after(test.get(), testAfter);
        Initializer.getInstance().addInitialisable(test.get());
    }

    @Test
    void afterAndBeforeCombined() {
        AtomicReference<Initialisable> test = new AtomicReference<Initialisable>();
        test.set(new DieWhenCalled());

        Initialisable testBefore = new Initialisable() {
            private boolean initialized = false;

            @Override
            public void init() {
                initialized = true;
                assertTrue(test.get().isInitialized());
            }

            @Override
            public boolean isInitialized() {
                return initialized;
            }
        };

        Initialisable testAfter = new Initialisable() {
            private boolean initialized = false;

            @Override
            public void init() {
                initialized = true;
                assertTrue(test.get().isInitialized());
            }

            @Override
            public boolean isInitialized() {
                return initialized;
            }
        };

        test.set(new Initialisable() {
            private boolean initialized = false;

            @Override
            public void init() {
                initialized = true;
                assertFalse(testAfter.isInitialized());
            }

            @Override
            public boolean isInitialized() {
                return initialized;
            }
        });

        Initializer.getInstance().before(test.get(), testBefore);
        Initializer.getInstance().after(test.get(), testAfter);

        Initializer.getInstance().addInitialisable(test.get());
    }

    @Test
    void nestedBefore() {
        class TestInitialisable implements Initialisable {
            private List<Initialisable> uninitialized;
            private List<Initialisable> initialized;

            private boolean isInit = false;

            TestInitialisable(List<Initialisable> uninitialized, List<Initialisable> initialized) {
                this.uninitialized = uninitialized;
                this.initialized = initialized;
            }

            @Override
            public void init() {
                isInit = true;
                uninitialized.forEach((ini) -> assertFalse(ini.isInitialized()));
                initialized.forEach((ini) -> assertTrue(ini.isInitialized()));
            }

            @Override
            public boolean isInitialized() {
                return isInit;
            }
        }


        Initialisable testBefore = new TestInitialisable(List.of(), List.of());
        Initialisable testAfter = new TestInitialisable(List.of(), List.of(testBefore));

        Initialisable test = new TestInitialisable(List.of(testAfter), List.of(testBefore));


        Initializer.getInstance().before(test, testBefore);
        Initializer.getInstance().after(test, testAfter);

        Initializer.getInstance().addInitialisable(test);
    }

    @Test
    void removeBefore() {
        class TestInitialisable implements Initialisable {
            private boolean initialized = false;
            public final int id;

            public TestInitialisable(int id) {
                this.id = id;
            }

            @Override
            public void init() {
                initialized = true;
            }

            @Override
            public boolean isInitialized() {
                return initialized;
            }
        }

        Initialisable test = new TestInitialisable(0);
        Initialisable testBefore1 = new TestInitialisable(1);
        Initialisable testBefore2 = new TestInitialisable(2);
        Initialisable testBefore3 = new TestInitialisable(3);
        Initialisable testBefore4 = new TestInitialisable(4);

        Initializer.getInstance().before(test, testBefore1);
        Initializer.getInstance().before(test, testBefore2);
        Initializer.getInstance().before(test, testBefore3);
        Initializer.getInstance().before(test, testBefore4);

        Initializer.getInstance().removeBefore(test, testBefore2);
        Initializer.getInstance().removeBefore(test, testBefore4);
        Initializer.getInstance().addInitialisable(test);

        Initializer.getInstance().init();

        assertFalse(testBefore2.isInitialized());
        assertFalse(testBefore4.isInitialized());

        assertTrue(testBefore1.isInitialized());
        assertTrue(testBefore3.isInitialized());
        assertTrue(test.isInitialized());
    }

    @Test
    void removeAfter() {
        class TestInitialisable implements Initialisable {
            private boolean initialized = false;
            public final int id;

            public TestInitialisable(int id) {
                this.id = id;
            }

            @Override
            public void init() {
                initialized = true;
            }

            @Override
            public boolean isInitialized() {
                return initialized;
            }
        }

        Initialisable test = new TestInitialisable(0);
        Initialisable testBefore1 = new TestInitialisable(1);
        Initialisable testBefore2 = new TestInitialisable(2);
        Initialisable testBefore3 = new TestInitialisable(3);
        Initialisable testBefore4 = new TestInitialisable(4);

        Initializer.getInstance().after(test, testBefore1);
        Initializer.getInstance().after(test, testBefore2);
        Initializer.getInstance().after(test, testBefore3);
        Initializer.getInstance().after(test, testBefore4);

        Initializer.getInstance().removeAfter(test, testBefore2);
        Initializer.getInstance().removeAfter(test, testBefore4);
        Initializer.getInstance().addInitialisable(test);

        Initializer.getInstance().init();

        assertFalse(testBefore2.isInitialized());
        assertFalse(testBefore4.isInitialized());

        assertTrue(testBefore1.isInitialized());
        assertTrue(testBefore3.isInitialized());
        assertTrue(test.isInitialized());
    }
}