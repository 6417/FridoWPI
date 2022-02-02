package ch.fridolins.fridowpi.command;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;

public interface ICommand extends Command {
    /**
     * @param requirements modules to reserve during execution
     */
    void requires(Subsystem... requirements);
}
