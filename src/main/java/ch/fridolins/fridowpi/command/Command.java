package ch.fridolins.fridowpi.command;

import ch.fridolins.fridowpi.module.IModule;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Subsystem;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Command extends CommandBase implements ICommand {
    @Override
    public void requires(Subsystem... requirements) {
        Set<Subsystem> allRequirements = new HashSet<>(Set.of(requirements));

        Set<IModule> subModuleRequirements = Arrays.stream(requirements)
                .filter((req) -> req instanceof IModule)
                .map((mod) -> (IModule) mod)
                .collect(Collectors.toSet());
        allRequirements.addAll(subModuleRequirements);
        addRequirements(allRequirements.toArray(Subsystem[]::new));
    }
}
