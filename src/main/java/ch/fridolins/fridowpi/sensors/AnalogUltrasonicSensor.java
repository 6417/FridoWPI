package ch.fridolins.fridowpi.sensors;

import ch.fridolins.fridowpi.sensors.base.IUltrasonic;
import edu.wpi.first.math.filter.LinearFilter;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.RobotController;

public class AnalogUltrasonicSensor implements IUltrasonic, Sendable {
    private AnalogInput sensor;
    private LinearFilter filter;
    double scale;

    /**
     * @param channel the analog input where the sensor is connected
     * @param scale the scale from volts to mm
     */
    public AnalogUltrasonicSensor(int channel, double scale) {
        // this.sensor = new AnalogPotentiometer(channel);
        this.sensor = new AnalogInput(channel);
        this.scale = scale;

        filter = LinearFilter.movingAverage(16);
        filter.reset();

        SendableRegistry.addLW(this, "AnalogUltrasonic", channel);
    }

    @Override
    public double getRawDistance() {
        return sensor.getValue() * scale * (5 / RobotController.getVoltage5V());
    }

    @Override
    public double getFilteredDistance() {
        return filter.calculate(this.getRawDistance());
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.addDoubleProperty("RawSensorValue", this::getRawDistance, null);
        builder.addDoubleProperty("FilteredSensorValue", this::getFilteredDistance, null);
        builder.setSmartDashboardType("AnalogUltrasonic");
    }
}
