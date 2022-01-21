package ch.fridolins.fridowpi.base.motors;

import java.lang.StackWalker.Option;
import java.util.Collection;
import java.util.Optional;
import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkMaxPIDController;
import org.apache.logging.log4j.core.tools.picocli.CommandLine.PicocliException;
import ch.fridolins.fridowpi.module.Module;
import ch.fridolins.fridowpi.base.motors.utils.PidValues;
import ch.fridolins.fridowpi.module.IModule;

public class FridoCanSparkMax extends CANSparkMax implements PIDController, LimitSwitchController, FeedBackDevice, IModule {

    private IModule moduleProxy = new Module();

    double position;
    private int deviceID;

    private Optional<Integer> selectedPIDSlot = Optional.empty();
    SparkMaxPIDController pidController;

    public FridoCanSparkMax(int deviceId, MotorType type) {
        super(deviceId, type);
        this.deviceID = deviceID;
    }

    public boolean isPIDInitialised() {
        if(this.pidController != null) {
            return true;
        }
        return false;
    }

    @Override
    public void setVelocity(double velocity) {
        if(isPIDInitialised()) {
            selectedPIDSlot.ifPresentOrElse(((slotIdx) -> pidController.setReference(velocity, ControlType.kVelocity, slotIdx)),
            () -> pidController.setReference(velocity, ControlType.kVelocity));
        }
        else{
            try{
                throw new Exception("PID Controller of Motor" + deviceID + "(deviceID) not initialized");
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setPosition(double position) {
        if(isPIDInitialised()) {
            selectedPIDSlot.ifPresentOrElse(
                (slotIdx) -> pidController.setReference(position, ControlType.kPosition, slotIdx),
                () -> pidController.setReference(position, ControlType.kPosition));
        }
        else {
            try{
                throw new Exception("PID Controller of Motor" + deviceID + "(deviceID) not initialized");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void setPidValues(PidValues values) {
        this.pidController = super.getPIDController();

        this.pidController.setP(values.kP);
        this.pidController.setI(values.kI);
        this.pidController.setD(values.kD);

        values.kF.ifPresent((kF) -> this.pidController.setFF(kF));

        this.pidController.setOutputRange(values.peakOutputReverse, values.peakOutputForward);
    }

    public void setPidValuesWithSlotIdx(PidValues values) {
        if(this.pidController != null) 
            this.pidController = super.getPIDController();

        this.pidController.setP(values.kP, values.slotIdX.get());
        this.pidController.setI(values.kI, values.slotIdX.get());
        this.pidController.setD(values.kD, values.slotIdX.get());

        values.kF.ifPresent((kF) -> this.pidController.setFF(kF, values.slotIdX.get()));

        this.pidController.setOutputRange(values.peakOutputReverse, values.peakOutputForward, values.slotIdX.get());

    }

    @Override
    public void selectPidSlot(int slotIndex) {
        this.selectedPIDSlot = Optional.of(slotIndex);
    }

    @Override
    public void enableForwardLimitSwitch(LimitSwitchPolarity polarity, boolean enable) {
    }

    @Override
    public void enableReverseLimitSwitch(LimitSwitchPolarity polarity, boolean enable) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean isForwardLimitSwitchActive() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isReverseLimitSwitchActive() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void configEncoder(FeedbackDevice device, int countsPerRev) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setEncoderDirection(boolean inverted) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setEncoderPosition(double position) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public double getEncoderTicks() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getEncoderVelocity() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void set(double speed) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public double get() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setInverted(boolean isInverted) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean getInverted() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void disable() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void stopMotor() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Collection<IModule> getAllSubModules() {
        return moduleProxy.getAllSubModules();
    }

    @Override
    public Collection<IModule> getSubModules() {
        return moduleProxy.getSubModules();
    }

    @Override
    public void registerSubmodule(IModule... subModule) {
        moduleProxy.registerSubmodule(subModule);
    }
}