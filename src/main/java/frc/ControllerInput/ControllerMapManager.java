package frc.ControllerInput;

import java.util.ArrayList;
import java.util.List;

import frc.ControllerInput.Actions.Action;

public class ControllerMapManager {
    private static ControllerMapManager instance = null;

    private List<Action> controllerActionList = new ArrayList<>();

    public void addControllerAction(Action action)
    {
        synchronized(controllerActionList)
        {
            controllerActionList.add(action);
        }
    }

    public void handleControllerActions()
    {
        synchronized(controllerActionList)
        {
            for(Action action : controllerActionList)
            {
                action.run();
            }
        }
    }

    public static ControllerMapManager getInstance()
    {
        if(instance == null)
        {
            instance = new ControllerMapManager();
        }

        return instance;
    }
}
