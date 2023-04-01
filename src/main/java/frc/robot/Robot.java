package frc.robot;

import java.util.ArrayList;

import javax.xml.crypto.Data;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Timer;
import frc.ControllerInput.ControllerMapManager;
import frc.Datalogger.CatzLog;
import frc.Datalogger.DataCollection;
import frc.Mechanisms.CatzArm;
import frc.Mechanisms.CatzDrivetrain;
import frc.Mechanisms.CatzElevator;
import frc.Mechanisms.CatzIntake;
import frc.Mechanisms.MechanismManager;

@SuppressWarnings("unused")
public class Robot extends TimedRobot 
{
  private final int XBOX_PORT = 0;
  public static XboxController xbox;

  private MechanismManager mechanismManager = MechanismManager.getInstance();
  private ControllerMapManager controllerMapManager = ControllerMapManager.getInstance();

  public static DataCollection   dataCollection   = DataCollection.getInstance();
  private       CatzIntake       intake           = CatzIntake.getInstance();
  private       CatzArm          arm              = CatzArm.getInstance();
  private       CatzElevator     elevator         = CatzElevator.getInstance();
  private       CatzDrivetrain   drivetrain       = CatzDrivetrain.getInstance();

  public static AHRS navX;

  public ArrayList<CatzLog> dataArrayList;

  @Override
  public void robotInit()
  {
    navX = new AHRS();
    navX.reset();

    xbox = new XboxController(XBOX_PORT);

    mechanismManager.setMechanisms(drivetrain, intake, arm, elevator);
    mechanismManager.startMechanisms(mechanismManager.getAllMechanisms());
    mechanismManager.registerDriverActions();
  }
  
  @Override
  public void robotPeriodic()
  {
    mechanismManager.updateSmartDashboard();
    mechanismManager.updateSmartDashboard_DEBUG(drivetrain, intake, arm, elevator);
  }

  @Override
  public void autonomousInit(){
    dataCollection.updateLogDataID();
  }
  
  @Override
  public void teleopInit(){
    dataCollection.updateLogDataID();
  }
  
  @Override
  public void teleopPeriodic()
  {
    controllerMapManager.handleControllerActions();
  }

  @Override
  public void disabledInit(){
    mechanismManager.stopMechanisms(mechanismManager.getAllMechanisms());
    drivetrain.setCoastMode();
    
    if(dataCollection.logDataValues == true)
    {
      dataCollection.stopDataCollection();

      try 
      {
        dataCollection.exportData(dataArrayList);
      } 
      catch (Exception e) 
      {
        e.printStackTrace();
      }
    }
  }
}