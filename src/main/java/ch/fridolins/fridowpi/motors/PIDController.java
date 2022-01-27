package ch.fridolins.fridowpi.motors;

import ch.fridolins.fridowpi.motors.utils.PidValues;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;

public interface PIDController extends MotorController{

    public void setPID(PidValues pidValues);
    
    public void setVelocity(double velocity);

    public void setPosition(double position);

    public void selectPidSlot(int slotIndex);
}