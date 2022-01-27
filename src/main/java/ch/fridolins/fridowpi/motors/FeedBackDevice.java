package ch.fridolins.fridowpi.motors;

public interface FeedBackDevice extends FridolinsMotor{

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
