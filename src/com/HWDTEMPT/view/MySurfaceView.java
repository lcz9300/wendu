
package com.HWDTEMPT.view;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.HWDTEMPT.hwdtempt.bleactivty;
import com.HWDTEMPT.model.StaticValue;
import com.HWDTEMPT.tool.RawDataArray;
import com.HWDTEMPT.tool.TxtReader;

import java.io.File;

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private int screenWidth,screenHeight;
    private SurfaceHolder holder;
    private MyThread myThread;
    private String gian;
    private  File file;
   // private static final String fgian = "5";
    public MySurfaceView(Context context, AttributeSet attrs) {

        super(context);
        try {
            file = new File(Environment.getExternalStorageDirectory() + "//" + "BMDUSB", "gn" + ".txt");
            
          
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        
        holder = this.getHolder();
        holder.addCallback(this);

    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
   
        Rect frameRect = holder.getSurfaceFrame();
        StaticValue.surfaceviewHeight = frameRect.height();

        myThread = new MyThread();
        myThread.setIsRun(true);
        myThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        myThread.setIsRun(false);
    }

    public class MyThread extends Thread {

        private boolean isRun = false;
        private Paint grid_paint = new Paint();
        private Paint ecg_paint = new Paint();
        private Paint paint_text=new Paint();
        float[] pts3;
       
        public MyThread() {
            grid_paint.setColor(Color.RED);
            grid_paint.setAntiAlias(true);// 表示抗锯齿
            grid_paint.setStyle(Paint.Style.STROKE);

            ecg_paint.setColor(Color.BLACK);
            ecg_paint.setStrokeWidth(4);
            ecg_paint.setAntiAlias(true);
            ecg_paint.setStyle(Paint.Style.STROKE);
            // 画字体
            paint_text = new Paint();
            paint_text.setStrokeWidth(8);
            paint_text.setTextSize(30);
            paint_text.setColor(Color.BLACK);
        }

        public void setIsRun(boolean isRun) {
            this.isRun = isRun;
 
        }

        public void run() {

            while (isRun) {
                Canvas c = null;
                
                try {
                    synchronized (holder) {
                        c = holder.lockCanvas();
                        c.drawColor(Color.WHITE);
                        
                        //
                        if (file.exists()) {
                            gian = TxtReader.GetString(file);
                            //Log.e("gian", gian);
                            String gn = gian.toString().trim();
                           
                           if (gn.equals("5")) {
                               drawDingbiao(c);
                            
                        }else {
                            drawDb10(c);
                        }
                           
                        }
                        
                        drawGrid(c);

                        synchronized (RawDataArray.syncRoot) {
                            this.pts3 = RawDataArray.getRawDataPosizationArray();
                            c.drawLines(this.pts3, ecg_paint);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (c != null) {
                        holder.unlockCanvasAndPost(c);
                    }
                }

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        private void drawDingbiao(Canvas canvas){//5mv
           // Log.e("tg", "tag");
         if (bleactivty.fenbianlvxiao) {
             grid_paint.setStrokeWidth(8);
             canvas.drawText("ECG", 1f * (20.28f), getHeight()/3.6f, paint_text);
             canvas.drawLine(2 * (20.28f), 15*20.28f,  2 * (20.28f),  20 * 20.28f,
                     grid_paint);
             canvas.drawLine(0, 2 * 20.28f,  5 * (20.28f), 2 * 20.28f,grid_paint);
             canvas.drawText("5mm", 1.0f * (20.28f),  1.5f * (20.28f), paint_text);
        } else if (bleactivty.fenbianlvda) {
            grid_paint.setStrokeWidth(8);
            canvas.drawText("ECG", 1f * (12.8f), getHeight()/4.5f, paint_text);
            canvas.drawLine(2 * (12.8f), 15*12.8f,  2 * (12.8f),  20 * 12.8f,
                    grid_paint);
            canvas.drawLine(0, 2 * 12.8f,  5 * (12.8f), 2 * 12.8f,grid_paint);
            canvas.drawText("5mm", 1.0f * (12.8f),  1.5f * (12.8f), paint_text);
       } else if (bleactivty.fenbianlvxiao2) {
           grid_paint.setStrokeWidth(8);
           canvas.drawText("ECG", 1f * (14.5f), getHeight()/4.5f, paint_text);
           canvas.drawLine(2 * (14.5f), 15*14.5f,  2 * (14.5f),  20 * 14.5f,
                   grid_paint);
           canvas.drawLine(0, 2 * 14.5f,  5 * (14.5f), 2 * 14.5f,grid_paint);
           canvas.drawText("5mm", 1.0f * (14.5f),  1.5f * (14.5f), paint_text);
      }   else if (bleactivty.fenbianlvxiaoqita) {
          grid_paint.setStrokeWidth(8);
          canvas.drawText("ECG", 1f * (20.28f), getHeight()/3.6f, paint_text);
          canvas.drawLine(2 * (20.28f), 15*20.28f,  2 * (20.28f),  20 * 20.28f,
                  grid_paint);
          canvas.drawLine(0, 2 * 20.28f,  5 * (20.28f), 2 * 20.28f,grid_paint);
          canvas.drawText("5mm", 1.0f * (20.28f),  1.5f * (20.28f), paint_text);
     } 
            
           
         
     
        }
        
        private void drawDb10(Canvas c){
            if (bleactivty.fenbianlvxiao) {
                grid_paint.setStrokeWidth(8);
                c.drawText("ECG", 1.0f * (20.28f), getHeight()/5.6f, paint_text);
                c.drawLine(2 * (20.28f), 10*20.28f,  2 * (20.28f), 20 * 20.28f,grid_paint);
                c.drawLine(0, 2 * 20.28f,  5 * (20.28f), 2 * 20.28f,grid_paint);
                c.drawText("5mm", 1.0f * (20.28f),  1.5f * (20.28f), paint_text);
        //        Log.e("tg", "tag1111111");
            } else if (bleactivty.fenbianlvda) {
                grid_paint.setStrokeWidth(8);
                c.drawText("ECG", 1.0f * (12.8f), getHeight()/7.2f, paint_text);
                c.drawLine(2 * (12.8f), 10*12.8f,  2 * (12.8f), 20 * 12.8f,grid_paint);
                c.drawLine(0, 2 * 12.8f,  5 * (12.8f), 2 * 12.8f,grid_paint);
                c.drawText("5mm", 1.0f * (12.8f),  1.5f * (12.8f), paint_text);
           //     Log.e("tg", "tag1111111");
            }else if (bleactivty.fenbianlvxiao2) {
                grid_paint.setStrokeWidth(8);
                c.drawText("ECG", 1.0f * (14.5f), getHeight()/7.2f, paint_text);
                c.drawLine(2 * (14.5f), 10*14.5f,  2 * (14.5f), 20 * 14.5f,grid_paint);
                c.drawLine(0, 2 * 14.5f,  5 * (14.5f), 2 * 14.5f,grid_paint);
                c.drawText("5mm", 1.0f * (14.5f),  1.5f * (14.5f), paint_text);
          //      Log.e("tg", "tag1111111");
            } else if (bleactivty.fenbianlvxiaoqita) {
                grid_paint.setStrokeWidth(8);
                c.drawText("ECG", 1.0f * (20.28f), getHeight()/5.6f, paint_text);
                c.drawLine(2 * (20.28f), 10*20.28f,  2 * (20.28f), 20 * 20.28f,grid_paint);
                c.drawLine(0, 2 * 20.28f,  5 * (20.28f), 2 * 20.28f,grid_paint);
                c.drawText("5mm", 1.0f * (20.28f),  1.5f * (20.28f), paint_text);
          //      Log.e("tg", "tag1111111");
            }
           
        }
        
        
        
        private void drawGrid(Canvas canvas) {

         // 画底部图像已经完成，按照医院规定，纵线之间的小格为0.04s，粗线之间的时间为0.2s，按照波特率，变为Fs/5为一个大格。

       //     int gapvertical = (StaticValue.surfaceviewHeight / 2) % (StaticValue.Fs / 5);// 此为画布上下预留的空间
            int gapvertical = 0;
            
            if (bleactivty.fenbianlvxiao) {
                // 画竖线
                for (int horizontal = 0; horizontal <= StaticValue.surfaceviewWidth; horizontal++) {
                    if (horizontal % 5 == 0)
                    {
                        // 这个位置就应该画大格，用粗线
                        grid_paint.setStrokeWidth(3);
                        canvas.drawLine(horizontal * (20.28f), gapvertical, horizontal
                                *(20.28f), 40 * (20.28f),grid_paint);
                    } else {
                        // 这个位置画小格，用细线
                        grid_paint.setStrokeWidth(1);
                        canvas.drawLine(horizontal * (20.28f), gapvertical, horizontal
                               *(20.28f),  40 * (20.28f),grid_paint);
                  
                        // 1mm=2.833 ..............................................
                        
                      /*  // 定标符号
                        
                        if (horizontal ==2) {
                            grid_paint.setStrokeWidth(6);
                            canvas.drawLine(2 * (14.5f), getHeight()*3/10,2 * (14.5f), getHeight()/2,
                                    grid_paint);
                            canvas.drawText("ECG", 1f * (14.5f), getHeight()/4, paint_text);
                        }*/
                    }
                }
                // 画横线
                for (int vertical = 0; vertical <= 40; vertical++) {
                    if (vertical % 5 == 0)
                    {
                        // 这个位置就应该画大格，用粗线
                        grid_paint.setStrokeWidth(3);
                        canvas.drawLine(0, vertical * (20.28f) + gapvertical,
                                StaticValue.surfaceviewWidth, vertical * (20.28f)
                                        + gapvertical, grid_paint);
                    } else {
                        // 这个位置画小格，用细线
                        grid_paint.setStrokeWidth(1);
                        canvas.drawLine(0, vertical * (20.28f) + gapvertical,
                                StaticValue.surfaceviewWidth, vertical * (20.28f)
                                        + gapvertical, grid_paint);
                    }
                } 
            }else if(bleactivty.fenbianlvda) {
                for (int horizontal = 0; horizontal <= StaticValue.surfaceviewWidth; horizontal++) {
                    if (horizontal % 5 == 0)
                    {
                        // 这个位置就应该画大格，用粗线
                        grid_paint.setStrokeWidth(3);
                        canvas.drawLine(horizontal * (12.8f), gapvertical, horizontal
                                * (12.8f), 40 * (12.8f),grid_paint);
                    } else {
                        // 这个位置画小格，用细线
                        grid_paint.setStrokeWidth(1);
                        canvas.drawLine(horizontal * (12.8f), gapvertical, horizontal
                                * (12.8f),  40 * (12.8f),grid_paint);
                        
                      /*  // 定标符号
                        
                        if (horizontal ==2) {
                            grid_paint.setStrokeWidth(6);
                            canvas.drawLine(2 * (14.5f), getHeight()*3/10,2 * (14.5f), getHeight()/2,
                                    grid_paint);
                            canvas.drawText("ECG", 1f * (14.5f), getHeight()/4, paint_text);
                        }*/
                    }
                }
                // 画横线
                for (int vertical = 0; vertical <= 40; vertical++) {
                    if (vertical % 5 == 0)
                    {
                        // 这个位置就应该画大格，用粗线
                        grid_paint.setStrokeWidth(3);
                        canvas.drawLine(0, vertical * (12.8f) + gapvertical,
                                StaticValue.surfaceviewWidth, vertical * (12.8f)
                                        + gapvertical, grid_paint);
                    } else {
                        // 这个位置画小格，用细线
                        grid_paint.setStrokeWidth(1);
                        canvas.drawLine(0, vertical * (12.8f) + gapvertical,
                                StaticValue.surfaceviewWidth, vertical * (12.8f)
                                        + gapvertical, grid_paint);
                    }
                } 
            }  else if (bleactivty.fenbianlvxiao2) {
                // 画竖线
                for (int horizontal = 0; horizontal <= StaticValue.surfaceviewWidth; horizontal++) {
                    if (horizontal % 5 == 0)
                    {
                        // 这个位置就应该画大格，用粗线
                        grid_paint.setStrokeWidth(3);
                        canvas.drawLine(horizontal * (14.5f), gapvertical, horizontal
                                * (14.5f), 40 * (14.5f),grid_paint);
                    } else {
                        // 这个位置画小格，用细线
                        grid_paint.setStrokeWidth(1);
                        canvas.drawLine(horizontal * (14.5f), gapvertical, horizontal
                                * (14.5f),  40 * (14.5f),grid_paint);
                        
                      /*  // 定标符号
                        
                        if (horizontal ==2) {
                            grid_paint.setStrokeWidth(6);
                            canvas.drawLine(2 * (14.5f), getHeight()*3/10,2 * (14.5f), getHeight()/2,
                                    grid_paint);
                            canvas.drawText("ECG", 1f * (14.5f), getHeight()/4, paint_text);
                        }*/
                    }
                }
                // 画横线
                for (int vertical = 0; vertical <= 40; vertical++) {
                    if (vertical % 5 == 0)
                    {
                        // 这个位置就应该画大格，用粗线
                        grid_paint.setStrokeWidth(3);
                        canvas.drawLine(0, vertical * (14.5f) + gapvertical,
                                StaticValue.surfaceviewWidth, vertical * (14.5f)
                                        + gapvertical, grid_paint);
                    } else {
                        // 这个位置画小格，用细线
                        grid_paint.setStrokeWidth(1);
                        canvas.drawLine(0, vertical * (14.5f) + gapvertical,
                                StaticValue.surfaceviewWidth, vertical * (14.5f)
                                        + gapvertical, grid_paint);
                    }
                } 
            }else if (bleactivty.fenbianlvxiao52) {
                // 画竖线
                for (int horizontal = 0; horizontal <= StaticValue.surfaceviewWidth; horizontal++) {
                    if (horizontal % 5 == 0)
                    {
                        // 这个位置就应该画大格，用粗线
                        grid_paint.setStrokeWidth(3);
                        canvas.drawLine(horizontal * (25f), gapvertical, horizontal
                                * (25f), 40 * (25f),grid_paint);
                    } else {
                        // 这个位置画小格，用细线
                        grid_paint.setStrokeWidth(1);
                        canvas.drawLine(horizontal * (25f), gapvertical, horizontal
                                * (25f),  40 * (25f),grid_paint);
                        
                      /*  // 定标符号
                        
                        if (horizontal ==2) {
                            grid_paint.setStrokeWidth(6);
                            canvas.drawLine(2 * (14.5f), getHeight()*3/10,2 * (14.5f), getHeight()/2,
                                    grid_paint);
                            canvas.drawText("ECG", 1f * (14.5f), getHeight()/4, paint_text);
                        }*/
                    }
                }
                // 画横线
                for (int vertical = 0; vertical <= 40; vertical++) {
                    if (vertical % 5 == 0)
                    {
                        // 这个位置就应该画大格，用粗线
                        grid_paint.setStrokeWidth(3);
                        canvas.drawLine(0, vertical * (25f) + gapvertical,
                                StaticValue.surfaceviewWidth, vertical * (25f)
                                        + gapvertical, grid_paint);
                    } else {
                        // 这个位置画小格，用细线
                        grid_paint.setStrokeWidth(1);
                        canvas.drawLine(0, vertical * (25f) + gapvertical,
                                StaticValue.surfaceviewWidth, vertical * (25f)
                                        + gapvertical, grid_paint);
                    }
                } 
            }  else  if (bleactivty.fenbianlvxiaoqita) {
                // 画竖线
                for (int horizontal = 0; horizontal <= StaticValue.surfaceviewWidth; horizontal++) {
                    if (horizontal % 5 == 0)
                    {
                        // 这个位置就应该画大格，用粗线
                        grid_paint.setStrokeWidth(3);
                        canvas.drawLine(horizontal * (20.28f), gapvertical, horizontal
                                *(20.28f), 40 * (20.28f),grid_paint);
                    } else {
                        // 这个位置画小格，用细线
                        grid_paint.setStrokeWidth(1);
                        canvas.drawLine(horizontal * (20.28f), gapvertical, horizontal
                               *(20.28f),  40 * (20.28f),grid_paint);
                  
                        // 1mm=2.833 ..............................................
                        
                      /*  // 定标符号
                        
                        if (horizontal ==2) {
                            grid_paint.setStrokeWidth(6);
                            canvas.drawLine(2 * (14.5f), getHeight()*3/10,2 * (14.5f), getHeight()/2,
                                    grid_paint);
                            canvas.drawText("ECG", 1f * (14.5f), getHeight()/4, paint_text);
                        }*/
                    }
                }
                // 画横线
                for (int vertical = 0; vertical <= 40; vertical++) {
                    if (vertical % 5 == 0)
                    {
                        // 这个位置就应该画大格，用粗线
                        grid_paint.setStrokeWidth(3);
                        canvas.drawLine(0, vertical * (20.28f) + gapvertical,
                                StaticValue.surfaceviewWidth, vertical * (20.28f)
                                        + gapvertical, grid_paint);
                    } else {
                        // 这个位置画小格，用细线
                        grid_paint.setStrokeWidth(1);
                        canvas.drawLine(0, vertical * (20.28f) + gapvertical,
                                StaticValue.surfaceviewWidth, vertical * (20.28f)
                                        + gapvertical, grid_paint);
                    }
                } 
            }
          
        
    
        }

    }

}
