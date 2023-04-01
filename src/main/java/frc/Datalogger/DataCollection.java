package frc.Datalogger;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.String;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SuppressWarnings("unused")
public class DataCollection 
{	
    public enum logID{
        NONE(0, "none", ""),
        SWERVE(1, "swerve", ""),
        SWERVE_DRIVING(2, "swerve driving", "time,target,lf-angle,lf-dist,lf-vel,lb-angle,lb-dist,lb-vel,rf-angle,rf-dist,rf-vel,rb-angle,rb-dist,rb-vel,lf-error"),
        SWERVE_STEERING(3, "swerve stering", "time,target,lf-angle,lf-err,lf-flip-err,lb-angle,lb-err,lb-flip-err,rf-angle,rf-err,rf-flip-err,rb-angle,rb-err,rb-flip-err"),
        BALANCE(4, "balance", "time,pitch,rate,power,pitchTerm,rateTerm,"),
        //dummy headers
        INTAKE(5, "intake", ""),
        ARM(6, "arm", ""),
        ELEVATOR(7, "elevator", "");        

        public final int id;
        public final String name;
        public final String header;

        private logID(int id, String name, String header){
            this.id = id;
            this.name = name;
            this.header = header;
        }
    }

    private static DataCollection instance = null;

    Date date;	
    SimpleDateFormat sdf;
    String dateFormatted;

    public boolean fileNotAppended = false;

    //decide the location and extender
    public final String logDataFilePath = "//media//sda1//RobotData";
    public final String logDataFileType = ".csv";

    public boolean logDataValues = false;   
    public static logID     logDataID;
    public ArrayList<CatzLog> logData;

    public final SendableChooser<logID> chosenDataID = new SendableChooser<>();

    public static int boolData = 0;

    public static final int shift0 = 1 << 0;
    public static final int shift1 = 1 << 1;
    public static final int shift2 = 1 << 2;
    public static final int shift3 = 1 << 3;
    public static final int shift4 = 1 << 4;
    public static final int shift5 = 1 << 5;
    public static final int shift6 = 1 << 6;
    public static final int shift7 = 1 << 7;

    public DataCollection()
    {
        dataCollectionShuffleboard();
    }

    public void updateLogDataID()
    {
        if(chosenDataID.getSelected() == logID.NONE)
        {
            stopDataCollection();
        }
        else
        {
            startDataCollection();
        }
        setLogDataID(chosenDataID.getSelected());
    }

    public void setLogDataID(final logID dataID)
    {
        logDataID = dataID;
    }
    
    public void dataCollectionInit(final ArrayList<CatzLog> list)
    {   
        date = Calendar.getInstance().getTime();
        sdf = new SimpleDateFormat("yyyy-MM-dd kk.mm.ss");	
        dateFormatted = sdf.format(date);

        logData = list;
    }

    /*-----------------------------------------------------------------------------------------
    *  Initialize drop down menu for data collection on Shuffleboard
    *----------------------------------------------------------------------------------------*/
    public void dataCollectionShuffleboard()
    {
        chosenDataID.setDefaultOption(logID.NONE.name, logID.NONE);
        
        chosenDataID.addOption(logID.SWERVE_STEERING.name, logID.SWERVE_STEERING);
        chosenDataID.addOption(logID.SWERVE_DRIVING.name, logID.SWERVE_DRIVING);
        chosenDataID.addOption(logID.BALANCE.name, logID.BALANCE);
        chosenDataID.addOption(logID.INTAKE.name, logID.INTAKE);
        chosenDataID.addOption(logID.ELEVATOR.name, logID.ELEVATOR);
        chosenDataID.addOption(logID.ARM.name, logID.ARM);

        SmartDashboard.putData("Data Collection", chosenDataID);
    }

    public void startDataCollection() 
    {
        logDataValues = true;
    }

    public void stopDataCollection() 
    {
        logDataValues = false; 
    }

    public static void resetBooleanData()
    {
        boolData = 0;
    }

    public static void booleanDataLogging(boolean bool1, int bitPos)
    {
        if(bool1 == true)
        {
            boolData |= (1 << bitPos);
        }
    }
    
    public void writeHeader(PrintWriter pw) 
    {
        pw.printf(logDataID.name);
    }
    
    //create log file
    public String createFilePath()
    {
        String logDataFullFilePath = logDataFilePath + " " + logDataID.name + " " + dateFormatted +  logDataFileType;
    	return logDataFullFilePath;
    }

    // print out data after fully updated
    public void exportData(ArrayList<CatzLog> data) throws IOException
    {   
        System.out.println("Export Data ///////////////");    
        try (
            
        FileWriter     fw = new FileWriter(createFilePath(), fileNotAppended);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter    pw = new PrintWriter(bw))

        {
            writeHeader(pw);
            pw.print("\n");

            // loop through arraylist and adds it to the StringBuilder
            int dataSize = data.size();
            for (int i = 0; i < dataSize; i++)
            {
                pw.print(data.get(i).toString() + "\n");
                pw.flush();
            }

            pw.close();
        }
    }

    public static logID getLogDataID()
    {
        return logDataID;
    }

    public static DataCollection getInstance()
    {
        if(instance == null)
        {
            instance = new DataCollection();
        }

        return instance;
    }
}