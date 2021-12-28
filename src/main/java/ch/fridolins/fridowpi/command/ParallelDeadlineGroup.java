package ch.fridolins.fridowpi.command;

import edu.wpi.first.wpilibj2.command.Subsystem;

import java.util.Set;

public class ParallelDeadlineGroup extends edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup implements ICommand {
    private Command commandProxy;

    public ParallelDeadlineGroup(edu.wpi.first.wpilibj2.command.Command deadlineCommand, edu.wpi.first.wpilibj2.command.Command... commands) {
        super(deadlineCommand, commands);
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
