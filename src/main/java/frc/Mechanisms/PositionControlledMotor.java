package frc.Mechanisms;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import frc.Mechanisms.IPos.General;
import frc.Utils.CatzMathUtils;

public class PositionControlledMotor{
    private final int     CURRENT_LIMIT_AMPS            = 55;
    private final int     CURRENT_LIMIT_TRIGGER_AMPS    = 55;
    private final double  CURRENT_LIMIT_TIMEOUT_SECONDS = 0.5;
    private final boolean ENABLE_CURRENT_LIMIT          = true;

    private final double MAX_POWER;
    private final double MIN_POWER;
    private final double POWER_GAIN_PER_ERROR;
    private final double MANUAL_EXT_POWER = 0.5;
    private final double DEADBAND_RADIUS;
    private final double UNIT_TO_ENC;

    private volatile IPos targetPos;
    private IPos currentPos = General.NULL;

    public double currentPosEnc;
    public double targetPower;
    public double distanceRemaining;
    
    private WPI_TalonFX motor;
    private SupplyCurrentLimitConfiguration currentLimit;

    public PositionControlledMotor(int MOTOR_CAN_ID, double MAX_POWER, double MIN_POWER, double DECEL_DIST, double DEADBAND_RADIUS, double UNIT_TO_ENC, IPos initPos){
        this.MAX_POWER = MAX_POWER;
        this.MIN_POWER = MIN_POWER;
        this.DEADBAND_RADIUS = DEADBAND_RADIUS;
        this.UNIT_TO_ENC = UNIT_TO_ENC;
        this.POWER_GAIN_PER_ERROR = MAX_POWER/DECEL_DIST;
        currentPos = initPos;

        currentLimit = new SupplyCurrentLimitConfiguration(
            ENABLE_CURRENT_LIMIT, 
            CURRENT_LIMIT_AMPS, 
            CURRENT_LIMIT_TRIGGER_AMPS, 
            CURRENT_LIMIT_TIMEOUT_SECONDS
        );

        motor = new WPI_TalonFX(MOTOR_CAN_ID);
        motor.configFactoryDefault();
        motor.configSupplyCurrentLimit(currentLimit);

        motor.setSelectedSensorPosition(0.0);
        motor.setNeutralMode(NeutralMode.Brake);
    }
    
    public void updateMotor(){
        currentPosEnc = motor.getSelectedSensorPosition();
        distanceRemaining = targetPos.getPos() - currentPosEnc / UNIT_TO_ENC;
        if(Math.abs(distanceRemaining) <= DEADBAND_RADIUS * UNIT_TO_ENC)
        {
            currentPos = targetPos;
            distanceRemaining = 0.0;
        }
        else{
            currentPos = General.NULL;
        }
        
        targetPower = CatzMathUtils.clamp(distanceRemaining * POWER_GAIN_PER_ERROR, MIN_POWER, MAX_POWER);
        motor.set(ControlMode.PercentOutput, targetPower);
    }
    
    public void motorManual(double direction){
        motor.set(ControlMode.PercentOutput, MANUAL_EXT_POWER * Math.signum(direction));
    }

    public void motorSetPos(IPos pos){
        targetPos = pos;
    }

    public IPos getCurrentPos()
    {
        return currentPos;
    }

    public IPos getTargetPos()
    {
        return targetPos;
    }
}