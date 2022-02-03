package ch.fridolins.fridowpi;

import ch.fridolins.fridowpi.initializer.Initialisable;
import ch.fridolins.fridowpi.initializer.IInitializer;
import ch.fridolins.fridowpi.initializer.Initializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ch.fridolins.fridowpi.module.Module;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

        Initializer.getInstance().addInitialisable(initialisable);
        Initializer.getInstance().addInitialisable(initialisable);
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


        Initializer.getInstance().after(testBefore, test.get());
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

        Initializer.getInstance().after(testBefore, test.get());
        Initializer.getInstance().after(test.get(), testAfter);
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


        Initializer.getInstance().after(testBefore, test);
        Initializer.getInstance().after(test, testAfter);

        Initializer.getInstance().addInitialisable(test);
    }

    @Test
    void thenComposerLinear() {
        final int[] idCounter = {0};

        final List<Initialisable> initialized = new ArrayList<>();

        class TestInitialisable implements Initialisable {
            private boolean hasBeenInitialised = false;
            private final int id;

            public TestInitialisable() {
                id = idCounter[0];
                idCounter[0]++;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                TestInitialisable that = (TestInitialisable) o;
                return id == that.id;
            }

            @Override
            public int hashCode() {
                return Objects.hash(id);
            }

            @Override
            public void init() {
                hasBeenInitialised = true;
                initialized.add(this);
            }

            @Override
            public boolean isInitialized() {
                return hasBeenInitialised;
            }
        }

        List<Initialisable> inits = new ArrayList<>();

        for (int i = 0; i < 3; i++)
            inits.add(new TestInitialisable());

        Initializer.getInstance().addInitialisable(inits.get(0))
                .then(inits.get(1))
                .then(inits.get(2))
                .close();

        Initializer.getInstance().init();

        assertSame(inits.size(), initialized.size());
        for (int i = 0; i < initialized.size(); i++)
            assertEquals(inits.get(i), initialized.get(i));
    }

    @Test
    void thenComposerAfter() {
        final int[] idCounter = {0};

        final List<Initialisable> initialized = new ArrayList<>();

        class TestInitialisable implements Initialisable {
            private boolean hasBeenInitialised = false;
            private final int id;

            public TestInitialisable() {
                id = idCounter[0];
                idCounter[0]++;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                TestInitialisable that = (TestInitialisable) o;
                return id == that.id;
            }

            @Override
            public int hashCode() {
                return Objects.hash(id);
            }

            @Override
            public void init() {
                hasBeenInitialised = true;
                initialized.add(this);
            }

            @Override
            public boolean isInitialized() {
                return hasBeenInitialised;
            }
        }

        List<Initialisable> inits = new ArrayList<>();

        for (int i = 0; i < 7; i++)
            inits.add(new TestInitialisable());

        Initializer.getInstance().addInitialisable(inits.get(0));
        Initializer.getInstance().addInitialisable(inits.get(1));
        Initializer.getInstance().addInitialisable(inits.get(6));

        Initializer.getInstance().after(inits.get(1), inits.get(2))
                .then(inits.get(3))
                .then(inits.get(4))
                .then(inits.get(5))
                .close();

        Initializer.getInstance().init();

        assertSame(inits.size(), initialized.size());
        for (int i = 0; i < initialized.size(); i++)
            assertEquals(inits.get(i), initialized.get(i));
    }

    @Test
    void thenComposerAfterWithReference() {
        final int[] idCounter = {0};

        final List<Initialisable> initialized = new ArrayList<>();

        class TestInitialisable implements Initialisable {
            private boolean hasBeenInitialised = false;
            private final int id;

            public TestInitialisable() {
                id = idCounter[0];
                idCounter[0]++;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                TestInitialisable that = (TestInitialisable) o;
                return id == that.id;
            }

            @Override
            public int hashCode() {
                return Objects.hash(id);
            }

            @Override
            public void init() {
                hasBeenInitialised = true;
                initialized.add(this);
            }

            @Override
            public boolean isInitialized() {
                return hasBeenInitialised;
            }
        }

        List<Initialisable> inits = new ArrayList<>();

        for (int i = 0; i < 7; i++)
            inits.add(new TestInitialisable());

        Initializer.getInstance().addInitialisable(inits.get(0));
        Initializer.getInstance().addInitialisable(inits.get(2));
        Initializer.getInstance().addInitialisable(inits.get(4));
        Initializer.getInstance().addInitialisable(inits.get(5));

        Initializer.getInstance().after(inits.get(0), inits.get(1))
                .then(inits.get(2))
                .then(inits.get(3))
                .then(inits.get(5))
                .then(inits.get(6))
                .close();

        Initializer.getInstance().init();

        assertSame(inits.size(), initialized.size());
        for (int i = 0; i < initialized.size(); i++)
            assertEquals(inits.get(i), initialized.get(i));
    }

    @Test
    void thenComposerMalformedThen() {
        final int[] idCounter = {0};

        final List<Initialisable> initialized = new ArrayList<>();

        class TestInitialisable implements Initialisable {
            private boolean hasBeenInitialised = false;
            private final int id;

            public TestInitialisable() {
                id = idCounter[0];
                idCounter[0]++;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                TestInitialisable that = (TestInitialisable) o;
                return id == that.id;
            }

            @Override
            public int hashCode() {
                return Objects.hash(id);
            }

            @Override
            public void init() {
                hasBeenInitialised = true;
                initialized.add(this);
            }

            @Override
            public boolean isInitialized() {
                return hasBeenInitialised;
            }
        }

        List<Initialisable> inits = new ArrayList<>();

        for (int i = 0; i < 3; i++)
            inits.add(new TestInitialisable());

        Initializer.getInstance().addInitialisable(inits.get(2)).then(inits.get(0)).then(inits.get(1)).then(inits.get(2)).close();

        Initializer.getInstance().init();

        assertSame(inits.size(), initialized.size());
        for (int i = 0; i < initialized.size(); i++)
            assertEquals(inits.get(i), initialized.get(i));
    }
}