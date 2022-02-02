package ch.fridolins.fridowpi.sensors.base;

public interface IAnalogLightbarrier {
    /**
     * @return the raw voltage value of the sensor
     */
    public double getRawVoltage();

    /**
     * @param threshold the value where the lightbarrier should trigger
     */
    public void setThreshold(double threshold);

    /**
     * @return if the light is triggered
     */
    public boolean isTriggered();
}
