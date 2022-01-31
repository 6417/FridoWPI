package ch.fridolins.fridowpi.motors;

import ch.fridolins.fridowpi.module.IModule;
import ch.fridolins.fridowpi.motors.utils.PidValues;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;

public interface FridolinsMotor extends MotorController, IModule {
    public enum IdleMode {
        kBrake, kCoast
    }

    public enum DirectionType {
        followMaster, invertMaster
    }

    public void setIdleMode(IdleMode type);

    public void follow(FridolinsMotor master, DirectionType direction);

    public void factoryDefault();

    public void setPID(PidValues pidValues);

    public void setVelocity(double velocity);

    public void setPosition(double position);

    public void selectPidSlot(int slotIndex);


    public enum LimitSwitchPolarity {
        kNormallyOpen, kNormallyClosed, kDisabled
    }

    public void enableForwardLimitSwitch(LimitSwitchPolarity polarity, boolean enable);

    public void enableReverseLimitSwitch(LimitSwitchPolarity polarity, boolean enable);

    public boolean isForwardLimitSwitchActive();

    public boolean isReverseLimitSwitchActive();

    public enum FridoFeedBackDevice {
        kRelative, kAlternative, kBuildin
    }

    public void configEncoder(FridoFeedBackDevice device, int countsPerRev);

    public void setEncoderDirection(boolean inverted);

    public void setEncoderPosition(double position);

    public double getEncoderTicks();

    public double getEncoderVelocity();

    public void configOpenLoopRamp(double rate);
}