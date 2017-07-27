package com.HWDTEMPT.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;



import com.HWDTEMPT.model.StaticValue;
import com.HWDTEMPT.tool.BloodDateWave;


public class BloodOx_view extends SurfaceView implements SurfaceHolder.Callback{
    private SurfaceHolder holder;
    private MyThread myThread;

    public BloodOx_view(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        holder = this.getHolder();
        holder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        Rect frameRect = holder.getSurfaceFrame();
        StaticValue.surfaceviewHeight = frameRect.height();
        
        myThread = new MyThread();
        myThread.setIsRun(true);
        myThread.start();
        
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        
    }
    public class MyThread extends Thread{
        private boolean isRun = false;
        private Paint grid_paint = new Paint();
        private Paint ecg_paint = new Paint();
        float[] pts3;
        public MyThread() {
            grid_paint.setColor(Color.GREEN);
            grid_paint.setAntiAlias(true);// ��ʾ�����
            grid_paint.setStyle(Paint.Style.STROKE);

            ecg_paint.setColor(Color.RED);
            ecg_paint.setStrokeWidth(5);
            ecg_paint.setAntiAlias(true);
            ecg_paint.setStyle(Paint.Style.STROKE);
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
                        drawGrid(c);

                        synchronized (BloodDateWave.sync) {
                            this.pts3 = BloodDateWave.getBloxDataPosizationArray();
                            c.drawLines(this.pts3, ecg_paint);
                            try {
                                Thread.sleep(5);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
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
        private void drawGrid(Canvas canvas) {

            // ���ײ�ͼ���Ѿ���ɣ�����ҽԺ�涨������֮���С��Ϊ0.04s������֮���ʱ��Ϊ0.2s�����ղ����ʣ���ΪFs/5Ϊһ�����

            int gapvertical = (StaticValue.surfaceviewHeight / 2) % (StaticValue.Fs / 5);// ��Ϊ��������Ԥ���Ŀռ�

            // ������
            for (int horizontal = 0; horizontal <= StaticValue.surfaceviewWidth
                    / (StaticValue.Fs / 25); horizontal++) {
                if (horizontal % 5 == 0)
                {
                    // ���λ�þ�Ӧ�û�����ô���
                    grid_paint.setStrokeWidth(3);
                    canvas.drawLine(horizontal * (StaticValue.Fs / 25), gapvertical, horizontal
                            * (StaticValue.Fs / 25), StaticValue.surfaceviewHeight
                            / (StaticValue.Fs / 5) * 5 * (StaticValue.Fs / 25) + gapvertical,
                            grid_paint);
                } /*else {
                    // ���λ�û�С����ϸ��
                    grid_paint.setStrokeWidth(1);
                    canvas.drawLine(horizontal * (StaticValue.Fs / 25), gapvertical, horizontal
                            * (StaticValue.Fs / 25), StaticValue.surfaceviewHeight
                            / (StaticValue.Fs / 5) * 5 * (StaticValue.Fs / 25) + gapvertical,
                            grid_paint);
                }*/
            }
            // ������
            for (int vertical = 0; vertical <= StaticValue.surfaceviewHeight / (StaticValue.Fs / 5)
                    * 5; vertical++) {
                if (vertical % 5 == 0)
                {
                    // ���λ�þ�Ӧ�û�����ô���
                    grid_paint.setStrokeWidth(3);
                    canvas.drawLine(0, vertical * (StaticValue.Fs / 25) + gapvertical,
                            StaticValue.surfaceviewWidth, vertical * (StaticValue.Fs / 25)
                                    + gapvertical, grid_paint);
                } /*else {
                    // ���λ�û�С����ϸ��
                    grid_paint.setStrokeWidth(1);
                    canvas.drawLine(0, vertical * (StaticValue.Fs / 25) + gapvertical,
                            StaticValue.surfaceviewWidth, vertical * (StaticValue.Fs / 25)
                                    + gapvertical, grid_paint);
                }*/
            }

        }
        
    }
  
  
}
