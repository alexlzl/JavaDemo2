package com.example.testroundrect;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class RectView extends View {

	public RectView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
    @Override
    public void draw(Canvas canvas) {
    	// TODO Auto-generated method stub
    	super.draw(canvas);
    	Paint paint = new Paint();
        paint.setAntiAlias(true);                       //设置画笔为无锯齿  
        paint.setColor(Color.BLACK);                    //设置画笔颜色  
        canvas.drawColor(Color.WHITE);                  //白色背景  
        paint.setStrokeWidth((float) 3.0);              //线宽  
        paint.setStyle(Style.STROKE);                   //空心效果  
        Rect r1=new Rect();                         //Rect对象  
        r1.left=50;                                 //左边  
        r1.top=50;                                  //上边  
        r1.right=450;                                   //右边  
        r1.bottom=250;                              //下边  
        canvas.drawRect(r1, paint);                 //绘制矩形  
        RectF r2=new RectF();                           //RectF对象  
        r2.left=50;                                 //左边  
        r2.top=400;                                 //上边  
        r2.right=450;                                   //右边  
        r2.bottom=600;                              //下边  
        canvas.drawRoundRect(r2, 30, 10, paint);        //绘制圆角矩形  
    }
}
