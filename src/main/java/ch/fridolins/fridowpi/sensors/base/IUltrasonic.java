package ch.fridolins.fridowpi.sensors.base;

public interface IUltrasonic {
    public double getRawDistance();
    public double getFilteredDistance();
}
