package com.bluerhino.library.hardware;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

public class TelephonyStatusManager {

	private static TelephonyStatusManager INSTANCE = null;

	public synchronized static TelephonyStatusManager getInstance() {
		if (null == INSTANCE) {
			INSTANCE = new TelephonyStatusManager();
		}
		return INSTANCE;
	}

	
	
	public String getSimType(Context context) {
		String simType = "";

		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE); // 获得手机SIMType 　　

		int type = tm.getNetworkType();

		if (type == TelephonyManager.NETWORK_TYPE_UMTS) {
			simType = "USIM";
		} else if (type == TelephonyManager.NETWORK_TYPE_GPRS) {
			simType = " SIM";
		} else if (type == TelephonyManager.NETWORK_TYPE_EDGE) {
			simType = " SIM";
		} else {
			simType = " UIM";
		}

		return simType;
	}

	public String getNetworkTypeName(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE); // 获得手机SIMType 　　
		Class<?> tmClass = tm.getClass();
		Method getNetworkTypeNameMethod = null;
		String typeName = "";
		try {
			getNetworkTypeNameMethod = tmClass.getMethod("getNetworkTypeName");
			typeName = (String) getNetworkTypeNameMethod.invoke(tm);

		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return typeName;
	}

    /**
     * 获取详细网络情况
     * 
     * @return
     */
//    public String getNetworkTypeDetails()
//    {
//        ConnectivityManager connectMgr = (ConnectivityManager) mContext
//                .getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo info = connectMgr.getActiveNetworkInfo();
//        // NetworkInfo: type: WIFI[], state: CONNECTED/CONNECTED, reason:
//        // (unspecified), extra: "cisco-dev", roaming: false, failover: false,
//        // isAvailable: true, simId: 0
//        if (info == null){
//        	return "None";
//        }
//        else if (info.getType() == ConnectivityManager.TYPE_WIFI)
//        {
//            return "WIFI";
//        }
//        else if (info != null
//                && info.getType() == ConnectivityManager.TYPE_MOBILE)
//        {
//            System.out.println("手机网络");
//            if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_CDMA)
//            {
//                return "CDMA";
//            }
//            else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_EDGE)
//            {
//                return "EDGE";
//            }
//            else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_EVDO_0)
//            {
//                return "EVDO_0";
//            }
//            else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_EVDO_A)
//            {
//                return "EVDO_A";
//            }
//            else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_GPRS)
//            {
//                return "GPRS";
//            }
//            else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_HSDPA)
//            {
//                return "HSDPA";
//            }
//            else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_HSPA)
//            {
//                return "HSPA";
//            }
//            else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_HSUPA)
//            {
//                return "HSUPA";
//            }
//            else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_UMTS)
//            {
//                return "UMTS";
//            }
//            else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_LTE)
//            {
//            	return  "LTE";
//            }
//            else
//            {
//                return "";
//            }
//        }
//        else
//        {
//            return "";
//        }
//    }
//	
	
	
	 /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     * 
     * @param context
     * @return 1 表示开启 ,2代表关闭
     */
    public final boolean isGpsOpened(final Context context)
    {
        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean result = false;
        if (gps)
        {
           result = true;
        }
        return result;
    }
}
