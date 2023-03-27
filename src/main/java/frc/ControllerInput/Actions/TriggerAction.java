package frc.ControllerInput.Actions;

import java.util.function.BooleanSupplier;

/**
 * Only executes the action when the trigger returns true
 */
public class TriggerAction extends Action{
    private BooleanSupplier trigger;
    private SimpleAction action;

    public TriggerAction(BooleanSupplier trigger, SimpleAction action)
    {
        this.trigger = trigger;
        this.action = action;
    }

    @Override
    public void run()
    {
        if(trigger.getAsBoolean() == true)
        {
            action.execute();
        }
    }
}