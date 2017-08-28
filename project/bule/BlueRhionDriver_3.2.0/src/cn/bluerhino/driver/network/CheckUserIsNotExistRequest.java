package cn.bluerhino.driver.network;

import cn.bluerhino.driver.BRURL;

import com.bluerhino.library.network.framework.BRJsonRequest;

public class CheckUserIsNotExistRequest extends BRJsonRequest {

	@SuppressWarnings("deprecation")
    private CheckUserIsNotExistRequest(Builder builder) {
	    super(builder);
    }
	
	public static class Builder extends JsonBuilder{

		public Builder() {
	        super();
	        setUrl(BRURL.CHECKUSER_IS_NOT_EXIST_POST_URL);
        }
		
	}

}
