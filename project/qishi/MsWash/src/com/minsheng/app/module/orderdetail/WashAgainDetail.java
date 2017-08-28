package com.minsheng.app.module.orderdetail;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.minsheng.app.base.BaseActivity;
import com.minsheng.app.configuration.MsConfiguration;
import com.minsheng.app.util.MsStartActivity;
import com.minsheng.wash.R;

/**
 * 
 * @describe:返厂重洗订单详情页面
 * 
 * @author:LiuZhouLiang
 * 
 * @2015-3-15下午10:10:36
 * 
 */
public class WashAgainDetail extends BaseActivity {
	private ListView lv;
	private WashAgainDetailAdapter adapter;
	private View headView;
	private Button btConfirm;

	@Override
	protected void onCreate() {
		// TODO Auto-generated method stub
		setBaseContentView(R.layout.wash_again_detail);
	}

	@Override
	protected boolean hasTitle() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void loadChildView() {
		// TODO Auto-generated method stub
		lv = (ListView) findViewById(R.id.wash_again_detail_lv);
		headView = baseLayoutInflater.inflate(R.layout.order_detail_head, null);
		btConfirm = (Button) findViewById(R.id.wash_again_detail_confirm);

	}

	@Override
	protected void getNetData() {
		// TODO Auto-generated method stub
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < 20; i++) {
			list.add(i + "");
		}
		adapter = new WashAgainDetailAdapter(list, lv, baseContext);
		lv.addHeaderView(headView);
		lv.setAdapter(adapter);

	}

	@Override
	protected void reloadCallback() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setChildViewListener() {
		// TODO Auto-generated method stub
		btConfirm.setOnClickListener(this);

	}

	@Override
	protected String setTitleName() {
		// TODO Auto-generated method stub
		return "选择重洗衣物";
	}

	@Override
	protected String setRightText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected int setLeftImageResource() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int setMiddleImageResource() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int setRightImageResource() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.wash_again_detail_confirm:
			/**
			 * 确认返厂重洗
			 */
			Intent intent = new Intent(this, UniversalOrderDetail.class);
			intent.putExtra(MsConfiguration.ORDER_TYPE, MsConfiguration.CONFIRM_GET_CASH);
			MsStartActivity.startActivity(this, intent);
			break;
		}
	}

}
