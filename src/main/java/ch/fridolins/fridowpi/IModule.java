package ch.fridolins.fridowpi;

import edu.wpi.first.wpilibj2.command.Subsystem;

import java.util.Collection;

public interface IModule extends Subsystem {
    Collection<IModule> getAllSubModules();
    Collection<IModule> getSubModules();
}
