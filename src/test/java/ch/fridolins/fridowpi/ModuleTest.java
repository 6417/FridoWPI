package ch.fridolins.fridowpi;

import ch.fridolins.fridowpi.module.Module;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

public class ModuleTest {
    TestModule module;

    public static class TestModule extends Module {
        public int periodicCounter = 0;

        public TestModule(boolean initSubModules) {
            if (initSubModules)
                for (int i = 1; i <= 10; i++)
                    registerSubmodule(new TestModule(false));
        }

        public TestModule() {
            for (int i = 1; i <= 10; i++)
                registerSubmodule(new TestModule(true));
            registerSubmodule(new TestModule(false));
        }

        @Override
        public void periodic() {
            periodicCounter++;
        }
    }

    @BeforeEach
    public void setUp() {
        module = new TestModule();
    }

    @Test
    public void registerSubModuleWillDieWhenThisIsASubModuleOfItsSelf() {
        class FailingModule extends Module {
            public FailingModule() {
                registerSubmodule(this);
            }
        }

        assertThrows(AssertionError.class, FailingModule::new);
        assertThrows(AssertionError.class, () -> new FailingModule().getAllSubModules());
    }

    @Test
    public void getAllSubModulesWillReturnAllSubModules() {
        var subModules = module.getAllSubModules();
        assertEquals(111, subModules.size());
    }

    private boolean checkPeriodicCounter(int expected) {
        AtomicBoolean result = new AtomicBoolean(true);
        module.getAllSubModules().forEach((mod) -> {
            result.set(result.get() && ((TestModule) mod).periodicCounter == expected);
        });
        return result.get();
    }

    @Test
    public void periodicWillAllSubModulesBeCalled() {
        assertTrue(checkPeriodicCounter(0));
        for (int i = 1; i <= 10; i++) {
            CommandScheduler.getInstance().run();
            assertTrue(checkPeriodicCounter(i));
        }
    }
}
