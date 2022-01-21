package ch.fridolins.fridowpi;

import ch.fridolins.fridowpi.command.*;
import ch.fridolins.fridowpi.module.IModule;
import ch.fridolins.fridowpi.module.Module;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class TestSubsystem extends SubsystemBase { }
class TestSubsystem2 extends SubsystemBase { }

class TestModule extends Module { }

class TestModuleWithSubmodules extends Module {
    class Submodule1 extends Module { }
    class Submodule2 extends Module { }

    Module submodule1;
    Module submodule2;

    public TestModuleWithSubmodules() {
        submodule1 = new Submodule1();
        submodule2 = new Submodule2();

        registerSubmodule(submodule1);
        registerSubmodule(submodule2);
    }
}

class TestModuleWithRecursiveSubmodules extends Module {
    public TestModuleWithRecursiveSubmodules() {
        registerSubmodule(new TestModuleWithSubmodules());
    }
}

public class CommandTest {
    Command command;
    ParallelCommandGroup parallelCommandGroup;
    SequentialCommandGroup sequentialCommandGroup;
    ParallelRaceGroup parallelRaceGroup;
    ParallelDeadlineGroup parallelDeadlineGroup;

    @BeforeEach
    protected void setup() {
        command = new Command();
    }

    @Test
    protected void SubsystemRequirementsAddedCorrectly() {
        TestSubsystem testSubsystem = new TestSubsystem();
        TestSubsystem testSubsystem2 = new TestSubsystem();
        command.requires(testSubsystem, testSubsystem2);

        assertTrue(command.getRequirements().contains(testSubsystem));
        assertTrue(command.getRequirements().contains(testSubsystem2));
    }

    @Test
    protected void ModuleRequirementsAddedCorrectly() {
        Module testModule = new TestModule();
        command.requires(testModule);

        assertTrue(command.getRequirements().contains(testModule));
    }

    @Test
    protected void SubModuleRequirementsAddedCorrectly() {
        Module testModule = new TestModuleWithSubmodules();
        Collection<IModule> subModules = testModule.getSubModules();

        command.requires(testModule);

        assertTrue(command.getRequirements().contains(testModule));

        for (IModule module : subModules) {
            assertTrue(command.getRequirements().contains(module));
        }
    }

    @Test
    protected void RecursiveSubModuleRequirementsAddedCorrectly() {
        Module testModule = new TestModuleWithRecursiveSubmodules();
        Collection<IModule> subModules = testModule.getAllSubModules();

        command.requires(testModule);

        assertTrue(command.getRequirements().contains(testModule));
        for (IModule module : subModules) {
            assertTrue(command.getRequirements().contains(module));
        }
    }
}

