package ch.fridolins.fridowpi.sensors.base;

public interface IUltrasonic {
    /**
     * @return the raw distance in mm
     */
    public double getRawDistance();

    /**
     * @return the filtered distance in mm
     */
    public double getFilteredDistance();
}
