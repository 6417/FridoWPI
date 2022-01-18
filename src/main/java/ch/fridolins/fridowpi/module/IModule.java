package ch.fridolins.fridowpi.module;

import edu.wpi.first.wpilibj2.command.Subsystem;

import java.util.Collection;

public interface IModule extends Subsystem {
    Collection<IModule> getAllSubModules();
    Collection<IModule> getSubModules();
    void registerSubmodule(IModule... subModule);
}
