package frc.Mechanisms;

/**
 * Class that handles states and movement of the intake, arm, and the elevator.
 */
public class CatzSuperstructure {
    private static CatzSuperstructure instance = null;

    private CatzIntake intake     = CatzIntake.getInstance();
    private CatzArm    arm        = CatzArm.getInstance();
    private CatzElevator elevator = CatzElevator.getInstance();

    public static CatzSuperstructure getInstance()
    {
        if(instance == null)
        {
            instance = new CatzSuperstructure();
        }
        return instance;
    }
}
