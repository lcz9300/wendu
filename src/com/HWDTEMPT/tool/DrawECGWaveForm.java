package com.HWDTEMPT.tool;


import com.HWDTEMPT.model.StaticValue;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class DrawECGWaveForm {

	private SurfaceView surface;
	private SurfaceHolder holder_heart;
	private Path path = new Path();;// 心电图路径
	private int heart_begin_x = 0;// 心电图起始x坐标
	private float heart_begin_y;// 心电图起始y坐标
	private int heart_end_x = 0;// 心电图终点x坐标
	private float heart_end_y;// 心电图终点y坐标
	private Canvas canvas;// 心电画布1
	private Paint paint = new Paint(); // 画笔
	private Paint grid_paint = new Paint(); // 画笔
	private int scaleY=0;
	// 初始化

	public DrawECGWaveForm(SurfaceView msfv,int ii) 
	{
		this.surface = msfv;
		holder_heart = surface.getHolder();
		heart_end_y = surface.getWidth() / 2;
		scaleY=ii;
		grid_paint.setColor(Color.RED);
		grid_paint.setStrokeWidth(3);
		// 画笔属性
		paint.setStrokeWidth(5);
		paint.setColor(Color.BLACK);
		paint.setAntiAlias(true);// 表示抗锯齿
		paint.setStyle(Paint.Style.STROKE);
	}

	
	
	
	public void simplydraw(int ECGData) {

		try {
			synchronized (holder_heart) {
				boolean flag1 = false;
				surface.postInvalidate();
				if (holder_heart == null) {
					return;
				}

				if (heart_end_x >= surface.getWidth()) {
					heart_end_x = 0;
					heart_begin_x = 0;
					flag1 = true;
				}
				canvas = holder_heart.lockCanvas(new Rect(heart_begin_x, 0,
						heart_begin_x +5, surface.getHeight()));
				if (canvas == null) {
					return;
				}
				// 画布背景色
				canvas.drawColor(Color.WHITE);
				 drawGrid(canvas);

				// 画直线用到的起始点和终点
				heart_begin_x = heart_end_x;
				heart_begin_y = heart_end_y;
				heart_end_x = heart_end_x + 5;

				heart_end_y = ((-ECGData / scaleY + (3*surface.getHeight()/5)));
				// 画直线
				path.moveTo(heart_begin_x, heart_begin_y);
				path.lineTo(heart_end_x, heart_end_y);
				canvas.drawPath(path, paint);
				if (flag1) {
					path.reset();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (canvas != null) {
				holder_heart.unlockCanvasAndPost(canvas);

			}
		}

	}


	private void drawGrid(Canvas canvas) {
		// TODO Auto-generated method stub
		
		int gaphorizontal=(surface.getWidth()%(StaticValue.Fs/5))/2 ;
		int gapvertical=(surface.getHeight()%(StaticValue.Fs/5))/2;
		
		//画竖线
		for(int horizontal =0; horizontal<=surface.getWidth()/(StaticValue.Fs/5); horizontal++){
            canvas.drawLine(
            		horizontal*(StaticValue.Fs/5)+gaphorizontal, gapvertical,
            		horizontal*(StaticValue.Fs/5)+gaphorizontal, surface.getHeight()-gapvertical,
                            grid_paint);
    
    }                   
    //画横线
    for(int vertical =0; vertical<=surface.getHeight()/(StaticValue.Fs/5); vertical++){
            canvas.drawLine(
            		gaphorizontal, vertical*(StaticValue.Fs/5)+gapvertical,
            		surface.getWidth()-gaphorizontal, vertical*(StaticValue.Fs/5)+gapvertical,
                            grid_paint);
           
    }
	}



	public void drawanalyse(int[] eCGsingnal, int[] js) {
		// TODO Auto-generated method stub
		int jiange =8;
		float[] ECGsingal = new float[eCGsingnal.length/8];
		float[] ECGR = new float[js.length];

		for (int i = 0; i <eCGsingnal.length/8; i++) {
			ECGsingal[i] = eCGsingnal[i * jiange];
		}

		for (int i = 0; i < js.length /5; i++) {
			ECGR[i *5] = (int) (((double) js[i * 2] / jiange) + 0.5);
			ECGR[i *5 + 1] = (-ECGsingal[(int) ECGR[i * 2]] / 50 + (surface
					.getHeight() / 2));
		}

		canvas = holder_heart.lockCanvas();

		// 画布背景色
		canvas.drawColor(Color.WHITE);
		drawGrid(canvas);
		// 画笔属性

		paint.setStrokeWidth(3);
		paint.setColor(Color.BLACK);
		paint.setAntiAlias(true);// 表示抗锯齿
		paint.setStyle(Paint.Style.STROKE);

		Paint paintR = new Paint(); // 画笔
		paintR.setColor(Color.BLUE);
		paintR.setStrokeWidth(6);

		path.reset();
		heart_end_x = 0;
		int tempsize = 0;
		while (tempsize < ECGsingal.length) {

			// 画直线用到的起始点和终点
			heart_begin_x = heart_end_x;
			heart_begin_y = heart_end_y;
			heart_end_x = heart_end_x + 1;

			heart_end_y = (-ECGsingal[tempsize] / 50 + (surface.getHeight() / 2));
			// 画直线
			path.moveTo(heart_begin_x, heart_begin_y);
			path.lineTo(heart_end_x, heart_end_y);
			tempsize++;
		}
		canvas.drawPath(path, paint);

		canvas.drawPoints(ECGR, paintR);

		// 恢复画布
		holder_heart.unlockCanvasAndPost(canvas);

	}

}
