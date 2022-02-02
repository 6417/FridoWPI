package ch.fridolins.fridowpi.module;

import edu.wpi.first.wpilibj2.command.Subsystem;

import java.util.Collection;

public interface IModule extends Subsystem {
    /**
     * @return all the submodules of the module and of the submodules themselves
     */
    Collection<IModule> getAllSubModules();

    /**
     * @return all the submodules of the module
     */
    Collection<IModule> getSubModules();
    void registerSubmodule(IModule... subModule);
}

