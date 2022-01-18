package ch.fridolins.fridowpi;

import ch.fridolins.fridowpi.command.*;
import ch.fridolins.fridowpi.module.IModule;
import ch.fridolins.fridowpi.module.Module;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommandGroupTest {
    ParallelCommandGroup parallelCommandGroup;

    @BeforeEach
    protected void setup() {
        parallelCommandGroup = new ParallelCommandGroup();
    }

    @Test
    protected void SubsystemRequirementsAddedCorrectly() {
        TestSubsystem testSubsystem = new TestSubsystem();
        TestSubsystem testSubsystem2 = new TestSubsystem();
        parallelCommandGroup.requires(testSubsystem, testSubsystem2);

        assertTrue(parallelCommandGroup.getRequirements().contains(testSubsystem));
        assertTrue(parallelCommandGroup.getRequirements().contains(testSubsystem2));
    }

    @Test
    protected void ModuleRequirementsAddedCorrectly() {
        Module testModule = new TestModule();
        parallelCommandGroup.requires(testModule);

        assertTrue(parallelCommandGroup.getRequirements().contains(testModule));
    }

    @Test
    protected void SubModuleRequirementsAddedCorrectly() {
        Module testModule = new TestModuleWithSubmodules();
        Collection<IModule> subModules = testModule.getSubModules();

        parallelCommandGroup.requires(testModule);

        assertTrue(parallelCommandGroup.getRequirements().contains(testModule));

        for (IModule module : subModules) {
            assertTrue(parallelCommandGroup.getRequirements().contains(module));
        }
    }

    @Test
    protected void RecursiveSubModuleRequirementsAddedCorrectly() {
        Module testModule = new TestModuleWithRecursiveSubmodules();
        Collection<IModule> subModules = testModule.getAllSubModules();

        parallelCommandGroup.requires(testModule);

        assertTrue(parallelCommandGroup.getRequirements().contains(testModule));
        for (IModule module : subModules) {
            assertTrue(parallelCommandGroup.getRequirements().contains(module));
        }
    }
}

