package ch.fridolins.fridowpi;

import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ModuleTest {
    TestModule module;

    public static class TestModule extends Module {
        public int periodicCounter = 0;
        public final int id;

        public TestModule(int id) {
            this.id = id;
        }

        public TestModule() {
            this.id = 0;
            for (int i = 1; i <= 10; i++)
                registerSubmodule(new TestModule(i));
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

    @Test()
    public void registerSubModuleWillDieWhenThisIsASubModuleOfItsSelf() {
        var subModules = module.getAllSubModules();
        class FailingModule extends Module {
            public FailingModule() {
                registerSubmodule(this);
            }
        }

        assertThrows(AssertionError.class, FailingModule::new);
    }
}
