package cn.bluerhino.driver.network;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import com.bluerhino.library.utils.ToastUtil;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import cn.bluerhino.driver.R;
import cn.bluerhino.driver.network.listener.RequestListener;
import cn.bluerhino.driver.utils.CustomMultipartEntity;
import cn.bluerhino.driver.utils.CustomMultipartEntity.ProgressListener;
import cn.bluerhino.driver.utils.LogUtil;
import cn.bluerhino.driver.utils.StringUtil;
import cn.bluerhino.driver.view.MyProgressBarDialog;

/**
 * @Description: 用于文件上传
 * @author sunliang
 * @date 2015年8月27日 下午1:56:59
 */
public class UploadFilesPost extends AsyncTask<String, Integer, String> {
	private final String TAG = UploadFilesPost.class.getSimpleName();
	private Context context;
	private Map<String, File> filePath;
	// public ProgressDialog mProgressDialog;
	MyProgressBarDialog mProgressDialog;
	private long totalSize;
	int progress;
	String uploadUrl;
	ErrorHandler mErrorhandler;
	RequestListener<String> request;

	/**
	 * 初始化传递参数
	 * 
	 * @param context
	 * @param uploadUrl
	 *            上传文件所需要的url
	 * @param filePath
	 *            文件集合
	 * @param request
	 *            请求后返回的数据
	 */
	public UploadFilesPost(Context context, String uploadUrl, Map<String, File> filePath,
			RequestListener<String> request) {
		this.context = context;
		this.filePath = filePath;
		this.uploadUrl = uploadUrl;
		this.request = request;
		if (StringUtil.isEmail(uploadUrl)) {
			LogUtil.d(TAG, "传递的url为空");
			return;
		}
		if (filePath == null || filePath.isEmpty()) {
			LogUtil.d(TAG, "所要上传的的文件为空");
			ToastUtil.showToast(context, R.string.myinfo_modify_take_photo_error);
			return;
		}
	}

	@Override
	protected void onPreExecute() {
		if (mErrorhandler == null) {
			mErrorhandler = new ErrorHandler();
		}

		if (mProgressDialog == null) {
			mProgressDialog = new MyProgressBarDialog(context);
			mProgressDialog.show();
		}
		/*
		 * if (mProgressDialog == null) { mProgressDialog = new
		 * ProgressDialog(context);
		 * mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		 * mProgressDialog.setMessage("正在上传文件,请稍后");
		 * mProgressDialog.setCancelable(true); mProgressDialog.show(); }
		 */
	}

	@Override
	protected String doInBackground(String... params) {
		String serverResponse = null;
		// 测试url uploadUrl = "http://115.29.34.206:7078/DriverApi/uploadImg";
		HttpPost httpPost = new HttpPost(uploadUrl);
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext httpContext = new BasicHttpContext();
		try {
			CustomMultipartEntity multipartContent = new CustomMultipartEntity(new ProgressListener() {
				@Override
				public void transferred(long num) {
					publishProgress((int) ((num / (float) totalSize) * 100));
					progress = (int) ((num / (float) totalSize) * 100);
				}
			});

			// 将要上传的文件作为参数设置
			for (Map.Entry<String, File> files : filePath.entrySet()) {
				String key = files.getKey();
				File file = files.getValue();
				if (file != null && file.exists()) {
					multipartContent.addPart(key, new FileBody(file));
					// multipartContent.addPart(key, new InputStreamBody(new
					// FileInputStream(file), file.getName()));;
					Log.d(TAG, "文件size" +file.length()/1000);
				} else {
					LogUtil.d(TAG, key + "所要上传的文件不存在");
				}
			}
			// 计算总上传图片的大小
			totalSize = multipartContent.getContentLength();
			LogUtil.d(TAG, "总上传文件size" + totalSize);
			// 开始上传
			httpPost.setEntity(multipartContent);
			HttpResponse response = httpClient.execute(httpPost, httpContext);
			serverResponse = EntityUtils.toString(response.getEntity());

		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			mErrorhandler.sendEmptyMessage(RequestListener.TIMEOUT_ERROR);
		} catch (ConnectTimeoutException e) {
			e.printStackTrace();
			mErrorhandler.sendEmptyMessage(RequestListener.TIMEOUT_ERROR);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			mErrorhandler.sendEmptyMessage(RequestListener.TIMEOUT_ERROR);
		} catch (IOException e) {
			e.printStackTrace();
			mErrorhandler.sendEmptyMessage(RequestListener.IO_ERROR);
		} catch (Exception e) {
			mErrorhandler.sendEmptyMessage(RequestListener.OTHER_ERROR);
		}
		return serverResponse;
	}

	@Override
	protected void onProgressUpdate(Integer... progress) {
		// 更新进度条
		mProgressDialog.setProgress((int) (progress[0]));
	}

	@Override
	protected void onPostExecute(String result) {
		if (result != null) {
			// 将接受到的数据返回给调用者
			request.onResponse(result);
			if (mProgressDialog != null && mProgressDialog.isShowing()) {
				mProgressDialog.dismiss();
			}
		}
		LogUtil.d(TAG, "upload_result: " + result);
	}

	@Override
	protected void onCancelled() {
		LogUtil.d(TAG, "cancle");
	}

	/**
	 * 用来处理上传文件错误
	 * 
	 * @author sunliang
	 */
	private class ErrorHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			if (mProgressDialog != null && mProgressDialog.isShowing()) {
				mProgressDialog.dismiss();
			}
			switch (msg.what) {
			case RequestListener.TIMEOUT_ERROR:
				ToastUtil.showToast(context, RequestListener.ERROR_TIMEOUT);
				break;
			case RequestListener.IO_ERROR:
				ToastUtil.showToast(context, RequestListener.ERROR_IO);
				break;
			default:
				ToastUtil.showToast(context, RequestListener.ERROR_IO);
				break;
			}
			super.handleMessage(msg);
		}
	}

}
