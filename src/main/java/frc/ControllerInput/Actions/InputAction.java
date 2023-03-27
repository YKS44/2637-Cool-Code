package frc.ControllerInput.Actions;

import java.util.function.Consumer;

import edu.wpi.first.wpilibj.XboxController;
import frc.robot.Robot;


public class InputAction extends Action{
    private Consumer<XboxController> inputController;

    public InputAction(Consumer<XboxController> xboxController)
    {
        inputController = xboxController;
    }

    @Override
    public void run() {
        inputController.accept(Robot.xbox);
    }
    
}
