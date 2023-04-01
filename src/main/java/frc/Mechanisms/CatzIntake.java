package frc.Mechanisms;

import java.util.concurrent.locks.ReentrantLock;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.ControllerInput.ControllerMapManager;
import frc.ControllerInput.Actions.TriggerAction;
import frc.Datalogger.CatzLog;
import frc.Datalogger.DataCollection.logID;
import frc.Utils.IPos;
import frc.Utils.PositionControlledMotor;
import frc.Utils.IPos.General;
import frc.Utils.IPos.IntakePosID;
import frc.robot.Robot;

@SuppressWarnings("unused")
public class CatzIntake extends AbstractMechanism {
    private static CatzIntake instance = null;

    private final int           CURRENT_LIMIT_AMPS            = 55;
    private final int           CURRENT_LIMIT_TRIGGER_AMPS    = 55;
    private final double        CURRENT_LIMIT_TIMEOUT_SECONDS = 0.5;
    private final int           THREAD_PERIOD_MS              = 20;
    private final boolean       ENABLE_CURRENT_LIMIT          = true;

    public static double MAX_SPEED         = 1.0; // dummy
    public static double DECEL             = 1.0; // dummy
    public static double kP                = 0.05; // dummy
    public static double kV                = 0.05; // dummy
    public static double MANUAL_EXT_POWER  = 0.5;
    public static double DEADBAND_RADIUS   = 2.0;
    public static double UNIT_TO_ENC       = 1.0; // dummy
    public static int    THREAD_PERIOD     = 20;
    
    public static int WRIST_CAN_ID = 0; // dummy
    public static logID MECH_ID = logID.INTAKE;
    public PositionControlledMotor wrist;

    private final int ROLLER_MOTOR_CAN_ID = 0;

    private CatzLog data;

    public volatile IntakePosID targetPos = IntakePosID.STW;
    public IPos currentPos                = General.NULL;

    private WPI_TalonFX rollerMotor;
    private SupplyCurrentLimitConfiguration rollerMotorCurrentLimit;

    public CatzIntake(){
        super(THREAD_PERIOD, MECH_ID);

        wrist = new PositionControlledMotor(
            "wrist",
            WRIST_CAN_ID, 
            DEADBAND_RADIUS,
            MAX_SPEED, 
            DECEL, 
            kP,
            kV, 
            UNIT_TO_ENC,
            IntakePosID.STW
        );

        rollerMotorCurrentLimit = new SupplyCurrentLimitConfiguration(ENABLE_CURRENT_LIMIT, CURRENT_LIMIT_AMPS, CURRENT_LIMIT_TRIGGER_AMPS, CURRENT_LIMIT_TIMEOUT_SECONDS);

        rollerMotor = new WPI_TalonFX(ROLLER_MOTOR_CAN_ID);
        rollerMotor.configFactoryDefault();
        rollerMotor.configSupplyCurrentLimit(rollerMotorCurrentLimit);

        rollerMotor.setSelectedSensorPosition(0.0);
        rollerMotor.setNeutralMode(NeutralMode.Brake);
    }
    
    public void setPos(IntakePosID posId){
        wrist.motorSetPos(posId);
    }
    
    public void runRollers(double power){
        rollerMotor.set(ControlMode.PercentOutput, power);
    }

    public static CatzIntake getInstance()
    {
        if(instance == null)
        {
            instance = new CatzIntake();
        }

        return instance;
    }

    @Override
    public void collectData(){
        data = wrist.collectMotorData();
        data.robotData8 = rollerMotor.getSelectedSensorVelocity();

        Robot.dataCollection.logData.add(data);
    }
    
    @Override
    public void update(){}

    @Override
    public void smartDashboard(){
        SmartDashboard.putNumber("Intake Roller Velocity", rollerMotor.getSelectedSensorVelocity());
        wrist.updateMotorSmartDashboard();
    }
    
    @Override
    public void smartDashboard_DEBUG(){
        wrist.updateMotorSmartDashboard_DEBUG();
    }

    @Override
    public void registerDriveAction() {
        ControllerMapManager.getInstance().addControllerAction(new TriggerAction(()->{return Robot.xbox.getAButtonPressed();}, ()->setPos(IntakePosID.POS1)));        
    }
}