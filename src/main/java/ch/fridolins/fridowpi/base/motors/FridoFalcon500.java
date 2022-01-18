package ch.fridolins.fridowpi.base.motors;

import com.ctre.phoenix.motorcontrol.can.TalonFX;

import ch.fridolins.fridowpi.base.motors.utils.PidValues;
import ch.fridolins.fridowpi.module.Module;

public class FridoFalcon500 extends TalonFX implements PIDController, LimitSwitchController, FeedBackDevice {

    public FridoFalcon500(int deviceNumber) {
        super(deviceNumber);
        //TODO Auto-generated constructor stub
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
    public void enableForwardLimitSwitch(LimitSwitchPolarity polarity, boolean enable) {
        // TODO Auto-generated method stub
        
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
    public void setVelocity(double velocity) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setPosition(double position) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setPidValues(PidValues values) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void selectPidSlot(int slotIndex) {
        // TODO Auto-generated method stub
        
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
}