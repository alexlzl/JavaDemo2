package cn.bluerhino.driver.helper.url;

import cn.bluerhino.driver.R;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.net.Uri;

/**
 * URL处理入口
 * 
 * @author likai
 */
public class URIFormater {
	private static boolean isInit = false;
	
	private static String SCHEMA ;
	private static String SCHEMA_INNER ;
	private static String HOST_NATIVE;
	private static String HOST_WAP;
	
	/**
	 * @return 是否成功初始化
	 */
	public static boolean init(Context context){
		if(isInit)
			return true;
		if(context == null){
			return false;
		}
		Resources resources = context.getApplicationContext().getResources();
		try{
			SCHEMA = resources.getString(R.string.schema_br);
			SCHEMA_INNER = resources.getString(R.string.schema_inner_br);
			HOST_NATIVE = resources.getString(R.string.host_native);
			HOST_WAP = resources.getString(R.string.host_wap);
			isInit = true;
		}catch(NotFoundException e){
			e.printStackTrace();
			isInit = false;
		}
		return isInit;
	}
	
	/**
	 * 将原始uri转化为内部uri
	 * @param uri 原始uri
	 * @return 内部uri
	 */
	public static Uri uriFormatter(Uri uri) {
		if(!isInit)
			return null;
		if(!SCHEMA.equalsIgnoreCase(uri.getScheme())){
			return null;
		}
		String host = uri.getHost();
		/**
		 * 加载一个wap页面
		 * 例如BlueRhionDriver://wap?url=xxxx&title=xxx
		 * 转为 br://native/wap?url=xxxx&title=xxx 与清单文件对应
		 */
		if(HOST_WAP.equalsIgnoreCase(host)){
			final String PAGE = "wap";
			final String QUERY = uri.getQuery();
			if(QUERY == null){
				return null;
			}
			StringBuilder sBuilder = new StringBuilder();
			sBuilder.append(SCHEMA_INNER).append("://");
			sBuilder.append(HOST_NATIVE).append('/');
			sBuilder.append(PAGE);
			sBuilder.append('?').append(QUERY);
			return Uri.parse(sBuilder.toString());
		}
		/**
		 * 加载原生界面
		 * 例如 BlueRhionDriver://native?page=competitionList
		 * 转为 br://native/competitionList?page=competitionList 与清单文件对应
		 */
		else if(HOST_NATIVE.equalsIgnoreCase(host)){
			final String PAGE = uri.getQueryParameter("page");
			if(PAGE == null){
				return null;
			}else{
				final String QUERY = uri.getQuery();
				if(QUERY == null){
					return null;
				}
				StringBuilder sBuilder = new StringBuilder();
				sBuilder.append(SCHEMA_INNER).append("://");
				sBuilder.append(HOST_NATIVE).append('/');
				sBuilder.append(PAGE);
				sBuilder.append('?').append(QUERY);
				return Uri.parse(sBuilder.toString());
			}
		}else{
			return null;
		}
	}

}
