package frc.Utils;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Timer;
import frc.Datalogger.CatzLog;
import frc.Utils.IPos.General;

public class PositionControlledMotor{
    private final int     CURRENT_LIMIT_AMPS            = 55;
    private final int     CURRENT_LIMIT_TRIGGER_AMPS    = 55;
    private final double  CURRENT_LIMIT_TIMEOUT_SECONDS = 0.5;
    private final boolean ENABLE_CURRENT_LIMIT          = true;

    private final String MOTOR_NAME;
    private final double MAX_SPEED;
    private final double DECEL;
    private final double MANUAL_EXT_POWER = 0.5;
    private final double UNIT_TO_ENC;
    private final double DEADBAND_RADIUS;

    private double kP;
    private double kV;

    public  volatile IPos finalPos;
    private IPos currentPos = General.NULL;

    private double currentPosEnc;
    private double currentVelEnc;
    private double posError;
    private double velError;
    private double targetPos;
    private double targetVel;
    private double targetPower;
    private double time;
    
    private Timer timer;
    private MotorTrajectory trajectory;
    
    private WPI_TalonFX motor;
    private SupplyCurrentLimitConfiguration currentLimit;

    public PositionControlledMotor(String MOTOR_NAME, int MOTOR_CAN_ID, double DEADBAND_RADIUS, double MAX_SPEED, double DECEL, double kP, double kV, double UNIT_TO_ENC, IPos initPos){
        this.MOTOR_NAME = MOTOR_NAME;
        this.DEADBAND_RADIUS = DEADBAND_RADIUS;
        this.MAX_SPEED = MAX_SPEED;
        this.DECEL = DECEL;
        this.kP = kP;
        this.kV = kV;
        this.UNIT_TO_ENC = UNIT_TO_ENC;
        
        motorSetPos(initPos);

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
        time = Math.min(timer.get(), trajectory.getDuration());

        currentPosEnc = motor.getSelectedSensorPosition();
        currentVelEnc = motor.getSelectedSensorVelocity();

        targetPos = trajectory.getPosition(time);
        targetVel = trajectory.getVelocity(time);

        posError = targetPos - currentPosEnc / UNIT_TO_ENC;
        velError = targetVel - currentVelEnc / UNIT_TO_ENC;

        targetPower = CatzMathUtils.clamp(posError * kP + velError * kV, -1.0, 1.0);
        if(finalPos.getPos() - currentPosEnc / UNIT_TO_ENC < DEADBAND_RADIUS){
            targetPower = 0.0;
            currentPos = finalPos;
        }
        else{
            currentPos = IPos.General.NULL;
        }

        motor.set(ControlMode.PercentOutput, targetPower);
    }

    public CatzLog collectMotorData(){
        return new CatzLog(
            time, finalPos.getPos(), targetPower, currentPosEnc, posError, velError, targetPos, targetVel
        );
    }

    public void updateMotorSmartDashboard(){
        SmartDashboard.putString(MOTOR_NAME + " Current Position", currentPos.getName());
    }
    
    public void updateMotorSmartDashboard_DEBUG(){
        SmartDashboard.putString(MOTOR_NAME + " Final Position", finalPos.getName());
        SmartDashboard.putNumber(MOTOR_NAME + " Encoder Position", currentPosEnc);
        SmartDashboard.putNumber(MOTOR_NAME + " Position error", posError);
        SmartDashboard.putNumber(MOTOR_NAME + " Velocity error", velError);
        SmartDashboard.putNumber(MOTOR_NAME + " Position target", targetPos);
        SmartDashboard.putNumber(MOTOR_NAME + " Velocity target", targetVel);
    }
    
    public void motorManual(double direction){
        motor.set(ControlMode.PercentOutput, MANUAL_EXT_POWER * Math.signum(direction));
    }

    public void motorSetPos(IPos pos){
        double dist =  pos.getPos() - finalPos.getPos();

        timer.reset();
        timer.start();

        trajectory = new MotorTrajectory(finalPos.getPos(), pos.getPos(), MAX_SPEED * Math.signum(dist), - DECEL * Math.signum(dist));
        finalPos = pos;
    }
}