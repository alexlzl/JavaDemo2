//package com.bluerhino.library.utils;
//
//import android.content.Context;
//import android.os.AsyncTask;
//import android.os.Handler;
//import android.os.Handler.Callback;
//import android.os.Message;
//
//import com.android.volley.RequestQueue;
//import com.android.volley.toolbox.JsonObjectRequest;
//
//public class VolleryASyncTask extends AsyncTask<Void, Void, Void> implements Callback {
//
//	private static final int MSG_ONPER = 1;
//	private static final int MSG_DOBACKGROUND = 2;
//	private static final int MSG_ONPOST = 3;
//
//	private onASyncListener mASyncListener;
//
//	public interface onASyncListener {
//		public void onPreExecute();
//
//		public void doInBackground();
//
//		public void onPostExecute();
//	}
//
//	private Context mContext;
//	private Handler mHandler;
//	private RequestQueue mRequestQueue;
//	private JsonObjectRequest mRequest;
//
//	public VolleryASyncTask(Builder builder) {
//		mContext = builder.mContext;
//		mHandler = builder.mHandler;
//		mRequestQueue = builder.mRequestQueue;
//		
//		if(null == mHandler){
//			mHandler = new Handler(this);
//		}
//	}
//	
//	@Override
//	protected Void doInBackground(Void... params) {
//		Message msg = Message.obtain(mHandler);
//		msg.what = MSG_DOBACKGROUND;
//		msg.obj = generateBackgroundMessage();
//		msg.sendToTarget();
//		return null;
//	}
//
//	@Override
//	protected void onPreExecute() {
//		mRequestQueue.add(generateRequest());
//		
//		Message msg = Message.obtain(mHandler);
//		msg.what = MSG_ONPER;
//		msg.obj = generatePreMessage();
//		msg.sendToTarget();
//		super.onPreExecute();
//	}
//
//	@Override
//	protected void onPostExecute(Void result) {
//		Message msg = Message.obtain(mHandler);
//		msg.what = MSG_ONPOST;
//		msg.obj = generatePostMessage();
//		msg.sendToTarget();
//		super.onPostExecute(result);
//	}
//
//	@Override
//	public boolean handleMessage(Message msg) {
//		switch (msg.what) {
//			case MSG_ONPER:
//				if(null != mASyncListener){
//					mASyncListener.onPreExecute();
//				}
//				break;
//			case MSG_DOBACKGROUND:
//				if(null != mASyncListener){
//					mASyncListener.doInBackground();
//				}
//				break;
//			case MSG_ONPOST:
//				if(null != mASyncListener){
//					mASyncListener.onPostExecute();
//				}
//				break;
//			default:
//				break;
//
//		}
//		return true;
//	}
//	
//	// #pragma mark  Virtual Function
//	public Object generatePreMessage(){
//		return null;
//	}
//	
//	public Object generateBackgroundMessage(){
//		return null;
//	}
//	
//	public Object generatePostMessage(){
//		return null;
//	}
//	
//	public JsonObjectRequest generateRequest(){
//		return null;
//	}
//
//	public static class Builder {
//		private Context mContext;
//		private Handler mHandler;
//		private RequestQueue mRequestQueue;
//
//		public Builder(Context mContext) {
//	        this.mContext = mContext;
//        }
//
//		public Builder(RequestQueue requestQueue) {
//	        this.mRequestQueue = requestQueue;
//        }
//
//		public final Builder setContext(Context context) {
//			this.mContext = context;
//			return this;
//		}
//
//		public final Builder setHandler(Handler handler) {
//			this.mHandler = handler;
//			return this;
//		}
//
//		public final Builder setRequestQueue(RequestQueue requestQueue) {
//			this.mRequestQueue = requestQueue;
//			return this;
//		}
//
//		public VolleryASyncTask build() {
//			return new VolleryASyncTask(this);
//		}
//	}
//}
