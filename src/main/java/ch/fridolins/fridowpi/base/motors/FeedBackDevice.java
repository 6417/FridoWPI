package ch.fridolins.fridowpi.base.motors;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;

public interface FeedBackDevice extends MotorController{

    public enum FeedbackDevice {
        QuadEncoder, CANEncoder
    }
    public void configEncoder(FeedbackDevice device, int countsPerRev);

    public void setEncoderDirection(boolean inverted);

    public void setEncoderPosition(double position);

    public double getEncoderTicks();
    
    public double getEncoderVelocity();
}
