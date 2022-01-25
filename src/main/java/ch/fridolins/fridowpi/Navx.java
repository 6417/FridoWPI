package ch.fridolins.fridowpi;


import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;

public class Navx extends AHRS {
    private static double xRotationOffset = 0.0;
    private static double yRotationOffset = 0.0;
    private static double zRotationOffset = 0.0;

    private static double xOffset = 0.0;
    private static double yOffset = 0.0;
    private static double zOffset = 0.0;


    public static void setXRotationOffset(double xRotationOffset) {
        Navx.xRotationOffset = xRotationOffset;
    }

    public static void setYRotationOffset(double yRotationOffset) {
        Navx.yRotationOffset = yRotationOffset;
    }

    public static void setZRotationOffset(double zRotationOffset) {
        Navx.zRotationOffset = zRotationOffset;
    }

    public static void setXOffset(double xOffset) {
        Navx.xOffset = xOffset;
    }

    public static void setYOffset(double yOffset) {
        Navx.yOffset = yOffset;
    }

    public static void setZOffset(double zOffset) {
        Navx.zOffset = zOffset;
    }

    private static SPI.Port port;

    public static void setup(SPI.Port port) {
        Navx.port = port;
    }


    private Navx() {
        super(port);
    }

    public static Navx instance;

    public static Navx getInstance() {
        if (instance == null)
            instance = new Navx();
        return instance;
    }
}
