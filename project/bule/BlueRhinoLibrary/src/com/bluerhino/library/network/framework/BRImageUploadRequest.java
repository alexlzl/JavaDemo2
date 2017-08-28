package com.bluerhino.library.network.framework;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

import android.graphics.Bitmap;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;

public class BRImageUploadRequest extends BRJsonRequest {

	private static final String MULTIPART_FORM_DATA = "multipart/form-data";
	
	private String BOUNDARY = "--------------" + System.currentTimeMillis();
	
	private WeakReference<BRImageBuilder> mWeakBuilder;

	@Deprecated
    protected BRImageUploadRequest(BRImageBuilder builder) {
		super(builder);
		mWeakBuilder = new WeakReference<BRImageUploadRequest.BRImageBuilder>(builder);
		setShouldCache(false);
	}
	
	private BRImageBuilder getBuilder(){
		return mWeakBuilder.get(); 
	}

	@Override
	public byte[] getBody() throws AuthFailureError {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		StringBuffer sb = new StringBuffer();
		/* 第一行 */
		// `"--" + BOUNDARY + "\r\n"`
		sb.append("--" + BOUNDARY);
		sb.append("\r\n");
		/* 第二行 */
		// Content-Disposition: form-data; name="参数的名称"; filename="上传的文件名" + "\r\n"
		sb.append("Content-Disposition: form-data;");
		sb.append(" name=\"");
		sb.append(getBuilder().mName);
		sb.append("\"");
		sb.append("; filename=\"");
		sb.append(getBuilder().mFileName);
		sb.append("\"");
		sb.append("\r\n");
		/* 第三行 */
		// Content-Type: 文件的 mime 类型 + "\r\n"
		sb.append("Content-Type: ");
		sb.append(getBuilder().mMime);
		sb.append("\r\n");
		/* 第四行 */
		// "\r\n"
		sb.append("\r\n");
		try {
			bos.write(sb.toString().getBytes("utf-8"));
			/* 第五行 */
			// 文件的二进制数据 + "\r\n"
			bos.write(getBuilder().mBitmapValue);
			bos.write("\r\n".getBytes("utf-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		/* 结尾行 */
		// `"--" + BOUNDARY + "--" + "\r\n"`
		String endLine = "--" + BOUNDARY + "--" + "\r\n";
		try {
			bos.write(endLine.toString().getBytes("utf-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bos.toByteArray();
	}

	@Override
	public String getBodyContentType() {
		return MULTIPART_FORM_DATA + "; boundary=" + BOUNDARY;
	}

	public static class BRImageBuilder extends JsonBuilder {
		private static final int DEFAULT_TIMEOUT = 5000;
		private String mName = "uploadFile";
		private String mMime = "image/png";
		private String mFileName;
		private byte[] mBitmapValue;
		
		public BRImageBuilder() {
	        super();
	        setRetryPolicy(new DefaultRetryPolicy(DEFAULT_TIMEOUT,
			        DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }
		
		public BRImageBuilder setName(String name){
			mName = name;
			return this;
		}
		
		public BRImageBuilder setFileName(String fileName){
			mFileName = fileName;
			return this;
		}
		
		public BRImageBuilder setMime(String mime) {
			mMime = mime;
			return this;
		}
		
		public BRImageBuilder setBitMap(Bitmap bitmap) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
			mBitmapValue = bos.toByteArray();
			return this;
		}

		@Override
        public JsonBuilder setSucceedListener(BRJsonResponse succeedListener) {
	        return super.setSucceedListener(succeedListener);
        }

		@Override
		public BRImageUploadRequest build() {
			return new BRImageUploadRequest(this);
		}
	}
}
