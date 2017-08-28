package com.bluerhino.library.utils;

import java.lang.ref.WeakReference;

import android.os.Handler;

public class WeakHandler<T> extends Handler {

	private WeakReference<T> mWeakReference;

	public WeakHandler(T reference) {
		super();
		mWeakReference = new WeakReference<T>(reference);
	}

	public T getReference() {
		return mWeakReference.get();
	}
}
