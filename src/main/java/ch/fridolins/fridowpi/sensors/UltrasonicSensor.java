package ch.fridolins.fridowpi.sensors;

import ch.fridolins.fridowpi.sensors.base.IUltrasonic;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.Ultrasonic;

public class UltrasonicSensor extends Ultrasonic implements IUltrasonic{

    public UltrasonicSensor(int pingChannel, int echoChannel) {
        super(pingChannel, echoChannel);
    }

    /***
     * @return "distance in mm"
     */
    @Override
    public double getDistance() {
        return super.getRangeMM();
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.addDoubleProperty("RawSensorValue", super::getRangeMM, null);
        builder.addDoubleProperty("FilteredSensorValue", this::getDistance, null);
        super.initSendable(builder);
    }
}
