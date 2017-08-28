package cn.bluerhino.driver.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.bluerhino.driver.R;

public class CurOrderRemarkLinearLayout extends LinearLayout {

	private TextView mRemakeTitle;
	private TextView mRemarkInfo;

	public CurOrderRemarkLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}


	@Override
    protected void onFinishInflate() {
	    super.onFinishInflate();
	    mRemakeTitle = (TextView) findViewById(R.id.orderinfo_remark_title);
	    mRemarkInfo = (TextView) findViewById(R.id.orderinfo_remark_info);
		
    }
	
	public void setTitleShow(){
		mRemakeTitle.setVisibility(View.VISIBLE);
	}
	
	public void setTitleDismiss(){
		mRemakeTitle.setVisibility(View.INVISIBLE);
	}
	
	public void setRemarkInfo(String remakeInfo){
		mRemarkInfo.setText(remakeInfo);
	}

	
	

	
}
