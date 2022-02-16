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

    /**
     * @param type idleMode to set the motor to
     */
    public void setIdleMode(IdleMode type);

    /**
     * @param master the motor to follow
     * @param direction the direction to follow
     */
    public void follow(FridolinsMotor master, DirectionType direction);

    /**
     * Resets the motor to factory defaults
     */
    public void factoryDefault();

    /**
     * @param pidValues the values to set the pid to
     */
    public void setPID(PidValues pidValues);

    public boolean pidAtTarget();

    /**
     * @param velocity velocity for the motor to target
     */
    public void setVelocity(double velocity);

    /**
     * @param position position for the motor to target
     */
    public void setPosition(double position);

    /**
     * @param slotIndex ID of the slot to select
     */
    public void selectPidSlot(int slotIndex);

    public enum LimitSwitchPolarity {
        kNormallyOpen, kNormallyClosed, kDisabled
    }

    /**
     * @param polarity default state of the limitswitch
     * @param enable enable or disable the joystick
     */
    public void enableForwardLimitSwitch(LimitSwitchPolarity polarity, boolean enable);

    /**
     * @param polarity default state of the limitswitch
     * @param enable enable or disable the joystick
     */
    public void enableReverseLimitSwitch(LimitSwitchPolarity polarity, boolean enable);

    /**
     * @return if the limit switch is pressed
     */
    public boolean isForwardLimitSwitchActive();

    /**
     * @return if the limit switch is pressed
     */
    public boolean isReverseLimitSwitchActive();

    public enum FridoFeedBackDevice {
        kRelative, kAlternative, kBuildin
    }

    /**
     * @param device the encoder to configure
     * @param countsPerRev the conversion of ticks to revolutions
     */
    public void configEncoder(FridoFeedBackDevice device, int countsPerRev);

    /**
     * @param inverted if the motor should be inverted
     */
    public void setEncoderDirection(boolean inverted);

    /**
     * @param position the position to set the encoder to
     */
    public void setEncoderPosition(double position);

    /**
     * @return the current ticks of the encoder
     */
    public double getEncoderTicks();

    /**
     * @return the velocity of the encoder
     */
    public double getEncoderVelocity();

    /**
     * @param rate the rate of the motor to accelerate in open loop control
     */
    public void configOpenLoopRamp(double rate);

    default LimitSwitch getForwardLimitSwitch() {
        return this::isForwardLimitSwitchActive;
    }

    default LimitSwitch getReverseLimitSwitch() {
        return this::isReverseLimitSwitchActive;
    }
}