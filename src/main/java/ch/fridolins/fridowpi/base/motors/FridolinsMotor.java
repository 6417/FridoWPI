package ch.fridolins.fridowpi.base.motors;

import ch.fridolins.fridowpi.module.IModule;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;

public interface FridolinsMotor extends IModule, MotorController {
    public enum IdleModeType {
        kBrake, kCoast
    }

    public enum DirectionType {
        followMaster, invertMaster
    }

    public void setIdleMode(IdleModeType type);

    public void follow(FridolinsMotor master, DirectionType direction);
    
    public void factoryDefault();
}
