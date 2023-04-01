package frc.Mechanisms;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.fasterxml.jackson.databind.ser.std.StdArraySerializers.IntArraySerializer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.concurrent.locks.ReentrantLock;

import frc.Datalogger.CatzLog;
import frc.Datalogger.DataCollection;
import frc.Datalogger.DataCollection.logID;
import frc.Utils.CatzMathUtils;
import frc.Utils.PositionControlledMotor;
import frc.Utils.IPos.ArmPosID;
import frc.robot.Robot;

@SuppressWarnings("unused")
public class CatzArm extends AbstractMechanism{ 
    private static CatzArm instance = null;

    public static double MAX_SPEED         = 1.0; // dummy
    public static double DECEL             = 1.0; // dummy
    public static double kP                = 0.05; // dummy
    public static double kV                = 0.05; // dummy
    public static double MANUAL_EXT_POWER  = 0.5;
    public static double DEADBAND_RADIUS   = 2.0;
    public static double UNIT_TO_ENC       = 1.0; // dummy
    public static int    THREAD_PERIOD     = 20;
    
    public static int ARM_CAN_ID = 0; // dummy
    public static logID MECH_ID = logID.ARM;
    public PositionControlledMotor motor;

    public CatzArm(){
        super(THREAD_PERIOD, MECH_ID);

        motor = new PositionControlledMotor(
            "Arm",
            ARM_CAN_ID, 
            DEADBAND_RADIUS,
            MAX_SPEED, 
            DECEL, 
            kP,
            kV, 
            UNIT_TO_ENC,
            ArmPosID.STW
        );
    }

    public void setPos(ArmPosID pos){
        motor.motorSetPos(pos);
    }

    public void setMotorManual(double direction){
        motor.motorManual(direction);
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
    public void collectData(){
        Robot.dataCollection.logData.add(motor.collectMotorData());
    }

    @Override
    public void update(){
        motor.updateMotor();
    }

    @Override
    public void registerDriveAction()
    {
        //register drive action
    }

    @Override
    public void smartDashboard(){
        motor.updateMotorSmartDashboard();
    }
    
    @Override
    public void smartDashboard_DEBUG(){
        motor.updateMotorSmartDashboard_DEBUG();
    }
}