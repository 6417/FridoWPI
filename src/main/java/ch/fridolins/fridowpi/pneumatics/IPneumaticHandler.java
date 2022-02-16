package ch.fridolins.fridowpi.pneumatics;

import ch.fridolins.fridowpi.initializer.Initialisable;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.wpilibj.PneumaticsModuleType;

public interface IPneumaticHandler extends Initialisable, Sendable {
    /** Enable compressor closed loop control using digital input. */
    void enableCompressor();

    /** Disable the compressor. */
    void disableCompressor();

    /**
     * @param id the id of the pcm
     * @param pcmType the type of the pcm
     */
    void configureCompressor(int id, PneumaticsModuleType pcmType);

    /**
     * @return the id of the pcm
     */
    int getCompressorId();

    /**
     * @return the type of the pcm
     */
    PneumaticsModuleType getCompressorType();
}