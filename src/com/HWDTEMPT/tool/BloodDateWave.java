package com.HWDTEMPT.tool;

import com.HWDTEMPT.model.StaticValue;

import java.util.ArrayList;
import java.util.List;

public class BloodDateWave {
    private static float getRawData;
    public static Object sync; 
    private static float scaleRate=1;
    public static String arraydata;
    private static float array[];
    private static float filtered;
   public static int arrayLength=1024;
   private static List<Float>Listdata=new ArrayList<Float>();
   
   static{
       sync = new Object();
       array = new float[arrayLength];
   }
   public static void initArray()
   {
        float f1 = 4.0F *StaticValue.surfaceviewWidth / arrayLength;
           float f2 = 0;
       array[0]=0;
       array[1]=0;
       int i=2;
       while(i<arrayLength-4)
       {
           f2=f2+f1;
           array[i]=f2;
           array[i+2]=f2;
           array[i+1]=0;
           array[i+3]=0;
           i=i+4;
       }
       
       array[arrayLength-2]=f2+f1;
       array[arrayLength-1]=0;
   }
 public static void getRawDataArray(){
     int i=1;
     
     while(i<arrayLength-4)
     {
         array[i]= array[i+4];
         i=i+2;
     }
     
     array[arrayLength-3]= array[arrayLength-1];
     
     getRawData = StaticValue.surfaceviewHeight/2 -Float.valueOf(arraydata)/scaleRate;
     if (Listdata.size() < StaticValue.Fs/150) {
         Listdata.add(getRawData);
        
    }else {
        Listdata.remove(0);
        Listdata.add(getRawData);
        float sum=0;
        for(int kk=0;kk<Listdata.size();kk++)
        {
            sum+=Listdata.get(kk);
        }
        filtered = sum/(StaticValue.Fs/50);
       }
     array[arrayLength-1] = filtered+StaticValue.surfaceviewHeight/2;
    } 
 public static float[] getBloxDataPosizationArray() {
     // TODO Auto-generated method stub
     return array;

 }
}
