package ch.fridolins.fridowpi.sensors;


import ch.fridolins.fridowpi.Initializer;
import ch.fridolins.fridowpi.base.Initialisable;
import ch.fridolins.fridowpi.sensors.base.INavx;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Function;

public class Navx extends AHRS implements INavx, Initialisable {
    private static Function<SPI.Port, INavx> factory;

    private static SPI.Port port;

    public static void setup(SPI.Port port) {
        Navx.port = port;
    }

    private static float yawOffset = 0.f;
    private static float pitchOffset = 0.f;
    private static float rollOffset = 0.f;

    public void setYawOffset(float offset) {
        yawOffset = offset;
    }

    public static void setPitchOffset(float offset) {
        pitchOffset = offset;
    }

    public static void setRollOffset(float offset) {
        rollOffset = offset;
    }

    public static void setFactory(Function<SPI.Port, INavx> factory) {
        Navx.factory = factory;
    }

    private Navx() {
        super(port);
        Initializer.getInstance().addInitialisable(this);
    }

    private static INavx instance;

    public static INavx getInstance() {
        if (instance == null) {
            logger.info("created new instance");
            instance = factory.apply(port);
        }
        return instance;
    }

    @Override
    public float getYaw() {
        return super.getYaw() + yawOffset;
    }

    @Override
    public float getPitch() {
        return super.getYaw() + pitchOffset;
    }

    @Override
    public float getRoll() {
        return super.getYaw() + rollOffset;
    }


    private static Logger logger = LogManager.getLogger(Navx.class);
    private boolean initialized = false;

    @Override
    public void init() {
        calibrate();
        logger.info("Navx calibrating.");

        while (isCalibrating()) {
        }

        logger.info("DONE calibrating Navx calibrating.");
        logger.info("resetting Navx.");
        reset();

        initialized = true;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }
}
