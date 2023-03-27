package frc.Mechanisms;

import java.util.concurrent.locks.ReentrantLock;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.ControllerInput.ControllerMapManager;
import frc.ControllerInput.Actions.TriggerAction;
import frc.Mechanisms.IPos.ElbowPosID;
import frc.Mechanisms.IPos.General;
import frc.Mechanisms.IPos.IntakePosID;
import frc.Mechanisms.IPos.WristPosID;
import frc.robot.Robot;

@SuppressWarnings("unused")
public class CatzIntake extends AbstractMechanism {
    private static CatzIntake instance = null;

    private final int           CURRENT_LIMIT_AMPS            = 55;
    private final int           CURRENT_LIMIT_TRIGGER_AMPS    = 55;
    private final double        CURRENT_LIMIT_TIMEOUT_SECONDS = 0.5;
    private final int           THREAD_PERIOD_MS              = 100;
    private final boolean       ENABLE_CURRENT_LIMIT          = true;
    private static final int    INTAKE_THREAD_PERIOD_MS       = 20;

    private final int ROLLER_MOTOR_CAN_ID = 0;

    public volatile IntakePosID targetPos = IntakePosID.STW;
    public IPos currentPos                = General.NULL;

    public CatzIntakeElbow elbow;
    public CatzIntakeWrist wrist;

    private WPI_TalonFX rollerMotor;
    private SupplyCurrentLimitConfiguration rollerMotorCurrentLimit;

    public CatzIntake(){
        super(INTAKE_THREAD_PERIOD_MS);

        elbow = new CatzIntakeElbow();
        wrist = new CatzIntakeWrist();

        rollerMotorCurrentLimit = new SupplyCurrentLimitConfiguration(ENABLE_CURRENT_LIMIT, CURRENT_LIMIT_AMPS, CURRENT_LIMIT_TRIGGER_AMPS, CURRENT_LIMIT_TIMEOUT_SECONDS);

        rollerMotor = new WPI_TalonFX(ROLLER_MOTOR_CAN_ID);
        rollerMotor.configFactoryDefault();
        rollerMotor.configSupplyCurrentLimit(rollerMotorCurrentLimit);

        rollerMotor.setSelectedSensorPosition(0.0);
        rollerMotor.setNeutralMode(NeutralMode.Brake);

        start();
    }
    
    public void setPos(IntakePosID posId){
        targetPos = posId;
        elbow.setPos(targetPos.elbowPosID);
        wrist.setPos(targetPos.wristPosID);
    }
    
    public void runRollers(double power){
        rollerMotor.set(ControlMode.PercentOutput, power);
    }
    
    @Override
    public void update(){
        if(elbow.motor.getCurrentPos() != General.NULL && wrist.motor.getCurrentPos() != General.NULL){
            currentPos = targetPos;
        }
        else{
            currentPos = General.NULL;
        }
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
    public void smartDashboard(){
        SmartDashboard.putString("Intake Current Position", currentPos.getName());
    }
    
    @Override
    public void smartDashboard_DEBUG(){
        SmartDashboard.putString("Intake Target Position", targetPos.name);
    }

    @Override
    public void registerDriveAction() {
        ControllerMapManager.getInstance().addControllerAction(new TriggerAction(()->{return Robot.xbox.getAButtonPressed();}, ()->setPos(IntakePosID.POS1)));        
    }

    private class CatzIntakeElbow extends AbstractMechanism{ 
        private final double MAX_POWER         = 1.0;
        private final double MIN_POWER         = 0.05;
        private final double DECEL_DIST        = 20;
        private final double MANUAL_EXT_POWER  = 0.5;
        private final double DEADBAND_RADIUS   = 2.0;
        private final double UNIT_TO_ENC       = 1.0; // duprivate
        private final int ELBOW_CAN_ID = 0; // dummy

        private PositionControlledMotor motor;

        public CatzIntakeElbow(){
            super(THREAD_PERIOD_MS);

            motor = new PositionControlledMotor(
                ELBOW_CAN_ID, 
                MAX_POWER, 
                MIN_POWER, 
                DECEL_DIST, 
                DEADBAND_RADIUS, 
                UNIT_TO_ENC,
                ElbowPosID.STW
            );

            start();
        }

        public void setPos(ElbowPosID pos){
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
        public void smartDashboard(){
            SmartDashboard.putString("Elbow Current Position", motor.getCurrentPos().getName());
        }

        @Override
        public void smartDashboard_DEBUG(){
            SmartDashboard.putString("Elbow Target Position", motor.getTargetPos().getName());
            SmartDashboard.putNumber("Elbow Encoder Position", motor.currentPosEnc);
            SmartDashboard.putNumber("Elbow Target Power", motor.targetPower);
            SmartDashboard.putNumber("Elbow Distance Remaining", motor.distanceRemaining);
        }

        @Override
        public void registerDriveAction() {
            // TODO Auto-generated method stub
            
        }
    }

    private class CatzIntakeWrist extends AbstractMechanism{ 
        private final double MAX_POWER         = 1.0;
        private final double MIN_POWER         = 0.05;
        private final double DECEL_DIST        = 20;
        private final double MANUAL_EXT_POWER  = 0.5;
        private final double DEADBAND_RADIUS   = 2.0;
        private final double UNIT_TO_ENC       = 1.0; // dummy
        
        private final int WRIST_CAN_ID = 0; // dummy
        private PositionControlledMotor motor;

        public CatzIntakeWrist(){
            super(THREAD_PERIOD_MS);

            motor = new PositionControlledMotor(
                WRIST_CAN_ID, 
                MAX_POWER, 
                MIN_POWER, 
                DECEL_DIST, 
                DEADBAND_RADIUS, 
                UNIT_TO_ENC,
                WristPosID.STW
            );

            start();
        }

        public void setPos(WristPosID pos){
            motor.motorSetPos(pos);
        }

        public void setMotorManual(double direction){
            motor.motorManual(direction);
        }

        @Override
        public void smartDashboard(){
            SmartDashboard.putString("Wrist Current Position", motor.getCurrentPos().getName());
        }
        
        @Override
        public void smartDashboard_DEBUG(){
            SmartDashboard.putString("Wrist Target Position", motor.getTargetPos().getName());
            SmartDashboard.putNumber("Wrist Encoder Position", motor.currentPosEnc);
            SmartDashboard.putNumber("Wrist Target Power", motor.targetPower);
            SmartDashboard.putNumber("Wrist Distance Remaining", motor.distanceRemaining);
        }
        
        @Override
        public void update(){
            motor.updateMotor();
        }

        @Override
        public void registerDriveAction() {
            // TODO Auto-generated method stub
            
        }
    }
}