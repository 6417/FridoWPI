package ch.fridolins.fridowpi.module;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class ModuleBase extends SubsystemBase implements IModule {
   abstract protected void registerSubmodule(IModule module);
}
