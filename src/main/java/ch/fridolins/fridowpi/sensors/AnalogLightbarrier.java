package ch.fridolins.fridowpi.sensors;

import ch.fridolins.fridowpi.sensors.base.IAnalogLightbarrier;
import edu.wpi.first.wpilibj.AnalogInput;

public class AnalogLightbarrier implements IAnalogLightbarrier {
    private AnalogInput input;
    private double threshold;

    public AnalogLightbarrier(int channel, double threshold) {
        this.input = new AnalogInput(channel);
        this.threshold = threshold;
    }

    @Override
    public double getRawVoltage() {
        return input.getVoltage();
    }

    @Override
    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    @Override
    public boolean isTriggered() {
        return getRawVoltage() >= this.threshold;
    }
}
