package ch.fridolins.fridowpi.sensors;

import ch.fridolins.fridowpi.sensors.base.IUltrasonic;
import edu.wpi.first.math.filter.LinearFilter;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.Ultrasonic;

public class UltrasonicSensor extends Ultrasonic implements IUltrasonic {
    private static LinearFilter filter;

    public UltrasonicSensor(int pingChannel, int echoChannel) {
        super(pingChannel, echoChannel);
        filter = LinearFilter.singlePoleIIR(0.1, 0.02);
    }

    /***
     * @return "distance in mm"
     */
    @Override
    public double getRawDistance() {
        return super.getRangeMM();
    }

    /***
     * @return "filtered distance in mm"
     * */
    @Override
    public double getFilteredDistance() {
        return filter.calculate(super.getRangeMM());
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.addDoubleProperty("RawSensorValue", this::getRawDistance, null);
        builder.addDoubleProperty("FilteredSensorValue", this::getFilteredDistance, null);
        super.initSendable(builder);
    }
}