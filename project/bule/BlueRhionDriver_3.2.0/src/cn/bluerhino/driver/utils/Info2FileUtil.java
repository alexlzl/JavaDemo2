package cn.bluerhino.driver.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

import android.os.Environment;

import com.baidu.location.BDLocation;

public class Info2FileUtil {

	public static final String parseBDLocation(BDLocation location) {
		StringBuffer sb = new StringBuffer();
		sb.append("当前时间: ");
		sb.append(DateFormat.getDateTimeInstance().format(new Date()));
		sb.append("\n服务器定位时间: " + location.getTime());
		sb.append("\nLongitude : ");
		sb.append(location.getLongitude());
		sb.append("\nLatitude: ");
		sb.append(location.getLatitude());
		// GPS 61
		if (location.getLocType() == BDLocation.TypeGpsLocation) {
			sb.append("\nGPS定位 : ");
			sb.append(location.getAddrStr());
		}
		// 网络定位 161
		else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
			sb.append("\n网络定位 : ");
			sb.append(location.getAddrStr());
		} else {
			sb.append("\n错误码: ");
			sb.append(location.getLocType());
		}
		sb.append("\n-------------\n");
		return sb.toString();
	}

	public static void saveInfo2File(String info) {
		Info2FileUtil.saveInfo2File(info, "brLocation");
	}
	
	public static void saveInfo2File(String info, String fileName) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			String path = Environment.getExternalStorageDirectory()
					.getAbsolutePath();
			File dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			FileWriter fw;
			try {
				fw = new FileWriter(path + "/" + fileName + ".log", true);
				fw.write(info);
				fw.flush();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void saveInfo2File(BDLocation location) {
		saveInfo2File(parseBDLocation(location));
	}

}
