package ch.fridolins.fridowpi.base.motors;

import edu.wpi.first.wpilibj.motorcontrol.MotorController;

public interface FeedBackDevice extends MotorController{

    public enum FridoFeedBackDevice {
        kRelative, kAlternative
    }
    
    public void configEncoder(FridoFeedBackDevice device, int countsPerRev);

    public void setEncoderDirection(boolean inverted);

    public void setEncoderPosition(double position);

    public double getEncoderTicks();
    
    public double getEncoderVelocity();

    public void configOpenLoopRamp(double rate);
}
