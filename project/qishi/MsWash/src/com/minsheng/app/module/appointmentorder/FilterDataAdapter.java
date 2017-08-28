package com.minsheng.app.module.appointmentorder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.minsheng.app.baseadapter.BaseListAdapter;
import com.minsheng.app.baseadapter.ViewHolderUtil;
import com.minsheng.app.util.DateUtils;
import com.minsheng.app.util.LogUtil;
import com.minsheng.wash.R;

/**
 * 
 * @describe:过滤订单视图数据适配器
 * 
 * @author:LiuZhouLiang
 * 
 * @time:2015-5-5下午3:47:42
 * 
 */
public class FilterDataAdapter extends BaseListAdapter<String> {
	private static String TAG="FilterDataAdapter";
	private PopupWindow window;

	public FilterDataAdapter(List<String> data,Object obj, Context context) {
		super(data, context);
		// TODO Auto-generated constructor stub
		window=(PopupWindow) obj;
	}

	@Override
	public View bindView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = baseInflater.inflate(
					R.layout.appointment_order_filter_item, parent, false);
		}
		TextView tv=(TextView) ViewHolderUtil.getItemView(convertView, R.id.appointment_order_filter_item_tv);
		tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				window.dismiss();
			}
		});
		int times = (int) (System.currentTimeMillis()/1000);
		long jintian = DateUtils.getDate4PHP(times);
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		long mingtian =DateUtils.getDate4PHP( DateUtils.getDateTime4PHPByNum(+1));
		long houtian = DateUtils.getDate4PHP( DateUtils.getDateTime4PHPByNum(+2));;
		long dahoutian = DateUtils.getDate4PHP( DateUtils.getDateTime4PHPByNum(+3));
		LogUtil.d(TAG, "jintian:" + sdf2.format(new Date(jintian *1000)));
		LogUtil.d(TAG, "mingtian:" + sdf2.format(new Date(mingtian *1000)));
		
		LogUtil.d(TAG, "houtian:" + sdf2.format(new Date(houtian *1000)));
		LogUtil.d(TAG, "dahoutian:" + sdf2.format(new Date(dahoutian *1000)));
		String str = null;
		SimpleDateFormat sdf = new SimpleDateFormat("M月d日");
		long timeStamp = 0;
		//今天
		if(jintian <= timeStamp 
				&& timeStamp < mingtian){
			str = "今天";
		//明天	
		}else if(mingtian <= timeStamp
				&& timeStamp < houtian){
			str = "明天";
		 //后天	
		}else if(houtian <= timeStamp
				&& timeStamp < dahoutian){
			str = "后天";
			
		}else{
			Date date1 = new Date(timeStamp*1000);
			str = sdf.format(date1);
		}
		
	    
	   
		return convertView;
	}

}
