package com.minsheng.app.view;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;
import android.widget.TextView;

public class MarqueeView extends TextView{

	// scrolling feature
	private Scroller mSlr;
	private boolean isFirst = true;

	// milliseconds for a round of scrolling
	private int mRndDuration = 25000;

	// the X offset when paused
	private int mXPaused = 0;

	// whether it's being paused
	private boolean mPaused = true;

	/*
	 * constructor
	 */
	public MarqueeView(Context context) {
		this(context, null);
	}

	/*
	 * constructor
	 */
	public MarqueeView(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.textViewStyle);
	}

	/*
	 * constructor
	 */
	public MarqueeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// customize the TextView
		setSingleLine();
		setEllipsize(null);
		setVisibility(INVISIBLE);
	}

	/**
	 * begin to scroll the text from the original position
	 */
	public void startScroll() {
		if(isFirst){
			mXPaused = -30;
			isFirst = false;
		}else{
			mXPaused = -1 * getWidth();
		}
		// assume it's paused
		mPaused = true;
		resumeScroll();
	}

	/**
	 * resume the scroll from the pausing point
	 */
	public void resumeScroll() {

		if (!mPaused)
			return;

		// Do not know why it would not scroll sometimes
		// if setHorizontallyScrolling is called in constructor.
		setHorizontallyScrolling(true);

		// use LinearInterpolator for steady scrolling
		mSlr = new Scroller(this.getContext(), new LinearInterpolator());
		setScroller(mSlr);

		int scrollingLen = calculateScrollingLen();
		int distance = scrollingLen - (getWidth() + mXPaused);
		int duration = (int)((float)mRndDuration * distance/scrollingLen);

		setVisibility(VISIBLE);
		mSlr.startScroll(mXPaused, 0, distance, 0, duration);
		mPaused = false;
	}

	/**
	 * calculate the scrolling length of the text in pixel
	 * 
	 * @return the scrolling length in pixels
	 */
	private int calculateScrollingLen() {
		TextPaint tp = getPaint();
		Rect rect = new Rect();
		String strTxt = getText().toString();
		tp.getTextBounds(strTxt, 0, strTxt.length(), rect);
		int scrollingLen = rect.width() + getWidth();
		rect = null;
		return scrollingLen;
	}

	/**
	 * pause scrolling the text
	 */
	public void pauseScroll() {
		if (null == mSlr)
			return;

		if (mPaused)
			return;

		mPaused = true;

		// abortAnimation sets the current X to be the final X,
		// and sets isFinished to be true
		// so current position shall be saved
		mXPaused = mSlr.getCurrX();

		mSlr.abortAnimation();
	}

	@Override
	/*
	 * override the computeScroll to restart scrolling when finished so as that
	 * the text is scrolled forever
	 */
	public void computeScroll() {
		super.computeScroll();

		if (null == mSlr)
			return;

		if (mSlr.isFinished() && (!mPaused)) {
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
