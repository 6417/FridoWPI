package ch.fridolins.fridowpi.motors;

import edu.wpi.first.wpilibj.motorcontrol.MotorController;

public interface LimitSwitchController extends MotorController{

    public enum LimitSwitchPolarity {
        kNormallyOpen, kNormallyClosed, kDisabled
    }

    public void enableForwardLimitSwitch(LimitSwitchPolarity polarity, boolean enable);

    public void enableReverseLimitSwitch(LimitSwitchPolarity polarity, boolean enable);

    public boolean isForwardLimitSwitchActive();

    public boolean isReverseLimitSwitchActive();
}