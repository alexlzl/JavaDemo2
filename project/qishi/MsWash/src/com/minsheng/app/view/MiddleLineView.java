package com.minsheng.app.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

public class MiddleLineView extends TextView{
	
	private Paint mPaint;

	public MiddleLineView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mPaint = new Paint();
		mPaint.setStrokeWidth(4.0f);
		mPaint.setColor(Color.parseColor("#505050"));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawLine(0f, this.getHeight(), this.getWidth(),
				this.getHeight(), mPaint);
	}
}
