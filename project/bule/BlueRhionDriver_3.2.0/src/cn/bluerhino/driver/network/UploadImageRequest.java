package cn.bluerhino.driver.network;

import cn.bluerhino.driver.BRURL;

import com.bluerhino.library.network.framework.BRImageUploadRequest;

public class UploadImageRequest extends BRImageUploadRequest {

	@SuppressWarnings("deprecation")
    private UploadImageRequest(BRImageBuilder builder) {
	    super(builder);
    }

	public static class Builder extends BRImageBuilder {

		public Builder() {
	        super();
			setUrl(BRURL.UPLOAD_IMAGE_POST_URL);
        }
	}
}
