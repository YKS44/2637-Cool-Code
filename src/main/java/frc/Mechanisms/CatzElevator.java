package frc.Mechanisms;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.fasterxml.jackson.databind.ser.std.StdArraySerializers.IntArraySerializer;
import edu.wpi.first.wpilibj.DigitalInput;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.Mechanisms.IPos.ElevatorPosID;
import frc.Utils.CatzMathUtils;

@SuppressWarnings("unused")
public class CatzElevator extends AbstractMechanism{ 
    private static CatzElevator instance = null;

    public static double MAX_POWER         = 1.0;
    public static double MIN_POWER         = 0.05;
    public static double DECEL_DIST        = 20;
    public static double MANUAL_EXT_POWER  = 0.5;
    public static double DEADBAND_RADIUS   = 2.0;
    public static double UNIT_TO_ENC       = 1.0; // dummy
    public static int    THREAD_PERIOD     = 100;
    
    private final int MID_LIMIT_SWITCH_CHANNEL = 1;
    public static int ELEVATOR_CAN_ID = 0; // dummy

    public PositionControlledMotor motor;
    private DigitalInput midLimitSwitch;

    public CatzElevator(){
        super(THREAD_PERIOD);

        motor = new PositionControlledMotor(
            ELEVATOR_CAN_ID, 
            MAX_POWER, 
            MIN_POWER, 
            DECEL_DIST, 
            DEADBAND_RADIUS, 
            UNIT_TO_ENC,
            ElevatorPosID.STW
        );

        start();
    }

    public void setPos(ElevatorPosID pos){
        motor.motorSetPos(pos);
    }

    public void setMotorManual(double direction){
        motor.motorManual(direction);
    }

    @Override
    public void update(){
        motor.updateMotor();
        
        if(midLimitSwitch.get() && motor.getTargetPos() == ElevatorPosID.MID){
            motor.motorManual(0.0);
        }
    }

    public static CatzElevator getInstance()
    {
        if(instance == null)
        {
            instance = new CatzElevator();
        }

        return instance;
    }

    @Override
    public void smartDashboard(){
        SmartDashboard.putString("Elevator Current Position", motor.getCurrentPos().getName());
    }
    
    @Override
    public void smartDashboard_DEBUG(){
        SmartDashboard.putString("Elevator Target Position", motor.getTargetPos().getName());
        SmartDashboard.putNumber("Elevator Encoder Position", motor.currentPosEnc);
        SmartDashboard.putNumber("Elevator Target Power", motor.targetPower);
        SmartDashboard.putNumber("Elevator Distance Remaining", motor.distanceRemaining);
    }

    @Override
    public void registerDriveAction() {
        // TODO Auto-generated method stub
        
    }
}