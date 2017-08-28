package cn.bluerhino.driver.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CircleView extends View {

	public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public CircleView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CircleView(Context context) {
		super(context);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStrokeWidth((float) 2.0);
		paint.setColor(Color.parseColor("#ffffff"));
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawCircle(getWidth()/2, getHeight()/2, getWidth()/2-3, paint);
		//paint.setStyle(Style.STROKE);
		/*RectF oval = new RectF(); // RectF对象
		oval.left = getLeft(); // 左边
		oval.top = getTop(); // 上边
		oval.right = getRight(); // 右边
		oval.bottom = getBottom(); // 下边
		canvas.drawArc(oval, 0, 360, false, paint);*/
	}

}
