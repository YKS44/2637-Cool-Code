package frc.Mechanisms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MechanismManager {
    private static MechanismManager instance = null;

    private List<AbstractMechanism> allMechanisms = new ArrayList<>();
    private List<AbstractMechanism> debugMechanisms = new ArrayList<>();


    /**
     * 
     * This method MUST be called before any others.
     * 
     */
    public void setMechanisms(AbstractMechanism... mechanisms)
    {
        allMechanisms = Arrays.asList(mechanisms);
    }

    public AbstractMechanism[] getAllMechanisms(){
        return allMechanisms.toArray(AbstractMechanism[]::new);
    }

    public void stopMechanisms(){
        allMechanisms.forEach(AbstractMechanism::kill);
    }

    public void startMechanisms(){
        allMechanisms.forEach(AbstractMechanism::start);
    }

    public void registerDriverActions()
    {
        allMechanisms.forEach(AbstractMechanism::registerDriveAction);
    }

    public void updateSmartDashboard()
    {
        allMechanisms.forEach(AbstractMechanism::smartDashboard);
    }

    /**
     * 
     * @param mechanisms Mechanisms that will be debugged.
     * 
     */
    public void updateSmartDashboard_DEBUG(AbstractMechanism... mechanisms)
    {
        if(mechanisms.length > 0 && debugMechanisms.isEmpty())
        {
            debugMechanisms = Arrays.asList(mechanisms);
        }

        debugMechanisms.forEach(AbstractMechanism::smartDashboard_DEBUG);
    }

    public static MechanismManager getInstance()
    {
        if(instance == null)
        {
            instance = new MechanismManager();
        }

        return instance;
    }
}