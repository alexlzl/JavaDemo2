package cn.bluerhino.driver.model.datasource.orderflow;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.bluerhino.driver.model.OrderInfo;
import com.android.volley.RequestQueue;

public interface IOrderFlowComponent {

    public OrderInfo getOrderInfo();

    public Context getContext();

    public Button getLeftTab();

    public Button getRightTab();

    public View getWaitTimeView();

    public RequestQueue getRequestQueue();
	
    public TextView  getOrderStateTv();
	    
    public LinearLayout getOrderStateParentView();
}
