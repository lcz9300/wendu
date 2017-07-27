package com.HWDTEMPT.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Shader;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.HWDTEMPT.hwdtempt.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Bloodhistory extends View
{
    private HashMap<Long, Integer> milliliter;
private float tb;
// 左右间距
private float interval_left_right;
// 左间距
private float interval_left;
// 画笔
private Paint paint_date, paint_brokenLine, paint_dottedline,
paint_brokenline_big, frampaint;
private Paint textPaint;
private int time_index;
// 点图
private Bitmap bitmap_point;
private Path path;
private float dotted_text;
private int flagtag;
private int tabtag;

public float getDotted_text()
{
return dotted_text;
}
public void setDotted_text(float dotted_text)
{
this.dotted_text = dotted_text;
}
public void setMilliliter(HashMap<Long, Integer> milliliter)
{
this.milliliter = milliliter;
}
private int fineLineColor = 0x5faaaaaa; // 灰色,背景柱状图
private int blueLineColor = 0xff00ffff; // 蓝色，线色
private int orangeLineColor = 0xffd56f2b; // 橙色，基准线色
public Bloodhistory(Context context, HashMap<Long, Integer> milliliter, int flagtag, int tabtag)
{
super(context);
init(milliliter,tabtag);
this.flagtag = flagtag;
this.tabtag = tabtag;
}
public void init(HashMap<Long, Integer> milliliter, int tabtag)
{
if (null == milliliter || milliliter.size() == 0)
return;
this.milliliter = milliliter;
Resources res = getResources();
// 定义基准宽度
tb = res.getDimension(R.dimen.historyscore_tb);
interval_left_right = tb * 2.0f;
interval_left = tb * 0.5f;
paint_date = new Paint();
paint_date.setStrokeWidth(tb * 0.1f);
paint_date.setTextSize(tb * 0.7f);
paint_date.setColor(fineLineColor);
paint_brokenLine = new Paint();
paint_brokenLine.setStrokeWidth(tb * 0.1f);
paint_brokenLine.setColor(blueLineColor);
paint_brokenLine.setAntiAlias(true);
paint_dottedline = new Paint();
paint_dottedline.setStyle(Paint.Style.STROKE);
paint_dottedline.setColor(fineLineColor);
paint_brokenline_big = new Paint();
paint_brokenline_big.setStrokeWidth(tb * 0.2f);
paint_brokenline_big.setColor(fineLineColor);
paint_brokenline_big.setAntiAlias(true);
frampaint = new Paint();
frampaint.setAntiAlias(true);
frampaint.setStrokeWidth(2f);
textPaint = new Paint();
textPaint.setAntiAlias(true);
textPaint.setStrokeWidth(1f);
textPaint.setTextSize(tb * 0.4f);
textPaint.setColor(Color.BLUE);
Paint P = new Paint();
path = new Path();
bitmap_point = BitmapFactory.decodeResource(getResources(),
R.drawable.icon_point_blue);
// 设置宽高尺寸
setLayoutParams(new LayoutParams((int) (31 * interval_left_right),
LayoutParams.MATCH_PARENT));
}

@Override
protected void onDraw(Canvas c)
{
if (null == milliliter || milliliter.size() == 0)
{
Log.w("onDraw()", "为空");
return;
}
drawStraightLine(c);
drawBrokenLine(c);
drawDate(c);
}
/**
* 绘制竖线 绘制背景Y轴
*
* @param c
*/
public void drawStraightLine(Canvas c)
{
paint_dottedline.setColor(fineLineColor);
Path path = new Path();
for (int i = 0; i < 32; i++)
{
path.moveTo(interval_left_right * i + tb, tb * 1.5f);
path.lineTo(interval_left_right * i + tb, getHeight());
// 虚线
PathEffect effects = new DashPathEffect(new float[] { tb * 0.3f,
tb * 0.3f, tb * 0.3f, tb * 0.3f }, tb * 0.1f);
paint_dottedline.setPathEffect(effects);
c.drawPath(path, paint_dottedline);
}
c.drawLine(0, getHeight() - tb * 0.2f, getWidth(), getHeight() - tb
* 0.2f, paint_brokenline_big);
}
/**
* 绘制折线
*
* @param c
*/
public void drawBrokenLine(Canvas c)
{
//float base = (getHeight() - tb * 3.0f) / Collections.max(milliliter) * 0.7f;
    float base = (getHeight() - tb * 3.0f) * 0.7f;
// 渲染
Shader mShader = new LinearGradient(0, 0, 0, getHeight(), new int[] {
Color.argb(150, 0, 200, 250), Color.argb(70, 0, 200, 250),
Color.argb(22, 0, 200, 250) }, null, Shader.TileMode.CLAMP);
frampaint.setShader(mShader);
for (int i = 0; i < milliliter.size() - 1; i++)
{
float x1 = interval_left_right * i + tb;
float y1 = getHeight() - tb * 1.5f - (base * milliliter.get(i));
float x2 = interval_left_right * (i + 1) + tb;
float y2 = getHeight() - tb * 1.5f - (base * milliliter.get(i + 1));
if (i == 0)
{
path.moveTo(tb,
getHeight() - tb * 1.5f - (base * milliliter.get(0)));
}
c.drawLine(x1, y1, x2, y2, paint_brokenLine);
path.lineTo(x1, y1);
// 打点
c.drawBitmap(bitmap_point, x1 - bitmap_point.getWidth() / 2, y1
- bitmap_point.getHeight() / 2, null);
Log.w("drawBitmap", "" + milliliter.size());
// 画步数值
c.drawText("" + milliliter.get(i),
x1 - bitmap_point.getWidth() / 2,
y1 - bitmap_point.getHeight() / 2, textPaint);
if (i == milliliter.size() - 2)
{
path.lineTo(x2, y2);
path.lineTo(x2, getHeight());
path.lineTo(tb, getHeight());
path.close();
c.drawPath(path, frampaint);
c.drawBitmap(bitmap_point, x2 - bitmap_point.getWidth() / 2, y2
- bitmap_point.getHeight() / 2, null);
c.drawText("" + milliliter.get(i + 1),
x2 - bitmap_point.getWidth() / 2,
y2 - bitmap_point.getHeight() / 2, textPaint);
}
}
}
/**
* 绘制时间
*
* @param c
*/
public void drawDate(Canvas c)
{
for (int i = 0; i < 31; i++)
{
paint_date.setStrokeWidth(tb * 0.4f);
c.drawText(i + 1 + "天", interval_left_right * i + tb / 2,
tb * 1.0f, paint_date);
}
}
}