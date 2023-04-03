package frc.Datalogger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CatzLog 
{
    
    //public int robotDataType;
    private double robotTime;
    private ArrayList<Double> datas = new ArrayList<>();

    public CatzLog(double time, double... datas)
    {
        robotTime  = time;
    }
  
    @Override
    public String toString()
    {
    
        return robotTime +", ";
        
        // + robotData1 + ", " + robotData2 + ", " + robotData3 + ", " + robotData4 + ", " + robotData5 + ", " 
        //                     + robotData6 + ", " + robotData7 + ", " + robotData8 + ", " + robotData9 + ", " + robotData10 + ","
        //                     + robotData11 + "," + robotData12 + ", " + robotData13+","  + robotData14+  ","
        //                     + ((robotData15 & DataCollection.shift0))      +  "," + ((robotData15 & DataCollection.shift1) >> 1) +  ","
        //                     + ((robotData15 & DataCollection.shift2) >> 2) +  "," + ((robotData15 & DataCollection.shift3) >> 3) +  ","
        //                     + ((robotData15 & DataCollection.shift0) >> 4) +  "," + ((robotData15 & DataCollection.shift1) >> 5) +  ","
        //                     + ((robotData15 & DataCollection.shift2) >> 6) +  "," + ((robotData15 & DataCollection.shift3) >> 7);
    }
}