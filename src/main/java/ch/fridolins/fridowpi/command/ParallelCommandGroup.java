package ch.fridolins.fridowpi.command;

import edu.wpi.first.wpilibj2.command.Subsystem;

import java.util.Set;

public class ParallelCommandGroup extends edu.wpi.first.wpilibj2.command.ParallelCommandGroup implements ICommand {
    private Command commandProxy;

    public ParallelCommandGroup(edu.wpi.first.wpilibj2.command.Command... commands) {
        super(commands);
        commandProxy = new Command();
    }

    @Override
    public void requires(Subsystem... requirements) {
        commandProxy.requires(requirements);
    }

    @Override
    public Set<Subsystem> getRequirements() {
        return commandProxy.getRequirements();
    }
}
