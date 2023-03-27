package frc.robot;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import frc.ControllerInput.ControllerMapManager;
import frc.Mechanisms.CatzArm;
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

  private CatzIntake       intake           = CatzIntake.getInstance();
  private CatzArm          arm              = CatzArm.getInstance();
  private CatzElevator     elevator         = CatzElevator.getInstance();

  public static AHRS navX;

  @Override
  public void robotInit()
  {
    navX = new AHRS();
    navX.reset();

    xbox = new XboxController(XBOX_PORT);

    mechanismManager.setMechanisms(intake, arm, elevator);
    mechanismManager.registerDriverActions();
  }

  @Override
  public void robotPeriodic()
  {
    mechanismManager.updateSmartDashboard();
    mechanismManager.updateSmartDashboard_DEBUG(intake, arm, elevator);
  }

  @Override
  public void teleopPeriodic()
  {
    controllerMapManager.handleControllerActions();
  }
}