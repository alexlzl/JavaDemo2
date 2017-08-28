package cn.bluerhino.driver.view;

import cn.bluerhino.driver.utils.LogUtil;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;
import android.widget.TextView;

/**
 * 
 * Describe:自定义TextView 实现滚动效果
 * 
 * Date:2015-9-6
 * 
 * Author:liuzhouliang
 */
public class MarqueeView extends TextView {
	private static final String TAG = MarqueeView.class.getName();

	private Scroller mScroller;
	private boolean isFirst = true;
	private int mRndDuration = 1000 * 10;
	private int mXPaused = 0;
	private boolean mPaused = true;
	int scrollingLength;

	public MarqueeView(Context context) {
		this(context, null);
	}

	public MarqueeView(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.textViewStyle);
	}

	public MarqueeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setSingleLine();
		setEllipsize(null);
		setVisibility(INVISIBLE);
	}

	/**
	 * 
	 * Describe:启动滚动
	 * 
	 * Date:2015-9-6
	 * 
	 * Author:liuzhouliang
	 */
	public void startScroll() {
		if (isFirst) {
			mXPaused = -30;
//			isFirst = false;
		} else {
			mXPaused = -1 * getWidth();
		}
		mPaused = true;
		resumeScroll();
	}

	public void resumeScroll() {

		if (!mPaused)
			return;
		mScroller = new Scroller(this.getContext(), new LinearInterpolator());
		setScroller(mScroller);
		scrollingLength = calculateScrollingLength();

		int distance = 0;
		int duration;
		if(isFirst){
			 distance = (scrollingLength - (getWidth() + mXPaused))*5;
			 duration = (int) ((float)  5000*distance / (scrollingLength*6));
			
			 LogUtil.d(TAG, "isFirst");
			 isFirst = false;
		}else{
			 distance = scrollingLength - (getWidth() + mXPaused);
			 duration = (int) ((float) mRndDuration * distance / scrollingLength);
			
			 LogUtil.d(TAG, "NOT First");
		}
		
		float textLength = measureTextLength(this);
		LogUtil.d(
				TAG,
				"文本长度===" + textLength + "textview长度==="
						+ ViewUtil.getWidth(this) + "scrollingLength=="
						+ scrollingLength + "duration==" + duration+"distance=="+distance);
		setVisibility(VISIBLE);
		mScroller.startScroll(mXPaused, 0, distance, 0, duration);
		mPaused = false;
	}

	/**
	 * 
	 * Describe:计算需要滚动的距离
	 * 
	 * Date:2015-9-6
	 * 
	 * Author:liuzhouliang
	 */
	private int calculateScrollingLength() {

		TextPaint tp = getPaint();
		Rect rect = new Rect();
		String strTxt = getText().toString();
		tp.getTextBounds(strTxt, 0, strTxt.length(), rect);
		int scrollingLen = rect.width() + getWidth();
		LogUtil.d(TAG, "TEXT_WIDTH" + rect.width() + "getWidth()=="
				+ getWidth());
		rect = null;

		return scrollingLen;
	}

	/**
	 * 
	 * Describe:获取文本宽度
	 * 
	 * Date:2015-9-6
	 * 
	 * Author:liuzhouliang
	 */
	public static float measureTextLength(TextView tv) {
		Paint paint = tv.getPaint();
		if (paint == null) {
			paint = new Paint();
		}
		return paint.measureText(tv.getText() + "");
	}

	public void pauseScroll() {
		if (null == mScroller)
			return;

		if (mPaused)
			return;
		mPaused = true;
		mXPaused = mScroller.getCurrX();
		mScroller.abortAnimation();
	}

	@Override
	public void computeScroll() {
		super.computeScroll();

		if (null == mScroller)
			return;

		if (mScroller.isFinished() && (!mPaused)) {
			this.startScroll();
		}
	}

	public int getRndDuration() {
		return mRndDuration;
	}

	public void setRndDuration(int duration) {
		this.mRndDuration = duration;
	}

	public boolean isPaused() {
		return mPaused;
	}
}
