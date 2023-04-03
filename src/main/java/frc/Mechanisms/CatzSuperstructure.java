package frc.Mechanisms;

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
