package com.minsheng.app.animation;

import com.minsheng.app.util.LogUtil;

import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.graphics.Camera;
import android.graphics.Matrix;

/**
 * 
 * 
 * @describe:旋转动画
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-3-19下午2:56:45
 * 
 */
public class Rotate3dAnimation extends Animation {
	private static final String TAG = "Rotate3dAnimation";
	private final float mFromDegrees;
	private final float mToDegrees;
	private final float mCenterX;
	private final float mCenterY;
	private final float mDepthZ;
	private final boolean mReverse;
	private Camera mCamera;

	public Rotate3dAnimation(float fromDegrees, float toDegrees, float centerX,
			float centerY, float depthZ, boolean reverse) {
		mFromDegrees = fromDegrees;
		mToDegrees = toDegrees;
		mCenterX = centerX;
		mCenterY = centerY;
		mDepthZ = depthZ;
		mReverse = reverse;
	}

	@Override
	public void initialize(int width, int height, int parentWidth,
			int parentHeight) {
		super.initialize(width, height, parentWidth, parentHeight);
		mCamera = new Camera();
		LogUtil.d(TAG, "initialize===============");
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		LogUtil.d(TAG, "applyTransformation时间" + interpolatedTime);
		final float fromDegrees = mFromDegrees;
		float degrees = fromDegrees
				+ ((mToDegrees - fromDegrees) * interpolatedTime);
		final float centerX = mCenterX;
		final float centerY = mCenterY;
		final Camera camera = mCamera;
		final Matrix matrix = t.getMatrix();
		camera.save();
		if (mReverse) {
			camera.translate(0.0f, 0.0f, mDepthZ * interpolatedTime);
		} else {
			camera.translate(0.0f, 0.0f, mDepthZ * (1.0f - interpolatedTime));
		}
		camera.rotateY(degrees);
		camera.getMatrix(matrix);
		camera.restore();
		matrix.preTranslate(-centerX, -centerY);
		matrix.postTranslate(centerX, centerY);
	}
}
