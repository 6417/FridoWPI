package ch.fridolins.fridowpi.pneumatics;

import ch.fridolins.fridowpi.initializer.Initialisable;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.wpilibj.PneumaticsModuleType;

public interface IPneumaticHandler extends Initialisable, Sendable {
    void enableCompressor();

    void disableCompressor();

    void configureCompressor(int id, PneumaticsModuleType pcmType);

    int getCompressorId();

    PneumaticsModuleType getCompressorType();
}