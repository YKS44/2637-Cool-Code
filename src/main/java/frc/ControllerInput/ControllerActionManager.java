package frc.ControllerInput;

import java.util.ArrayList;
import java.util.List;

import frc.ControllerInput.Actions.Action;

public class ControllerActionManager {
    private static ControllerActionManager instance = null;

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
            controllerActionList.forEach(Action::run);
        }
    }

    public static ControllerActionManager getInstance()
    {
        if(instance == null)
        {
            instance = new ControllerActionManager();
        }

        return instance;
    }
}
