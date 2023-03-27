package frc.Mechanisms;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.fasterxml.jackson.databind.ser.std.StdArraySerializers.IntArraySerializer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.concurrent.locks.ReentrantLock;

import frc.Mechanisms.IPos.ArmPosID;
import frc.Utils.CatzMathUtils;

@SuppressWarnings("unused")
public class CatzArm extends AbstractMechanism{ 
    private static CatzArm instance = null;

    public static double MAX_POWER         = 1.0;
    public static double MIN_POWER         = 0.05;
    public static double DECEL_DIST        = 20;
    public static double MANUAL_EXT_POWER  = 0.5;
    public static double DEADBAND_RADIUS   = 2.0;
    public static double UNIT_TO_ENC       = 1.0; // dummy
    public static int    THREAD_PERIOD     = 100;
    
    public static int ARM_CAN_ID = 0; // dummy
    public PositionControlledMotor motor;

    public CatzArm(){
        super(THREAD_PERIOD);

        motor = new PositionControlledMotor(
            ARM_CAN_ID, 
            MAX_POWER, 
            MIN_POWER, 
            DECEL_DIST, 
            DEADBAND_RADIUS, 
            UNIT_TO_ENC,
            ArmPosID.STW
        );

        start();
    }

    public void setPos(ArmPosID pos){
        motor.motorSetPos(pos);
    }

    public void setMotorManual(double direction){
        motor.motorManual(direction);
    }

    @Override
    public void update(){
        motor.updateMotor();
    }

    @Override
    public void registerDriveAction()
    {

    }

    public static CatzArm getInstance()
    {
        if(instance == null)
        {
            instance = new CatzArm();
        }

        return instance;
    }

    @Override
    public void smartDashboard(){
        SmartDashboard.putString("Arm Current Position", motor.getCurrentPos().getName());
    }
    
    @Override
    public void smartDashboard_DEBUG(){
        SmartDashboard.putString("Arm Target Position", motor.getTargetPos().getName());
        SmartDashboard.putNumber("Arm Encoder Position", motor.currentPosEnc);
        SmartDashboard.putNumber("Arm Target Power", motor.targetPower);
        SmartDashboard.putNumber("Arm Distance Remaining", motor.distanceRemaining);
    }
}