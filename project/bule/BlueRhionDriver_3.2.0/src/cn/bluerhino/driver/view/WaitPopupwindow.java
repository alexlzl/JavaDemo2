package cn.bluerhino.driver.view;


import android.content.Context;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import cn.bluerhino.driver.R;
import cn.bluerhino.driver.utils.TimeUtil;
import cn.bluerhino.driver.view.framework.ViewBuilder;

public class WaitPopupwindow extends PopupWindow {
	
	private long currentBaseTime;

	private Context mContext;
	private View contentView;
	
	private Button leftTabBar;
	private Button rightTabBar;
	private Chronometer chronometer;

//	public WaitPopupwindow(Context context) {
//		super(View.inflate(context, R.layout.timer_count, null), RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
//		mContext = context;
//		contentView = getContentView();
//		createPopWindow();
//		finishInflate();
//	}

	public void createPopWindow() {
		setFocusable(true);
	}
	
	public void finishInflate() {
		leftTabBar = ViewBuilder.findView(contentView, R.id.order_flow_lefttab_item);
		rightTabBar = ViewBuilder.findView(contentView, R.id.order_flow_righttab_item);
		chronometer = ViewBuilder.findView(contentView, R.id.chronometer);
		
		currentBaseTime = SystemClock.elapsedRealtime()+TimeUtil.getMillSecond(2, 0, 0);
		chronometer.setBase(currentBaseTime);
		chronometer.start();		
		chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
			@Override
			public void onChronometerTick(Chronometer chronometer) {
				//如果超过一个小时会提示产生了等待费
				if(SystemClock.elapsedRealtime() - currentBaseTime > 1000*60){
				}
			}
		});
		
		LayoutParams leftTabParams = (LayoutParams) leftTabBar.getLayoutParams();
		leftTabParams.weight = 1;
		leftTabBar.setLayoutParams(leftTabParams);
		leftTabBar.setText("从发货地出发");
		LayoutParams rightTabParams = (LayoutParams) rightTabBar.getLayoutParams();
		rightTabParams.weight = 0;
		rightTabBar.setLayoutParams(rightTabParams);
		
		leftTabBar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		
		rightTabBar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();			
			}
		});
		
	}
	
	public void setNewStyle(){
		LayoutParams leftTabParams = (LayoutParams) leftTabBar.getLayoutParams();
		leftTabParams.weight = 1;
		leftTabBar.setLayoutParams(leftTabParams);
		leftTabBar.setText("服务完成");

		LayoutParams rightTabParams = (LayoutParams) rightTabBar.getLayoutParams();
		rightTabParams.weight = 1;
		rightTabBar.setLayoutParams(rightTabParams);
		rightTabBar.setText("再次出发");
	}
	
	public void show(){
		showAtLocation(contentView, Gravity.CENTER, 0, 0);
	}

}
