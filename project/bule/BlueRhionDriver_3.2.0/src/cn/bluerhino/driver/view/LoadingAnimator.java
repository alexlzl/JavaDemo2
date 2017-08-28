package cn.bluerhino.driver.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class LoadingAnimator extends ImageView {

	private AnimationDrawable mAnimationDrawable;
	private boolean isAnimating;
	public LoadingAnimator(Context context) {
		super(context);
		init();
	}

	public LoadingAnimator(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public LoadingAnimator(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		//setBackgroundResource(R.drawable.loading_animation_list);
		mAnimationDrawable= (AnimationDrawable) getDrawable();
	}
	
	public boolean isAnimating() {
		return isAnimating;
	}
	
	public void start(){
		removeCallbacks(mStopAnimationRunnable);
		post(mStartAnimationRunnable);
	}
	
	public void stop(){
		isAnimating = false;
		removeCallbacks(mStartAnimationRunnable);
		post(mStopAnimationRunnable);
	}
	
	private final Runnable mStartAnimationRunnable = new Runnable() {
		@Override
		public void run() {
			setVisibility(View.VISIBLE);
			mAnimationDrawable.start();
			isAnimating = true;
		}
	};
	
	private final Runnable mStopAnimationRunnable = new Runnable() {
		@Override
		public void run() {
			mAnimationDrawable.stop();
			setVisibility(View.GONE);
			isAnimating = false;
		}
	};
}
