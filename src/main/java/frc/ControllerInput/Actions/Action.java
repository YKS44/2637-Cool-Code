package frc.ControllerInput.Actions;


/**
 * A base class for all other actions to extend
 */
public abstract class Action {
    public abstract void run();

    public interface SimpleAction
    {
        void execute();
    }
}