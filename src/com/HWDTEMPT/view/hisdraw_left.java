package com.HWDTEMPT.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.View;

import com.HWDTEMPT.hwdtempt.App2;
import com.HWDTEMPT.model.StaticValue;
import com.HWDTEMPT.tool.TxtReader;

import java.io.File;
public class hisdraw_left extends View{
    private Paint paint_date, paint_date1,paint_point, paint_Grid,paint_text;
    private String gian,filename;
    private  File file;
    
    public hisdraw_left(Context context,String filename) {
        super(context);
        this.filename=filename;
        // TODO Auto-generated constructor stub
        
        
        try {
            if (filename!=null) {
                file = new File(Environment.getExternalStorageDirectory() + "//" + "BMDUSB", "gain" +filename+ ".txt");
                String gn = TxtReader.GetString(file);
                gian =gn.toString().trim();
            }
            
            
          
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        
        
        paint_date = new Paint();
        paint_date.setStrokeWidth(5);
        paint_date.setColor(Color.BLACK);
        
        paint_date1 = new Paint();
        paint_date1.setStrokeWidth(6);
        paint_date1.setColor(Color.BLACK);
        
        
        
        paint_point = new Paint();
        paint_point.setStrokeWidth(12);
        paint_point.setColor(Color.BLUE);
        
        paint_Grid=new Paint();
        paint_Grid.setColor(Color.RED);
        paint_point.setAntiAlias(true);
        
        
        paint_text = new Paint();
        paint_text.setStrokeWidth(8);
        paint_text.setTextSize(25);
        paint_text.setColor(Color.BLACK);

    }
    protected void onDraw(Canvas c) {
        // 画布背景色
        //c.drawColor(Color.WHITE);
        drawGrid(c);
        //drawleft(c);
        if (gian!=null) {
            if (gian.equals("5")) {
                
                drawleBb(c);
            }else {
                drawleBb10(c);
            }
        }else {
            drawleBb(c);
        }
               
    }
    
    public void drawleBb(Canvas c){
        paint_date.setStrokeWidth(8);
        
        paint_date.setColor(Color.RED);
        
        c.drawText("ECG", getWidth()/5, (getHeight()/10)*3.8f, paint_text);
        
        c.drawLine((getWidth()/5)*3,(getHeight()/10)*4.0f,(getWidth()/5)*3,getHeight()/2,paint_date);
        
    }
    public void drawleBb10(Canvas c){
        paint_date.setStrokeWidth(8);
        
        paint_date.setColor(Color.RED);
        
        c.drawText("ECG", getWidth()/5, (getHeight()/10)*2.8f, paint_text);
        
        c.drawLine((getWidth()/5)*3,(getHeight()/10)*3.0f,(getWidth()/5)*3,getHeight()/2,paint_date);
        
    }
    
    public void drawleft(Canvas c){
       
            paint_date.setStrokeWidth(6);
           
            paint_date.setColor(Color.RED);
            c.drawLine(0,getHeight()/2,getWidth()/5,getHeight()/2,paint_date);
            //c.drawLine(getWidth()/5,0,getWidth()/5,(getHeight()/8)*5,paint_Grid);
            //c.drawLine(startX, startY, stopX, stopY, paint);
            c.drawLine(getWidth()/5,getHeight()/2,getWidth()/5,(getHeight()/9)*2.5f,paint_date);
            c.drawLine(getWidth()/5,(getHeight()/9)*2.5f,(getWidth()/5)*3,(getHeight()/9)*2.5f,paint_date);
            c.drawLine((getWidth()/5)*3,(getHeight()/9)*2.5f,(getWidth()/5)*3,getHeight()/2,paint_date);
            c.drawLine((getWidth()/5)*3,getHeight()/2,(getWidth()/5)*4.5f,getHeight()/2,paint_date);
            c.drawText("ECG", getWidth()/5, (getHeight()/10)*2.5f, paint_text);
            
            //画刻度
            c.drawLine(0,5* (14.5f),5* (14.5f),5* (14.5f),paint_date1);
            c.drawLine(0,3* (14.5f),0,5* (14.5f),paint_date1);
            c.drawLine(5* (14.5f),3* (14.5f),5* (14.5f),5* (14.5f),paint_date1);
            c.drawText("5mm", getWidth()/5, (getHeight()/20)*2, paint_text);
            
      
        
    }
    public void drawGrid(Canvas c) {

        int start = (getHeight() - ((StaticValue.Fs /25)) *30) / 2;
        
        float start1 = (getHeight() - ((14.5f)) *30) / 2;
      if (App2.fenbianlvxiao) {
          // 画竖线
          for (int horizontal = 0; horizontal <getWidth(); horizontal++) {
              if (horizontal % 5 == 0)
              {
                  // 这个位置就应该画大格，用粗线
                  paint_Grid.setStrokeWidth(3);
                  c.drawLine(horizontal * (20.28f), 0, horizontal
                          * (20.28f),getHeight(),paint_Grid);
              } else {
                  // 这个位置画小格，用细线
                  paint_Grid.setStrokeWidth(1);
                  c.drawLine(horizontal * (20.28f), 0, horizontal
                          * (20.28f),getHeight(),paint_Grid);
              }
          }    
          
          
          
          // 画横线
          for (int vertical = 0; vertical <=200; vertical++) {
              if (vertical % 5 == 0)
              {
                  // 这个位置就应该画大格，用粗线
                  paint_Grid.setStrokeWidth(3);
                  c.drawLine(0, vertical * (20.28f) ,getWidth(),vertical * (20.28f), paint_Grid);
              } else {
                  // 这个位置画小格，用细线
                  paint_Grid.setStrokeWidth(1);
                  c.drawLine(0, vertical * (20.28f) ,getWidth(),vertical * (20.28f), paint_Grid);
              }
          }
    }  else if (App2.fenbianlvda) {
        // 画竖线
        for (int horizontal = 0; horizontal <getWidth(); horizontal++) {
            if (horizontal % 5 == 0)
            {
                // 这个位置就应该画大格，用粗线
                paint_Grid.setStrokeWidth(3);
                c.drawLine(horizontal * (12.8f), 0, horizontal
                        * (12.8f),getHeight(),paint_Grid);
            } else {
                // 这个位置画小格，用细线
                paint_Grid.setStrokeWidth(1);
                c.drawLine(horizontal * (12.8f), 0, horizontal
                        * (12.8f),getHeight(),paint_Grid);
            }
        }    
        
        
        
        // 画横线
        for (int vertical = 0; vertical <=200; vertical++) {
            if (vertical % 5 == 0)
            {
                // 这个位置就应该画大格，用粗线
                paint_Grid.setStrokeWidth(3);
                c.drawLine(0, vertical * (12.8f) ,getWidth(),vertical * (12.8f), paint_Grid);
            } else {
                // 这个位置画小格，用细线
                paint_Grid.setStrokeWidth(1);
                c.drawLine(0, vertical * (12.8f) ,getWidth(),vertical * (12.8f), paint_Grid);
            }
        }
    } else if (App2.fenbianlvxiao2) {
        // 画竖线
        for (int horizontal = 0; horizontal <getWidth(); horizontal++) {
            if (horizontal % 5 == 0)
            {
                // 这个位置就应该画大格，用粗线
                paint_Grid.setStrokeWidth(3);
                c.drawLine(horizontal * (14.5f), 0, horizontal
                        * (14.5f),getHeight(),paint_Grid);
            } else {
                // 这个位置画小格，用细线
                paint_Grid.setStrokeWidth(1);
                c.drawLine(horizontal * (14.5f), 0, horizontal
                        * (14.5f),getHeight(),paint_Grid);
            }
        }    
        
        
        
        // 画横线
        for (int vertical = 0; vertical <=200; vertical++) {
            if (vertical % 5 == 0)
            {
                // 这个位置就应该画大格，用粗线
                paint_Grid.setStrokeWidth(3);
                c.drawLine(0, vertical * (14.5f) ,getWidth(),vertical * (14.5f), paint_Grid);
            } else {
                // 这个位置画小格，用细线
                paint_Grid.setStrokeWidth(1);
                c.drawLine(0, vertical * (14.5f) ,getWidth(),vertical * (14.5f), paint_Grid);
            }
        }
    }   else if (App2.fenbianlvxiaoqita) {
        // 画竖线
        for (int horizontal = 0; horizontal <getWidth(); horizontal++) {
            if (horizontal % 5 == 0)
            {
                // 这个位置就应该画大格，用粗线
                paint_Grid.setStrokeWidth(3);
                c.drawLine(horizontal * (20.28f), 0, horizontal
                        * (20.28f),getHeight(),paint_Grid);
            } else {
                // 这个位置画小格，用细线
                paint_Grid.setStrokeWidth(1);
                c.drawLine(horizontal * (20.28f), 0, horizontal
                        * (20.28f),getHeight(),paint_Grid);
            }
        }    
        
        
        
        // 画横线
        for (int vertical = 0; vertical <=200; vertical++) {
            if (vertical % 5 == 0)
            {
                // 这个位置就应该画大格，用粗线
                paint_Grid.setStrokeWidth(3);
                c.drawLine(0, vertical * (20.28f) ,getWidth(),vertical * (20.28f), paint_Grid);
            } else {
                // 这个位置画小格，用细线
                paint_Grid.setStrokeWidth(1);
                c.drawLine(0, vertical * (20.28f) ,getWidth(),vertical * (20.28f), paint_Grid);
            }
        }
  } 
     
        
        
    }

}
