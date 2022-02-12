package ch.fridolins.fridowpi.module;

import ch.fridolins.fridowpi.initializer.Initialisable;
import ch.fridolins.fridowpi.initializer.Initializer;
import edu.wpi.first.wpilibj2.command.Subsystem;

import java.util.Collection;

public interface IModule extends Subsystem, Initialisable{
    Collection<IModule> getAllSubModules();
    Collection<IModule> getSubModules();
    void registerSubmodule(IModule... subModule);
    }
}

