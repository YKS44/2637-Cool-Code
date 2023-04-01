package frc.Mechanisms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MechanismManager {
    private static MechanismManager instance = null;

    private List<AbstractMechanism> allMechanisms = new ArrayList<>();
    private List<AbstractMechanism> debugMechanisms = new ArrayList<>();

    public void setMechanisms(AbstractMechanism... mechanisms)
    {
        allMechanisms = Arrays.asList(mechanisms);
    }

    public AbstractMechanism[] getAllMechanisms(){
        return (AbstractMechanism[]) allMechanisms.toArray();
    }

    public void stopMechanisms(AbstractMechanism... mechanisms){
        Arrays.asList(mechanisms).forEach(AbstractMechanism::kill);
    }

    public void startMechanisms(AbstractMechanism... mechanisms){
        Arrays.asList(mechanisms).forEach(AbstractMechanism::start);
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