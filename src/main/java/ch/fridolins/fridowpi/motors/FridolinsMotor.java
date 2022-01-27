package ch.fridolins.fridowpi.motors;

import ch.fridolins.fridowpi.module.IModule;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;

public interface FridolinsMotor extends MotorController, IModule{
    public enum FridoIdleMode {
        kBrake, kCoast
    }

    public enum DirectionType {
        followMaster, invertMaster
    }

    public void setIdleMode(FridoIdleMode type);

    public void follow(FridolinsMotor master, DirectionType direction);
    
    public void factoryDefault();
}