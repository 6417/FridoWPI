package ch.fridolins.fridowpi.base.motors;

public interface FridolinsMotor {
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
