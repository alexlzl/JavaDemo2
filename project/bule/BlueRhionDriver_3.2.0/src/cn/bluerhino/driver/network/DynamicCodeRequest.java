package cn.bluerhino.driver.network;

import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import android.annotation.SuppressLint;
import android.util.Base64;

import cn.bluerhino.driver.BRURL;

import com.bluerhino.library.network.framework.BRJsonRequest;

public class DynamicCodeRequest extends BRJsonRequest {

	@SuppressWarnings("deprecation")
    protected DynamicCodeRequest(JsonBuilder builder) {
	    super(builder);
    }
	
	public static class Builder extends JsonBuilder{

		public Builder() {
	        super();
	        
	        setUrl(BRURL.DYNAMIC_CODE_POST_URL);
        }
	}
	
	@SuppressLint("TrulyRandom")
    public static final String verifyEncode(String str) {
		try {
			String key = "lxn@2014";
			String ALGORITHM_DES = "DES/CBC/PKCS5Padding";
			DESKeySpec dks = new DESKeySpec(key.getBytes());
			
	    	SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            //key的长度不能够小于8位字节
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            IvParameterSpec iv = new IvParameterSpec(key.getBytes());
            AlgorithmParameterSpec paramSpec = iv;
            cipher.init(Cipher.ENCRYPT_MODE, secretKey,paramSpec);

            byte[] bytes = cipher.doFinal(str.getBytes());
            return Base64.encodeToString(bytes, Base64.DEFAULT);
		} catch(Exception e) {}
		return str;
	}

}
