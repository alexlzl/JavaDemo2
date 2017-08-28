package com.example.testroundrect;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CustomImageView extends ImageView{
	private float[] rids = {60.0f,60.0f,60.0f,60.0f,0.0f,0.0f,0.0f,0.0f,};  
	public CustomImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	protected void onDraw(Canvas canvas) {  
        Path path = new Path();  
        int w = this.getWidth();  
        int h = this.getHeight();  
        path.addRoundRect(new RectF(0,0,w,h),rids,Path.Direction.CW);  
        canvas.clipPath(path);  
        super.onDraw(canvas);  
    }  
}
