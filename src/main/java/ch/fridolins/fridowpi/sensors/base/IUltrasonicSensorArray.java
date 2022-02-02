package ch.fridolins.fridowpi.sensors.base;

import edu.wpi.first.math.geometry.Rotation2d;

public interface IUltrasonicSensorArray extends IUltrasonic {
    /**
     * @return instance of the left sensor
     */
    public IUltrasonic getLeftSensor();

    /**
     * @return instance of the right sensor
     */
    public IUltrasonic getRightSensor();

    /**
     * @return the angle of the sensor array to the wall
     */
    public Rotation2d getRawAngle();

    /**
     * @return the filtered angle of the sensor array to the wall
     */
    public Rotation2d getFilteredAngle();

    /**
     * @return the distance between the 2 sensors in the array
     */
    public double getSensorDistance();
}