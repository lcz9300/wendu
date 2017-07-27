package com.HWDTEMPT.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.HWDTEMPT.hwdtempt.App2;
import com.HWDTEMPT.hwdtempt.bleactivty;
import com.HWDTEMPT.model.StaticValue;

public class History_Draw extends View {


    private int[] ECGDraw,Rpoints,Qpoints,Q1points,Spoints,S2points,Ppoints,Tpoints;
    private Paint paint_date, paint_point, paint_Grid;


    public History_Draw(Context context,int[] ECGDraw,int[] Rpoints,int[] Qpoints,int []Q1points,int []Spoints,int[]S2points,int[]Ppoints,int[]Tpoints) {
        super(context);
        init(ECGDraw,Rpoints,Qpoints,Q1points,Spoints,S2points,Ppoints,Tpoints);
    }

    public void init(int[] ECGDraw,int[] Rpoints,int[] Qpoints,int []Q1points,int []Spoints,int[]S2points,int[]Ppoints,int[]Tpoints) {
        
        this.ECGDraw=ECGDraw;
        this.Rpoints=Rpoints;
        this.Qpoints=Qpoints;
        this.Q1points=Q1points;
        this.Spoints=Spoints;
        this.S2points=S2points;
        this.Ppoints=Ppoints;
        this.Tpoints=Tpoints;
        paint_date = new Paint();
        paint_date.setStrokeWidth(5);
        paint_date.setColor(Color.BLACK);
        
        paint_point = new Paint();
        paint_point.setStrokeWidth(12);
        paint_point.setColor(Color.BLUE);
        
        paint_Grid=new Paint();
        paint_Grid.setColor(Color.RED);
        paint_point.setAntiAlias(true);

        
        setLayoutParams(new LayoutParams(
                (int) (this.ECGDraw.length),
                LayoutParams.MATCH_PARENT));
    }

    


    protected void onDraw(Canvas c) {
        // 画布背景色
        //c.drawColor(Color.WHITE);
        drawGrid(c);
        drawLine(c);
        //drawpoints(c);
        
    }

    /**
     * 绘制网格
     * 
     * @param c
     */
    public void drawGrid(Canvas c) {

     // modified for Mate8 2016.12.27
     
   
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
            //     Log.e("dddddddddd", "66666666666");
         }    
         
         
         
         // 画横线
         for (int vertical = 0; vertical <=200; vertical++) {
        //     Log.e("yyyyy", "9999999");
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
           //           Log.e("ffffffff", "77777777777");
         }
   
    }else if(App2.fenbianlvda) {
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
          //      Log.e("dddddddddd", "66666666666");
        }    
        
        
        
        // 画横线
        for (int vertical = 0; vertical <=200; vertical++) {
            Log.e("yyyyy", "9999999");
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
          //           Log.e("ffffffff", "77777777777");
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
         //       Log.e("dddddddddd", "66666666666");
        }    
        
        
        
        // 画横线
        for (int vertical = 0; vertical <=200; vertical++) {
        //    Log.e("yyyyy", "9999999");
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
           //          Log.e("ffffffff", "77777777777");
        }
  
   }else if (App2.fenbianlvxiaoqita) {
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
          //     Log.e("dddddddddd", "66666666666");
       }    
       
       
       
       // 画横线
       for (int vertical = 0; vertical <=200; vertical++) {
      //     Log.e("yyyyy", "9999999");
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
         //           Log.e("ffffffff", "77777777777");
       }
 
  }
       
  
  /* 
        int start = (getHeight() - ((StaticValue.Fs /25)) *30) / 2;

        
        // 画竖线
        for (int horizontal = 0; horizontal <getWidth(); horizontal++) {
            if (horizontal % 5 == 0)
            {
                // 这个位置就应该画大格，用粗线
                paint_Grid.setStrokeWidth(3);
                c.drawLine(horizontal * (StaticValue.Fs / 25), start, horizontal
                        * (StaticValue.Fs / 25),getHeight()-start,paint_Grid);
            } else {
                // 这个位置画小格，用细线
                paint_Grid.setStrokeWidth(1);
                c.drawLine(horizontal * (StaticValue.Fs / 25), start, horizontal
                        * (StaticValue.Fs / 25),getHeight()-start,paint_Grid);
            }
            Log.e("dddddddddd", "66666666666");
        }    
        
        
        
        // 画横线
        for (int vertical = 0; vertical <=30; vertical++) {
            if (vertical % 5 == 0)
            {
                // 这个位置就应该画大格，用粗线
                paint_Grid.setStrokeWidth(3);
                c.drawLine(0, vertical * (StaticValue.Fs / 25) +start ,getWidth(),vertical * (StaticValue.Fs / 25) +start, paint_Grid);
            } else {
                // 这个位置画小格，用细线
                paint_Grid.setStrokeWidth(1);
                c.drawLine(0, vertical * (StaticValue.Fs / 25) +start ,getWidth(),vertical * (StaticValue.Fs / 25) +start, paint_Grid);
            }
            Log.e("ffffffff", "77777777777");
        }
              
 */     
        
        // modified for Note 4 2016.12.20
        /*float start1 = (getHeight() - ((20.4f)) *30) / 2;
        
        // 画竖线
        for (int horizontal = 0; horizontal <getWidth(); horizontal++) {
            if (horizontal % 5 == 0)
            {
                // 这个位置就应该画大格，用粗线
                paint_Grid.setStrokeWidth(3);
                c.drawLine(horizontal * (20.4f), start1, horizontal
                        * (20.4f),getHeight()-start1,paint_Grid);
            } else {
                // 这个位置画小格，用细线
                paint_Grid.setStrokeWidth(1);
                c.drawLine(horizontal * (20.4f), start1, horizontal
                        * (20.4f),getHeight()-start1,paint_Grid);
            }
        }    
        
        
        
        // 画横线
        for (int vertical = 0; vertical <=30; vertical++) {
            if (vertical % 5 == 0)
            {
                // 这个位置就应该画大格，用粗线
                paint_Grid.setStrokeWidth(3);
                c.drawLine(0, vertical * (20.4f) +start1 ,getWidth(),vertical * (20.4f) +start1, paint_Grid);
            } else {
                // 这个位置画小格，用细线
                paint_Grid.setStrokeWidth(1);
                c.drawLine(0, vertical * (20.4f) +start1 ,getWidth(),vertical * (20.4f) +start1, paint_Grid);
            }
        }
        */
    
    }
    


    /**
     * 绘制折线
     * 
     * 
     */
    public void drawLine(Canvas c) {
        int datasize = ECGDraw.length;
        int tempsize = 0;
        float xx=0,yy=0;
        while (tempsize < datasize - 8) {
            
            float tmpx=(float) (xx+1.1);
            float tmpy=(-ECGDraw[tempsize]/88888 + (getHeight() / 2));//88888
            c.drawLine(xx, yy, tmpx,tmpy, paint_date);
            xx=tmpx;
            yy=tmpy;

            tempsize = tempsize +1;
        }

    }
    
    
    public void drawpoints(Canvas c)
    {
        for(int i=0;i<Rpoints.length;i++)
        {
            int tmpx=Rpoints[i];
            float tmpy=(-ECGDraw[Rpoints[i]]/88888 + (getHeight() / 2));//88888
            c.drawPoint(tmpx, tmpy, paint_point);
        }
    
    }
}
