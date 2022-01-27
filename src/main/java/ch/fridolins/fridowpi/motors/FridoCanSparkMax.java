package ch.fridolins.fridowpi.motors;

import java.util.Collection;
import java.util.Optional;

import ch.fridolins.fridowpi.motors.utils.PidValues;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxLimitSwitch;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.SparkMaxRelativeEncoder.Type;
import ch.fridolins.fridowpi.module.Module;
import ch.fridolins.fridowpi.module.IModule;

public class FridoCanSparkMax extends CANSparkMax implements PIDController, LimitSwitchController, FeedBackDevice, IModule, FridolinsMotor {
    
    private IModule moduleProxy = new Module();
    
    SparkMaxLimitSwitch forwardLimitSwitch;
    SparkMaxLimitSwitch reverseLimitSwitch;
    SparkMaxPIDController pidController;
    RelativeEncoder relativeEncoder;
    int ticksPerRotation;
    Optional<Integer> selectedPIDSlotIdx = Optional.empty();

    public FridoCanSparkMax(int deviceId, MotorType type) {
        super(deviceId, type);
    }

    @Override
    public void setPosition(double position) {
        if (this.pidController != null) {
            selectedPIDSlotIdx.ifPresentOrElse(
                    (slotIdx) -> pidController.setReference(position, ControlType.kPosition, slotIdx),
                    () -> pidController.setReference(position, ControlType.kPosition));
        } else {
            try {
                throw new Exception("PID Controller not initialized");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setVelocity(double velocity) {
        if (this.pidController != null) {
            selectedPIDSlotIdx.ifPresentOrElse(
                    (slotIdx) -> pidController.setReference(velocity, ControlType.kVelocity, slotIdx),
                    () -> pidController.setReference(velocity, ControlType.kVelocity));
        } else {
            try {
                throw new Exception("PID Controller not initialized");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private SparkMaxLimitSwitch.Type convertFromFridoLimitSwitchPolarity(LimitSwitchController.LimitSwitchPolarity polarity) {
        switch (polarity) {
            case kNormallyOpen:
                return SparkMaxLimitSwitch.Type.kNormallyOpen;
            case kNormallyClosed:
                return SparkMaxLimitSwitch.Type.kNormallyClosed;
            default:
                return SparkMaxLimitSwitch.Type.kNormallyOpen;
        }
    }

    private SparkMaxLimitSwitch getForwardLimitSwitchInstance() {
        return forwardLimitSwitch;
    }

    private SparkMaxLimitSwitch getForwardLimitSwitchInstance(LimitSwitchController.LimitSwitchPolarity polarity) {
        if (forwardLimitSwitch == null) {
            forwardLimitSwitch = super.getForwardLimitSwitch(convertFromFridoLimitSwitchPolarity(polarity));
        }
        return forwardLimitSwitch;
    }

    private SparkMaxLimitSwitch getReverseLimitSwitchInstance() {
        return reverseLimitSwitch;
    }

    private SparkMaxLimitSwitch getReverselimitSwitchInstance(LimitSwitchController.LimitSwitchPolarity polarity) {
        if (reverseLimitSwitch == null) {
            reverseLimitSwitch = super.getReverseLimitSwitch(convertFromFridoLimitSwitchPolarity(polarity));
        }
        return reverseLimitSwitch;
    }

    @Override
    public void enableForwardLimitSwitch(LimitSwitchController.LimitSwitchPolarity polarity, boolean enable) {
        getForwardLimitSwitchInstance(polarity).enableLimitSwitch(enable);
    }

    @Override
    public void enableReverseLimitSwitch(LimitSwitchController.LimitSwitchPolarity polarity, boolean enable) {
        getReverselimitSwitchInstance(polarity).enableLimitSwitch(enable);
    }

    @Override
    public boolean isForwardLimitSwitchActive() {
        return getForwardLimitSwitchInstance().isPressed();
    }

    @Override
    public boolean isReverseLimitSwitchActive() {
        return getReverseLimitSwitchInstance().isPressed();
    }

    private IdleMode convertFromFridoIdleModeType(FridoIdleMode idleModeType) {
        switch (idleModeType) {
            case kBrake:
                return IdleMode.kBrake;
            case kCoast:
                return IdleMode.kCoast;
            default:
                return IdleMode.kBrake;
        }
    }

    @Override
    public void setIdleMode(FridolinsMotor.FridoIdleMode type) {
        this.setIdleMode(convertFromFridoIdleModeType(type));
    }

    private boolean convertFromFridoDirectionsType(FridolinsMotor.DirectionType direction) {
        switch (direction) {
            case followMaster:
                return false;
            case invertMaster:
                return true;
            default:
                return false;
        }
    }

    @Override
    public void follow(FridolinsMotor master, FridolinsMotor.DirectionType direction) {
        if (master instanceof FridoCanSparkMax) {
            super.follow((FridoCanSparkMax) master, convertFromFridoDirectionsType(direction));
        }
    }

    @Override
    public void stopMotor() {
        super.stopMotor();
    }

    @Override
    public void setInverted(boolean inverted) {
        super.setInverted(inverted);
    }

    @Override
    public void setEncoderDirection(boolean inverted) {
        if (relativeEncoder == null)
            throw new Error("configEncoder must be called before using the setEncoderDirection function");
        this.relativeEncoder.setInverted(inverted);
    }

    @Override
    public void setEncoderPosition(double position) {
        this.relativeEncoder.setPosition(position);
    }

    @Override
    public double getEncoderTicks() {
        return this.relativeEncoder.getPosition();
    }

    @Override
    public double getEncoderVelocity() {
        return this.relativeEncoder.getVelocity();
    }

    @Override
    public void factoryDefault() {
        super.restoreFactoryDefaults();
    }

    @Override
    public void configEncoder(FridoFeedBackDevice device, int countsPerRev) {
        this.relativeEncoder = super.getEncoder(Type.kQuadrature, countsPerRev);
        this.relativeEncoder.setPositionConversionFactor(countsPerRev);
    }

    public void selectBuiltinFeedbackSensor() {
        this.relativeEncoder = super.getEncoder();
        super.getEncoder().setPositionConversionFactor(0);
    }

    @Override
    public void configOpenLoopRamp(double rate) {
        super.setOpenLoopRampRate(rate);
    }

    private void setMotionMagicParametersIfNecessary(PidValues pidValues) {
        if (pidValues.cruiseVelocity.isPresent() || pidValues.acceleration.isPresent())
            assert pidValues.slotIdX.isPresent() : "To set cruiseVelocity or acceleration slotIdx needs to be set";
        pidValues.cruiseVelocity.ifPresent((cruiseVelocity) -> this.pidController
                .setSmartMotionMaxVelocity(cruiseVelocity, pidValues.slotIdX.get()));
        pidValues.acceleration.ifPresent(
                (acceleration) -> this.pidController.setSmartMotionMaxAccel(acceleration, pidValues.slotIdX.get()));
    }

    @Override
    public void setPID(PidValues pidValues) {
        this.pidController = super.getPIDController();
        pidValues.slotIdX.ifPresentOrElse((slotIdx) -> setPIDWithSlotIdx(pidValues),
                () -> setPIDWithOutSlotIdx(pidValues));

        setMotionMagicParametersIfNecessary(pidValues);
    }

    private void setPIDWithOutSlotIdx(PidValues pidValues) {
        this.pidController.setP(pidValues.kP);
        this.pidController.setI(pidValues.kI);
        this.pidController.setD(pidValues.kD);
        pidValues.kF.ifPresent((kF) -> this.pidController.setFF(kF));
        this.pidController.setOutputRange(pidValues.peakOutputReverse, pidValues.peakOutputForward);
    }

    private void setPIDWithSlotIdx(PidValues pidValues) {
        this.pidController.setP(pidValues.kP, pidValues.slotIdX.get());
        this.pidController.setI(pidValues.kI, pidValues.slotIdX.get());
        this.pidController.setD(pidValues.kD, pidValues.slotIdX.get());
        pidValues.kF.ifPresent((kF) -> this.pidController.setFF(kF, pidValues.slotIdX.get()));
        this.pidController.setOutputRange(pidValues.peakOutputReverse, pidValues.peakOutputForward,
                pidValues.slotIdX.get());
    }

    @Override
    public void selectPidSlot(int slotIndex) {
        selectedPIDSlotIdx = Optional.of(slotIndex);
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