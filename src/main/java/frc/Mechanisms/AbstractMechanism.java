package frc.Mechanisms;

import frc.Datalogger.DataCollection.logID;
import frc.robot.Robot;
import edu.wpi.first.wpilibj.Timer;

public abstract class AbstractMechanism implements Runnable{

    private boolean runThread;

    private Thread thread;
    public  Timer mechTime;
    private int threadPeriod;
    private logID dataCollectionID;

    public AbstractMechanism(int threadPeriod, logID dataCollectionID){
        this.threadPeriod = threadPeriod;
        this.dataCollectionID = dataCollectionID;
        mechTime = new Timer();

        if(thread == null){
            thread = new Thread(this);
            runThread = false;
            thread.start();
        }
    }

    public void start(){
        mechTime.reset();
        mechTime.start();
        runThread = true;
    }

    public void kill(){
        runThread = false;
        mechTime.stop();
        System.out.println("Thread Killed");
    }

    public abstract void update();
    public abstract void collectData();
    public abstract void smartDashboard();
    public abstract void smartDashboard_DEBUG();
    public abstract void registerDriveAction();

    @Override
    public void run(){
        while(runThread == true){
            update();
            if(Robot.dataCollection.chosenDataID.getSelected() == dataCollectionID){
                collectData();
            }
            
            try
            {
                Thread.sleep(threadPeriod);
            }
            catch(InterruptedException e)
            {
                System.out.println("Interrupted: " + e.getMessage());   
            }
        }
    }
}