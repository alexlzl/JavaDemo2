package cn.bluerhino.driver.utils;

import java.net.URLEncoder;
import java.util.List;

import org.json.JSONArray;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import cn.bluerhino.driver.ApplicationController;

import com.bluerhino.library.hardware.TelephonyStatusManager;
import com.bluerhino.library.network.NetWorkStatusManager;

public class DeviceInfo {

	public static final String PHONE_MODLE = Build.MODEL;
	public static final String SYSTEM_VERSION = Build.VERSION.RELEASE;
	public static final int SDK_VERSION = Build.VERSION.SDK_INT;
	
	private static DeviceInfo INSTANCE;
	
	private NetWorkStatusManager mNetWorkManager;
	private TelephonyStatusManager mTelephonyStatusManager;
	
	public synchronized static DeviceInfo getInstance(){
		if(null == INSTANCE){
			INSTANCE = new DeviceInfo();
		}
		return INSTANCE;
	}
	
	private DeviceInfo(){
		mNetWorkManager = NetWorkStatusManager.getInstance();
		mTelephonyStatusManager = TelephonyStatusManager.getInstance();
	}
	
	public String getAppVersionName(Context context,String packageName) {
		String versionName = "";
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
			versionName = packageInfo.versionName;
			if (TextUtils.isEmpty(versionName)) {
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return versionName;
	}
	
	public String getNetWorkModel(Context context) {
		try{
		switch (mNetWorkManager.getNetWorkType(context)) {
			case NetWorkStatusManager.NETWORKTYPE_WAP:
				return "WAP";
			case NetWorkStatusManager.NETWORKTYPE_2G:
				return "2G";
			case NetWorkStatusManager.NETWORKTYPE_3G:
				return "3G";
			case NetWorkStatusManager.NETWORKTYPE_WIFI:
				return "wifi";
			default:
				return "";
		}
		}catch(Exception e){
			
		}
		return "";
		
	}
	public String getDeviceId(Context context){
		String deviceid=  "";
		
		try{
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE); 
			deviceid = tm.getDeviceId();
		}catch(Exception e){
			
		}
		if(ApplicationController.testSwitch){
			return deviceid + "test";
		}else{
			return deviceid;
		}
		
	}
	public String getIPAddress(){
		String ipaddress = "";
		try{
			ipaddress = mNetWorkManager.getLocalIpAddress();
		}catch(Exception e){
			
		}
		return ipaddress == null ? "": URLEncoder.encode(ipaddress);
	}
	
	public String getMacAddress(Context context){
		
		String macaddress =  "";
		try{
		macaddress = mNetWorkManager.getMacAddress(context);
		}catch(Exception e){
			
		}
		return macaddress == null? "":URLEncoder.encode(macaddress);
	}
	
	public String getNetworkTypeName(Context context){
		String network = "";
		try{
			network = mTelephonyStatusManager.getNetworkTypeName(context);
		}catch(Exception e){
			
		}
		return network == null ? "" : URLEncoder.encode(network);
	}
	
	public String  getSimType(Context context){
		String simtype = "";
		try{
			simtype = mTelephonyStatusManager.getSimType(context);
		}catch(Exception e){
			
		}
		return simtype == null? "": URLEncoder.encode(simtype);
	}

	public final boolean isGpsOpened(final Context context){
		Boolean gps = false;
		try{
			gps = mTelephonyStatusManager.isGpsOpened(context);
		}catch(Exception e){
			
		}
		return gps;
	}
	
	
	public final JSONArray getApps(Context context){
		JSONArray jsonArray = new JSONArray();
		try{
			
			List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
			for(int i=0;i<packages.size();i++) { 
				//JSONObject apps = new JSONObject();
				String apps = "";
				PackageInfo packageInfo = packages.get(i);

				if ((packageInfo.applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)==0){
					String appName = packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString(); 
					String packageName = packageInfo.packageName; 
					String versionName = packageInfo.versionName; 
					int versionCode = packageInfo.versionCode; 
					apps = appName+":"+packageName+":"+versionName+":"+versionCode;
	                jsonArray.put(apps);
				}
			}
		}catch(Exception e){
			
		}
		return jsonArray;
	}
	
	public JSONArray getContacts(Context context) {
		JSONArray jsonArray = new JSONArray();
        
		try{ 
			
			// 查询的字段
	        String[] projection = {ContactsContract.CommonDataKinds.Phone._ID,
	                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
	                ContactsContract.CommonDataKinds.Phone.DATA1};
	        
	        Uri phoneUri=ContactsContract.CommonDataKinds.Phone.CONTENT_URI;  
	        Cursor cur=context.getContentResolver().query(phoneUri,projection,null, null, null); 
	        
	        while(cur.moveToNext()){
	        	//JSONObject users = new JSONObject();
                String displayName = cur.getString(1);  
                String phoneNumber = cur.getString(2);  
                
                
                String users= "";
	           // users.put("name", displayName);
	           // users.put("tel", phoneNumber);
                users = displayName+":"+phoneNumber;
	            jsonArray.put(users);
	        }  
	    }catch(Exception e){
	    	
	    }
		return jsonArray;
		
	} 
	
	
}
